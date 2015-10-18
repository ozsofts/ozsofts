package net.ozsofts.wechat.core.models.entity.base;

import net.ozsofts.wechat.core.models.entity.WXAccount;
import net.ozsofts.wechat.core.models.entity.WXGroup;

public class BaseWXUser {
	// @Id
	// @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/** 用户的标识，对当前公众号唯一 */
	// @Column(name = "open_id", length = 64)
	private String openId;
	/** 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。 */
	// @Column(name = "union_id", length = 64)
	private String unionId;
	/** 用户的昵称 */
	// @Column(name = "nick_name", length = 64)
	private String nickName;
	/** 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知 */
	// @Column(name = "sex", length = 1)
	private String sex;
	/** 用户的语言，简体中文为zh_CN */
	// @Column(name = "language", length = 16)
	private String language;
	/** 用户所在城市 */
	// @Column(name = "city", length = 16)
	private String city;
	/** 用户所在省份 */
	// @Column(name = "province", length = 64)
	private String province;
	/** 用户所在国家 */
	// @Column(name = "country", length = 64)
	private String country;
	/**
	 * 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。
	 * 若用户更换头像，原有头像URL将失效。
	 */
	// @Column(name = "head_img_url", length = 256)
	private String headImgUrl;
	/** 用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息。 */
	// @Column(name = "is_subscribe")
	private boolean subscribe = true;
	/** 用户关注日期，YYYYMMDD。如果用户曾多次关注，则取最后关注时间 */
	// @Column(name = "sub_date", length = 64)
	private String subscribeDate;
	/** 用户关注时间，HHMMSS。如果用户曾多次关注，则取最后关注时间 */
	// @Column(name = "sub_time", length = 64)
	private String subscribeTime;
	/** 用户备注 */
	// @Column(name = "remark", length = 128)
	private String remark;
	/** 用户手机号码，在用户参与活动中获取，一般是没有的 */
	// @Column(name = "mobile", length = 32)
	private String mobile;

	/** 用户所属的群组 */
	private WXGroup group;
	/** 用户所属的公众号 */
	private WXAccount account;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getUnionId() {
		return unionId;
	}

	public void setUnionId(String unionId) {
		this.unionId = unionId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

	public boolean isSubscribe() {
		return subscribe;
	}

	public void setSubscribe(boolean subscribe) {
		this.subscribe = subscribe;
	}

	public String getSubscribeDate() {
		return subscribeDate;
	}

	public void setSubscribeDate(String subscribeDate) {
		this.subscribeDate = subscribeDate;
	}

	public String getSubscribeTime() {
		return subscribeTime;
	}

	public void setSubscribeTime(String subscribeTime) {
		this.subscribeTime = subscribeTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public WXGroup getGroup() {
		return group;
	}

	public void setGroup(WXGroup group) {
		this.group = group;
	}

	public WXAccount getAccount() {
		return account;
	}

	public void setAccount(WXAccount account) {
		this.account = account;
	}
}
