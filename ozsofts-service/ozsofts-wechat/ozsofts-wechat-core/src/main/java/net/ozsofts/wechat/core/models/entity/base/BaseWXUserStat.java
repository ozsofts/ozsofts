package net.ozsofts.wechat.core.models.entity.base;

import net.ozsofts.wechat.core.models.entity.WXAccount;

public class BaseWXUserStat {
	// @Id
	// @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/** 统计日期，YYYYMMDD */
	// //@Column(name = "stat_date", length = 10)
	private String statDate;
	/** 统计日关注用户数 */
	// //@Column(name = "sub_count")
	private Integer subscribeCount;
	/** 统计日取消关注用户数 */
	// //@Column(name = "unsub_count")
	private Integer unsubscribeCount;
	/** 当前总用户人数 */
	// //@Column(name = "user_count")
	private Integer userCount;
	/** 今天消息数，指上行的用户消息 */
	// //@Column(name = "msg_count")
	private Integer msgCount;

	/** 此统计信息针对的公众号 */
	private WXAccount account;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStatDate() {
		return statDate;
	}

	public void setStatDate(String statDate) {
		this.statDate = statDate;
	}

	public Integer getSubscribeCount() {
		return subscribeCount;
	}

	public void setSubscribeCount(Integer subscribeCount) {
		this.subscribeCount = subscribeCount;
	}

	public Integer getUnsubscribeCount() {
		return unsubscribeCount;
	}

	public void setUnsubscribeCount(Integer unsubscribeCount) {
		this.unsubscribeCount = unsubscribeCount;
	}

	public Integer getUserCount() {
		return userCount;
	}

	public void setUserCount(Integer userCount) {
		this.userCount = userCount;
	}

	public Integer getMsgCount() {
		return msgCount;
	}

	public void setMsgCount(Integer msgCount) {
		this.msgCount = msgCount;
	}

	public WXAccount getAccount() {
		return account;
	}

	public void setAccount(WXAccount account) {
		this.account = account;
	}
}
