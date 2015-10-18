package net.ozsofts.wechat.core.service.message;

import java.util.List;
import java.util.regex.Pattern;

import net.ozsofts.wechat.core.message.type.ReceiveImageMessage;
import net.ozsofts.wechat.core.message.type.ReceiveLinkMessage;
import net.ozsofts.wechat.core.message.type.ReceiveLocationMessage;
import net.ozsofts.wechat.core.message.type.ReceiveMessage;
import net.ozsofts.wechat.core.message.type.ReceiveTextMessage;
import net.ozsofts.wechat.core.message.type.ReceiveVideoMessage;
import net.ozsofts.wechat.core.message.type.ReceiveVoiceMessage;
import net.ozsofts.wechat.core.message.type.ResponseMessage;
import net.ozsofts.wechat.core.models.WXAccount;
import net.ozsofts.wechat.core.models.WXMessage;
import net.ozsofts.wechat.core.models.WXRespMessage;
import net.ozsofts.wechat.core.models.WXRule;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * 对所有上行的信息进行保存
 * 
 * @author jack
 */
public class MessageService {

	/**
	 * <p>
	 * 保存上行消息
	 * 
	 * @param account
	 * @param recvMessage
	 * @return
	 * @throws Exception
	 */
	public static WXMessage save(WXAccount account, ReceiveMessage recvMessage) throws Exception {
		if (ReceiveMessage.MSGTYPE_EVENT.equals(recvMessage.getMsgType())) {
			return null;
		}

		WXMessage message = WXMessage.dao.findByMsgid(recvMessage.getMsgId());
		if (message != null) {
			// 微信会有重发机制，在这种情况下，不需要再进行保存
			return message;
		}

		message = new WXMessage();
		message.set("to_user", recvMessage.getToUserName());
		message.set("from_user", recvMessage.getFromUserName());

		message.set("msg_date", recvMessage.getCreateTime().substring(0, 8));
		message.set("msg_time", recvMessage.getCreateTime().substring(8));
		message.set("msg_type", recvMessage.getMsgType());
		message.set("msg_id", recvMessage.getMsgId());
		message.set("direction", "RECV");

		if (ReceiveMessage.MSGTYPE_TEXT.equals(recvMessage.getMsgType())) {
			message.set("content", ((ReceiveTextMessage) recvMessage).getContent());
		} else if (ReceiveMessage.MSGTYPE_IMAGE.equals(recvMessage.getMsgType())) {
			message.set("pic_url", ((ReceiveImageMessage) recvMessage).getPicUrl());
			message.set("media_id", ((ReceiveVideoMessage) recvMessage).getMediaId());
		} else if (ReceiveMessage.MSGTYPE_VOICE.equals(recvMessage.getMsgType())) {
			message.set("format", ((ReceiveVoiceMessage) recvMessage).getFormat());
			message.set("media_id", ((ReceiveVoiceMessage) recvMessage).getMediaId());
		} else if (ReceiveMessage.MSGTYPE_VIDEO.equals(recvMessage.getMsgType())
						|| ReceiveMessage.MSGTYPE_SHORTVIDEO.equals(recvMessage.getMsgType())) {
			message.set("media_id", ((ReceiveVideoMessage) recvMessage).getMediaId());
			message.set("thumb_media_id", ((ReceiveVideoMessage) recvMessage).getThumbMediaId());
		} else if (ReceiveMessage.MSGTYPE_LOCATION.equals(recvMessage.getMsgType())) {
			message.set("location_x", ((ReceiveLocationMessage) recvMessage).getLocationX());
			message.set("location_y", ((ReceiveLocationMessage) recvMessage).getLocationY());
			message.set("scale", ((ReceiveLocationMessage) recvMessage).getScale());
			message.set("label", ((ReceiveLocationMessage) recvMessage).getLabel());
		} else if (ReceiveMessage.MSGTYPE_LINK.equals(recvMessage.getMsgType())) {
			message.set("title", ((ReceiveLinkMessage) recvMessage).getTitle());
			message.set("description", ((ReceiveLinkMessage) recvMessage).getDescription());
			message.set("url", ((ReceiveLinkMessage) recvMessage).getUrl());
		}

		message.set("account_id", account.getLong("id"));

		// TODO 对媒介进行处理
		String mediaId = message.getStr("media_id");
		if (StringUtils.isNotBlank(mediaId)) {
		}

		mediaId = message.getStr("thumb_media_id");
		if (StringUtils.isNotBlank(mediaId)) {
		}

		message.save();
		return message;
	}

	/**
	 * <p>
	 * 对系统层面的关键字消息进行处理
	 * 
	 * @param account
	 * @param recvMessage
	 * @return
	 */
	public static ResponseMessage handleKeyword(WXAccount account, ReceiveMessage recvMessage) {
		if (ReceiveMessage.MSGTYPE_TEXT.equals(recvMessage.getMsgType())) {
			return null;
		}

		String content = ((ReceiveTextMessage) recvMessage).getContent();

		// 匹配关键字,取得所有refType=""的规则，这些规则是系统规则
		List<WXRule> rules = WXRule.dao.findRules(account.getId(), WXRule.RULE_TYPE_KEYWORD, null, 0l);
		return matcheResponseMessage(account.getId(), content, rules);
	}

	public static ResponseMessage matcheResponseMessage(long accountId, String content, List<WXRule> ruleList) {
		WXRule matchedRule = null;
		for (WXRule rule : ruleList) {
			String matchType = rule.getStr("match_type");
			String ruleValue = rule.getStr("rule_value");

			if (WXRule.MATCH_TYPE_REGEX.equals(matchType)) {
				matchedRule = Pattern.matches(ruleValue, content) ? rule : null;
			} else if (WXRule.MATCH_TYPE_STARTSWITH.equals(matchType)) {
				matchedRule = content.startsWith(ruleValue) ? rule : null;
			} else if (WXRule.MATCH_TYPE_ENDSWITH.equals(matchType)) {
				matchedRule = content.endsWith(ruleValue) ? rule : null;
			} else if (WXRule.MATCH_TYPE_CONTAINS.equals(matchType)) {
				matchedRule = content.indexOf(ruleValue) > -1 ? rule : null;
			}

			if (matchedRule != null)
				break;
		}

		WXRespMessage wxrespmsg = null;
		if (matchedRule == null) {
			// 没有找到匹配的关键字
			wxrespmsg = WXRespMessage.dao.findOne(accountId, WXRule.RULE_TYPE_AUTO, "", 0l, false);
		} else {
			// 找到匹配的规则
			wxrespmsg = WXRespMessage.dao.findById(matchedRule.getLong("id"));
		}
		return wxrespmsg != null ? wxrespmsg.toResponseMessage() : null;
	}
}
