package net.ozsofts.wechat.core.models;

import net.ozsofts.wechat.core.message.type.ResponseImageMessage;
import net.ozsofts.wechat.core.message.type.ResponseMessage;
import net.ozsofts.wechat.core.message.type.ResponseMusicMessage;
import net.ozsofts.wechat.core.message.type.ResponseTextMessage;
import net.ozsofts.wechat.core.message.type.ResponseVideoMessage;

import com.jfinal.plugin.activerecord.Model;

public class WXMessage extends Model<WXMessage> {
	private static final long serialVersionUID = 3269550578305589762L;

	public static final WXMessage dao = new WXMessage();

	public WXMessage findByMsgid(String msgId) {
		return dao.findFirst("select * from t_wx_messages where msg_id=? limit 1", msgId);
	}

	public void saveResponseMessage(long accountId, ResponseMessage respMessage) {
		WXMessage wxmsg = new WXMessage();

		String msgType = respMessage.getMsgType();

		wxmsg.set("to_user", respMessage.getToUserName());
		wxmsg.set("from_user", respMessage.getFromUserName());
		wxmsg.set("msg_type", msgType);

		if (ResponseMessage.MSGTYPE_TEXT.equals(msgType)) {
			wxmsg.set("content", ((ResponseTextMessage) respMessage).getContent());
		} else if (ResponseMessage.MSGTYPE_IMAGE.equals(msgType) || ResponseMessage.MSGTYPE_VOICE.equals(msgType)) {
			wxmsg.set("media_id", ((ResponseImageMessage) respMessage).getMediaId());
		} else if (ResponseMessage.MSGTYPE_VIDEO.equals(msgType)) {
			wxmsg.set("title", ((ResponseVideoMessage) respMessage).getTitle());
			wxmsg.set("description", ((ResponseVideoMessage) respMessage).getDescription());
			wxmsg.set("media_id", ((ResponseVideoMessage) respMessage).getMediaId());
			wxmsg.set("thumb_media_id", ((ResponseVideoMessage) respMessage).getThumbMediaId());
		} else if (ResponseMessage.MSGTYPE_MUSIC.equals(msgType)) {
			wxmsg.set("title", ((ResponseMusicMessage) respMessage).getTitle());
			wxmsg.set("description", ((ResponseMusicMessage) respMessage).getDescription());
			wxmsg.set("music_url", ((ResponseMusicMessage) respMessage).getMusicUrl());
			wxmsg.set("hq_music_url", ((ResponseMusicMessage) respMessage).getHqMusicUrl());
			wxmsg.set("thumb_media_id", ((ResponseMusicMessage) respMessage).getThumbMediaId());
		} else if (ResponseMessage.MSGTYPE_NEWS.equals(msgType)) {
			// 暂不支持图文信息下传
		}
		wxmsg.set("account_id", accountId);
		wxmsg.save();
	}
}
