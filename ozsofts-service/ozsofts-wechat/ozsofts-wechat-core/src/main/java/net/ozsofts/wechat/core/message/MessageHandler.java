package net.ozsofts.wechat.core.message;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.ozsofts.wechat.core.message.type.ReceiveMessage;
import net.ozsofts.wechat.core.message.type.ResponseMessage;
import net.ozsofts.wechat.core.models.WXAccount;
import net.ozsofts.wechat.core.models.WXMessage;
import net.ozsofts.wechat.core.service.actevent.ActEventListener;
import net.ozsofts.wechat.core.service.message.CommonMessageListener;
import net.ozsofts.wechat.core.service.user.UserListener;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

public class MessageHandler {
	private static Logger log = LoggerFactory.getLogger(MessageHandler.class);

	private Map<String, Class<?>> receiveMessageMap = new HashMap<String, Class<?>>();

	private Map<String, List<MessageListener>> messageListenerMap = new TreeMap<String, List<MessageListener>>();
	private List<MessageListener> commonMessageListenerList = new LinkedList<MessageListener>();

	public static MessageHandler me = new MessageHandler();

	public String handleMessage(WXAccount wxaccount, String reqdata) {
		Map<String, String> msgData = parseRequestXml(reqdata);

		String msgType = msgData.get("MsgType");

		try {
			Class<?> clazz = receiveMessageMap.get(msgType);
			if (clazz == null) {
				log.error("[{}] 处理消息时对应的类型[{}]没有解析器!", wxaccount.getSystemId(), msgType);
				return "";
			}

			ReceiveMessage receiveMessage = (ReceiveMessage) clazz.newInstance();
			// 对上行的消息进行解析
			receiveMessage.parse(msgData);

			// 将上行的消息调用每一个侦听器对象
			String messageType = receiveMessage.getMsgType();
			List<MessageListener> messageListenerList = new LinkedList<MessageListener>(this.commonMessageListenerList);
			if (this.messageListenerMap.containsKey(messageType)) {
				messageListenerList.addAll(this.messageListenerMap.get(messageType));
			}

			ResponseMessage respMessage = null;
			for (MessageListener listener : messageListenerList) {
				respMessage = listener.onMessage(wxaccount, receiveMessage);
				// 注意，如果有多个侦听器关注一个类型的消息话，以第一个有返回的为主，后续的侦听器将不再起作用
				if (respMessage != null) {

					// 保存下行消息
					WXMessage.dao.saveResponseMessage(wxaccount.getId(), respMessage);
					// 返回下行消息
					return respMessage.toXML();
				}
			}
		} catch (Exception e) {
			log.error("[{}] 处理消息时未正确初始化[{}]", wxaccount.getSystemId(), msgType);
			return "";
		}

		// 回复此字符串表示服务器已经处理，微信不需要再重发相同的消息
		return "success";
	}

	@SuppressWarnings("rawtypes")
	private Map<String, String> parseRequestXml(String reqdata) {
		Map<String, String> values = new HashMap<String, String>();

		try {
			SAXReader reader = new SAXReader();
			StringReader sr = new StringReader(reqdata);
			InputSource is = new InputSource(sr);
			Document document = reader.read(is);
			Element rootElement = document.getRootElement();

			for (Iterator iter = rootElement.elementIterator(); iter.hasNext();) {
				Element el = (Element) iter.next();

				String name = el.getName();
				String value = el.getTextTrim();

				values.put(name, value);
			}
		} catch (Exception ex) {
		}

		return values;
	}

	public void registerMessageListener(String messageType, MessageListener listener) {
		if (StringUtils.isBlank(messageType)) {
			this.commonMessageListenerList.add(listener);
		} else {
			List<MessageListener> listenerList = this.messageListenerMap.get(messageType);
			if (listenerList == null) {
				listenerList = new LinkedList<MessageListener>();
				this.messageListenerMap.put(messageType, listenerList);
			}
			listenerList.add(listener);
		}
	}

	public void initialize() {
		String packageName = ReceiveMessage.class.getPackage().getName();

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		try {
			receiveMessageMap.put(ReceiveMessage.MSGTYPE_TEXT, classLoader.loadClass(packageName + ".ReceiveTextMessage"));
			receiveMessageMap.put(ReceiveMessage.MSGTYPE_IMAGE, classLoader.loadClass(packageName + ".ReceiveImageMessage"));
			receiveMessageMap.put(ReceiveMessage.MSGTYPE_VOICE, classLoader.loadClass(packageName + ".ReceiveVoiceMessage"));
			receiveMessageMap.put(ReceiveMessage.MSGTYPE_VIDEO, classLoader.loadClass(packageName + ".ReceiveVideoMessage"));
			receiveMessageMap.put(ReceiveMessage.MSGTYPE_SHORTVIDEO, classLoader.loadClass(packageName + ".ReceiveShortvideoMessage"));
			receiveMessageMap.put(ReceiveMessage.MSGTYPE_LOCATION, classLoader.loadClass(packageName + ".ReceiveLocationMessage"));
			receiveMessageMap.put(ReceiveMessage.MSGTYPE_LINK, classLoader.loadClass(packageName + ".ReceiveLinkMessage"));
			receiveMessageMap.put(ReceiveMessage.MSGTYPE_EVENT, classLoader.loadClass(packageName + ".ReceiveEventMessage"));
		} catch (Exception ex) {
		}

		// 对所有上行消息都要先保存并做关键字检查
		registerMessageListener(null, new CommonMessageListener());
		// 对所有上行消息检查是否与活动相关
		registerMessageListener(null, new ActEventListener());

		registerMessageListener(ReceiveMessage.MSGTYPE_EVENT, new UserListener());
	}
}
