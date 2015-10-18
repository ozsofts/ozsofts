package net.ozsofts.wechat.core.service;

import java.util.List;

import net.ozsofts.wechat.core.functions.GroupFunction;
import net.ozsofts.wechat.core.functions.UserFunction;
import net.ozsofts.wechat.core.models.WXAccount;
import net.ozsofts.wechat.core.models.WXGroup;
import net.ozsofts.wechat.core.models.WXUser;
import net.ozsofts.wechat.core.servlet.TokenService;

import org.apache.commons.lang.StringUtils;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;

public class AccountService {
	/**
	 * <p>
	 * 对公众号进行初始化，具体的处理是读取公众号所有的群组信息和用户信息
	 * 
	 * @param systemId
	 *            公众号标识
	 * @throws Exception
	 */
	@Before(Tx.class)
	public static void initAccount(String systemId) throws Exception {
		WXAccount account = WXAccount.dao.findBySystemId(systemId);
		if (account == null) {
			// TODO 公众号不存在
		}
		if (account.getBoolean("is_init")) {
			// TODO 已经初始化，不需要再进行初始化
		}

		String token = TokenService.me.getAccessToken(systemId);
		if (StringUtils.isBlank(token)) {
			// TODO 公众号的AccessToken还未初始化，不能进行后续处理
		}

		// 首先取得所有群组信息并保存到数据库中
		List<WXGroup> groupList = GroupFunction.getAllGroups(token);
		for (WXGroup wxgroup : groupList) {
			wxgroup.set("account_id", account.getLong("id"));
			wxgroup.save();
		}

		// 先读取所有用户的open_id信息
		List<String> openIdList = UserFunction.getOpenIds(token);
		for (String openId : openIdList) {
			WXUser wxuser = UserFunction.getUserInfo(token, openId);
			wxuser.set("account_id", account.getLong("id"));
			wxuser.save();
		}

		// 更新公众号的初始状态
		account.set("is_init", true);
		account.update();
	}
}
