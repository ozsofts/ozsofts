package net.ozsofts.wechat.service;

import java.net.URLDecoder;
import java.util.concurrent.TimeUnit;

import net.ozsofts.wechat.core.models.WXAccount;
import net.ozsofts.wechat.core.models.WXGroup;
import net.ozsofts.wechat.core.models.WXUser;
import net.ozsofts.wechat.core.service.AccountService;
import net.ozsofts.wechat.core.servlet.TokenService;

import org.junit.Before;
import org.junit.Test;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;

public class AccountServiceTest {

	@Before
	public void setup() throws Exception {
		C3p0Plugin cp = new C3p0Plugin("jdbc:mysql://localhost/wechat", "wechat", "admin");
		ActiveRecordPlugin arp = new ActiveRecordPlugin(cp);
		arp.addMapping("t_wx_accounts", WXAccount.class);
		arp.addMapping("t_wx_groups", WXGroup.class);
		arp.addMapping("t_wx_users", WXUser.class);

		cp.start();
		arp.start();

		TokenService.me.initialize();
		TimeUnit.SECONDS.sleep(10l);
	}

	@Test
	public void test() throws Exception {
		AccountService.initAccount("ikdyserv");
	}

	public static void main(String[] args) throws Exception {
		System.out.println(URLDecoder.decode("%E7%BB%AE%E5%A9%B7+%EE%8C%AE", "UTF-8"));
	}

}
