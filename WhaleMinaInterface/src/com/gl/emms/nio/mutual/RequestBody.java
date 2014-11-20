package com.gl.emms.nio.mutual;



import java.io.Serializable;
import java.util.HashMap;
/**
 * ����Ӧ�����
 * @author 3979434@qq.com
 *
 */
public class RequestBody implements Serializable {

	/**
	 * @author  3979434@qq.com 
	 * ����˷�����Ϣ����
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ����key
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
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}


	/**
	 * ������
	 */
	private String sender;
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}


	private String reciever;
	
	/**
	 * ����˵��
	 */
	private String data;
	
	
	private long timestamp;
	
	public RequestBody()
	{
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
		buffer.append("<sender>").append(this.getReciever()).append("</sender>");
		buffer.append("<reciever>").append(this.getReciever()).append("</reciever>");
		buffer.append("<timestamp>").append(timestamp).append("</timestamp>");
		buffer.append("<data>");
		buffer.append("</data>");
		buffer.append("</request>");
		return buffer.toString();
	}

	
	public String toXmlString()
	{
		
		return toString();
	}
}
