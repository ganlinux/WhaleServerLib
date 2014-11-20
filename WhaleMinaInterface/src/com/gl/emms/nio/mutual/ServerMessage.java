package com.gl.emms.nio.mutual;

import java.io.Serializable;
import java.util.HashMap;
/**
 * ��Ϣ����
 * @author  @author 3979434@qq.com
 *
 */
public class ServerMessage implements Serializable {

	/**
	 * @author  3979434@qq.com
	 * ��Ϣ����
	 */
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * ��Ϣ���ͣ��û��Զ�����Ϣ���
	 */
	private String type;

	/**
	 * ��Ϣ���ݣ���type ���Ϊ�κ�������Ϣ��content ���� format �ɱ�ʾΪ text,json ,xml���ݸ�ʽ
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
	 * ��Ϣ�������˺�
	 */
	private String sender;
	/**
	 * ��Ϣ�����߽�����
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
