package net.ozsofts.wechat.core.servlet;

import net.ozsofts.wechat.core.Constants;
import net.ozsofts.wechat.core.models.WXAccount;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

public class ValidateInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		Controller controller = inv.getController();
		if ("GET".equalsIgnoreCase(controller.getRequest().getMethod())) {
			String systemId = controller.getPara(0);

			WXAccount wxaccount = WXAccount.dao.findBySystemId(systemId);
			if (wxaccount == null || !wxaccount.isEnabled()) {
				// 没有找到相关的账号信息或者账号目前没有启用,直接返回
				return;
			}

			String timestamp = controller.getPara(Constants.PARA_TIMESTAMP);
			String nonce = controller.getPara(Constants.PARA_NONCE);
			String echoStr = controller.getPara(Constants.PARA_ECHOSTR);

			// 在验证URL时需要使用接口中的加密类型进行判断
			String verifyEncryptType = controller.getPara(Constants.PARA_ENCRYPT_TYPE);
			String signature = "";
			if (WXAccount.ENCRYPT_TYPE_AES.equalsIgnoreCase(verifyEncryptType)) {
				signature = controller.getPara(Constants.PARA_MSG_SIGNATURE);
			} else {
				signature = controller.getPara(Constants.PARA_SIGNATURE);
			}

			String returnText = wxaccount.verifyUrl(timestamp, nonce, echoStr, verifyEncryptType, signature);
			controller.renderText(returnText);
			return;
		}

		// 交给后续处理
		inv.invoke();
	}
}
