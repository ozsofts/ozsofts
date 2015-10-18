package net.ozsofts.wechat.core.models;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;

public class WXActMessage extends Model<WXActMessage> {
	private static final long serialVersionUID = 3269550578305589762L;

	public static final WXActMessage dao = new WXActMessage();

	/**
	 * <p>
	 * 取得活动所有的用户信息
	 * 
	 * @param actEventId
	 * @return
	 */
	public List<WXActMessage> findMessagesByEvent(Long actEventId) {
		return dao.find("select * from t_wx_actmessages where actevent_id=?", actEventId);
	}
}
