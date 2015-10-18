package net.ozsofts.wechat.core.models.entity.base;

import net.ozsofts.wechat.core.models.entity.WXAccount;

/**
 * <p>
 * 微信消息。
 * <p>
 * 用户上行的消息一般都是简单类型的消息，很少会出现图文消息。
 * 
 * <p>
 * 下行用户的消息分为三类：
 * <ul>
 * <li>1) 主动向用户群发的消息。这类信息以图文消息为主。</li>
 * <li>2) 被动向用户回复的消息。这类消息可以是图文消息也可以是普通类型的消息，视业务规则而定。</li>
 * <li>3) 与用户进行互动的消息。除去被动回复外，还可以主动向用户回复信息，主要是普通消息，也会回复图文消息。</li>
 * </ul>
 * 
 * <p>
 * 在系统中需要记录与用户沟通时的消息数据。
 * 
 * @author jack
 */
public class BaseWXMessage {
	// @Id
	// @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/** 发送消息的用户 */
	// @Column(name = "to_user", length = 64)
	private String toUser;
	/** 接收消息的用户 */
	// @Column(name = "from_user", length = 64)
	private String fromUser;
	/** 消息创建日期，YYYYMMDD */
	// @Column(name = "create_date", length = 10)
	private String createDate;
	/** 消息创建时间，HHMMSS */
	// @Column(name = "create_time", length = 10)
	private String createTime;
	/** 消息类型 */
	// @Column(name = "msg_type", length = 32)
	private String msgType;
	/** 消息的传送方向，RECV表示公众号接收到的消息，RPLY表示公众号回复的消息 */
	// @Column(name = "direction", length = 16)
	private String direction;
	/** 消息标识号，64位整型 */
	// @Column(name = "msg_id", length = 64)
	private String msgId;

	//
	// 针对文本消息
	//
	/** 文本消息内容 */
	// @Column(name = "content", length = 1024)
	private String content;

	//
	// 针对图片信息
	//
	/** 图片的链接地址 */
	// @Column(name = "pic_url", length = 256)
	private String picUrl;

	//
	// 针对语音信息
	//
	/** 语音格式 */
	// @Column(name = "format", length = 32)
	private String format;

	//
	// 针对视频和小视频消息
	//
	/** 视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据。 */
	// @Column(name = "thumb_media_id", length = 64)
	private String thumbMediaId;

	//
	// 针对图片、视频以及小视频消息
	//
	/** 图片、语音消息媒体id，可以调用多媒体文件下载接口拉取数据。 */
	// @Column(name = "media_id", length = 64)
	private String mediaId;

	//
	// 针对地理位置消息
	//
	/** 地理位置纬度 */
	// @Column(name = "location_x", length = 64)
	private String locationX;
	/** 地理位置经度 */
	// @Column(name = "location_y", length = 64)
	private String locationY;
	/** 地图缩放大小 */
	// @Column(name = "scale", length = 64)
	private String scale;
	/** 地理位置信息 */
	// @Column(name = "label", length = 64)
	private String label;

	//
	// 针对链接消息
	//
	/** 消息标题 */
	// @Column(name = "title", length = 512)
	private String title;
	/** 消息描述 */
	// @Column(name = "description", length = 1024)
	private String description;
	/** 消息链接 */
	// @Column(name = "url", length = 256)
	private String url;

	/** 消息所属的公众号 */
	private WXAccount account;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToUser() {
		return toUser;
	}

	public void setToUser(String toUser) {
		this.toUser = toUser;
	}

	public String getFromUser() {
		return fromUser;
	}

	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getThumbMediaId() {
		return thumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		this.thumbMediaId = thumbMediaId;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public String getLocationX() {
		return locationX;
	}

	public void setLocationX(String locationX) {
		this.locationX = locationX;
	}

	public String getLocationY() {
		return locationY;
	}

	public void setLocationY(String locationY) {
		this.locationY = locationY;
	}

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public WXAccount getAccount() {
		return account;
	}

	public void setAccount(WXAccount account) {
		this.account = account;
	}
}
