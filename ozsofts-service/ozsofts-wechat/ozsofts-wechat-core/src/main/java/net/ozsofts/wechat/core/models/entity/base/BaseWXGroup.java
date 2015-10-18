package net.ozsofts.wechat.core.models.entity.base;

import net.ozsofts.wechat.core.models.entity.WXAccount;

public class BaseWXGroup {
	// @Id
	// @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/** 微信群组标识 */
	// @Column(name = "group_id")
	private int groupId;
	/** 群组名称 */
	// @Column(name = "name", length = 64)
	private String name;
	/** 群组中的用户数量 */
	// @Column(name = "user_count")
	private Integer userCount;

	/** 群组所属的微信公众号 */
	private WXAccount account;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getUserCount() {
		return userCount;
	}

	public void setUserCount(Integer userCount) {
		this.userCount = userCount;
	}

	public WXAccount getAccount() {
		return account;
	}

	public void setAccount(WXAccount account) {
		this.account = account;
	}
}
