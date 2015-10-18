package net.ozsofts.wechat.core.models;

import java.util.List;

import org.joda.time.DateTime;

import com.jfinal.plugin.activerecord.Model;

public class WXActEvent extends Model<WXActEvent> {
	private static final long serialVersionUID = 3269550578305589762L;

	public static final WXActEvent dao = new WXActEvent();

	//
	// 活动状态
	//
	/** 初始状态 */
	public final static int STATUS_INIT = -1;
	/** 启用状态 */
	public final static int STATUS_OK = 0;

	/**
	 * <p>
	 * 读取所有的可用的活动信息
	 * 
	 * @param wxAccountId
	 *            活动对应的公众号的标识
	 * @param maxActId
	 *            最大的活动标识，如果设置了此参数，则从此参数后读取活动的信息
	 * @return
	 */
	public List<WXActEvent> findAllEvents(Long wxAccountId, Long maxActId) {
		// String currDate = DateUtil.formatDate(new Date(), "yyyyMMdd");
		String currDate = new DateTime().toString("yyyyMMdd");
		if (maxActId == null) {
			maxActId = 0l;
		}

		return find("select * from t_wx_actevents where account_id=? and start_date<=? and end_date>=? and status=? and id>? order by id asc",
						wxAccountId, currDate, currDate, STATUS_OK, maxActId);
	}
}
