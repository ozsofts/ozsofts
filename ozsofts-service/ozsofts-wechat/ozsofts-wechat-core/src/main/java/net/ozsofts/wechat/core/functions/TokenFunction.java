package net.ozsofts.wechat.core.functions;

import java.util.HashMap;
import java.util.Map;

import net.ozsofts.wechat.core.Constants;
import net.ozsofts.wechat.core.WechatCommService;
import net.sf.json.JSONObject;

public class TokenFunction {
	public static Map<String, Object> getToken(String appId, String secret) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put(Constants.PARA_GRANT_TYPE, "client_credential");
		params.put(Constants.PARA_APP_ID, appId);
		params.put(Constants.PARA_SECRET, secret);

		JSONObject json = WechatCommService.instance().get("/cgi-bin/token", null, params);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put(Constants.PARA_ACCESS_TOKEN, json.getString(Constants.PARA_ACCESS_TOKEN));
		result.put(Constants.PARA_EXPIRES_IN, json.getInt(Constants.PARA_EXPIRES_IN));
		return result;
	}
}
