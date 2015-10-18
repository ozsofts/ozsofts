package com.mobirit.mtmmcp.touch.web;

import net.ozsofts.utils.ui.freemarker.DirectiveUtils;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.render.FreeMarkerRender;

import freemarker.template.Configuration;

public class WechatConfig extends JFinalConfig {

	@Override
	public void configConstant(Constants c) {
		c.setDevMode(true);

		Configuration cfg = FreeMarkerRender.getConfiguration();
		DirectiveUtils.exposeRapidMacros(cfg);
		cfg.setTagSyntax(Configuration.AUTO_DETECT_TAG_SYNTAX); // 设置支持"[]"
	}

	@Override
	public void configHandler(Handlers hs) {
	}

	@Override
	public void configInterceptor(Interceptors me) {
	}

	@Override
	public void configPlugin(Plugins me) {
		C3p0Plugin cp = new C3p0Plugin("jdbc:mysql://localhost/wechat", "wechat", "admin");
		me.add(cp);

		ActiveRecordPlugin arp = new ActiveRecordPlugin(cp);
		// arp.addMapping("t_wx_accounts", WXAccount.class);
		// arp.addMapping("t_wx_groups", WXGroup.class);
		// arp.addMapping("t_wx_users", WXUser.class);
		// arp.addMapping("t_wx_messages", WXMessage.class);
		// arp.addMapping("t_wx_actevents", WXActEvent.class);
		// arp.addMapping("t_wx_actusers", WXActUser.class);
		// arp.addMapping("t_wx_actmessages", WXActMessage.class);
		me.add(arp);
	}

	@Override
	public void configRoute(Routes rs) {
		// rs.add("/admin/user", UserController.class);
		// rs.add("/admin/act", ActEventController.class, "/WEB-INF/templates/actevent");
	}

}
