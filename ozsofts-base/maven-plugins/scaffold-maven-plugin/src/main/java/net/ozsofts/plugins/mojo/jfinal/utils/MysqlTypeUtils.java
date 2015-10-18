package net.ozsofts.plugins.mojo.jfinal.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 类型工具类，mysql类型与java类型映射
 */
public class MysqlTypeUtils {

	private static Map<String, String> typeMap = new HashMap<String, String>();

	// Java数据类型和MySql数据类型对照表
	// http://dev.mysql.com/doc/connector-j/en/connector-j-reference-type-conversions.html
	static {
		typeMap.put("BIT", "byte[]");
		typeMap.put("TINYINT", "Integer");
		typeMap.put("TINYINT UNSIGNED", "Integer");
		typeMap.put("BOOL", "Boolean");
		typeMap.put("BOOLEAN", "Boolean");

		typeMap.put("SMALLINT", "Integer");
		typeMap.put("MEDIUMINT", "Integer");
		typeMap.put("MEDIUMINT UNSIGNED", "Integer");

		typeMap.put("INT", "Integer");
		typeMap.put("INT UNSIGNED", "Long");
		typeMap.put("INTEGER", "Integer");
		typeMap.put("INTEGER UNSIGNED", "Long");
		typeMap.put("PK (INTEGER UNSIGNED)", "long");

		typeMap.put("BIGINT", "Long");
		typeMap.put("BIGINT UNSIGNED", "BigInteger");
		typeMap.put("FLOAT", "Float");
		typeMap.put("DOUBLE", "Double");

		typeMap.put("DECIMAL", "BigDecimal");
		typeMap.put("DATE", "Date");
		typeMap.put("DATETIME", "Date");
		typeMap.put("TIMESTAMP", "Date");
		typeMap.put("TIME", "Date");
		typeMap.put("YEAR", "Date");

		typeMap.put("CHAR", "String");
		typeMap.put("VARCHAR", "String");

		typeMap.put("BINARY", "byte[]");
		typeMap.put("VARBINARY", "byte[]");
		typeMap.put("TINYBLOB", "byte[]");
		typeMap.put("TINYTEXT", "String");
		typeMap.put("BLOB", "byte[]");
		typeMap.put("TEXT", "String");

		typeMap.put("MEDIUMBLOB", "byte[]");
		typeMap.put("MEDIUMTEXT", "String");
		typeMap.put("LONGBLOB", "String");
		typeMap.put("LONGTEXT", "String");
		typeMap.put("ENUM", "String");
		typeMap.put("SET", "String");
	}

	/**
	 * 将sql类型转化为java类型
	 * 
	 * @param type
	 * @return String
	 */
	public static String getJavaType(String type, int size) {
		if (type.startsWith("BIT") && size == 1) {
			return "Boolean";
		}
		if (type.startsWith("TINYINT") && size == 1) {
			return "Boolean";
		}
		return typeMap.get(type.toUpperCase());
	}
}