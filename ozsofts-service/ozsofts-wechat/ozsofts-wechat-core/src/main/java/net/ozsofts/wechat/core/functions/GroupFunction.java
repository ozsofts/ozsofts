package net.ozsofts.wechat.core.functions;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.ozsofts.wechat.core.WechatCommService;
import net.ozsofts.wechat.core.models.WXGroup;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class GroupFunction {

	//
	// 对分组进行管理的方法
	//
	/**
	 * <p>
	 * 创建分组
	 * 
	 * @param name
	 *            新创建的分组的名称
	 */
	public static WXGroup createGroup(String token, String name) throws Exception {
		Map<String, Object> groupMessage = new HashMap<String, Object>();
		groupMessage.put("name", name);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("group", groupMessage);

		JSONObject result = WechatCommService.instance().post("/cgi-bin/groups/create", token, null, params);
		JSONObject groupObject = result.getJSONObject("group");

		int groupId = new Long(groupObject.getLong("id")).intValue();

		WXGroup wxgroup = new WXGroup();
		wxgroup.set("group_id", groupId);
		wxgroup.set("name", name);

		return wxgroup;
	}

	/**
	 * <p>
	 * 修改分组的名称
	 * 
	 * @param token
	 * @param groupId
	 *            分组微信标识号
	 * @param newName
	 *            新的名称
	 * @throws Exception
	 */
	public static void changeGroupName(String token, int groupId, String newName) throws Exception {
		Map<String, Object> groupMessage = new HashMap<String, Object>();
		groupMessage.put("id", groupId);
		groupMessage.put("name", newName);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("group", groupMessage);

		WechatCommService.instance().post("/cgi-bin/groups/update", token, null, params);
	}

	/**
	 * <p>
	 * 删除分组
	 * 
	 * @param token
	 * @param groupId
	 *            分组微信标识号
	 * @throws Exception
	 */
	public static void deleteGroup(String token, int groupId) throws Exception {
		Map<String, Object> groupMessage = new HashMap<String, Object>();
		groupMessage.put("id", groupId);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("group", groupMessage);

		WechatCommService.instance().post("/cgi-bin/groups/delete", token, null, params);
	}

	/**
	 * <p>
	 * 修改用户的分组
	 * 
	 * @param token
	 * @param openId
	 * @param groupId
	 *            分组微信标识号
	 * @throws Exception
	 */
	public static void changeUserGroup(String token, String openId, int groupId) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("openid", openId);
		params.put("to_groupid", groupId);

		WechatCommService.instance().post("/cgi-bin/groups/members/update", token, null, params);
	}

	/**
	 * <p>
	 * 批量修改用户分组
	 * 
	 * @param token
	 * @param openIds
	 * @param groupId
	 *            分组微信标识号
	 * @throws Exception
	 */
	public static void batchChangeUserGroup(String token, String[] openIds, int groupId) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("openid_list", openIds);
		params.put("to_groupid", groupId);

		WechatCommService.instance().post("/cgi-bin/groups/members/batchupdate", token, null, params);
	}

	/**
	 * <p>
	 * 取得公众号所有的用户组信息
	 */
	public static List<WXGroup> getAllGroups(String token) throws Exception {
		JSONObject result = WechatCommService.instance().get("/cgi-bin/groups/get", token, null);

		List<WXGroup> groupList = new LinkedList<WXGroup>();

		JSONArray groups = result.getJSONArray("groups");
		for (int i = 0; i < groups.size(); i++) {
			JSONObject jo = groups.getJSONObject(i);
			int groupId = new Long(jo.getLong("id")).intValue();
			String name = jo.getString("name");
			int count = new Long(jo.getLong("count")).intValue();

			WXGroup wxgroup = new WXGroup();
			wxgroup.set("group_id", groupId);
			wxgroup.set("name", name);
			wxgroup.set("user_count", count);
			groupList.add(wxgroup);
		}

		return groupList;
	}
}
