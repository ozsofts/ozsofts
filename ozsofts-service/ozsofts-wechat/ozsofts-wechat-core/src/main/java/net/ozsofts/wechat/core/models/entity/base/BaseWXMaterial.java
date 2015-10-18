package net.ozsofts.wechat.core.models.entity.base;

import net.ozsofts.wechat.core.models.entity.WXAccount;

/**
 * <p>
 * 微信素材
 * 
 * @author jack
 */
public class BaseWXMaterial {
	// @Id
	// @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/** 媒体类型 */
	// @Column(name = "type", length = 32)
	private String type;
	/** 媒体标识 */
	// @Column(name = "media_id", length = 64)
	private String mediaId;
	/** 媒体标题，一般在视频材料使用 */
	// @Column(name = "title", length = 256)
	private String title;
	/** 媒体说明，一般在视频材料使用 */
	// @Column(name = "introduction", length = 1024)
	private String introduction;

	/** 素材所属的公众号 */
	private WXAccount account;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public WXAccount getAccount() {
		return account;
	}

	public void setAccount(WXAccount account) {
		this.account = account;
	}
}
