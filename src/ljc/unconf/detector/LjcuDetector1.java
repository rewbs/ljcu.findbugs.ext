package ljc.unconf.detector;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.ClassContext;

/**
 * Report a bug on every class!
 */
public class LjcuDetector1 implements Detector {

	private BugReporter reporter;

	/**
	 * Instantiated when analysis starts.
	 * @param reporter accumulates detected bugs
	 */
	public LjcuDetector1(BugReporter reporter) {
		this.reporter = reporter;
	}
	
	/**
	 * Invoked for every class that is analyses
	 * @param classContext information about the class under scrutiny 
	 */	
	@Override
	public void visitClassContext(ClassContext classContext) {
		// Report a bug on every class :)
		reporter.reportBug(
				new BugInstance("LJCU_BUG_1", Priorities.HIGH_PRIORITY)
					.addClass(classContext.getClassDescriptor())
					
					// We can add more info to the bug:
					//.addString("my extra data");
				);
	}

	/**
	 * Invoked after all classes have been analysed by all detectors.
	 */	
	@Override
	public void report() {
	}


}
