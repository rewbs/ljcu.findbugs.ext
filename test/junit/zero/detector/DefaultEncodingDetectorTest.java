package zero.detector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugPattern;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.Detector2;
import edu.umd.cs.findbugs.DetectorToDetector2Adapter;
import edu.umd.cs.findbugs.FindBugs2;
import edu.umd.cs.findbugs.I18N;
import edu.umd.cs.findbugs.MethodAnnotation;
import edu.umd.cs.findbugs.NoOpFindBugsProgress;
import edu.umd.cs.findbugs.ba.AnalysisCacheToAnalysisContextAdapter;
import edu.umd.cs.findbugs.ba.AnalysisContext;
import edu.umd.cs.findbugs.ba.FieldSummary;
import edu.umd.cs.findbugs.ba.XClass;
import edu.umd.cs.findbugs.ba.XMethod;
import edu.umd.cs.findbugs.classfile.CheckedAnalysisException;
import edu.umd.cs.findbugs.classfile.ClassDescriptor;
import edu.umd.cs.findbugs.classfile.DescriptorFactory;
import edu.umd.cs.findbugs.classfile.Global;
import edu.umd.cs.findbugs.classfile.IAnalysisCache;
import edu.umd.cs.findbugs.classfile.IClassFactory;
import edu.umd.cs.findbugs.classfile.IClassPath;
import edu.umd.cs.findbugs.classfile.IClassPathBuilder;
import edu.umd.cs.findbugs.classfile.ICodeBaseLocator;
import edu.umd.cs.findbugs.classfile.analysis.AnnotationValue;
import edu.umd.cs.findbugs.classfile.impl.ClassFactory;
import findbugs.test.support.ExpectBug;
import findbugs.test.support.SimpleBugReporter;

public class DefaultEncodingDetectorTest {

	@Test
	public void testTheUniverse() throws CheckedAnalysisException, IOException, InterruptedException {

		String filePath = "test/data/bin";
		SimpleBugReporter bugReporter = new SimpleBugReporter();
		
		BugPattern bugPattern = new BugPattern(
				"ZERO_DEFAULT_ENCODING", "DENC", "Reliance on default encoding {0}:{1}", false, "Reliance on default encoding {0}:{1}", "", "");

		// register the rules message
		I18N.instance().registerBugPattern(bugPattern);

		// a great deal of code to say 'analyze the files in this directory'
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

		Detector detector = new DefaultEncodingDetector(bugReporter);
		Detector2 det = new DetectorToDetector2Adapter(detector);

		// digest class descriptors (required by findbugs)
		for (ClassDescriptor d : classesToAnalyze) {			
			XClass xclass = Global.getAnalysisCache().getClassAnalysis(XClass.class, d);
			AnalysisContext.currentXFactory().intern(xclass);
		}
		
		// find all expected bugs
		Map<MethodAnnotation, Integer> expectedBugs = new HashMap<MethodAnnotation, Integer>();
		for (ClassDescriptor d : classesToAnalyze) {	
			XClass xclass = Global.getAnalysisCache().getClassAnalysis(XClass.class, d);
			for (XMethod m: xclass.getXMethods()) {
				MethodAnnotation method = MethodAnnotation.fromXMethod(m);
				AnnotationValue expectedBug = m.getAnnotation(DescriptorFactory.createClassDescriptor(ExpectBug.class));
				if (expectedBug != null) {
					expectedBugs.put(method, (Integer)expectedBug.getValue("occurrences"));
				}
			}
		}
		
		// finally, perform the analysis
		for (ClassDescriptor d : classesToAnalyze) {
			det.visitClass(d);
		}
		
		List<String> failures = new ArrayList<String>();  
		
		// Check all found bugs are expected
		for (Entry<MethodAnnotation, List<BugInstance>> entry : bugReporter.getBugsPerMethod().entrySet()) {
			MethodAnnotation buggyMethod = entry.getKey();
			List<BugInstance> foundBugs = entry.getValue();
			int numFoundBugs = foundBugs.size();
			
			int numExpectedBugs = 0;
			if (expectedBugs.containsKey(buggyMethod)) {
				numExpectedBugs = expectedBugs.remove(buggyMethod);
			} 
			
			if (numExpectedBugs != numFoundBugs) {
				failures.add(String.format("%s, expected %d bug(s), %d detected %s\n",
						buggyMethod, numExpectedBugs, numFoundBugs, format(foundBugs)));
			}
		}
		
		for (Entry<MethodAnnotation, Integer> entry : expectedBugs.entrySet()) {
			failures.add(String.format("%s, expected %d bug(s), 0 detected.\n",
					entry.getKey(), entry.getValue()));
		}

		if (failures.size() > 0) {
			System.out.println(failures.toString());
			throw new RuntimeException("Failures not as expected - see stdout for details.");
		}
	}

	private String format(List<BugInstance> bugs) {
		StringBuilder bugDetails = new StringBuilder();
		for (BugInstance bug : bugs) {
			bugDetails.append(String.format("%s, ", bug.getPrimarySourceLineAnnotation()));
		}
		return bugDetails.toString();
	}
}
