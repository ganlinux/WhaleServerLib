package com.gl.emms.nio.mutual;

import java.io.Serializable;
import java.util.HashMap;
/**
 * 消息对象
 * @author  @author 3979434@qq.com
 *
 */
public class ServerMessage implements Serializable {

	/**
	 * @author  3979434@qq.com
	 * 消息对象
	 */
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * 消息类型，用户自定义消息类别
	 */
	private String type;

	/**
	 * 消息类容，于type 组合为任何类型消息，content 根据 format 可表示为 text,json ,xml数据格式
	 */
	private String content;
	
	private HashMap<String, String> data;
	public String get(String k) {
		return data.get(k);
	}
	public void put(String k, String v) {
		data.put(k, v);
	}

	public void remove(String k) {
		data.remove(k);
	}

	public HashMap<String, String> getData() {
		return data;
	}
	
	
	/**
	 * 消息发送者账号
	 */
	private String sender;
	/**
	 * 消息发送者接收者
	 */
	private String receiver;
	
	private long timestamp;
	
	
	public ServerMessage()
	{
		data = new HashMap<String, String>();
		timestamp = System.currentTimeMillis();
	}
	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	
	public String toString()
	{
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		buffer.append("<message>");
		buffer.append("<type>").append(type).append("</type>");
		buffer.append("<content>");
		for (String key : data.keySet()) {
			buffer.append("<" + key + ">").append(data.get(key)).append(
					"</" + key + ">");
		}
		buffer.append("</content>");
		buffer.append("<sender>").append(this.getSender()==null?"":this.getSender()).append("</sender>");
		buffer.append("<receiver>").append(this.getReceiver()==null?"":this.getReceiver()).append("</receiver>");
		buffer.append("<timestamp>").append(timestamp).append("</timestamp>");
		buffer.append("</message>");
		return buffer.toString();
	}
	public String toXmlString()
	{
		
		return toString();
	}
	
	
}
