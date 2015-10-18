package net.ozsofts.utils.ui.freemarker;

import java.net.URLEncoder;
import java.util.List;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

/**
 * 对URL进行编码
 * 
 * @author Jack
 * 
 */
public class URLEncodeMethod implements TemplateMethodModelEx {

	@SuppressWarnings({ "rawtypes" })
	public Object exec(List args) throws TemplateModelException {
		if (args != null && args.size() == 1) {
			if (args.get(0) == null) {
				return "";
			}

			String str = args.get(0).toString();
			try {
				return URLEncoder.encode(str, "UTF-8");
			} catch (Exception ex) {
				return str;
			}
		} else {
			return "";
		}
	}
}
