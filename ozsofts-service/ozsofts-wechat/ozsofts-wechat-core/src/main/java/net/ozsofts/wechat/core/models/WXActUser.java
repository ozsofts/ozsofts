package net.ozsofts.wechat.core.models;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;

public class WXActUser extends Model<WXActUser> {
	private static final long serialVersionUID = 3269550578305589762L;

	public static final WXActUser dao = new WXActUser();

	/**
	 * <p>
	 * 查找活动对应的用户信息
	 * 
	 * @param openId
	 * @return
	 */
	public WXActUser getUserByOpenId(Long actEventId, String openId) {
		return dao.findFirst("select * from t_wx_actusers where actevent_id=? and open_id=?", actEventId, openId);
	}

	/**
	 * <p>
	 * 取得活动所有的用户信息
	 * 
	 * @param actEventId
	 * @return
	 */
	public List<WXActUser> findUsersByEvent(Long actEventId) {
		return dao.find("select * from t_wx_actusers where actevent_id=?", actEventId);
	}
}
