package org.soal.detector;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;
import org.soal.findbugs.test.support.DetectorTester;
import org.soal.findbugs.test.support.ExpectBug;
import org.soal.findbugs.test.support.SimpleBugReporter;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugPattern;
import edu.umd.cs.findbugs.MethodAnnotation;
import edu.umd.cs.findbugs.ba.XClass;
import edu.umd.cs.findbugs.ba.XMethod;
import edu.umd.cs.findbugs.classfile.CheckedAnalysisException;
import edu.umd.cs.findbugs.classfile.ClassDescriptor;
import edu.umd.cs.findbugs.classfile.DescriptorFactory;
import edu.umd.cs.findbugs.classfile.Global;
import edu.umd.cs.findbugs.classfile.analysis.AnnotationValue;

public class DefaultEncodingDetectorTest {

	/**
	 * Run FindBugs analysis on <code>DefaultEncodingTestData</code> with detector 
	 *
	 * @throws CheckedAnalysisException the checked analysis exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws InterruptedException the interrupted exception
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws NoSuchMethodException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 */
	@Test
	public void testDetector() throws CheckedAnalysisException, IOException, InterruptedException, SecurityException, IllegalArgumentException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {

		// Prepare detector to analyse
		String classPathToAnalyse = "test/data/bin";
		SimpleBugReporter bugReporter = new SimpleBugReporter();
		BugPattern bugPattern = new BugPattern(
				"SOAL_DEFAULT_ENCODING", "DENC", "Reliance on default encoding {0}:{1}", false, "Reliance on default encoding {0}:{1}", "", "");

		// Prepare engine for analysis of our detector 
		DetectorTester detectorTester = new DetectorTester();
		detectorTester.setBugPattern(bugPattern);
		detectorTester.setBugReporter(bugReporter);
		
		// Perform analysis
		List<ClassDescriptor> analysedClasses =
			detectorTester.analyse(DefaultEncodingDetector.class, classPathToAnalyse);
		
		
		// Find all expected bugs in analysed classes
		Map<MethodAnnotation, Integer> expectedBugs = new HashMap<MethodAnnotation, Integer>();
		for (ClassDescriptor d : analysedClasses) {	
			XClass xclass = Global.getAnalysisCache().getClassAnalysis(XClass.class, d);
			for (XMethod m: xclass.getXMethods()) {
				MethodAnnotation method = MethodAnnotation.fromXMethod(m);
				AnnotationValue expectedBug = m.getAnnotation(DescriptorFactory.createClassDescriptor(ExpectBug.class));
				if (expectedBug != null) {
					expectedBugs.put(method, (Integer)expectedBug.getValue("occurrences"));
				}
			}
		}
			
		// Check all detected bugs were expected
		List<String> failures = new ArrayList<String>();  
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
		
		// Check all expected bugs were detected
		for (Entry<MethodAnnotation, Integer> entry : expectedBugs.entrySet()) {
			failures.add(String.format("%s, expected %d bug(s), 0 detected.\n",
					entry.getKey(), entry.getValue()));
		}

		// Report unexpected / missing failures		
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
