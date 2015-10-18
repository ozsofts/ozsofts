package net.ozsofts.plugins.mojo;

import java.io.File;

import net.ozsofts.plugins.mojo.hibernate.HibernateMojo;

import org.apache.maven.execution.DefaultMavenExecutionRequest;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.plugin.testing.resources.TestResources;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuilder;
import org.apache.maven.project.ProjectBuildingRequest;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

public class HibernateMojoTest /* extends AbstractMojoTestCase */{

	@Rule
	public MojoRule rule = new MojoRule();

	@Rule
	public TestResources resources = new TestResources();

	// @Before
	// protected void setUp() throws Exception {
	// super.setUp();
	// }
	//
	// @After
	// protected void tearDown() throws Exception {
	// super.tearDown();
	// }

	@Test
	public void testHibernate() throws Exception {
		// File pom = getTestFile("src/test/resources/unit/project-to-test/pom.xml");
		// assertNotNull(pom);
		// assertTrue(pom.exists());

		// MavenExecutionRequest executionRequest = new DefaultMavenExecutionRequest();
		// ProjectBuildingRequest buildingRequest = executionRequest.getProjectBuildingRequest();
		// ProjectBuilder projectBuilder = this.lookup(ProjectBuilder.class);
		// MavenProject project = projectBuilder.build(pom, buildingRequest).getProject();
		// assertNotNull(project);

		// HibernateMojo mojo = (HibernateMojo) this.lookupMojo("hibernate", pom);
		// Assert.assertNotNull(mojo);
		// mojo.execute();

		System.out.println("start 2.");

		File projectCopy = this.resources.getBasedir("project-hibernate-mojo");
		File pom = new File(projectCopy, "pom.xml");
		Assert.assertNotNull(pom);
		Assert.assertTrue(pom.exists());

		MavenExecutionRequest executionRequest = new DefaultMavenExecutionRequest();
		ProjectBuildingRequest configuration = executionRequest.getProjectBuildingRequest()
						.setRepositorySession(new DefaultRepositorySystemSession());
		MavenProject project = rule.lookup(ProjectBuilder.class).build(pom, configuration).getProject();

		HibernateMojo mojo = (HibernateMojo) rule.lookupConfiguredMojo(project, "hibernate");
		Assert.assertNotNull(mojo);
		mojo.execute();

		System.out.println("done.");
	}
}
