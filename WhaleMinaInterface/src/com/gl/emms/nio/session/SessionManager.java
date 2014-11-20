 
package com.gl.emms.nio.session;

import java.util.Collection;


/**
 *  �ͻ��˵� session����ӿ�
 *  ������ʵ�ִ˽ӿڹ���session
 *  @author 3979434@qq.com
 */
 
public interface  SessionManager  {

	
	/**
	 * ����µ�session
	 */
	public void addSession(String account,EMMSSession session);
	
	/**
	 * 
	 * @param account �ͻ���session�� key һ����� �û��˺�����Ӧsession
	 * @return
	 */
	EMMSSession getSession(String account);
	
	/**
	 * ��ȡ����session
	 * @return
	 */
	public Collection<EMMSSession> getSessions();
	
	/**
	 * ɾ��session
	 * @param session
	 */
    public void  removeSession(EMMSSession session) ;
    
    
    /**
	 * ɾ��session
	 * @param session
	 */
    public void  removeSession(String account);
    
    /**
	 * session�Ƿ����
	 * @param session
	 */
    public boolean containsCIMSession(EMMSSession ios);
    
    /**
	 * session��ȡ��Ӧ�� �û� key  
	 * @param session
	 */
    public String getAccount(EMMSSession ios);
}