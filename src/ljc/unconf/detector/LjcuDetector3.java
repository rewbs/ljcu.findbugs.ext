package ljc.unconf.detector;

import org.apache.bcel.classfile.Method;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;

/**
 * Report a bug if a method called "foo" or "bar" is declared.
 * Extend a built-in visitor to simplify the code.
 */
public class LjcuDetector3 extends OpcodeStackDetector {

	private BugReporter reporter;

	public LjcuDetector3(BugReporter reporter) {
		this.reporter = reporter;
	}
	
	@Override
	public void visit(Method method) {
		super.visit(method);
				
		String methodName = method.getName();
		
		if ("foo".equals(methodName) || "bar".equals(methodName)) {
			reporter.reportBug(
				new BugInstance("LJCU_BUG_3", Priorities.HIGH_PRIORITY)
					.addClass(this)
					.addMethod(this)
					.addSourceLine(this)
				);
		}
		
	}

	
	@Override
	public void sawOpcode(int seen) {
	}

}
