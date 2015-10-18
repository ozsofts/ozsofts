package net.ozsofts.wechat.web.controller;

import java.util.List;

import net.ozsofts.wechat.core.models.WXMessage;
import net.ozsofts.wechat.core.models.WXUser;
import net.ozsofts.wechat.web.service.MessageService;

import com.jfinal.core.Controller;

public class ActEventController extends Controller {

	public void listUsers() {
		List<WXUser> list = WXUser.dao.find("select * from t_wx_actusers au inner join t_wx_users u on u.open_id=au.open_id");
		setAttr("list", list);

		render("list-users.html");
	}

	public void listMessages() {
		List<WXMessage> messages = MessageService.findMessages("");
		setAttr("messages", messages);

		List<WXUser> users = WXUser.dao.find("select * from t_wx_actusers au inner join t_wx_users u on u.open_id=au.open_id");
		setAttr("users", users);

		render("list-messages.html");
	}
}
