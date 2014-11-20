package com.gl.emms.nio.mutual;



import java.io.Serializable;
import java.util.HashMap;
/**
 * 请求应答对象
 * @author 3979434@qq.com
 *
 */
public class ReplyBody implements Serializable {

	/**
	 * @author  3979434@qq.com 
	 * 服务端返回消息对象
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 请求key
	 */
	private String type;
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getReciever() {
		return reciever;
	}
	public void setReciever(String reciever) {
		this.reciever = reciever;
	}
	


	/**
	 * 返回码
	 */
	private String sender;
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}


	private String reciever;
	
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
	
	
	private long timestamp;
	
	public ReplyBody()
	{
		timestamp = System.currentTimeMillis();
	}
	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		data = new HashMap<String, String>();
		this.timestamp = timestamp;
	}

	 

	
	
	public String toString()
	{
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		buffer.append("<request>");
		buffer.append("<type>").append(this.getType()).append("</type>");
		buffer.append("<sender>").append(this.getReciever()).append("</sender>");
		buffer.append("<reciever>").append(this.getReciever()).append("</reciever>");
		buffer.append("<timestamp>").append(timestamp).append("</timestamp>");
		buffer.append("<data>");
		for (String key : data.keySet()) {
			buffer.append("<" + key + ">").append(data.get(key)).append(
					"</" + key + ">");
		}
		buffer.append("</data>");
		buffer.append("</request>");
		return buffer.toString();
	}

	
	public String toXmlString()
	{
		
		return toString();
	}
}
