package zero.detector;

import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import edu.umd.cs.findbugs.BugCode;
import edu.umd.cs.findbugs.BugCollectionBugReporter;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugPattern;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.Detector2;
import edu.umd.cs.findbugs.DetectorFactory;
import edu.umd.cs.findbugs.DetectorFactoryCollectionFacade;
import edu.umd.cs.findbugs.DetectorToDetector2Adapter;
import edu.umd.cs.findbugs.FindBugs2;
import edu.umd.cs.findbugs.I18N;
import edu.umd.cs.findbugs.NoOpFindBugsProgress;
import edu.umd.cs.findbugs.Plugin;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.Project;
import edu.umd.cs.findbugs.ba.AnalysisCacheToAnalysisContextAdapter;
import edu.umd.cs.findbugs.ba.AnalysisContext;
import edu.umd.cs.findbugs.ba.FieldSummary;
import edu.umd.cs.findbugs.ba.XClass;
import edu.umd.cs.findbugs.classfile.CheckedAnalysisException;
import edu.umd.cs.findbugs.classfile.ClassDescriptor;
import edu.umd.cs.findbugs.classfile.Global;
import edu.umd.cs.findbugs.classfile.IAnalysisCache;
import edu.umd.cs.findbugs.classfile.IClassFactory;
import edu.umd.cs.findbugs.classfile.IClassPath;
import edu.umd.cs.findbugs.classfile.IClassPathBuilder;
import edu.umd.cs.findbugs.classfile.ICodeBaseLocator;
import edu.umd.cs.findbugs.classfile.impl.ClassFactory;
import edu.umd.cs.findbugs.config.UserPreferences;

public class DefaultEncodingDetectorTest {

	@Test
	public void testDifferentApproach() throws CheckedAnalysisException, IOException, InterruptedException {

		String filePath = "test/data/bin";
		SimpleBugReporter bugReporter = new SimpleBugReporter();
		
		BugPattern bugPattern = new BugPattern(
				"ZERO_DEFAULT_ENCODING", "DENC", "Reliance on default encoding {0}:{1}", false, "Reliance on default encoding {0}:{1}", "", "");
		


		// register the rules message
		I18N.instance().registerBugPattern(bugPattern);

		// a great deal of code to say 'analyze the files in this directory'
		IClassFactory classFactory = ClassFactory.instance();
		IClassPath classPath = classFactory.createClassPath();
		IAnalysisCache analysisCache = classFactory.createAnalysisCache(classPath, bugReporter);
		Global.setAnalysisCacheForCurrentThread(analysisCache);
		FindBugs2.registerBuiltInAnalysisEngines(analysisCache);
		IClassPathBuilder builder = classFactory.createClassPathBuilder(bugReporter);
		ICodeBaseLocator locator = classFactory.createFilesystemCodeBaseLocator(filePath);
		builder.addCodeBase(locator, true);
		builder.build(classPath, new NoOpFindBugsProgress());
		List<ClassDescriptor> classesToAnalyze = builder.getAppClassList();
		AnalysisCacheToAnalysisContextAdapter analysisContext = new AnalysisCacheToAnalysisContextAdapter();
		analysisContext.setFieldSummary(new FieldSummary());
		AnalysisContext.setCurrentAnalysisContext(analysisContext);

		Detector detector = new DefaultEncodingDetector(bugReporter);
		Detector2 det = new DetectorToDetector2Adapter(detector);
		
		// digest class descriptors
		for (ClassDescriptor d : classesToAnalyze) {
			XClass info = Global.getAnalysisCache().getClassAnalysis(XClass.class, d);
			AnalysisContext.currentXFactory().intern(info);
		}
		
		// finally, perform the analysis
		for (ClassDescriptor d : classesToAnalyze) {
			det.visitClass(d);
		}

		System.out.println(bugReporter.getBugs().size());
		for (BugInstance b : bugReporter.getBugs()) {
			System.out.println(b.getAnnotations());
		}

	}

	@Ignore @Test
	public void testAllRegressionFiles() throws IOException,
			InterruptedException {

		// Create a new findbugs engine
		FindBugs2 engine = new FindBugs2();

		// Point the engine to the class files to run against
		Project project = new Project();
		project.addFile("bin");
		engine.setProject(project);

		// Set up a bug reporter
		BugCollectionBugReporter bugReporter = new BugCollectionBugReporter(
				project);
		bugReporter.setPriorityThreshold(Priorities.LOW_PRIORITY);
		engine.setBugReporter(bugReporter);

		// Initialise the detector under test

		Plugin fakePlugin = new Plugin("edu.umd.cs.findbugs.fakeplugin", null);

		fakePlugin.setEnabled(true);
		fakePlugin.addBugCode(new BugCode("DENC",
				"Reliance on default encoding"));
		fakePlugin.addBugPattern(new BugPattern("ZERO_DEFAULT_ENCODING",
				"DENC", "BAD_PRACTICE", false, "Reliance", "foo", "bah"));
		DetectorFactory df = new DetectorFactory(fakePlugin,
				DefaultEncodingDetector.class, true, "fast", "", "");
		fakePlugin.addDetectorFactory(df);
		engine.setDetectorFactoryCollection(DetectorFactoryCollectionFacade
				.resetDetectorFactoryCollection(fakePlugin));

		UserPreferences preferences = UserPreferences
				.createDefaultUserPreferences();
		preferences.enableDetector(df, true);
		// preferences.getFilterSettings().clearAllCategories();
		engine.setUserPreferences(preferences);

		// Begin analysis
		engine.execute();
		System.out.println(engine.getBugCount());

		// Analyse reported bugs to ensure they are as expected
		assertFalse(
				"No bugs were reported. Something is wrong with the configuration",
				bugReporter.getBugCollection().getCollection().isEmpty());

		for (BugInstance bug : bugReporter.getBugCollection()) {
			System.out.println(bug.getMessageWithPriorityTypeAbbreviation());
			System.out.println("  " + bug.getPrimarySourceLineAnnotation());
		}

		/*
		 * setUpEngine();
		 * 
		 * engine.execute();
		 * 
		 * // If there are zero bugs, then something's wrong assertFalse(
		 * "No bugs were reported. Something is wrong with the configuration",
		 * bugReporter.getBugCollection().getCollection().isEmpty());
		 * 
		 * 
		 * List<BugInstance> unexpectedBugs = new ArrayList<BugInstance>(); for
		 * (BugInstance bug : bugReporter.getBugCollection()) { if
		 * (isUnexpectedBug(bug) && bug.getPriority() ==
		 * Priorities.HIGH_PRIORITY) { unexpectedBugs.add(bug);
		 * System.out.println(bug.getMessageWithPriorityTypeAbbreviation());
		 * System.out.println("  " + bug.getPrimarySourceLineAnnotation()); } }
		 * 
		 * if (!unexpectedBugs.isEmpty()) Assert.fail("Unexpected bugs (" +
		 * unexpectedBugs.size() + "):"+ getBugsLocations(unexpectedBugs));
		 */
	}

	// /**
	// * Returns a printable String concatenating bug locations.
	// */
	// private String getBugsLocations(List<BugInstance> unexpectedBugs) {
	// StringBuilder message = new StringBuilder();
	// for (BugInstance bugInstance : unexpectedBugs) {
	// message.append("\n");
	// if
	// (bugInstance.getBugPattern().getType().equals(FB_MISSING_EXPECTED_WARNING))
	// message.append("missing " );
	// else
	// message.append("unexpected " );
	// StringAnnotation pattern = (StringAnnotation)
	// bugInstance.getAnnotations().get(2);
	// message.append(pattern.getValue());
	// message.append(" ");
	// message.append(bugInstance.getPrimarySourceLineAnnotation());
	// }
	// return message.toString();
	// }

	// /**
	// * Returns if a bug instance is unexpected for this test.
	// */
	// private boolean isUnexpectedBug(BugInstance bug) {
	// return FB_MISSING_EXPECTED_WARNING.equals(bug.getType())
	// || FB_UNEXPECTED_WARNING.equals(bug.getType());
	// }
	//
	// /**
	// * Loads the default detectors from findbugs.jar, to isolate the test from
	// * others that use fake plugins.
	// */
	// private void loadFindbugsPlugin() throws MalformedURLException {
	// File workingDir = new File(System.getProperty("user.dir"));
	// File findbugsJar = new File(workingDir.getParentFile(),
	// "findbugs/build/lib/findbugs.jar");
	// URL[] pluginList = new URL[] { findbugsJar.toURI().toURL() };
	// DetectorFactoryCollection dfc = new DetectorFactoryCollection();
	// dfc.setPluginList(pluginList);
	// DetectorFactoryCollection.resetInstance(dfc);
	// }

	/**
	 * Sets up a FB engine to run on the 'findbugsTestCases' project. It enables
	 * all the available detectors and reports all the bug categories. Uses a
	 * normal priority threshold.
	 */
	// private void setUpEngine() {
	// this.engine = new FindBugs2();
	// Project project = new Project();
	// project.setProjectName("findbugsTestCases");
	// this.engine.setProject(project);
	//
	// DetectorFactoryCollection detectorFactoryCollection =
	// DetectorFactoryCollection
	// .instance();
	// engine.setDetectorFactoryCollection(detectorFactoryCollection);
	//		
	// bugReporter = new BugCollectionBugReporter(project);
	// bugReporter.setPriorityThreshold(Priorities.LOW_PRIORITY);
	//		
	// engine.setBugReporter(this.bugReporter);
	// UserPreferences preferences =
	// UserPreferences.createDefaultUserPreferences();
	// DetectorFactory checkExpectedWarnings =
	// DetectorFactoryCollection.instance().getFactory("CheckExpectedWarnings");
	// preferences.enableDetector(checkExpectedWarnings, true);
	// preferences.getFilterSettings().clearAllCategories();
	// this.engine.setUserPreferences(preferences);
	//
	//
	// // This is ugly. We should think how to improve this.
	// project.addFile("../findbugsTestCases/build/classes/");
	// project.addAuxClasspathEntry("../findbugsTestCases/lib/j2ee.jar");
	// project.addAuxClasspathEntry("lib/junit.jar");
	// project.addAuxClasspathEntry("../findbugs/lib/jsr305.jar");
	// project.addAuxClasspathEntry("../findbugs/lib/annotations.jar");
	// }

}
