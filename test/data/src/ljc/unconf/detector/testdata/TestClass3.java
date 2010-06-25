package ljc.unconf.detector.testdata;

public class TestClass3 {
	
	public String ok() {
		return new TestClass2().foo();
	}
	
	public String alright() {
		return TestClass2.bar();
	}
}
