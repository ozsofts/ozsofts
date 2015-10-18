package net.ozsofts.utils.ui.freemarker;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

/**
 * 自定义日期函数 ${formatDateTime(dateString,pattern)} dateString表示日期、时间、日期时间字符串 pattern表示需要转换的格式(可选，默认格式是yyyy-MM-dd HH:mm:ss)
 * 
 * @author Jack
 * 
 */
public class DateFormatMethod implements TemplateMethodModelEx {

	@SuppressWarnings({ "rawtypes" })
	public Object exec(List args) throws TemplateModelException {
		if (args != null && args.size() >= 1) {
			if (args.get(0) == null) {
				return "";
			}

			String date = args.get(0).toString();
			if (args.size() >= 2) {
				String fm = args.get(1).toString();
				DateTime dt = null;
				switch (date.length()) {
				case 6:
					dt = DateTimeFormat.forPattern("HHmmss").parseDateTime(date);
					break;
				case 8:
					dt = DateTimeFormat.forPattern("yyyyMMdd").parseDateTime(date);
					break;
				case 14:
					dt = DateTimeFormat.forPattern("yyyyMMddHHmmss").parseDateTime(date);
					break;
				default:
					dt = new DateTime();
				}
				return dt.toString(fm);
			} else {
				return date;
			}
		} else {
			return "";
		}
	}
}
