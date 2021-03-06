package net.ozsofts.wechat.core.functions;

import net.ozsofts.wechat.core.WechatCommService;
import net.ozsofts.wechat.core.message.type.ResponseMessage;

public class CustomFunction {
	// 发送客服消息的服务地址
	private static final String SEND_FUNC_URL = "/cgi-bin/message/custom/send";

	//
	// 通过客服接口发送信息，注意只有在用户上行消息的48小时内调用此接口才有作用
	//
	/** 发送客服信息 */
	public static void sendMessage(String token, ResponseMessage message) throws Exception {
		WechatCommService.instance().post(SEND_FUNC_URL, token, null, message.toMap());
	}
}
