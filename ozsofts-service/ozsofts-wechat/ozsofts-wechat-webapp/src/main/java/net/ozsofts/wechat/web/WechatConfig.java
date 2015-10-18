package net.ozsofts.wechat.web;

import java.util.Properties;

import net.ozsofts.utils.ui.freemarker.DirectiveUtils;
import net.ozsofts.utils.ui.freemarker.ModelRegister;
import net.ozsofts.wechat.core.models.WXAccount;
import net.ozsofts.wechat.core.models.WXActEvent;
import net.ozsofts.wechat.core.models.WXActMessage;
import net.ozsofts.wechat.core.models.WXActUser;
import net.ozsofts.wechat.core.models.WXGroup;
import net.ozsofts.wechat.core.models.WXMessage;
import net.ozsofts.wechat.core.models.WXUser;
import net.ozsofts.wechat.core.servlet.TokenService;
import net.ozsofts.wechat.core.servlet.WechatController;
import net.ozsofts.wechat.web.controller.ActEventController;
import net.ozsofts.wechat.web.controller.UserController;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.render.FreeMarkerRender;

import freemarker.template.Configuration;

public class WechatConfig extends JFinalConfig {

	@Override
	public void configConstant(Constants c) {
		c.setDevMode(true);
		c.setBaseViewPath("/WEB-INF/t");

		Configuration cfg = FreeMarkerRender.getConfiguration();
		DirectiveUtils.exposeRapidMacros(cfg);

		try {
			// IMPORTANT 增加模板中要使用的静态方法定义
			ModelRegister register = new ModelRegister(cfg);
			register.registerGlobalModel("wx.message.service", register.useClass("net.ozsofts.wechat.web.service.MessageService"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void configHandler(Handlers hs) {
	}

	@Override
	public void configInterceptor(Interceptors me) {
	}

	@Override
	public void configPlugin(Plugins me) {
		Prop jdbcProp = PropKit.use("jdbc.properties");
		C3p0Plugin cp = new C3p0Plugin(jdbcProp.get("db.jdbcUrl"), jdbcProp.get("db.user"), jdbcProp.get("db.password"));
		me.add(cp);

		// DruidPlugin dp = new DruidPlugin(jdbcProp.get("db.jdbcUrl"), jdbcProp.get("db.user"), jdbcProp.get("db.password"));
		// dp.setTestWhileIdle(false).setTestOnBorrow(false).setTestOnReturn(false);
		// dp.addFilter(new StatFilter());
		// WallFilter wall = new WallFilter();
		// wall.setDbType("mysql");
		// dp.addFilter(wall);
		// me.add(dp);

		ActiveRecordPlugin arp = new ActiveRecordPlugin(cp);
		arp.addMapping("t_wx_accounts", WXAccount.class);
		arp.addMapping("t_wx_groups", WXGroup.class);
		arp.addMapping("t_wx_users", WXUser.class);
		arp.addMapping("t_wx_messages", WXMessage.class);
		arp.addMapping("t_wx_actevents", WXActEvent.class);
		arp.addMapping("t_wx_actusers", WXActUser.class);
		arp.addMapping("t_wx_actmessages", WXActMessage.class);
		me.add(arp);
	}

	@Override
	public void configRoute(Routes rs) {
		rs.add("/wechat", WechatController.class);

		rs.add("/admin/user", UserController.class, "/user");
		rs.add("/admin/act", ActEventController.class, "/actevent");
	}

	@Override
	public void afterJFinalStart() {
		// 由于我们用到freemarker，所以在此进行freemarker配置文件的装载
		Properties p = loadPropertyFile("freemarker.properties");
		FreeMarkerRender.setProperties(p);

		TokenService.me.initialize();
	}

}
