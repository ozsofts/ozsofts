package net.ozsofts.wechat.web.service;

import java.util.List;

import net.ozsofts.wechat.core.models.WXMessage;

public class MessageService {
	public static List<WXMessage> findMessages(String param) {
		System.out.println("param:" + param);

		List<WXMessage> messages = WXMessage.dao
						.find("select * from t_wx_actmessages am inner join t_wx_messages m on m.msg_id=am.msg_id inner join t_wx_users u on u.open_id=m.from_user order by am.id");
		return messages;
	}
}
