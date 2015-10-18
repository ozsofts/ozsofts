package ${basePackage}.model;

import java.util.*;
import com.jfinal.plugin.activerecord.*;

@SuppressWarnings("serial")
public class ${className} extends Model<${className}> {

	public static final String TABLE_NAME = "${table.sqlName}";
	public static final String PRIMARY_KEY = "${table.primaryKey}";

	<#list table.columns as column>
	// "${column.sqlName}" -> ${column.remarks}
	</#list>

	public static final ${className} dao = new ${className}();

	<#list table.columns as column>
	public ${column.javaType} get${column.methodName}(){
		return get("${column.sqlName}");
	}

	public ${className} set${column.methodName}(${column.javaType} ${column.paraName}){
		return set("${column.sqlName}", ${column.paraName});
	}

	</#list>
}