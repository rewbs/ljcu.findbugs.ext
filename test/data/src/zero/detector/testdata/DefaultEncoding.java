package zero.detector.testdata;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Formatter;
import java.util.Locale;
import java.util.Scanner;

import edu.umd.cs.findbugs.annotations.ExpectWarning;

public class DefaultEncoding {

	public class MyBAOS extends ByteArrayOutputStream {
		
		@ExpectWarning("DENC")
		public void bar() {
			// Problem - should be flagged up
			this.toString();
		}

	}
	
	public class MyOtherBAOS extends ByteArrayOutputStream {
		
		public void foo() {
			// Problem - should be flagged up
			super.toString();
		}
		
		public String toString() {
			try {
				// not a problem - shouldn't be flagged up
				return super.toString("UTF-8");
			} catch (UnsupportedEncodingException e) {
			}
			return "";
		}
		
		public void bar() {
			// not a problem - shouldn't be flagged up
			this.toString();
		}

	}
	
//	public static class SubFile extends File {
//
//		/**
//		 * 
//		 */
//		private static final long serialVersionUID = 1L;
//
//		public SubFile(String pathname) {
//			super(pathname);
//		}
//
//		
//	}
//	
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		String a = "hello";
//		
//		new SubFile(".").delete();
//		
//		//Bad
//		try {
//			a.getBytes();
//			new String(new byte[]{});
//			new String(new byte[]{}, 0, 0);
//			(new ByteArrayOutputStream()).toString();			
//			new FileReader("");
//			new FileReader(new File(""));
//			new FileReader(new FileDescriptor());
//			new FileWriter("");
//			new FileWriter(new File(""));
//			new FileWriter(new FileDescriptor());	
//			new InputStreamReader(new FileInputStream(""));
//			new OutputStreamWriter(new FileOutputStream(""));
//			new PrintStream(new File(""));
//			new PrintStream(new FileOutputStream(""));
//			new PrintStream(new FileOutputStream(""), true);
//			new PrintStream("");
//			new PrintWriter(new File(""));
//			new PrintWriter(new FileOutputStream(""));
//			new PrintWriter(new FileOutputStream(""), true);
//			new PrintWriter("");
//			a.getBytes();
//			new Scanner(new FileInputStream(""));
//			new Formatter("");
//			new Formatter(new File(""));
//			new Formatter(new FileOutputStream(""));
//			"".toUpperCase();
//			 		
//		} catch (Exception e) {
//		}
//		
//
//		
//		//OK:
//		try {
//			a.getBytes(Charset.forName("UTF-8"));
//			a.getBytes("UTF-8");
//			new String(new byte[]{}, "UTF-8");
//			new String(new byte[]{}, 0, 0, "UTF-8");
//			a.getBytes("UTF-8");
//			(new ByteArrayOutputStream()).toString("UTF-8");			
//			new InputStreamReader(new FileInputStream(""), "UTF-8");
//			new OutputStreamWriter(new FileOutputStream(""), "UTF-8");
//			new PrintStream(new File(""), "UTF-8");
//			new PrintStream(new FileOutputStream(""), true, "UTF-8");
//			new PrintStream("", "UTF-8");
//			new PrintWriter(new File(""), "UTF-8");
//			new PrintWriter("", "UTF-8");
//			new Scanner(new FileInputStream(""), "UTF-8");
//			new Formatter("", "UTF-8");
//			new Formatter(new File(""), "UTF-8");
//			new Formatter(new FileOutputStream(""), "UTF-8");
//			"".toUpperCase(Locale.CANADA);
//			"".toLowerCase(Locale.CANADA);			
//		} catch (Exception e) {
//		}
//	}

}
