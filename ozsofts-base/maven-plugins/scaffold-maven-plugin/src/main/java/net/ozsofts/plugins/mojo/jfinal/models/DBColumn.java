package net.ozsofts.plugins.mojo.jfinal.models;

import net.ozsofts.plugins.mojo.jfinal.utils.StrUtils;
import net.ozsofts.plugins.mojo.jfinal.utils.MysqlTypeUtils;

import org.apache.commons.lang.StringUtils;

import com.jfinal.kit.StrKit;

/**
 * 数据库表结构
 */
public class DBColumn implements java.io.Serializable, Cloneable {

	private static final long serialVersionUID = -1090817782877325006L;

	public final String name;
	public final String remarks;
	public final boolean isPk;
	public final String type;
	public final int size;

	public DBColumn(String name, String remarks, boolean isPk, String type, int size) {
		super();
		this.name = name;
		this.remarks = remarks;
		this.isPk = isPk;
		this.type = type;
		this.size = size;
	}

	// 数据库的字段xxx_xxx
	public String getSqlName() {
		return this.name;
	}

	// 转成get、set方法名·_·转第二位大写
	public String getMethodName() {
		return StrUtils.toClassName(this.name);
	}

	// 方法的参数名
	public String getParaName() {
		String[] names = this.name.split("_");
		for (int i = 1; i < names.length; i++) {
			names[i] = StrKit.firstCharToUpperCase(names[i]);
		}
		return StringUtils.join(names);
	}

	// 字段大写XXX_XXX
	public String getSqlNameUpper() {
		return this.name.toUpperCase();
	}

	public String getRemarks() {
		return this.remarks;
	}

	public String getJavaType() {
		return MysqlTypeUtils.getJavaType(this.type, this.size);
	}

	public int getSize() {
		return this.size;
	}

	@Override
	public String toString() {
		return name + " | " + getJavaType() + " | " + size + " | " + remarks;
	}
}
