package net.ozsofts.wechat.core.message.type;

import java.util.Map;

public class ReceiveTextMessage extends ReceiveNormalMessage {
	/** 文本消息的内容 */
	private String content;

	public void parse(Map<String, String> params) {
		super.parse(params);

		this.content = params.get("Content");
	}

	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString()).append(" | ");
		sb.append(content);
		return sb.toString();
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
