package net.ozsofts.wechat.core.servlet;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.ozsofts.wechat.core.Constants;
import net.ozsofts.wechat.core.message.MessageHandler;
import net.ozsofts.wechat.core.models.WXAccount;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * 接收微信上行消息的接口。
 * 
 * @author jack
 */
public class WechatServlet extends HttpServlet {
	private static final long serialVersionUID = -378142073245737705L;

	private static Logger log = LoggerFactory.getLogger(WechatServlet.class);

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String uri = req.getRequestURI();
		String systemId = uri.substring(uri.lastIndexOf("/") + 1);
		System.out.println("[In Servlet GET ] systemId:[" + systemId + "] [" + uri + "]");

		Map<String, String> params = getRequestParams(req);
		params.put("system_id", systemId);

		try {
			WXAccount wxaccount = WXAccount.dao.findBySystemId(systemId);

			String timestamp = params.get(Constants.PARA_TIMESTAMP);
			String nonce = params.get(Constants.PARA_NONCE);
			String echoStr = params.get(Constants.PARA_ECHOSTR);

			// 在验证URL时需要使用接口中的加密类型进行判断
			String verifyEncryptType = params.get(Constants.PARA_ENCRYPT_TYPE);
			String signature = "";
			if (WXAccount.ENCRYPT_TYPE_AES.equalsIgnoreCase(verifyEncryptType)) {
				signature = params.get(Constants.PARA_MSG_SIGNATURE);
			} else {
				signature = params.get(Constants.PARA_SIGNATURE);
			}

			String returnText = wxaccount.verifyUrl(timestamp, nonce, echoStr, verifyEncryptType, signature);
			if (StringUtils.isNotBlank(returnText)) {
				String echostr = params.get(Constants.PARA_ECHOSTR);

				resp.setContentType("text/plain");
				resp.setCharacterEncoding("UTF-8");
				resp.setStatus(HttpServletResponse.SC_OK);
				resp.getWriter().print(echostr);
			}
		} catch (Exception ex) {
			log.error("在处理接入消息时出现错误!", ex);
		}
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String uri = req.getRequestURI();
		String systemId = uri.substring(uri.lastIndexOf("/") + 1);

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
			log.debug("[" + systemId + "] 上行原始数据: " + new String(buffer, "UTF8"));
		}

		// 检查接收数据的正确性
		Map<String, String> params = getRequestParams(req);
		params.put("system_id", systemId);

		if (log.isDebugEnabled()) {
			StringBuilder sb = new StringBuilder().append("[").append(systemId).append("] 接口收到请求:").append(params);
			log.debug(sb.toString());
		}

		String result = "";
		try {
			WXAccount wxaccount = WXAccount.dao.findBySystemId(systemId);

			String timestamp = params.get(Constants.PARA_TIMESTAMP);
			String nonce = params.get(Constants.PARA_NONCE);
			String requestEncryptType = params.get(Constants.PARA_ENCRYPT_TYPE);
			String signature = "";
			if (WXAccount.ENCRYPT_TYPE_AES.equalsIgnoreCase(requestEncryptType)) {
				signature = params.get(Constants.PARA_MSG_SIGNATURE);
			} else {
				signature = params.get(Constants.PARA_SIGNATURE);
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
			log.error("在处理上行消息时出现错误!", ex);
		}

		// 返回处理结果，消息的处理在线程中进行
		resp.setContentType("text/plain");
		resp.setCharacterEncoding("UTF-8");
		resp.setStatus(HttpServletResponse.SC_OK);
		resp.getWriter().print(result);
	}

	@SuppressWarnings("unchecked")
	private Map<String, String> getRequestParams(HttpServletRequest req) {

		Map<String, String> params = new HashMap<String, String>();
		Map<String, String[]> requestParams = req.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			params.put(name, valueStr);
		}

		return params;
	}

	public void init(ServletConfig config) throws ServletException {
		super.init();

		try {
			// 先在这里进行初始化
			TokenService.me.initialize();

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ServletException("WeixinServlet初始化错误!", ex);
		}
	}
}
