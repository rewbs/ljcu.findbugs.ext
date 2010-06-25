package ljc.unconf.detector;

import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.SourceLineAnnotation;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.classfile.DescriptorFactory;

/**
 * Report a bug if a method called "foo" or "bar" is declared.
 */
public class LjcuDetector2 implements Detector {

	private BugReporter reporter;

	/**
	 * Instantiated when analysis starts.
	 * @param reporter accumulates detected bugs
	 */
	public LjcuDetector2(BugReporter reporter) {
		this.reporter = reporter;
	}
	
	/**
	 * Invoked for every class that is analyses
	 * @param classContext information about the class under scrutiny 
	 */	
	@Override
	public void visitClassContext(ClassContext classContext) {
		// Obtain BCEL class object
		JavaClass javaClass = classContext.getJavaClass();
		
		// Check BCEL method objects
		for (Method method : javaClass.getMethods()) {
			String methodName = method.getName();
			
			if ("foo".equals(methodName) || "bar".equals(methodName)) {
			
				BugInstance bug = new BugInstance("LJCU_BUG_2", Priorities.HIGH_PRIORITY)
					.addClass(classContext.getClassDescriptor())
					.addMethod(javaClass, method)
					.addSourceLine(
							SourceLineAnnotation.forFirstLineOfMethod(
									DescriptorFactory.instance()
										.getMethodDescriptor(javaClass, method)
							));
				
				reporter.reportBug(bug);
			}
		}
		

	}

	/**
	 * Invoked after all classes have been analysed by all detectors.
	 */	
	@Override
	public void report() {
	}


}
