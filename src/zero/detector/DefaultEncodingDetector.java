package zero.detector;

import java.util.HashSet;
import java.util.Set;

import org.apache.bcel.classfile.Code;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.MethodAnnotation;
import edu.umd.cs.findbugs.ba.XFactory;
import edu.umd.cs.findbugs.ba.XMethod;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;

/**
 *<p>
 * Flags up invocations of JCL methods that rely on the default platform
 * encoding.
 *</p>
 *<p>
 * If a Java application assumes that the default platform encoding is
 * acceptable, the app's behaviour will vary from platform to platform. In
 * particular, conversions between byte[] and java.lang.String (in either
 * direction) may yield inconsistent results. To ensure Java code is portable,
 * the desired encoding should be specified explicitly wherever such a
 * conversion takes place.
 *</p>
 *<p>
 * This FindBugs pattern detects invocations of Java Class Library methods and
 * constructors that are known to use the default platform encoding.
 *</p>
 */
public class DefaultEncodingDetector extends OpcodeStackDetector {
	
	//private final BugAccumulator bugAccumulator;
	private final BugReporter bugReporter;
	private final Set<String> culprits = new HashSet<String>();
	private DefaultEncodingAnnotationDatabase defaultEncodingAnnotationDatabase;
	
	public DefaultEncodingDetector(BugReporter bugReporter) {
		//this.bugAccumulator = new BugAccumulator(bugReporter);
		this.bugReporter = bugReporter;
//		this.culprits.add("java.lang.String.getBytes:()[B");
//		this.culprits.add("java.io.FileReader.<init>:(Ljava/lang/String)V");
//		this.culprits.add("java.io.FileWriter.<init>:(Ljava/lang/String)V");
//		this.culprits.add("java.lang.String.<init>:([B)V");
//		this.culprits.add("java.lang.String.<init>:([BII)V");
//		this.culprits.add("java.io.ByteArrayOutputStream.toString:()Ljava/lang/String;");
//		this.culprits.add("java.io.FileReader.<init>:(Ljava/lang/String;)V");
//		this.culprits.add("java.io.FileReader.<init>:(Ljava/io/File;)V");
//		this.culprits.add("java.io.FileReader.<init>:(Ljava/io/FileDescriptor;)V");
//		this.culprits.add("java.io.FileWriter.<init>:(Ljava/lang/String;)V");
//		this.culprits.add("java.io.FileWriter.<init>:(Ljava/io/File;)V");
//		this.culprits.add("java.io.FileWriter.<init>:(Ljava/io/FileDescriptor;)V");
//		this.culprits.add("java.io.InputStreamReader.<init>:(Ljava/io/InputStream;)V");
//		this.culprits.add("java.io.OutputStreamWriter.<init>:(Ljava/io/OutputStream;)V");
//		this.culprits.add("java.io.PrintStream.<init>:(Ljava/io/File;)V");
//		this.culprits.add("java.io.PrintStream.<init>:(Ljava/io/OutputStream;)V");
//		this.culprits.add("java.io.PrintStream.<init>:(Ljava/io/OutputStream;Z)V");
//		this.culprits.add("java.io.PrintStream.<init>:(Ljava/lang/String;)V");
//		this.culprits.add("java.io.PrintWriter.<init>:(Ljava/io/File;)V");
//		this.culprits.add("java.io.PrintWriter.<init>:(Ljava/io/OutputStream;)V");
//		this.culprits.add("java.io.PrintWriter.<init>:(Ljava/io/OutputStream;Z)V");
//		this.culprits.add("java.io.PrintWriter.<init>:(Ljava/lang/String;)V");
//		this.culprits.add("java.util.Scanner.<init>:(Ljava/io/InputStream;)V");
//		this.culprits.add("java.util.Formatter.<init>:(Ljava/lang/String;)V");
//		this.culprits.add("java.util.Formatter.<init>:(Ljava/io/File;)V");
//		this.culprits.add("java.util.Formatter.<init>:(Ljava/io/OutputStream;)V");
		this.defaultEncodingAnnotationDatabase = new DefaultEncodingAnnotationDatabase();		
	}

	@Override
	public void visitCode(Code code) {
		super.visitCode(code);
	}

	@Override
	public void sawOpcode(int seen) {
		switch(seen) {
		case INVOKEVIRTUAL:
		case INVOKESPECIAL:
		case INVOKESTATIC:		
			XMethod callSeen = XFactory.createXMethod(MethodAnnotation.fromCalledMethod(this));
			
			DefaultEncodingAnnotation annotation = defaultEncodingAnnotationDatabase.getDirectAnnotation(callSeen);
			//if (this.culprits.contains(invokedMethod)) {
			if (annotation != null) {
				bugReporter.reportBug(
						new BugInstance(this, "ZERO_DEFAULT_ENCODING", HIGH_PRIORITY)
						.addClassAndMethod(this)
						.addCalledMethod(this)
						.addSourceLine(this));
			}
		}
	}
}
