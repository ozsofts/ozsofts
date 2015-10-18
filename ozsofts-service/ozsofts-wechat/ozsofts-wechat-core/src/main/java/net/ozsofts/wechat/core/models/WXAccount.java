package net.ozsofts.wechat.core.models;

import java.util.List;

import net.ozsofts.wechat.utils.AesException;
import net.ozsofts.wechat.utils.SHA1;
import net.ozsofts.wechat.utils.WXBizMsgCrypt;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.plugin.activerecord.Model;

public class WXAccount extends Model<WXAccount> {
	private static final long serialVersionUID = -6826195256951458565L;

	private static Logger log = LoggerFactory.getLogger(WXAccount.class);

	public static final WXAccount dao = new WXAccount();

	//
	// 公众号类型
	//
	/** 订阅号标识 */
	public static final String ACCOUNT_TYPE_DY = "DY";
	/** 服务号标识 */
	public static final String ACCOUNT_TYPE_FW = "FW";

	//
	// 消息加解密类型定义
	//
	/** 不加密 */
	public static final String ENCRYPT_TYPE_RAW = "raw";
	/** AES加密 */
	public static final String ENCRYPT_TYPE_AES = "aes";

	//
	// 方便的BEAN存取方法
	//
	public Long getId() {
		return getLong("id");
	}

	public WXAccount setId(Long id) {
		set("id", id);
		return this;
	}

	public String getSystemId() {
		return getStr("system_id");
	}

	public WXAccount setSystemId(String systemId) {
		set("system_id", systemId);
		return this;
	}

	public String getName() {
		return get("name");
	}

	public WXAccount setName(String name) {
		set("name", name);
		return this;
	}

	public String getAppId() {
		return get("app_id");
	}

	public WXAccount setAppId(String appId) {
		set("app_id", appId);
		return this;
	}

	public String getSecret() {
		return get("secret");
	}

	public WXAccount setSecret(String secret) {
		set("secret", secret);
		return this;
	}

	public String getToken() {
		return get("token");
	}

	public WXAccount setToken(String token) {
		set("token", token);
		return this;
	}

	public String getEncodingAESKey() {
		return get("encoding_aes_key");
	}

	public WXAccount setEncodingAESKey(String encodingAESKey) {
		set("encoding_aes_key", encodingAESKey);
		return this;
	}

	public String getEncryptType() {
		return get("encrypt_type");
	}

	public WXAccount setEncryptType(String encryptType) {
		set("encrypt_type", encryptType);
		return this;
	}

	public String getAccountType() {
		return get("account_type");
	}

	public WXAccount setAccountType(String accountType) {
		set("account_type", accountType);
		return this;
	}

	public boolean isAuth() {
		return get("is_auth");
	}

	public WXAccount setAuth(boolean auth) {
		set("is_auth", auth);
		return this;
	}

	public boolean isInit() {
		return get("is_init");
	}

	public WXAccount setInit(boolean init) {
		set("is_init", init);
		return this;
	}

	public boolean isEnabled() {
		return get("is_enabled");
	}

	public WXAccount setEnabled(boolean enabled) {
		set("is_enabled", enabled);
		return this;
	}

	//
	// 微信账号相关的方法
	//
	/**
	 * <p>
	 * 在设置微信参数时需要通过这样的处理进行验证
	 * 
	 * @param timestamp
	 * @param nonce
	 * @param echoStr
	 * @param verifyEncryptType
	 * @param signature
	 * @return
	 */
	public String verifyUrl(String timestamp, String nonce, String echoStr, String verifyEncryptType, String signature) {
		String returnStr = "";
		try {
			if (StringUtils.isNotBlank(verifyEncryptType) && WXAccount.ENCRYPT_TYPE_AES.equalsIgnoreCase(verifyEncryptType)) {
				WXBizMsgCrypt msgCrypt = new WXBizMsgCrypt(getToken(), getEncodingAESKey(), getAppId());
				returnStr = msgCrypt.verifyUrl(signature, timestamp, nonce, echoStr);
			} else {
				if (verifySignature(signature, timestamp, nonce)) {
					returnStr = echoStr;
				}
			}
		} catch (AesException e) {
			log.error("在验证消息的签名时出现错误!", e);
		}
		return returnStr;
	}

	public String handleRequestMessage(String timestamp, String nonce, String requestEncryptType, String signature, String postData) throws Exception {
		String returnStr = "";
		try {
			if (!getEncryptType().equalsIgnoreCase(requestEncryptType)) {
				throw new Exception("[" + getSystemId() + "] 配置的加密方式[" + getEncryptType() + "]与上送[" + requestEncryptType + "]不匹配，请检查配置!");
			}

			if (WXAccount.ENCRYPT_TYPE_AES.equalsIgnoreCase(requestEncryptType)) {
				WXBizMsgCrypt msgCrypt = new WXBizMsgCrypt(getToken(), getEncodingAESKey(), getAppId());
				returnStr = msgCrypt.decryptMsg(signature, timestamp, nonce, postData);
			} else {
				if (verifySignature(signature, timestamp, nonce)) {
					returnStr = postData;
				}
			}
		} catch (AesException e) {
			log.error("在验证消息的签名时出现错误!", e);
		}

		return returnStr;
	}

	public String handleResponseMessage(String timestamp, String nonce, String responseData) throws Exception {
		if (WXAccount.ENCRYPT_TYPE_RAW.equalsIgnoreCase(getEncryptType()))
			return responseData;

		WXBizMsgCrypt msgCrypt = new WXBizMsgCrypt(getToken(), getEncodingAESKey(), getAppId());
		return msgCrypt.encryptMsg(responseData, timestamp, nonce);
	}

	private boolean verifySignature(String signature, String timestamp, String nonce) throws AesException {
		return signature.equals(SHA1.getSHA1(getToken(), timestamp, nonce, ""));
	}

	//
	// DAO方法
	//
	public List<WXAccount> getAll() {
		return dao.find("select * from t_wx_accounts");
	}

	public WXAccount findBySystemId(String systemId) {
		return dao.findFirst("select * from t_wx_accounts where system_id=?", systemId);
	}
}
