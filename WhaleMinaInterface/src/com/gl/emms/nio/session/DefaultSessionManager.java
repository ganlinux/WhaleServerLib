 
package com.gl.emms.nio.session;

import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.gl.emms.nio.constant.EMMSConstant;

/** 
 * �Դ�Ĭ�� session����ʵ�֣� ��λ��������ʵ�� AbstractSessionManager�ӿ���ʵ���Լ��� session����
 *��������Ⱥʱ ��Ҫ��CIMSession ��Ϣ�������ݿ����nosql �� �������洢�ռ��У��������з����������Է���
 * @author farsunset (3979434@qq.com)
 */
public class DefaultSessionManager implements SessionManager{


    private static HashMap<String,EMMSSession> sessions =new  HashMap<String,EMMSSession>();
    
    
    private static final AtomicInteger connectionsCounter = new AtomicInteger(0);
 
   

    /**
     *  
     */
    public void addSession(String account,EMMSSession session) {
        if(session!=null)
        {
        	session.setAttribute(EMMSConstant.SESSION_KEY, account);
        	sessions.put(account, session);
        	connectionsCounter.incrementAndGet();
        }
        
    }

     
    public EMMSSession getSession(String account) {
    	
    	 
       return sessions.get(account);
    }

    

     
    public Collection<EMMSSession> getSessions() {
        return sessions.values();
    }
 
    public void  removeSession(EMMSSession session) {
        
    	 
    	sessions.remove(session.getAttribute(EMMSConstant.SESSION_KEY));
    }

     
    public void  removeSession(String account) {
        
    	sessions.remove(account);
    	
    }
    
    
    public boolean containsCIMSession(EMMSSession ios)
    {
    	return sessions.containsKey(ios.getAttribute(EMMSConstant.SESSION_KEY)) || sessions.containsValue(ios);
    }

    
    public String getAccount(EMMSSession ios)
    {
    	 if(ios.getAttribute(EMMSConstant.SESSION_KEY)==null)
    	 {
    		for(String key:sessions.keySet())
    		{
    			if(sessions.get(key).equals(ios) || sessions.get(key).getNid()==ios.getNid())
    			{
    				return key;
    			}
    		}
    	 }else
    	 {
    	    return ios.getAttribute(EMMSConstant.SESSION_KEY).toString();
    	 }
    	 
    	 return null;
    }
 
}
