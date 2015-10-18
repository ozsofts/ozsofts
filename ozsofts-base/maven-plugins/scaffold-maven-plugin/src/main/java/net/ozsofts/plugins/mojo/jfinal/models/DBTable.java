package net.ozsofts.plugins.mojo.jfinal.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.ozsofts.plugins.mojo.jfinal.utils.StrUtils;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.codehaus.plexus.util.StringUtils;

/**
 * 对应数据库表
 */
public class DBTable implements java.io.Serializable, Cloneable {

	private static final long serialVersionUID = -4582158020187561010L;

	public final String name;
	public final String remarks;

	private String primaryKey;
	private String className;

	private final List<DBColumn> columns = new ArrayList<DBColumn>();

	public DBTable(String name, String remarks, String tablePrefix, String classPrefix) {
		super();
		this.name = name;
		this.remarks = remarks;

		// this.classPrefix = classPrefix;
		// this.tablePrefix = tablePrefix;

		if (name.startsWith(tablePrefix)) {
			name = name.substring(tablePrefix.length());
		}
		this.className = (StringUtils.isNotBlank(classPrefix) ? classPrefix : "") + StrUtils.toClassName(name);
		if (className.endsWith("s")) {
			this.className = this.className.substring(0, this.className.length() - 1);
		}
	}

	public List<DBColumn> getColumns() {
		return columns;
	}

	public void addColumns(Collection<DBColumn> columns) {
		this.columns.addAll(columns);
	}

	public void addColumn(DBColumn column) {
		this.columns.add(column);
	}

	public String getClassName() {
		return this.className;
	}

	public String getClassNameLower() {
		return this.className.toLowerCase();
	}

	public String getSqlName() {
		return this.name;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}