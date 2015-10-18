package com.mobirit.mtmmcp.touch.web.handler;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.ozsofts.utils.web.CookieUtils;

import com.jfinal.handler.Handler;
import com.mobirit.mtmmcp.touch.models.BusinessSystem;

/**
 * <p>
 * 对业务系统进行预先处理。
 * 
 * @author jack
 */
public class BusinessSystemHandler extends Handler {

	@Override
	public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
		String systemCode = "";

		// 检查Session中是否存在已经保存的System信息
		Cookie cookie = CookieUtils.getCookie(request, "__m_biz_code__");
		if (cookie != null) {
			systemCode = cookie.getValue();
		} else {
			// 从请求的Server数据中读取systemCode信息
			String serverName = request.getServerName();
			systemCode = serverName.substring(0, serverName.indexOf(".") - 1);

			// 从数据库中读取业务系统的信息
			BusinessSystem bs = BusinessSystem.dao.findFirst("select * from t_mv_business_systems where code=?", systemCode);

			// 在这里保存与业务系统相关的默认信息
		}

	}

}
