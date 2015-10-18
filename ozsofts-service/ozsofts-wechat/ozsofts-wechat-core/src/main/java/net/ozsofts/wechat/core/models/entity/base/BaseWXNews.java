package net.ozsofts.wechat.core.models.entity.base;

import net.ozsofts.wechat.core.models.entity.WXAccount;

/**
 * <p>
 * 微信图文信息
 * 
 * @author jack
 */
public class BaseWXNews {
	// @Id
	// @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/** 标题 */
	// @Column(name = "title", length = 256)
	private String title;
	/** 图文消息的封面图片素材id（必须是永久mediaID） */
	// @Column(name = "thumb_media_id", length = 64)
	private String thumbMediaId;
	/** 作者 */
	// @Column(name = "author", length = 64)
	private String author;
	/** 图文消息的摘要，仅有单图文消息才有摘要，多图文此处为空 */
	// @Column(name = "digest", length = 64)
	private String digest;
	/** 是否显示封面，0为false，即不显示，1为true，即显示 */
	// @Column(name = "show_cover_pic", length = 1)
	private String showCoverPic;
	/** 图文消息的具体内容，支持HTML标签，必须少于2万字符，小于1M，且此处会去除JS */
	// @Column(name = "content", length = 4096)
	private String content;
	/** 图文消息的原文地址，即点击“阅读原文”后的URL */
	// @Column(name = "content_source_url", length = 256)
	private String contentSourceUrl;

	/** 图文消息所属的公众号 */
	private WXAccount account;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getThumbMediaId() {
		return thumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		this.thumbMediaId = thumbMediaId;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}

	public String getShowCoverPic() {
		return showCoverPic;
	}

	public void setShowCoverPic(String showCoverPic) {
		this.showCoverPic = showCoverPic;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContentSourceUrl() {
		return contentSourceUrl;
	}

	public void setContentSourceUrl(String contentSourceUrl) {
		this.contentSourceUrl = contentSourceUrl;
	}

	public WXAccount getAccount() {
		return account;
	}

	public void setAccount(WXAccount account) {
		this.account = account;
	}
}
