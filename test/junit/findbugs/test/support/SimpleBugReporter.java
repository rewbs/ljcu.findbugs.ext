package findbugs.test.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.BugReporterObserver;
import edu.umd.cs.findbugs.MethodAnnotation;
import edu.umd.cs.findbugs.ProjectStats;
import edu.umd.cs.findbugs.classfile.ClassDescriptor;
import edu.umd.cs.findbugs.classfile.MethodDescriptor;

public class SimpleBugReporter implements BugReporter {
	private List<BugInstance> bugs = new ArrayList<BugInstance>();

	private Map<MethodAnnotation, List<BugInstance>> bugsPerMethod = new HashMap<MethodAnnotation, List<BugInstance>>(); 
	
	public SimpleBugReporter() {
	}
	
	public List<BugInstance> getBugs() {
		return bugs;
	}
	
	public Map<MethodAnnotation, List<BugInstance>> getBugsPerMethod() {
		return bugsPerMethod;
	}

	@Override
	public void addObserver(BugReporterObserver arg0) {
	}

	@Override
	public void finish() {
	}

	@Override
	public ProjectStats getProjectStats() {
		return new ProjectStats();
	}

	@Override
	public BugReporter getRealBugReporter() {
		return this;
	}

	@Override
	public void reportBug(BugInstance bug) {
		// Add bug to flat list of bugs
		bugs.add(bug);

		// Add bug per-method list of bugs
		MethodAnnotation buggyMethod = bug.getPrimaryMethod();
		if (!bugsPerMethod.containsKey(buggyMethod)) {
			bugsPerMethod.put(buggyMethod, new ArrayList<BugInstance>());
		}
		List<BugInstance> methodBugs = bugsPerMethod.get(buggyMethod);
		methodBugs.add(bug);
		
	}

	@Override
	public void reportQueuedErrors() {
	}

	@Override
	public void setErrorVerbosity(int arg0) {
	}

	@Override
	public void setPriorityThreshold(int arg0) {
	}

	@Override
	public void logError(String arg0) {
		System.err.println(arg0);
	}

	@Override
	public void logError(String arg0, Throwable arg1) {
		System.err.println(arg0);
		arg1.printStackTrace(System.err);
	}

	@Override
	public void reportMissingClass(ClassNotFoundException arg0) {
		arg0.printStackTrace(System.err);
	}

	@Override
	public void reportMissingClass(ClassDescriptor arg0) {
		System.err.println("Class not found: " + arg0);
	}

	@Override
	public void reportSkippedAnalysis(MethodDescriptor arg0) {
		System.err.println("Skipped Method: " + arg0);
	}

	@Override
	public void observeClass(ClassDescriptor arg0) {
	}

}