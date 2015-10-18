package net.ozsofts.plugins.mojo.hibernate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.plexus.util.StringUtils;

public class EntityData {
	/** 解析基本的包名和Bean的类名 */
	private static Pattern baseEntityPattern = Pattern.compile("^(.+)\\.entity\\.base\\.Base([A-Z].+)");
	/** 对Entity类名的前缀进行处理 */
	private static Pattern entityPartern = Pattern.compile("^([A-Z]+).+");

	/** 基本bean的全名，包含包名 */
	private String baseEntityName;

	/** 基本包名， 如com.mobirit.mggw.core.entity.base.BaseShortMessage，基本包名应当为 com.mobirit.mggw.core */
	private String basePackage;

	/** Bean的类名 */
	private String entityName;
	/**
	 * 前缀是小写的bean的名称，对于缩写开头的要注意，如TMPEntity，修改前缀后应当为 tmpEntity
	 * <p>
	 * 其它的情况按命名规范，只需要将首字母小写就可以了。
	 */
	private String entityPrefix;

	public static EntityData initialize(String baseEntityName) {
		EntityData entityData = new EntityData();

		entityData.baseEntityName = baseEntityName;

		Matcher baseMatcher = baseEntityPattern.matcher(baseEntityName);
		if (!baseMatcher.find(0)) {
			return null;
		}

		entityData.basePackage = baseMatcher.group(1);
		entityData.entityName = baseMatcher.group(2);

		Matcher matcher = entityPartern.matcher(entityData.entityName);
		if (!matcher.find(0)) {
			return null;
		}

		String prefix = matcher.group(1);
		int length = prefix.length();
		if (length > 1) {
			// 前面多于一个字符是大写的，n-1字符全部变成小写
			String replace = prefix.substring(0, length - 1);
			entityData.entityPrefix = entityData.entityName.replaceFirst(replace, replace.toLowerCase());
		} else {
			entityData.entityPrefix = StringUtils.uncapitalise(entityData.entityName);
		}

		return entityData;
	}

	public String getEntityName() {
		return entityName;
	}

	public String getEntityPrefix() {
		return entityPrefix;
	}

	public String getBaseEntityName() {
		return baseEntityName;
	}

	public String getBasePackage() {
		return basePackage;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(basePackage).append(" | ").append(entityName).append(" | ").append(entityPrefix);
		return sb.toString();
	}
}
