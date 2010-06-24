package ljc.unconf.detector;

import org.apache.bcel.classfile.Method;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.PreorderDetector;

public class LjcuDetector2 extends PreorderDetector {

	private BugReporter reporter;

	public LjcuDetector2(BugReporter reporter) {
		this.reporter = reporter;
	}
	
	@Override
	public void visitMethod(Method obj) {
		super.visitMethod(obj);
		
		reporter.reportBug(
			new BugInstance("LJCU_BUG_2", Priorities.HIGH_PRIORITY)
			.addClass(this)
			.addMethod(this)
			);
		
	}

}
