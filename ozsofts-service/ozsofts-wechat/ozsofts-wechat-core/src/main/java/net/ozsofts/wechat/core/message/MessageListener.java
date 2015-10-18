package net.ozsofts.wechat.core.message;

import net.ozsofts.wechat.core.message.type.ReceiveMessage;
import net.ozsofts.wechat.core.message.type.ResponseMessage;
import net.ozsofts.wechat.core.models.WXAccount;

/**
 * <p>
 * 消息侦听器对象
 * 
 * @author jack
 */
public interface MessageListener {
	/**
	 * <p>
	 * 对消息进行处理
	 * 
	 * @param wxaccount
	 *            当前消息上行的公众号信息
	 * @param message
	 *            上行的消息
	 * @throws Exception
	 */
	public ResponseMessage onMessage(WXAccount wxaccount, ReceiveMessage message) throws Exception;
}
