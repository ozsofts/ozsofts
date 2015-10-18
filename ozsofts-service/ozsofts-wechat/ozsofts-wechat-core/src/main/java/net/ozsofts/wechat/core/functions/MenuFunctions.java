package net.ozsofts.wechat.core.functions;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.ozsofts.wechat.core.WechatCommService;
import net.sf.json.JSONObject;

public class MenuFunctions {
	/**
	 * 创建新的菜单
	 * 
	 * @param menuInfos
	 */
	public void create(String token, MenuInfo[] menuInfos) throws Exception {
		Map<String, Object> menuMap = new TreeMap<String, Object>();
		List<Map<String, Object>> menuList = new LinkedList<Map<String, Object>>();
		for (MenuInfo menu : menuInfos) {
			menuList.add(menu.toMap());
		}
		menuMap.put("button", menuList);

		WechatCommService.instance().post("/cgi-bin/menu/create", token, null, menuMap);
	}

	/**
	 * <p>
	 * 取得当前的微信菜单信息
	 */
	public MenuInfo[] get(String token) throws Exception {
		JSONObject jsonMenu = WechatCommService.instance().get("/cgi-bin/menu/get", token, null);
		System.out.println(jsonMenu.toString());
		return null;
	}

	/**
	 * <p>
	 * 删除菜单。注意，是全部删除。
	 */
	public void delete(String token) throws Exception {
		WechatCommService.instance().post("/cgi-bin/menu/delete", token, null, null);
	}
}
