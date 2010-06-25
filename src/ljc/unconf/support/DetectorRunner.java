package ljc.unconf.support;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import ljc.unconf.detector.LjcuDetector5;

import org.soal.findbugs.test.support.DetectorTester;
import org.soal.findbugs.test.support.SimpleBugReporter;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugPattern;
import edu.umd.cs.findbugs.classfile.CheckedAnalysisException;

public class DetectorRunner {

	public static void main(String[] args) throws SecurityException, IllegalArgumentException, CheckedAnalysisException, IOException, InterruptedException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		
		SimpleBugReporter bugReporter = new SimpleBugReporter();
		BugPattern bugPattern = new BugPattern(
				"", "DENC", "0:{0}; 1:{1}; 2:{2}; 3:{3}", false, "", "", "");

		DetectorTester detectorTester = new DetectorTester();
		detectorTester.setBugPattern(bugPattern);
		detectorTester.setBugReporter(bugReporter);
		detectorTester.analyse(LjcuDetector5.class, "test/data/bin");

		System.out.println(format(bugReporter.getBugs()));
	}
	
	private static String format(List<BugInstance> bugs) {
		StringBuilder bugDetails = new StringBuilder();
		for (BugInstance bug : bugs) {
			bugDetails.append(String.format("%s, ", bug.getPrimarySourceLineAnnotation()));
		}
		return bugDetails.toString();
	}
}
