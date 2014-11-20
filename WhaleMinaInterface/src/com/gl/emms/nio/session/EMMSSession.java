package com.gl.emms.nio.session;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;

import com.gl.emms.nio.constant.EMMSConstant;

/**
 * IoSession��װ��,��Ⱥʱ ���˶���������
 * 
 * @author 3979434@qq.com
 */

public class EMMSSession  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static String ID = "ID";
	public static String HOST = "HOST";

	private IoSession session;
	
	private String gid;//sessionȫ��ID
	private Long nid;//session�ڱ�̨�������ϵ�ID
	private String deviceId;//�ͻ����豸ID
	private String host;//session�󶨵ķ�����IP
	private String account;//session�󶨵��˺�
	private String channel;//�ն��豸����
	private String deviceModel;//�ն��豸�ͺ�
	
	private Long bindTime;//��¼ʱ��
	
	private Long heartbeat;//����ʱ��
	
	public EMMSSession(IoSession session) {
		this.session = session;
		this.nid = session.getId();
	}
 
	public EMMSSession()
	{
		
	}
	
	
 

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		if(session!=null)
		{
			session.setAttribute(EMMSConstant.SESSION_KEY, account);
		}
		this.account = account;
	}

	 
	
	 


	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public Long getNid() {
		return nid;
	}

	public void setNid(Long nid) {
		this.nid = nid;
	}

	public String getDeviceId() {
		return deviceId;
	}


	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}


   

	public String getHost() {
		return host;
	}



	public Long getBindTime() {
		return bindTime;
	}

	public void setBindTime(Long bindTime) {
		this.bindTime = bindTime;
	}

	public Long getHeartbeat() {
		return heartbeat;
	}

	public void setHeartbeat(Long heartbeat) {
		this.heartbeat = heartbeat;
		if(session!=null)
		{
			session.setAttribute(EMMSConstant.HEARTBEAT_KEY, heartbeat);
		}
	}

	public void setHost(String host) {
		this.host = host;
	}



	public void setAttribute(String key, Object value) {
		if(session!=null)
		session.setAttribute(key, value);
	}


	public boolean containsAttribute(String key) {
		if(session!=null)
		return session.containsAttribute(key);
		return false;
	}
	
	public Object getAttribute(String key) {
		if(session!=null)
		return session.getAttribute(key);
		return null;
	}

	public void removeAttribute(String key) {
		if(session!=null)
		session.removeAttribute(key);
	}

	public SocketAddress getRemoteAddress() {
		if(session!=null)
		return session.getRemoteAddress();
		return null;
	}

	public boolean write(Object msg) {
		if(session!=null)
		{
			WriteFuture wf = session.write(msg);
			wf.awaitUninterruptibly(5, TimeUnit.SECONDS);
			return wf.isWritten();
		}
		return false;
	}

	public boolean isConnected() {
		if(session!=null)
		return session.isConnected();
		return false;
	}

	public boolean  isLocalhost()
	{
		
		try {
			String ip = InetAddress.getLocalHost().getHostAddress();
			return ip.equals(host) && session!=null;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return false;
		 
	}
	
 
	public void close(boolean immediately) {
		if(session!=null)
		session.close(immediately);
	}

	 
	public boolean equals(Object o) {
        
		if (o instanceof EMMSSession) {
			
			EMMSSession t = (EMMSSession) o;
			if(!t.isLocalhost())
			{
				return false;
			}
			if (t.session.getId() == session.getId()&& t.host.equals(host)) {
				return true;
			}
			return false;
		} else {
			return false;
		}

	}

	public void setIoSession(IoSession session) {
		this.session = session;
	}

	public IoSession getIoSession() {
		return session;
	}
	
	
  

}