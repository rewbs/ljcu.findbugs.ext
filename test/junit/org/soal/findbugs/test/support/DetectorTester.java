package org.soal.findbugs.test.support;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import edu.umd.cs.findbugs.BugPattern;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.Detector2;
import edu.umd.cs.findbugs.DetectorToDetector2Adapter;
import edu.umd.cs.findbugs.FindBugs2;
import edu.umd.cs.findbugs.I18N;
import edu.umd.cs.findbugs.NoOpFindBugsProgress;
import edu.umd.cs.findbugs.ba.AnalysisCacheToAnalysisContextAdapter;
import edu.umd.cs.findbugs.ba.AnalysisContext;
import edu.umd.cs.findbugs.ba.FieldSummary;
import edu.umd.cs.findbugs.ba.XClass;
import edu.umd.cs.findbugs.classfile.CheckedAnalysisException;
import edu.umd.cs.findbugs.classfile.ClassDescriptor;
import edu.umd.cs.findbugs.classfile.Global;
import edu.umd.cs.findbugs.classfile.IAnalysisCache;
import edu.umd.cs.findbugs.classfile.IClassFactory;
import edu.umd.cs.findbugs.classfile.IClassPath;
import edu.umd.cs.findbugs.classfile.IClassPathBuilder;
import edu.umd.cs.findbugs.classfile.ICodeBaseLocator;
import edu.umd.cs.findbugs.classfile.impl.ClassFactory;

/**
 * Performs FindBugs analysis using a single Detector.
 */
public class DetectorTester {
	
	private BugPattern bugPattern;
	private BugReporter bugReporter;
	
	public void setBugPattern(BugPattern bp) {
		this.bugPattern = bp;
	}
	

	public void setBugReporter(BugReporter br) {
		this.bugReporter = br;
	}

	/**
	 * Thanks to Josh Cummings for figuring a lot of this out:
	 * http://tech.joshuacummings.com/2010/05/testing-custom-findbugs-detectors-in.html
	 * 
	 * @param detectorClass class of detector to run
	 * @param filePath path containing classes to analyse
	 * 
	 * @return list of classes that were analysed
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @throws CheckedAnalysisException 
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws IllegalArgumentException 
	 */
	public List<ClassDescriptor> analyse(Class<? extends Detector> detectorClass, String filePath) throws CheckedAnalysisException, IOException,
			InterruptedException, SecurityException, NoSuchMethodException,
			IllegalArgumentException, InstantiationException,
			IllegalAccessException, InvocationTargetException {

		// Prepare FindBugs analysis context
		IClassFactory classFactory = ClassFactory.instance();
		IClassPath classPath = classFactory.createClassPath();
		IAnalysisCache analysisCache = classFactory.createAnalysisCache(classPath, bugReporter);
		Global.setAnalysisCacheForCurrentThread(analysisCache);
		FindBugs2.registerBuiltInAnalysisEngines(analysisCache);
		IClassPathBuilder builder = classFactory.createClassPathBuilder(bugReporter);
		ICodeBaseLocator locator = classFactory.createFilesystemCodeBaseLocator(filePath);
		builder.addCodeBase(locator, true);
		builder.build(classPath, new NoOpFindBugsProgress());
		List<ClassDescriptor> classesToAnalyze = builder.getAppClassList();
		AnalysisCacheToAnalysisContextAdapter analysisContext = new AnalysisCacheToAnalysisContextAdapter();
		analysisContext.setFieldSummary(new FieldSummary());
		AnalysisContext.setCurrentAnalysisContext(analysisContext);

		// Digest class descriptors of classes to analyse
		for (ClassDescriptor d : classesToAnalyze) {			
			XClass xclass = Global.getAnalysisCache().getClassAnalysis(XClass.class, d);
			AnalysisContext.currentXFactory().intern(xclass);
		}
		
		// Create detector. Some detectors have constructor logic that expects
		// the analysis context to be prepared, so we create the detector by reflection 
		// after preparing the context.
		Constructor<? extends Detector> detectorConstructor = detectorClass.getConstructor(BugReporter.class);
		Detector detector = detectorConstructor.newInstance(bugReporter);

		// Register bug pattern messages
		I18N.instance().registerBugPattern(bugPattern);
		
		// Wrap in detector2 - FindBugs internals...
		Detector2 detector2 = new DetectorToDetector2Adapter(detector);

		// Perform the analysis
		for (ClassDescriptor d : classesToAnalyze) {
			detector2.visitClass(d);
		}
		
		return classesToAnalyze;
	}



}
