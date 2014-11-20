package com.gl.emms.nio.filter;



import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.gl.emms.nio.mutual.NumberUtil;


/**
 *  �ͻ�����Ϣ����ǰ���б���,���ڴ˼�����Ϣ
 *  @author 3979434@qq.com
 *
 */

public class ClientMessageEncoder extends ProtocolEncoderAdapter {

	
	@Override
	public void encode(IoSession iosession, Object message, ProtocolEncoderOutput out) throws Exception {

		IoBuffer buffer = IoBuffer.allocate(2048).setAutoExpand(true);  

        byte[] bytes = message.toString().getBytes();  
        byte[] sizeBytes = NumberUtil.int2bytes(bytes.length);  

        buffer.put(sizeBytes);//��ǰ4λ���ó���������ֽڳ���  
        buffer.put(bytes);//��Ϣ����  
        buffer.flip();  
        out.write(buffer);
	}
	
/*
	@Override
	public void encode(IoSession iosession, Object message, ProtocolEncoderOutput out) throws Exception {

		IoBuffer buff = IoBuffer.allocate(2048).setAutoExpand(true);
		//buff.putString( message.toString(), charset.newEncoder());
		buff.put(message.toString().getBytes("UTF-8"));
		buff.put(CIMConstant.MESSAGE_SEPARATE);
		buff.flip();
		out.write(buff);
	}
*/





}
