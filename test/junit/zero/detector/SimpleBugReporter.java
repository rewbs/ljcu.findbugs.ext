package zero.detector;

import java.util.ArrayList;
import java.util.List;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.BugReporterObserver;
import edu.umd.cs.findbugs.ProjectStats;
import edu.umd.cs.findbugs.classfile.ClassDescriptor;
import edu.umd.cs.findbugs.classfile.MethodDescriptor;

class SimpleBugReporter implements BugReporter {
	private ProjectStats stats = new ProjectStats();
	private List<BugInstance> bugs = new ArrayList<BugInstance>();

	public List<BugInstance> getBugs() {
		return bugs;
	}

	public SimpleBugReporter() {
	}

	@Override
	public void addObserver(BugReporterObserver arg0) {
//		observers.add(arg0);
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

	@Override
	public ProjectStats getProjectStats() {
		return stats;
	}

	@Override
	public BugReporter getRealBugReporter() {
		return this;
	}

	@Override
	public void reportBug(BugInstance arg0) {
		bugs.add(arg0);
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