 
package com.gl.emms.nio.handle;

/**
 *  ������ӿ�,���е�����ʵ�ֱ���ʵ�ִ˽ӿ�
 *  @author 3979434@qq.com
 */
import com.gl.emms.nio.mutual.ReplyBody;
import com.gl.emms.nio.mutual.SentBody;
import com.gl.emms.nio.session.EMMSSession;
 
public    interface   EMMSRequestHandler  {

    public abstract ReplyBody process(EMMSSession session,SentBody message);
}