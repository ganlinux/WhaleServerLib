
package com.gl.emms.nio.constant;

/** 
 * 常量
 *
 * @author ganlinux
 */
public   interface  EMMSConstant  {
	
	public static String KEY_CIM_SERVIER_HOST = "KEY_CIM_SERVIER_HOST";

	public static String KEY_CIM_SERVIER_PORT = "KEY_CIM_SERVIER_PORT";
	
	public static byte  MESSAGE_SEPARATE='\b';


	public static int  EMMS_DEFAULT_MESSAGE_ORDER=1;


	public static final String SESSION_KEY ="account";

	public static final String HEARTBEAT_KEY ="heartbeat";

	public static class MessageType{

		//账号在其他设备绑定时，会收到该类型消息
		public static String TYPE_999 ="999";

	}
}