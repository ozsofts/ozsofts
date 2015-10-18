package net.ozsofts.wechat.core.models;

import java.net.URLDecoder;

import com.jfinal.plugin.activerecord.Model;

public class WXUser extends Model<WXUser> {
	private static final long serialVersionUID = -6826195256951458565L;

	public static final WXUser dao = new WXUser();

	public String getNickName() {
		try {
			return URLDecoder.decode(getStr("nick_name"), "UTF-8");
		} catch (Exception ex) {
			return getStr("nick_name");
		}
	}

	public WXUser getUserByOpenId(String openId) {
		return dao.findFirst("select * from t_wx_users where open_id=?", openId);
	}
}
