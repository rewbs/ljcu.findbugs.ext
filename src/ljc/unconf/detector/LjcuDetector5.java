package ljc.unconf.detector;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.ClassContext;

public class LjcuDetector5 implements Detector {

	private BugReporter reporter;

	public LjcuDetector5(BugReporter reporter) {
		this.reporter = reporter;
	}
	
	@Override
	public void visitClassContext(ClassContext classContext) {
	}

	@Override
	public void report() {
	}

}
