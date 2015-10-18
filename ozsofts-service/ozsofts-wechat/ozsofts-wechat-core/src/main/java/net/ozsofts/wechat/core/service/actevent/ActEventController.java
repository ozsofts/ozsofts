package net.ozsofts.wechat.core.service.actevent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.ozsofts.wechat.core.message.type.ReceiveMessage;
import net.ozsofts.wechat.core.message.type.ReceiveTextMessage;
import net.ozsofts.wechat.core.message.type.ResponseMessage;
import net.ozsofts.wechat.core.models.WXActEvent;
import net.ozsofts.wechat.core.models.WXActMessage;
import net.ozsofts.wechat.core.models.WXActUser;
import net.ozsofts.wechat.core.models.WXRespMessage;
import net.ozsofts.wechat.core.models.WXRule;
import net.ozsofts.wechat.core.models.WXUser;
import net.ozsofts.wechat.core.service.message.MessageService;

import org.joda.time.DateTime;

/**
 * <p>
 * 活动控制对象，一个活动有一个这样的对象进行管理。
 * 
 * @author jack
 */
public class ActEventController {
	/** 参与活动的用户信息 */
	private List<WXActUser> eventUserList;
	/** 保存所有参与活动的用户的openId */
	private List<String> openIdList;

	/** 活动中上行的信息 */
	private List<WXActMessage> eventMessageList;

	/** 当前活动 */
	private WXActEvent actevent;

	/** 当前活动标识 */
	private Long wxActEventId;
	/** 活动对应的公众号标识 */
	private Long wxAccountId;

	public ActEventController(WXActEvent actevent) {
		this.actevent = actevent;
	}

	public void initialize() throws Exception {
		this.wxActEventId = actevent.getLong("id");
		this.wxAccountId = actevent.getLong("account_id");

		eventUserList = WXActUser.dao.findUsersByEvent(this.wxActEventId);
		openIdList = new ArrayList<String>();
		for (WXActUser actuser : eventUserList) {
			openIdList.add(actuser.getStr("open_id"));
		}

		eventMessageList = WXActMessage.dao.findMessagesByEvent(this.wxActEventId);
	}

	public boolean check(Long wxAccountId, ReceiveMessage message) {
		if (this.wxAccountId != wxAccountId) {
			// 活动与公众号进行关联，一个活动只能对应一个公众号，如果上行消息的公众号不匹配，则不是当前的活动
			return false;
		}

		return true;
	}

	public ResponseMessage handleMessage(ReceiveMessage message) {

		// 检查是否是当前活动的登录命令
		if (ReceiveMessage.MSGTYPE_TEXT.equals(message.getMsgType())) {
			String content = ((ReceiveTextMessage) message).getContent();

			// 首先判断是否是签到指令
			String loginCmd = actevent.getStr("code") + "\\s(1\\d{10})";
			Matcher matcher = Pattern.compile(loginCmd).matcher(content);
			if (matcher.matches()) {
				// 如果用户第二次发送签到指令，则不再进行处理
				if (openIdList.contains(message.getFromUserName())) {
					// 一个用户同时只能参与一个活动

					// 显示活动签到回复信息
					WXRespMessage wxrespmsg = WXRespMessage.dao.findOne(wxAccountId, WXRule.RULE_TYPE_DEFAULT, "ACTEVENT", wxActEventId, false);
					return wxrespmsg != null ? wxrespmsg.toResponseMessage() : null;
				}

				openIdList.add(message.getFromUserName());

				WXUser wxuser = WXUser.dao.getUserByOpenId(message.getFromUserName());
				if (wxuser == null) {
					// TODO 没有找到相关的User，返回错误信息
					return null;
				}

				// 记录手机号码
				String mobile = matcher.group(1);
				wxuser.set("mobile", mobile);
				wxuser.update();

				// 保存参与活动的用户
				WXActUser wxactuser = new WXActUser();
				wxactuser.set("open_id", wxuser.get("open_id"));
				wxactuser.set("nick_name", wxuser.get("nick_name"));
				wxactuser.set("sex", wxuser.get("sex"));
				wxactuser.set("head_img_url", wxuser.get("head_img_url"));
				wxactuser.set("msg_id", message.getMsgId());
				wxactuser.set("login_date", new DateTime().toString("yyyyMMdd"));
				wxactuser.set("login_time", new DateTime().toString("HHmmss"));
				wxactuser.set("message_count", 1); // 本条也算是一条
				wxactuser.set("actevent_id", actevent.getLong("id"));
				wxactuser.save();

				eventUserList.add(wxactuser);

				// 返回签到回复信息
				WXRespMessage wxrespmsg = WXRespMessage.dao.findOne(wxAccountId, WXRule.RULE_TYPE_DEFAULT, "ACTEVENT", wxActEventId, false);
				return wxrespmsg != null ? wxrespmsg.toResponseMessage() : null;
			} else {
				// 对活动关键字进行处理
				List<WXRule> rules = WXRule.dao.findRules(wxAccountId, WXRule.RULE_TYPE_KEYWORD, "ACTEVENT", wxActEventId);
				ResponseMessage respMessage = MessageService.matcheResponseMessage(wxAccountId, content, rules);
				if (respMessage != null) {
					return respMessage;
				}
			}
		}

		// 保存其它的上行消息
		WXActMessage actmsg = new WXActMessage();
		actmsg.set("open_id", message.getFromUserName());
		actmsg.set("msg_id", message.getMsgId());
		actmsg.set("actevent_id", this.wxActEventId);
		actmsg.save();

		// 最简单的处理是把信息保存在内存中
		eventMessageList.add(actmsg);

		return null;
	}
}
