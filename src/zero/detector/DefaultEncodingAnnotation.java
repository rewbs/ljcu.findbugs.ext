package zero.detector;

import edu.umd.cs.findbugs.ba.AnnotationEnumeration;

public class DefaultEncodingAnnotation extends AnnotationEnumeration<DefaultEncodingAnnotation> {

	public final static DefaultEncodingAnnotation DEFAULT_ENCODING = new DefaultEncodingAnnotation("DefaultEncoding", 1);

	private final static DefaultEncodingAnnotation[] myValues = { DEFAULT_ENCODING };

	public static DefaultEncodingAnnotation[] values() {
		return myValues.clone();
	}

	private DefaultEncodingAnnotation(String s, int i) {
		super(s, i);
	}

}
