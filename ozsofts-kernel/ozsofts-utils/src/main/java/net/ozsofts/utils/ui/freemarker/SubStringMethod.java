package net.ozsofts.utils.ui.freemarker;

import java.util.List;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

/**
 * 自定义截取字符串函数
 * ${subString(content,endIndex,endString)} 
 * 
 * content表示需要截取的字符串内容 
 * endIndex表示截取的位数 
 * endString表示截取后填补的内容(可选，默认是...)
 * 
 * @author Jack
 */
public class SubStringMethod implements TemplateMethodModelEx {

	@SuppressWarnings({ "rawtypes" })
	public Object exec(List args) throws TemplateModelException {
		String content = "";
		if (args != null && args.size() >= 2) {
			content = args.get(0).toString();
			String end = "...";
			if (args.size() >= 3) {
				end = args.get(2).toString();
			}
			int length = Integer.valueOf(args.get(1).toString()).intValue();
			if (content.length() > length) {
				content = content.substring(0, length) + end;
			}
		}
		return content;
	}

}
