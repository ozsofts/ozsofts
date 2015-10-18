package net.ozsofts.wechat.core.service.actevent;

import net.ozsofts.wechat.core.message.MessageListener;
import net.ozsofts.wechat.core.message.type.ReceiveMessage;
import net.ozsofts.wechat.core.message.type.ResponseMessage;
import net.ozsofts.wechat.core.models.WXAccount;

/**
 * <p>
 * 对微信活动进行处理
 * 
 * @author jack
 */
public class ActEventListener implements MessageListener {
	@Override
	public ResponseMessage onMessage(WXAccount wxaccount, ReceiveMessage recvMessage) throws Exception {
		return ActEventService.handleMessage(wxaccount, recvMessage);
	}
}
