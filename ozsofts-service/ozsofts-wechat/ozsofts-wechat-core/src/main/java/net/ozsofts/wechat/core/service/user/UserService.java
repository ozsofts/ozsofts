package net.ozsofts.wechat.core.service.user;

import net.ozsofts.wechat.core.functions.UserFunction;
import net.ozsofts.wechat.core.message.type.ResponseMessage;
import net.ozsofts.wechat.core.models.WXAccount;
import net.ozsofts.wechat.core.models.WXRespMessage;
import net.ozsofts.wechat.core.models.WXRule;
import net.ozsofts.wechat.core.models.WXUser;
import net.ozsofts.wechat.core.servlet.TokenService;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserService {
	private static Logger log = LoggerFactory.getLogger(UserService.class);

	public static ResponseMessage subscribe(WXAccount account, String openId) {
		WXUser wxuser = WXUser.dao.getUserByOpenId(openId);
		if (wxuser != null) {
			// 如果原来已经有用户信息，则检查是否退订过
			if (wxuser.getBoolean("is_subscribe")) {
				// 如果当前是关注状态，则直接返回成功
				return null;
			}

			wxuser.set("is_subscribe", true);

			DateTime dt = new DateTime();
			wxuser.set("sub_date", dt.toString("yyyyMMdd"));
			wxuser.set("sub_time", dt.toString("HHmmss"));

			wxuser.update();
		} else {
			try {
				String token = TokenService.me.getAccessToken(account.getSystemId());

				// 新关注的用户,根据openId从微信取相关的数据
				wxuser = UserFunction.getUserInfo(token, openId);
				wxuser.set("account_id", account.getLong("id"));
				wxuser.save();
			} catch (Exception ex) {
				log.error("保存用户关注信息时出现错误-sysId:[{}], openId:[{}]", account.getStr("system_id"), openId);
				return null;
			}
		}

		WXRespMessage wxrespmsg = WXRespMessage.dao.findOne(account.getId(), WXRule.RULE_TYPE_DEFAULT, "", 0l, false);
		if (wxrespmsg == null) {
			return null;
		}

		return wxrespmsg.toResponseMessage();
	}

	public static ResponseMessage unsubscribe(WXAccount account, String openId) {
		WXUser wxuser = WXUser.dao.getUserByOpenId(openId);
		if (wxuser == null) {
			return null;
		}

		if (!wxuser.getBoolean("is_subscribe")) {
			return null;
		}

		wxuser.set("is_subscribe", false);

		DateTime dt = new DateTime();
		wxuser.set("unsub_date", dt.toString("yyyyMMdd"));
		wxuser.set("unsub_time", dt.toString("HHmmss"));

		wxuser.update();
		return null;
	}
}
