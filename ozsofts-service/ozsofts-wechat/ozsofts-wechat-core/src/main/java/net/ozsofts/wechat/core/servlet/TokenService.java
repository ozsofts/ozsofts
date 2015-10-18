package net.ozsofts.wechat.core.servlet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import net.ozsofts.wechat.core.functions.TokenFunction;
import net.ozsofts.wechat.core.models.WXAccount;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TokenService {
	private static Logger log = LoggerFactory.getLogger(TokenService.class);

	public final static String ACCESS_TOKEN = "access_token";
	public final static String EXPIRES_IN = "expires_in";

	public static TokenService me = new TokenService();

	private Map<String, TokenData> tokenDataMap = new HashMap<String, TokenData>();

	public void initialize() {
		// 启用处理线程
		new RefreshThread().start();
	}

	public String getAccessToken(String systemId) {
		TokenData tokenData = tokenDataMap.get(systemId);
		if (tokenData == null) {
			return "";
		}

		return tokenData.getAccessToken();
	}

	/**
	 * <p>
	 * 调用微信的服务刷新access token
	 */
	public void refreshToken(WXAccount wxaccount, TokenData tokenData) {
		try {
			Map<String, Object> tokenMessages = TokenFunction.getToken(wxaccount.getAppId(), wxaccount.getSecret());
			tokenData.setAccessToken((String) tokenMessages.get(ACCESS_TOKEN));
			int expired = (Integer) tokenMessages.get(EXPIRES_IN);
			tokenData.setUpdatedTime(System.currentTimeMillis() + expired * 1000); // 转化为毫秒

			StringBuilder sb = new StringBuilder();
			sb.append("[").append(wxaccount.getSystemId()).append("]");
			sb.append("当前最新的 [access-token:").append(tokenData.getAccessToken()).append("]");
			sb.append("  将在[").append(expired).append("ms]后进行更新!");
			log.info(sb.toString());
		} catch (Exception ex) {
			log.error("[refresh token] get weixin token error!", ex);
		}
	}

	/**
	 * <p>
	 * 对access token进行刷新，每隔一分钟启动一次，在token失效前5分钟开始做token的刷新
	 */
	class RefreshThread extends Thread {
		public RefreshThread() {
			setName("[TokenService:Thread]");
		}

		public void run() {
			while (true) {
				List<WXAccount> wxaccountList = WXAccount.dao.getAll();
				for (WXAccount wxaccount : wxaccountList) {
					if (!wxaccount.isEnabled()) {
						tokenDataMap.remove(wxaccount.getSystemId());
						continue;
					}

					TokenData tokenData = tokenDataMap.get(wxaccount.getSystemId());
					if (tokenData == null) {
						tokenData = new TokenData();
						tokenData.setSystemId(wxaccount.getSystemId());
						tokenDataMap.put(wxaccount.getSystemId(), tokenData);
					} else {
						long currTime = System.currentTimeMillis();
						if (tokenData.getUpdatedTime() - currTime > 300000) {
							// 如果原来的token还有五分钟过期才需要刷新一次
							continue;
						}
					}

					// 刷新AccessToken
					refreshToken(wxaccount, tokenData);
				}

				try {
					TimeUnit.MINUTES.sleep(5l);
				} catch (Exception ex) {
				}
			}
		}
	}

	class TokenData {
		private String accessToken;
		private long updatedTime;
		private String systemId;

		public String getAccessToken() {
			return accessToken;
		}

		public void setAccessToken(String accessToken) {
			this.accessToken = accessToken;
		}

		public long getUpdatedTime() {
			return updatedTime;
		}

		public void setUpdatedTime(long updatedTime) {
			this.updatedTime = updatedTime;
		}

		public String getSystemId() {
			return systemId;
		}

		public void setSystemId(String systemId) {
			this.systemId = systemId;
		}
	}
}
