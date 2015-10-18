package net.ozsofts.plugins.mojo.hibernate;

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
@Mojo(name = "hibernate")
public class HibernateMojo extends AbstractMojo {

	@Parameter(property = "source.directory", defaultValue = "${project.build.sourceDirectory}", readonly = true)
	private File sourceDirectory;

	/** 包含Base类的包名 */
	@Parameter
	private List<String> packages;

	@Parameter(defaultValue = "${project}", readonly = true)
	private MavenProject project;

	/** 直接使用Base类的类名列表 */
	@Parameter
	private List<String> beans;

	/** <p> */
	@Parameter(defaultValue = "true")
	private boolean generateDao;

	@Parameter(defaultValue = "true")
	private boolean generateEntity;

	@Parameter(defaultValue = "true")
	private boolean generateManager;

	public void execute() throws MojoExecutionException {
		if (!sourceDirectory.exists()) {
			getLog().error("[scaffold:hibernate] the directory [" + sourceDirectory.getAbsolutePath()
							+ "] is not existing, please check configuration!");
			return;
		}

		// 初始化FreeMarker的配置
		Configuration configuration = new Configuration();
		ClassTemplateLoader ctloader = new ClassTemplateLoader(getClass(), "templates");
		configuration.setTemplateLoader(ctloader);
		configuration.setLocale(Locale.CHINA);
		configuration.setDefaultEncoding("UTF-8");

		try {

			if ((beans == null || (beans != null && beans.isEmpty())) && (packages == null || (packages != null && packages.isEmpty()))) {
				// 进入手动模式
				getLog().warn("[scaffold:hibernate] must config beans or packages in plugin configuration!");
			} else {
				getLog().info("[scaffold:hibernate] starting to generate pojo codes ......");

				// 进行自动模式
				generateAuto(configuration);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void generateAuto(Configuration configuration) {
		System.out.println("Beans   :" + beans);
		System.out.println("Packages:" + packages);

		if ((beans == null || (beans != null && beans.isEmpty())) && packages != null) {
			beans = getBeans(packages);
		}

		// 每个包一个目录
		Map<String, List<EntityData>> packageMap = new HashMap<String, List<EntityData>>();

		for (String beanName : beans) {
			System.out.println(beanName);

			EntityData entityData = EntityData.initialize(beanName);
			if (entityData == null) {
				continue;
			}

			System.out.println(entityData);

			List<EntityData> entityList = packageMap.get(entityData.getBasePackage());
			if (entityList == null) {
				entityList = new ArrayList<EntityData>();
				packageMap.put(entityData.getBasePackage(), entityList);
			}

			entityList.add(entityData);
			handleBean(configuration, entityData, generateEntity, generateDao, generateManager);
		}

		for (String basePackage : packageMap.keySet()) {
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("entityList", packageMap.get(basePackage));

			File basePackageDir = new File(this.sourceDirectory, basePackage.replaceAll("\\.", "/"));

			if (generateDao) {
				generateFileFromTemplate(configuration, "spring-dao.ftl", new File(basePackageDir, "dao"), "applicationContext-dao.xml", data);
			}

			// 生成Manager相关的接口和实现类
			if (generateManager) {
				generateFileFromTemplate(configuration, "spring-manager.ftl", new File(basePackageDir, "manager"), "applicationContext-manager.xml",
								data);
			}
		}
	}

	private ArrayList<String> getBeans(List<String> packages) {
		ArrayList<String> beanList = new ArrayList<String>();

		for (String baseEntityPackage : packages) {
			if (!baseEntityPackage.endsWith(".entity.base")) {
				getLog().warn("[scaffold:hibernate] the pacakge [" + baseEntityPackage + "] must end by '.entity.base', please check configuration!");
				continue;
			}

			File baseEntityDirFile = new File(sourceDirectory, baseEntityPackage.replaceAll("\\.", "/"));
			if (!baseEntityDirFile.exists()) {
				getLog().error("[scaffold:hibernate] the directory [" + baseEntityDirFile.getAbsolutePath()
								+ "] saving beanEntity isn't existing, please check configuration!");
			}

			// 取出所有的BaseEntity类文件
			File[] baseEntityFiles = baseEntityDirFile.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.startsWith("Base") && name.endsWith(".java");
				}
			});

			for (File baseEntityFile : baseEntityFiles) {
				beanList.add(baseEntityPackage + "." + baseEntityFile.getName().replaceAll("\\.java", ""));
			}
		}

		return beanList;
	}

	private void handleBean(Configuration configuration, EntityData entityData, boolean generateEntity, boolean generateDao, boolean generateManager) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("entity", entityData);

		File basePackageDir = new File(this.sourceDirectory, entityData.getBasePackage().replaceAll("\\.", "/"));

		// 生成POJO的Bean类
		if (generateEntity) {
			generateFileFromTemplate(configuration, "entity.ftl", new File(basePackageDir, "entity"), entityData.getEntityName() + ".java", data);
		}

		// 生成DAO相关的接口和实现类
		if (generateDao) {
			generateFileFromTemplate(configuration, "dao.ftl", new File(basePackageDir, "dao"), entityData.getEntityName() + "Dao.java", data);
			generateFileFromTemplate(configuration, "daoImpl.ftl", new File(basePackageDir, "dao/impl"), entityData.getEntityName() + "DaoImpl.java",
							data);
		}

		// 生成Manager相关的接口和实现类
		if (generateManager) {
			generateFileFromTemplate(configuration, "manager.ftl", new File(basePackageDir, "manager"), entityData.getEntityName() + "Manager.java",
							data);
			generateFileFromTemplate(configuration, "managerImpl.ftl", new File(basePackageDir, "manager/impl"), entityData.getEntityName()
							+ "ManagerImpl.java", data);
		}
	}

	private void generateFileFromTemplate(Configuration configuration, String templateName, File outputDirFile, String outputFilename,
					Map<String, Object> data) {
		Writer writer = null;
		try {
			Template template = configuration.getTemplate(templateName);

			if (!outputDirFile.exists()) {
				outputDirFile.mkdirs();
			}

			File output = new File(outputDirFile, outputFilename);
			if (output.exists()) {
				getLog().warn("[scaffold:hibernate] file [" + output.getAbsolutePath() + "] exists, ignore it!");
				return;
			}

			writer = new FileWriter(output);

			// Merge the data-model and the template
			System.out.println("Generated: " + output.getCanonicalPath());
			template.process(data, writer);
		} catch (Exception ex) {
			getLog().error("[scaffold:hibernate] there are errors using template [" + templateName + "]!", ex);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (Exception ex) {
				}
			}
		}
	}
}
