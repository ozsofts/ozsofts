package net.ozsofts.wechat.core.functions;

import java.net.URLEncoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.ozsofts.wechat.core.WechatCommService;
import net.ozsofts.wechat.core.models.WXUser;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang.StringUtils;

public class UserFunction {
	/**
	 * <p>
	 * 取得公众号所有的用户信息
	 */
	public static String getOpenIds(String token, String nextOpenId, List<String> openIdList) throws Exception {
		if (openIdList == null) {
			return null;
		}

		Map<String, String> params = new HashMap<String, String>();
		if (StringUtils.isNotBlank(nextOpenId)) {
			params.put("next_openid", nextOpenId);
		}

		JSONObject result = WechatCommService.instance().get("/cgi-bin/user/get", token, params);
		System.out.println(result.toString());

		int totalCount = new Long(result.getLong("total")).intValue();
		int thisCount = new Long(result.getLong("count")).intValue();
		System.out.println("total:" + totalCount + "   count:" + thisCount);
		if (thisCount == 0) {
			return null;
		}

		if (result.containsKey("data")) {
			JSONObject data = result.getJSONObject("data");
			JSONArray openIds = data.getJSONArray("openid");
			for (int i = 0; i < openIds.size(); i++) {
				openIdList.add(openIds.getString(i));
			}
		}

		String newNextOpenId = result.getString("next_openid");
		return newNextOpenId;
	}

	public static List<String> getOpenIds(String token) throws Exception {
		List<String> openIdList = new LinkedList<String>();

		String nextOpenId = null;
		do {
			nextOpenId = getOpenIds(token, nextOpenId, openIdList);
		} while (StringUtils.isNotBlank(nextOpenId));

		return openIdList;
	}

	public static WXUser getUserInfo(String token, String openId) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("openid", openId);

		JSONObject result = WechatCommService.instance().get("/cgi-bin/user/info", token, params);

		WXUser wxuser = new WXUser();

		/* 用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息。 */
		wxuser.set("is_subscribe", result.containsKey("subscribe") && result.getString("subscribe").equals("1") ? true : false);

		/* 用户所在城市 */
		wxuser.set("city", result.containsKey("city") ? result.getString("city") : "");
		/* 用户所在省份 */
		wxuser.set("province", result.containsKey("province") ? result.getString("province") : "");
		/* 用户所在国家 */
		wxuser.set("country", result.containsKey("country") ? result.getString("country") : "");

		wxuser.set("open_id", result.containsKey("openid") ? result.getString("openid") : "");
		/* 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。 */
		wxuser.set("union_id", result.containsKey("unionid") ? result.getString("unionid") : "");
		/* 用户的昵称 */
		wxuser.set("nick_name", result.containsKey("nickname") ? URLEncoder.encode(result.getString("nickname"), "UTF-8") : "");
		System.out.println(result.getString("nickname"));
		/* 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知 */
		wxuser.set("sex", result.containsKey("sex") ? result.getString("sex") : "0");
		/* 用户的语言，简体中文为zh_CN */
		wxuser.set("language", result.containsKey("language") ? result.getString("language") : "");

		/* 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。 */
		wxuser.set("head_img_url", result.containsKey("headimgurl") ? result.getString("headimgurl") : "");

		if (result.containsKey("subscribe_time")) {
			long subscribeTime = result.getLong("subscribe_time") * 1000;
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(subscribeTime);

			/* 用户关注日期，YYYYMMDD。如果用户曾多次关注，则取最后关注时间 */
			wxuser.set("sub_date", DateUtil.formatDate(cal.getTime(), "yyyyMMdd"));
			/* 用户关注时间，HHMMSS。如果用户曾多次关注，则取最后关注时间 */
			wxuser.set("sub_time", DateUtil.formatDate(cal.getTime(), "HHmmss"));
		}

		/* 用户备注 */
		wxuser.set("remark", result.containsKey("remark") ? result.getString("remark") : "");

		if (result.containsKey("groupid")) {
			wxuser.set("group_id", new Long(result.getLong("groupid")).intValue());
		}

		return wxuser;
	}
}
