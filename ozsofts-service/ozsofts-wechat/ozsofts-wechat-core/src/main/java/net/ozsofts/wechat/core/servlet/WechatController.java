package net.ozsofts.wechat.core.servlet;

import java.io.BufferedInputStream;

import javax.servlet.http.HttpServletRequest;

import net.ozsofts.wechat.core.Constants;
import net.ozsofts.wechat.core.message.MessageHandler;
import net.ozsofts.wechat.core.models.WXAccount;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

public class WechatController extends Controller {
	private static Logger log = LoggerFactory.getLogger(WechatController.class);

	@Before(ValidateInterceptor.class)
	public void action() {
		String systemId = getPara(0);
		WXAccount wxaccount = WXAccount.dao.findBySystemId(systemId);
		if (wxaccount == null || !wxaccount.isEnabled()) {
			log.error("[{}] {}!", systemId, wxaccount == null ? "没有对应的账号信息" : "账号目前的状态不可用，请检查");

			// 没有找到相关的账号信息或者账号目前没有启用,直接返回
			renderText("");
			return;
		}

		String result = "";
		try {
			HttpServletRequest req = super.getRequest();
			// 检查通过后，读取POST的数据包信息
			BufferedInputStream bis = new BufferedInputStream(req.getInputStream());
			int offset = 0;
			int remain = req.getContentLength();
			byte[] buffer = new byte[remain];
			while (remain > 0) {
				int nread = bis.read(buffer, offset, remain);
				if (nread == -1)
					break;

				offset += nread;
				remain -= nread;
			}

			if (log.isDebugEnabled()) {
				log.debug("[{}] 上行原始数据:\n{}", systemId, new String(buffer, "UTF8"));
			}

			String timestamp = getPara(Constants.PARA_TIMESTAMP);
			String nonce = getPara(Constants.PARA_NONCE);
			String requestEncryptType = getPara(Constants.PARA_ENCRYPT_TYPE);
			String signature = "";
			if (WXAccount.ENCRYPT_TYPE_AES.equalsIgnoreCase(requestEncryptType)) {
				signature = getPara(Constants.PARA_MSG_SIGNATURE);
			} else {
				signature = getPara(Constants.PARA_SIGNATURE);
			}

			// 检查上行消息的合法性以及对数据进行解密处理
			String requestData = wxaccount.handleRequestMessage(timestamp, nonce, requestEncryptType, signature, new String(buffer, "UTF8"));
			if (log.isDebugEnabled()) {
				log.debug("[{}] 实际消息内容:\n{}", systemId, requestData);
			}

			// 调用消息处理对象进行消息的实际处理
			String responseData = MessageHandler.me.handleMessage(wxaccount, requestData);
			// 将消息处理后的结果进行打包处理
			result = wxaccount.handleResponseMessage(timestamp, nonce, responseData);

			if (log.isDebugEnabled()) {
				log.debug("[{}] 接口返回结果:\n{}", systemId, result);
			}

		} catch (Exception ex) {
			log.error("处理微信请求时出现错误!", ex);
		}

		renderText(result);
	}
}
