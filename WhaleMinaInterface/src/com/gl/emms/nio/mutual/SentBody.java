package com.gl.emms.nio.mutual;

import java.io.Serializable;
import java.util.HashMap;

import android.util.Log;
/**
 * java |android 客户端请求结构
 *  @author 3979434@qq.com
 *
 */
public class SentBody implements Serializable {

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

	/*
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	 */

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
	public String getReciever() {
		return reciever;
	}
	public void setReciever(String reciever) {
		this.reciever = reciever;
	}
	/**
	 * 返回说明
	 */
	//private String data;


	private long timestamp;

	public SentBody()
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
	
	public String toString()
	{

		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		buffer.append("<request>");
		buffer.append("<type>").append(this.getType()).append("</type>");
		buffer.append("<sender>").append(this.getSender()).append("</sender>");
		buffer.append("<reciever>").append(this.getReciever()).append("</reciever>");
		buffer.append("<timestamp>").append(timestamp).append("</timestamp>");
		buffer.append("<data>");
		for (String key : data.keySet()) {
			buffer.append("<" + key + ">").append(data.get(key)).append(
					"</" + key + ">");
		}
		buffer.append("</data>");
		buffer.append("</request>");
		Log.v("whale", "buffer:"+buffer);
		return buffer.toString();
	}


	public String toXmlString()
	{

		return toString();
	}
}
