package net.ozsofts.plugins.mojo.jfinal;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import net.ozsofts.plugins.mojo.jfinal.models.DBColumn;
import net.ozsofts.plugins.mojo.jfinal.models.DBTable;

import org.apache.commons.io.FileUtils;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class JFinalGenerater {
	//
	//
	//
	/** 基础包名 */
	private String basePackage;
	/** 类名前缀 */
	private String classPrefix;
	private String tablePrefix;

	/** 输出目录 */
	private File outputDirectory;

	private List<String> includeTables;
	private List<String> excludeTables;

	// private DataSource ds = null;
	private Connection conn = null;
	private Configuration configuration = null;

	public JFinalGenerater(String url, String user, String password, String basePackage, String tablePrefix, String classPrefix, File outputDirectory)
					throws Exception {
		this.basePackage = basePackage;
		this.classPrefix = classPrefix;
		this.tablePrefix = tablePrefix;

		this.outputDirectory = outputDirectory;

		// 初始化数据源对象
		// MysqlDataSource mysql = new MysqlDataSource();
		// mysql.setUrl(url);
		// mysql.setUser(user);
		// mysql.setPassword(password);
		Class.forName("com.mysql.jdbc.Driver").newInstance();

		Properties props = new Properties();
		props.setProperty("remarks", "true"); // 设置可以获取remarks信息
		props.setProperty("useInformationSchema", "true");// 设置可以获取tables remarks信息
		props.put("user", user);
		props.put("password", password);
		conn = DriverManager.getConnection(url, props);

		// 初始化FreeMarker的配置
		configuration = new Configuration();
		ClassTemplateLoader ctloader = new ClassTemplateLoader(getClass(), "templates");
		configuration.setTemplateLoader(ctloader);
		configuration.setLocale(Locale.CHINA);
		configuration.setDefaultEncoding("UTF-8");
	}

	public void generate() throws Exception {
		// 获取表结构
		List<DBTable> tables = this.tables();

		// ### 3.2 配置参数说明
		// |参数名称|类型|说明|
		// |:----| :---- | :----|
		// |`autofocus`|`boolean`|编辑器初始后是否默认获取焦点。 默认 `false`|
		StringBuilder tableInfo = new StringBuilder();
		String sqlName;
		for (DBTable dbTable : tables) {
			sqlName = dbTable.getSqlName();
			tableInfo.append("### ").append(sqlName.replaceAll("_", "\\\\_")).append("  ").append(dbTable.remarks).append('\n');
			tableInfo.append("|列名|类型|长度|注释|").append('\n');
			tableInfo.append("|:----|:----|:----:|:----|").append('\n');
			// 列名，类型，长度，注释

			for (DBColumn column : dbTable.getColumns()) {
				tableInfo.append("|").append(column.name);
				tableInfo.append("|").append(column.type);
				tableInfo.append("|").append(column.size);
				tableInfo.append("|").append(column.remarks);
				tableInfo.append("|").append('\n');
			}
			tableInfo.append('\n');
		}

		// 数据字典信息生成
		File docDirectory = new File(outputDirectory, "database");
		FileUtils.writeStringToFile(new File(docDirectory, "doc.md"), tableInfo.toString(), "UTF-8");

		// 生成model
		generateModel(tables);
	}

	/**
	 * 组装所有的表
	 * 
	 * @return
	 * @throws SQLException
	 */
	private List<DBTable> tables() throws SQLException {
		DatabaseMetaData metaData = conn.getMetaData();

		// 组装所有的表
		ResultSet rs = metaData.getTables(conn.getCatalog(), "root", null, new String[] { "TABLE" });
		List<DBTable> tables = new ArrayList<DBTable>();
		while (rs.next()) {
			String name = rs.getString("TABLE_NAME");
			String remarks = rs.getString("REMARKS");
			DBTable table = new DBTable(name, remarks, tablePrefix, classPrefix);
			tables.add(table);
		}
		rs.close();
		// 组装所有的表结构
		for (DBTable table : tables) {
			// 获取主键
			ResultSet rss = metaData.getPrimaryKeys(null, null, table.name);
			String primaryKey = null;
			while (rss.next()) {
				primaryKey = rss.getString("COLUMN_NAME");
			}
			rss.close();
			table.setPrimaryKey(primaryKey);

			// 获取结构
			ResultSet columnRs = metaData.getColumns(null, null, table.name, null);
			while (columnRs.next()) {
				// 列名
				String name = columnRs.getString("COLUMN_NAME");
				// 备注
				String remarks = columnRs.getString("REMARKS");
				// 字段类别
				String type = columnRs.getString("TYPE_NAME");
				// 字段长度
				int size = columnRs.getInt("COLUMN_SIZE");

				// TABLE_CAT=0, oauth
				// TABLE_SCHEM=1, null
				// TABLE_NAME=2, oauth_access_tokens
				// COLUMN_NAME=3, access_token
				// DATA_TYPE=4, 12
				// TYPE_NAME=5, VARCHAR
				// COLUMN_SIZE=6, 40
				// BUFFER_LENGTH=7, 65535
				// DECIMAL_DIGITS=8, null
				// NUM_PREC_RADIX=9, 10
				// NULLABLE=10, 0
				// REMARKS=11, access_token
				// COLUMN_DEF=12, null
				// SQL_DATA_TYPE=13, 0
				// SQL_DATETIME_SUB=14, 0
				// CHAR_OCTET_LENGTH=15, 40
				// ORDINAL_POSITION=16, 1
				// IS_NULLABLE=17, NO
				// SCOPE_CATALOG=18, null
				// SCOPE_SCHEMA=19, null
				// SCOPE_TABLE=20, null
				// SOURCE_DATA_TYPE=21, null
				// IS_AUTOINCREMENT=22, NO
				// IS_GENERATEDCOLUMN=23
				boolean isPk = (null != primaryKey && primaryKey.equals(name));
				DBColumn column = new DBColumn(name, remarks, isPk, type, size);
				table.addColumn(column);
			}
			columnRs.close();
		}
		return tables;
	}

	/**
	 * 生成model
	 * 
	 * @param tables
	 * @param templatePath
	 * @param outRoot
	 * @param helper
	 * @param context
	 */
	private void generateModel(List<DBTable> tables) throws Exception {
		// 项目所在目录
		String sourceDirectory = this.basePackage.replace('.', File.separatorChar);
		File classOutputDirectory = new File(this.outputDirectory, sourceDirectory);
		if (classOutputDirectory.exists()) {
			classOutputDirectory.delete();
		}
		classOutputDirectory.mkdirs();

		for (DBTable dbTable : tables) {
			String fileName = dbTable.getClassName() + ".java";

			// 设置table的数据s
			// 参数传递
			Map<String, Object> context = new HashMap<String, Object>();
			context.put("table", dbTable);

			// 渲染模板
			context.put("basePackage", basePackage);
			context.put("classPrefix", classPrefix);
			context.put("now", new Date());

			context.put("className", dbTable.getClassName());
			renderFromTemplate("class.ftl", new File(classOutputDirectory, fileName), context);
		}
	}

	private void renderFromTemplate(String templateName, File outputFile, Map<String, Object> context) throws Exception {
		Writer writer = null;
		try {
			Template template = configuration.getTemplate(templateName);

			writer = new FileWriter(outputFile);

			// Merge the data-model and the template
			System.out.println("Generated: " + outputFile.getCanonicalPath());
			template.process(context, writer);
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (Exception ex) {
				}
			}
		}
	}

	public void setIncludeTables(List<String> includeTables) {
		this.includeTables = includeTables;
	}

	public void setExcludeTables(List<String> excludeTables) {
		this.excludeTables = excludeTables;
	}
}
