package net.ozsofts.wechat.core.service.user;

import net.ozsofts.wechat.core.message.MessageListener;
import net.ozsofts.wechat.core.message.type.ReceiveEventMessage;
import net.ozsofts.wechat.core.message.type.ReceiveMessage;
import net.ozsofts.wechat.core.message.type.ResponseMessage;
import net.ozsofts.wechat.core.models.WXAccount;

public class UserListener implements MessageListener {
	@Override
	public ResponseMessage onMessage(WXAccount wxaccount, ReceiveMessage message) throws Exception {
		if (!ReceiveMessage.MSGTYPE_EVENT.equals(message.getMsgType())) {
			return null;
		}

		ReceiveEventMessage event = (ReceiveEventMessage) message;
		if (!ReceiveEventMessage.EVENT_SUBSCRIBE.equals(event.getEvent()) && !ReceiveEventMessage.EVENT_UNSUBSCRIBE.equals(event.getEvent())) {
			return null;
		}

		ResponseMessage respMessage = null;
		if (ReceiveEventMessage.EVENT_SUBSCRIBE.equals(event.getEvent())) {
			respMessage = UserService.subscribe(wxaccount, message.getFromUserName());
		} else if (ReceiveEventMessage.EVENT_UNSUBSCRIBE.equals(event.getEvent())) {
			respMessage = UserService.unsubscribe(wxaccount, message.getFromUserName());
		}

		return respMessage;
	}
}
