package net.ozsofts.wechat.core.models;

import java.util.List;

import net.ozsofts.wechat.utils.SHA1;

import org.junit.Before;
import org.junit.Test;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;

public class WXAccountTest {

	@Before
	public void setup() {
		C3p0Plugin cp = new C3p0Plugin("jdbc:mysql://localhost/wechat", "wechat", "admin");
		ActiveRecordPlugin arp = new ActiveRecordPlugin(cp);
		arp.addMapping("t_wx_accounts", WXAccount.class);

		cp.start();
		arp.start();
	}

	@Test
	public void testCreate() {
		new WXAccount().set("system_id", "nyymh").set("app_id", "appId").save();
	}

	@Test
	public void testSign() throws Exception {
		String token = "serviceikdy";
		String timestamp = "1444739474";
		String nonce = "1552693534";
		System.out.println(SHA1.getSHA1(token, timestamp, nonce, ""));
	}

	@Test
	public void testSelect() {
		List<WXAccount> accountList = WXAccount.dao.find("select * from t_wx_accounts");
		System.out.println(accountList.size());

		WXAccount account = WXAccount.dao.findById(1);
		System.out.println(account.getStr("system_id"));

		account = WXAccount.dao.findFirst("select * from t_wx_accounts where system_id=?", "nyymh");
		System.out.println(account.getStr("system_id"));

		account = WXAccount.dao.findFirst("select * from t_wx_accounts where system_id=?", "nonexist");
		System.out.println(account);
	}

}
