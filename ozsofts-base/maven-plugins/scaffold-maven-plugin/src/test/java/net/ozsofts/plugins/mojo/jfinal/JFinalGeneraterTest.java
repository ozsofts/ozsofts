package net.ozsofts.plugins.mojo.jfinal;

import static org.junit.Assert.fail;

import java.io.File;

import org.junit.Test;

public class JFinalGeneraterTest {

	@Test
	public void testGenerate() {
		String url = "jdbc:mysql://localhost/wechat";
		String user = "wechat";
		String password = "admin";

		File outputDirectory = new File("/tmp/testJFnal");
		String basePackage = "net.ozsofts.scaffold.test.models";
		String classPrefix = "WX";
		String tablePrefix = "t_wx_";

		try {
			if (!outputDirectory.exists()) {
				outputDirectory.mkdirs();
			}

			JFinalGenerater generater = new JFinalGenerater(url, user, password, basePackage, tablePrefix, classPrefix, outputDirectory);
			generater.generate();
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("should not throw any exceptions!");
		}
	}
}
