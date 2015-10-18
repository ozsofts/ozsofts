package net.ozsofts.plugins.mojo.jfinal.utils;

import java.io.File;

import com.jfinal.kit.StrKit;

/**
 * 字符串处理
 */
public class StrUtils {

	/**
	 * 将数据库表名字 xxx_xxx 转换成 XxxXxx
	 * 
	 * @param name
	 * @return
	 */
	public static String toClassName(String name) {
		String[] names = name.split("_");
		StringBuffer upperName = new StringBuffer();
		for (String string : names) {
			upperName.append(StrKit.firstCharToUpperCase(string));
		}
		return upperName.toString();
	}

	/**
	 * 包名转成路径
	 * 
	 * @param packageName
	 * @return
	 */
	public static String pkgToPath(String packageName) {
		return packageName.replace('.', File.separatorChar);
	}
}