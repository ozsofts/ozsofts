package net.ozsofts.wechat.core.models.entity.base;

/**
 * <p>
 * 微信公众号信息。
 * 
 * @author jack
 */
// @MappedSuperclass
public class BaseWXAccount {

	// @Id
	// @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/** 公众号的系统内部ID，这个参数应当设置在微信回调URL中，参数的名称中sid */
	// @Column(name = "system_id", length = 64)
	private String systemId;

	/** 应用ID */
	// @Column(name = "app_id", length = 128)
	private String appId;
	/** 应用密钥 */
	// @Column(name = "secret", length = 128)
	private String secret;
	/** 令牌 */
	// @Column(name = "token", length = 128)
	private String token;
	/** 消息加解密密钥 */
	// @Column(name = "encoding_aes_key", length = 128)
	private String encodingAesKey;
	/** 消息加解密类型 */
	// @Column(name = "encrypt_type", length = 16)
	private String encryptType;

	/** 公众号类型，分为订阅号和服务号 */
	// @Column(name = "account_type", length = 16)
	private String type;
	/** 公众号是否通过认证 */
	// @Column(name = "is_auth")
	private boolean auth = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getEncodingAesKey() {
		return encodingAesKey;
	}

	public void setEncodingAesKey(String encodingAesKey) {
		this.encodingAesKey = encodingAesKey;
	}

	public String getEncryptType() {
		return encryptType;
	}

	public void setEncryptType(String encryptType) {
		this.encryptType = encryptType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isAuth() {
		return auth;
	}

	public void setAuth(boolean auth) {
		this.auth = auth;
	}
}
