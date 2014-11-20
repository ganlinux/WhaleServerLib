 
package com.gl.emms.nio.handle;

//import org.apache.log4j.Logger;

import org.apache.mina.core.session.IoSession;

import com.gl.emms.nio.constant.EMMSConstant;
import com.gl.emms.nio.mutual.ReplyBody;
import com.gl.emms.nio.mutual.SentBody;
import com.gl.emms.nio.session.EMMSSession;

/**
 *客户端心跳实现
 * 
 * @author
 */
public class HeartbeatHandler implements EMMSRequestHandler {

	//protected final Logger logger = Logger.getLogger(HeartbeatHandler.class);

	public ReplyBody process(EMMSSession session, SentBody message) {

		//logger.warn("heartbeat... from "+session.getRemoteAddress().toString());
		ReplyBody reply = new ReplyBody();
		//reply.setKey(CIMConstant.RequestKey.CLIENT_HEARTBEAT);
		//reply.setCode(CIMConstant.ReturnCode.CODE_200);
		session.setHeartbeat(System.currentTimeMillis());
		return reply;
	}
	
 
	
}