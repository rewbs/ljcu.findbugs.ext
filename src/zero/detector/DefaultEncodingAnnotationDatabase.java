package zero.detector;

import edu.umd.cs.findbugs.ba.AnalysisContext;
import edu.umd.cs.findbugs.ba.AnnotationDatabase;

public class DefaultEncodingAnnotationDatabase extends AnnotationDatabase<DefaultEncodingAnnotation> {

	public DefaultEncodingAnnotationDatabase() {
		this.setAddClassOnly(false);
		this.loadAuxiliaryAnnotations();
	}

	@Override
	public void loadAuxiliaryAnnotations() {
		if (IGNORE_BUILTIN_ANNOTATIONS) return;
//		AnalysisContext currentAnalysisContext = AnalysisContext.currentAnalysisContext();
//		boolean missingClassWarningsSuppressed;
//		if (currentAnalysisContext != null) {
//		= AnalysisContext.currentAnalysisContext().setMissingClassWarningsSuppressed(true);

		addMethodAnnotation("java.lang.String","getBytes", "()[B", false, DefaultEncodingAnnotation.DEFAULT_ENCODING);
		addMethodAnnotation("java.io.FileReader","<init>", "(Ljava/lang/String)V", false, DefaultEncodingAnnotation.DEFAULT_ENCODING);
		addMethodAnnotation("java.io.FileWriter","<init>", "(Ljava/lang/String)V", false, DefaultEncodingAnnotation.DEFAULT_ENCODING);
		addMethodAnnotation("java.lang.String","<init>", "([B)V", false, DefaultEncodingAnnotation.DEFAULT_ENCODING);
		addMethodAnnotation("java.lang.String","<init>", "([BII)V", false, DefaultEncodingAnnotation.DEFAULT_ENCODING);
		addMethodAnnotation("java.io.ByteArrayOutputStream","toString", "()Ljava/lang/String;", false, DefaultEncodingAnnotation.DEFAULT_ENCODING);
		addMethodAnnotation("java.io.FileReader", "<init>", "(Ljava/lang/String;)V", false, DefaultEncodingAnnotation.DEFAULT_ENCODING);
		addMethodAnnotation("java.io.FileReader", "<init>", "(Ljava/io/File;)V", false, DefaultEncodingAnnotation.DEFAULT_ENCODING);
		addMethodAnnotation("java.io.FileReader", "<init>", "(Ljava/io/FileDescriptor;)V", false, DefaultEncodingAnnotation.DEFAULT_ENCODING);
		addMethodAnnotation("java.io.FileWriter", "<init>", "(Ljava/lang/String;)V", false, DefaultEncodingAnnotation.DEFAULT_ENCODING);
		addMethodAnnotation("java.io.FileWriter", "<init>", "(Ljava/io/File;)V", false, DefaultEncodingAnnotation.DEFAULT_ENCODING);
		addMethodAnnotation("java.io.FileWriter", "<init>", "(Ljava/io/FileDescriptor;)V", false, DefaultEncodingAnnotation.DEFAULT_ENCODING);
		addMethodAnnotation("java.io.InputStreamReader", "<init>", "(Ljava/io/InputStream;)V", false, DefaultEncodingAnnotation.DEFAULT_ENCODING);
		addMethodAnnotation("java.io.OutputStreamWriter", "<init>", "(Ljava/io/OutputStream;)V", false, DefaultEncodingAnnotation.DEFAULT_ENCODING);
		addMethodAnnotation("java.io.PrintStream", "<init>", "(Ljava/io/File;)V", false, DefaultEncodingAnnotation.DEFAULT_ENCODING);
		addMethodAnnotation("java.io.PrintStream", "<init>", "(Ljava/io/OutputStream;)V", false, DefaultEncodingAnnotation.DEFAULT_ENCODING);
		addMethodAnnotation("java.io.PrintStream", "<init>", "(Ljava/io/OutputStream;Z)V", false, DefaultEncodingAnnotation.DEFAULT_ENCODING);
		addMethodAnnotation("java.io.PrintStream", "<init>", "(Ljava/lang/String;)V", false, DefaultEncodingAnnotation.DEFAULT_ENCODING);
		addMethodAnnotation("java.io.PrintWriter", "<init>", "(Ljava/io/File;)V", false, DefaultEncodingAnnotation.DEFAULT_ENCODING);
		addMethodAnnotation("java.io.PrintWriter", "<init>", "(Ljava/io/OutputStream;)V", false, DefaultEncodingAnnotation.DEFAULT_ENCODING);
		addMethodAnnotation("java.io.PrintWriter", "<init>", "(Ljava/io/OutputStream;Z)V", false, DefaultEncodingAnnotation.DEFAULT_ENCODING);
		addMethodAnnotation("java.io.PrintWriter", "<init>", "(Ljava/lang/String;)V", false, DefaultEncodingAnnotation.DEFAULT_ENCODING);
		addMethodAnnotation("java.util.Scanner", "<init>", "(Ljava/io/InputStream;)V", false, DefaultEncodingAnnotation.DEFAULT_ENCODING);
		addMethodAnnotation("java.util.Formatter", "<init>", "(Ljava/lang/String;)V", false, DefaultEncodingAnnotation.DEFAULT_ENCODING);
		addMethodAnnotation("java.util.Formatter", "<init>", "(Ljava/io/File;)V", false, DefaultEncodingAnnotation.DEFAULT_ENCODING);
		addMethodAnnotation("java.util.Formatter", "<init>", "(Ljava/io/OutputStream;)V", false, DefaultEncodingAnnotation.DEFAULT_ENCODING);

//		AnalysisContext.currentAnalysisContext().setMissingClassWarningsSuppressed(missingClassWarningsSuppressed);

	}

}