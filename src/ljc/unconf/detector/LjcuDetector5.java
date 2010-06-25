package ljc.unconf.detector;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantString;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;

/**
 * Report a bug if a String constant containing "foo" is declared.
 */
public class LjcuDetector5 extends OpcodeStackDetector {

	private static final String FORBIDDEN_STRING = "foo";
	private BugReporter reporter;

	public LjcuDetector5(BugReporter reporter) {
		this.reporter = reporter;
	}

	@Override
	public void sawOpcode(int seen) {
		
		if (seen != Constants.LDC
				&& seen != Constants.LDC_W) {
			// If this bytecode is not a load constant,
			// we're not interested.			
			return;
		}

		Constant operand = this.getConstantRefOperand();
		if (!(operand instanceof ConstantString)) {
			// If the constant being loaded is not a String,
			// we're not interested.			
			return;
		}
			
		String value = ((ConstantString)operand).getBytes(this.getConstantPool());
		if (value.contains(FORBIDDEN_STRING)) {
			// The String litteral being loaded is forbidden!
			// Report Bug.
			reporter.reportBug(
				new BugInstance("LJCU_BUG_5", Priorities.HIGH_PRIORITY)
				.addClass(this)
				.addMethod(this)
				.addSourceLine(this)
				.addString(value)
			);			
		}
		
	}


}
