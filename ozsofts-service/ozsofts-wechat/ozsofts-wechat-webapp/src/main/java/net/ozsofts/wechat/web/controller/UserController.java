package net.ozsofts.wechat.web.controller;

import net.ozsofts.wechat.core.models.WXUser;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

public class UserController extends Controller {

	public void list() {
		Page<WXUser> page = WXUser.dao.paginate(3, 10, "select *", "from t_wx_users");
		setAttr("page", page);

		render("list.html");
	}
}
