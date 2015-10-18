package net.ozsofts.wechat.core.models;

import java.util.ArrayList;
import java.util.List;

import net.ozsofts.wechat.core.message.type.ResponseMessage;

import org.apache.commons.lang.StringUtils;

import com.jfinal.plugin.activerecord.Model;

public class WXRespMessage extends Model<WXRespMessage> {
	private static final long serialVersionUID = 3269550578305589762L;

	public static final WXRespMessage dao = new WXRespMessage();

	public WXRespMessage findOne(long accountId, String ruleType, String refType, long refId, boolean auto) {
		StringBuilder sql = new StringBuilder(
						"select m.* from t_wx_rules r inner join t_wx_resp_messages m on r.resp_message_id=m.id where r.account_id=? and r.rule_type=? and r.ref_type=?");

		List<Object> params = new ArrayList<Object>();
		params.add(accountId);
		params.add(StringUtils.isNotBlank(ruleType) ? ruleType : WXRule.RULE_TYPE_AUTO);
		params.add(StringUtils.isBlank(refType) ? "" : refType);
		if (StringUtils.isNotBlank(refType) && refId > 0l) {
			sql.append(" and ref_id=?");
			params.add(refId);
		}
		sql.append(" limit 1");

		WXRespMessage wxrespmsg = findFirst(sql.toString(), params.toArray());

		// 如果需要自动回复，则读取自动回复的定义
		if (wxrespmsg == null && auto) {
			wxrespmsg = findOne(accountId, WXRule.RULE_TYPE_AUTO, refType, refId, false);
		}

		return wxrespmsg;
	}

	public ResponseMessage toResponseMessage() {
		ResponseMessage respMessage = null;

		String msgType = get("msg_type");
		if (ResponseMessage.MSGTYPE_TEXT.equals(msgType)) {
			// 文本消息
		} else if (ResponseMessage.MSGTYPE_IMAGE.equals(msgType)) {
			// 图片消息
		} else if (ResponseMessage.MSGTYPE_VOICE.equals(msgType)) {
			// 语音消息
		} else if (ResponseMessage.MSGTYPE_VIDEO.equals(msgType)) {
			// 视频消息
		} else if (ResponseMessage.MSGTYPE_MUSIC.equals(msgType)) {
			// 音乐消息
		}

		return respMessage;
	}
}
