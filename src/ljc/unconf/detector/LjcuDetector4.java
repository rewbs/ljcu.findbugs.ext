package ljc.unconf.detector;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;

/**
 * Report a bug if a method called "foo" or "bar" is called.
 */
public class LjcuDetector4  extends OpcodeStackDetector {

	private BugReporter reporter;

	public LjcuDetector4(BugReporter reporter) {
		this.reporter = reporter;
	}
	
	@Override
	public void sawOpcode(int seen) {
		switch(seen) {
		case INVOKEVIRTUAL:
		case INVOKESPECIAL:
		case INVOKESTATIC:
			String invokedMethodName = this.getMethodDescriptorOperand()
					.getName();

			if ("foo".equals(invokedMethodName) || "bar".equals(invokedMethodName)) {
				
				reporter.reportBug(
						new BugInstance("LJCU_BUG_3", Priorities.HIGH_PRIORITY)
						.addClass(this)
						.addMethod(this)
						.addSourceLine(this)
				);
			}
		}
	}
}
