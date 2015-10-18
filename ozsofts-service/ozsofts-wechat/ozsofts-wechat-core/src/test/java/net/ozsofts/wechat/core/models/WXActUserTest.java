package net.ozsofts.wechat.core.models;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;

public class WXActUserTest {

	@Before
	public void setup() {
		C3p0Plugin cp = new C3p0Plugin("jdbc:mysql://localhost/wechat", "wechat", "admin");
		ActiveRecordPlugin arp = new ActiveRecordPlugin(cp);
		arp.addMapping("t_wx_users", WXUser.class);
		arp.addMapping("t_wx_actevents", WXActEvent.class);
		arp.addMapping("t_wx_actusers", WXActUser.class);

		cp.start();
		arp.start();
	}

	@Test
	public void testSelect() {
		List<WXActUser> list = WXActUser.dao.find("select * from t_wx_actusers au inner join t_wx_users u on u.open_id=au.open_id");
		System.out.println(list.size());
	}

}
