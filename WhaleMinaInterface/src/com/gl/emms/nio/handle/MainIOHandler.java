 
package com.gl.emms.nio.handle;

import java.util.HashMap;





//import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import android.util.Log;

import com.gl.emms.nio.constant.EMMSConstant;
import com.gl.emms.nio.mutual.ReplyBody;
import com.gl.emms.nio.mutual.SentBody;
import com.gl.emms.nio.session.EMMSSession;

/**
 *  
 * 客户端请求的入口，所有请求都首先经过它分发处理
 * @author farsunset (3979434@qq.com)
 */
public class MainIOHandler extends IoHandlerAdapter {

	//protected final Logger logger = Logger.getLogger(MainIOHandler.class);

	private HashMap<String, EMMSRequestHandler> handlers = new HashMap<String, EMMSRequestHandler>();


	 
	public void sessionCreated(IoSession session) throws Exception {
		//logger.warn("sessionCreated()... from "+session.getRemoteAddress().toString());
	}

	 
	public void sessionOpened(IoSession session) throws Exception {

	}

	 
	public void messageReceived(IoSession ios, Object message)
			throws Exception {
		//logger.debug("message: " + message.toString());
		Log.v("whale", "MainIOHandler: " + message.toString());
		EMMSSession cimSession =new  EMMSSession(ios);
		ReplyBody reply = new ReplyBody();
		/*
		SentBody body = (SentBody) message;
		String key = body.getKey();

		CIMRequestHandler handler = handlers.get(key);
		if (handler == null) {
			reply.setCode(CIMConstant.ReturnCode.CODE_405);
			reply.setCode("KEY ["+key+"] 服务端未定义");
		} else {
			reply = handler.process(cimSession, body);
		}
		
        if(reply!=null)
        {
        	//reply.setKey(key);
        	cimSession.write(reply);
    	//	logger.debug("-----------------------process done. reply: " + reply.toString());
        }
        
        
        //设置心跳时间 
        cimSession.setAttribute(CIMConstant.HEARTBEAT_KEY, System.currentTimeMillis());
        */
	}

	/**
	 */
	public void sessionClosed(IoSession ios) throws Exception {
		
		EMMSSession cimSession =new  EMMSSession(ios);
		try{
		//	logger.warn("sessionClosed()... from "+cimSession.getRemoteAddress());
			EMMSRequestHandler handler = handlers.get("sessionClosedHander");
			if(handler!=null && cimSession.containsAttribute(EMMSConstant.SESSION_KEY))
			{
				handler.process(cimSession, null);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 */
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
	//	logger.warn("sessionIdle()... from "+session.getRemoteAddress().toString());
		if(!session.containsAttribute(EMMSConstant.SESSION_KEY))
		{
			session.close(true);
		}else
		{
			//如果5分钟之内客户端没有发送心态，则可能客户端断网，关闭连接
			Object heartbeat = session.getAttribute(EMMSConstant.HEARTBEAT_KEY);
			if(heartbeat!=null && System.currentTimeMillis()-Long.valueOf(heartbeat.toString()) >= 300000)
			{
				session.close(false);
			}
		}
	}

	/**
	 */
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
	//	logger.error("exceptionCaught()... from "+session.getRemoteAddress());
	//	logger.error(cause);
		cause.printStackTrace();
	}

	/**
	 */
	public void messageSent(IoSession session, Object message) throws Exception {
		
		 //设置心跳时间 
        session.setAttribute(EMMSConstant.HEARTBEAT_KEY, System.currentTimeMillis());
	}


	public HashMap<String, EMMSRequestHandler> getHandlers() {
		return handlers;
	}


	public void setHandlers(HashMap<String, EMMSRequestHandler> handlers) {
		this.handlers = handlers;
	}
	
	

}