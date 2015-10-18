package net.ozsofts.wechat.core.service.message;

import net.ozsofts.wechat.core.message.MessageListener;
import net.ozsofts.wechat.core.message.type.ReceiveMessage;
import net.ozsofts.wechat.core.message.type.ResponseMessage;
import net.ozsofts.wechat.core.models.WXAccount;

/**
 * <p>
 * 对所有上行的信息进行通用处理
 * 
 * @author jack
 */
public class CommonMessageListener implements MessageListener {

	@Override
	public ResponseMessage onMessage(WXAccount wxaccount, ReceiveMessage recvMessage) throws Exception {
		if (ReceiveMessage.MSGTYPE_EVENT.equals(recvMessage.getMsgType())) {
			// 对于事件通知类信息，不在本类做处理
			return null;
		}

		// 保存上行的消息
		MessageService.save(wxaccount, recvMessage);

		// 对可能的关键字消息进行处理
		return MessageService.handleKeyword(wxaccount, recvMessage);
	}
}
