package net.ozsofts.wechat.service;

import net.ozsofts.wechat.core.models.WXAccount;
import net.ozsofts.wechat.core.models.WXGroup;
import net.ozsofts.wechat.core.models.WXUser;
import net.ozsofts.wechat.core.service.user.UserService;

import org.junit.Before;
import org.junit.Test;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;

public class UserServiceTest {

	@Before
	public void setup() throws Exception {
		C3p0Plugin cp = new C3p0Plugin("jdbc:mysql://localhost/wechat", "wechat", "admin");
		ActiveRecordPlugin arp = new ActiveRecordPlugin(cp);
		arp.addMapping("t_wx_accounts", WXAccount.class);
		arp.addMapping("t_wx_groups", WXGroup.class);
		arp.addMapping("t_wx_users", WXUser.class);

		cp.start();
		arp.start();

		// WechatPorts.instance();
		// TimeUnit.SECONDS.sleep(10l);
	}

	@Test
	public void testSubscribe() {
		WXAccount wxaccount = WXAccount.dao.findBySystemId("ikdyserv");

		UserService.subscribe(wxaccount, "oIyvoji3o1pr9o3zTawR0jRUtgbo");
	}

	@Test
	public void testUnsubscribe() {
		WXAccount wxaccount = WXAccount.dao.findBySystemId("ikdyserv");

		UserService.unsubscribe(wxaccount, "oIyvoji3o1pr9o3zTawR0jRUtgbo");
	}

}
