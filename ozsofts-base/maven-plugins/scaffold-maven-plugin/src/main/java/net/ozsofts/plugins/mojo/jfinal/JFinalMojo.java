package net.ozsofts.plugins.mojo.jfinal;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * <p>
 * 根据已经生成的BaseEntity类生成所有数据操作需要的Entity类、DAO接口和实现以及Manager接口和实现。
 * <p>
 * 在自动处理模式下，BaseEntity类所在的包名一定要遵循xxx.xxx.xxx.entity.base的方式，这样所有生成的类名和包名如下所示：
 * <table border="1">
 * <thead>
 * <td>对象名称</td>
 * <td>生成包名</td>
 * <td>生成类名</td>
 * <td>Spring Bean名称</td></thead>
 * <tr>
 * <td>Bean类</td>
 * <td>xxx.xxx.xxx.entity</td>
 * <td>Entity</td>
 * <td>N/A</td>
 * </tr>
 * <tr>
 * <td>Dao接口</td>
 * <td>xxx.xxx.xxx.dao</td>
 * <td>EntityDao</td>
 * <td>N/A</td>
 * </tr>
 * <tr>
 * <td>Dao实现</td>
 * <td>xxx.xxx.xxx.dao.impl</td>
 * <td>EntityDaoImpl</td>
 * <td><B>entityDao</B></td>
 * </tr>
 * <tr>
 * <td>Manager接口</td>
 * <td>xxx.xxx.xxx.manager</td>
 * <td>EntityManager</td>
 * <td>N/A</td>
 * </tr>
 * <tr>
 * <td>Manager实现</td>
 * <td>xxx.xxx.xxx.manager.impl</td>
 * <td>EntityManagerImpl</td>
 * <td><B>entityManager</B></td>
 * </tr>
 * </table>
 */
@Mojo(name = "jfinal")
public class JFinalMojo extends AbstractMojo {

	/** 结果输出的目录 */
	@Parameter(property = "output.directory", defaultValue = "${project.build.directory/target/gensrc}", readonly = true)
	private File outputDirectory;

	/** 需要处理的数据库表，如果存在这个选项，则excludeTables不适用 */
	@Parameter
	private List<String> includeTables;

	/** 需要剔除的数据表，只有在不设置includeTables时才可以使用 */
	@Parameter
	private List<String> excludeTables;

	/** 数据库表的地址 */
	@Parameter
	private String dbUrl;
	/** 数据库用户名 */
	@Parameter
	private String user;
	/** 数据库密码 */
	@Parameter
	private String password;

	/** 数据库表的前缀，在处理会过滤到此信息 */
	@Parameter
	private String tablePrefix;
	/** 生成model所在的包名 */
	@Parameter(required = true)
	private String basePackage;
	/** 生成model类时类名的前缀 */
	@Parameter
	private String classPrefix;

	public void execute() throws MojoExecutionException {
		if (!outputDirectory.exists()) {
			outputDirectory.mkdirs();
		}

		try {
			JFinalGenerater generater = new JFinalGenerater(dbUrl, user, password, basePackage, tablePrefix, classPrefix, outputDirectory);
			generater.setIncludeTables(includeTables);
			generater.setExcludeTables(excludeTables);

			generater.generate();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
