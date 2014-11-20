 
package com.gl.emms.nio.handle;

/**
 *  请求处理接口,所有的请求实现必须实现此接口
 *  @author 3979434@qq.com
 */
import com.gl.emms.nio.mutual.ReplyBody;
import com.gl.emms.nio.mutual.SentBody;
import com.gl.emms.nio.session.EMMSSession;
 
public    interface   EMMSRequestHandler  {

    public abstract ReplyBody process(EMMSSession session,SentBody message);
}