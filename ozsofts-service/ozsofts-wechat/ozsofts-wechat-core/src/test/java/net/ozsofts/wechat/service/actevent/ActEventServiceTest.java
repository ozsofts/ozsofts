package net.ozsofts.wechat.service.actevent;

import java.util.HashMap;
import java.util.Map;

import net.ozsofts.wechat.core.message.type.ReceiveTextMessage;
import net.ozsofts.wechat.core.models.WXAccount;
import net.ozsofts.wechat.core.models.WXActEvent;
import net.ozsofts.wechat.core.models.WXActMessage;
import net.ozsofts.wechat.core.models.WXActUser;
import net.ozsofts.wechat.core.models.WXGroup;
import net.ozsofts.wechat.core.models.WXMessage;
import net.ozsofts.wechat.core.models.WXUser;
import net.ozsofts.wechat.core.service.actevent.ActEventService;
import net.ozsofts.wechat.core.service.message.MessageService;

import org.junit.Before;
import org.junit.Test;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;

public class ActEventServiceTest {

	@Before
	public void setup() throws Exception {
		C3p0Plugin cp = new C3p0Plugin("jdbc:mysql://localhost/wechat", "wechat", "admin");
		ActiveRecordPlugin arp = new ActiveRecordPlugin(cp);
		arp.addMapping("t_wx_accounts", WXAccount.class);
		arp.addMapping("t_wx_groups", WXGroup.class);
		arp.addMapping("t_wx_users", WXUser.class);
		arp.addMapping("t_wx_messages", WXMessage.class);
		arp.addMapping("t_wx_actevents", WXActEvent.class);
		arp.addMapping("t_wx_actusers", WXActUser.class);
		arp.addMapping("t_wx_actmessages", WXActMessage.class);

		cp.start();
		arp.start();
	}

	@Test
	public void testHandleMessage() throws Exception {
		WXAccount wxaccount = WXAccount.dao.findBySystemId("ikdyserv");

		ReceiveTextMessage message = new ReceiveTextMessage();
		Map<String, String> params = new HashMap<String, String>();
		params.put("ToUserName", "gh_1210bd0b69ed");
		params.put("FromUserName", "oIyvojuqIh3b3O4QZmKIukZeWk0Y");
		params.put("CreateTime", String.valueOf(System.currentTimeMillis() / 1000));
		params.put("MsgType", "text");
		params.put("MsgId", String.valueOf(System.currentTimeMillis()));
		params.put("Content", "签到 13609742016");
		message.parse(params);

		MessageService.save(wxaccount, message);
		ActEventService.handleMessage(wxaccount, message);

		message = new ReceiveTextMessage();
		params = new HashMap<String, String>();
		params.put("ToUserName", "gh_1210bd0b69ed");
		params.put("FromUserName", "oIyvojuqIh3b3O4QZmKIukZeWk0Y");
		params.put("CreateTime", String.valueOf(System.currentTimeMillis() / 1000));
		params.put("MsgType", "text");
		params.put("MsgId", String.valueOf(System.currentTimeMillis()));
		params.put("Content", "我很喜欢这个活动，谢谢！");
		message.parse(params);

		MessageService.save(wxaccount, message);
		ActEventService.handleMessage(wxaccount, message);
	}

}
