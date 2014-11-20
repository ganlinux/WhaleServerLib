 
package com.gl.emms.nio.filter;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/** 
 * �������Ϣ ����������� ������
 * ������Ϣ��������ܣ� ���� encoderʱ������Ϣ���ܣ���ClientMessageCodecFactory�� decoderʱ����Ϣ����
 * @author 3979434@qq.com
 */
public class ServerMessageCodecFactory implements ProtocolCodecFactory {

    private final ServerMessageEncoder encoder;

    private final ServerMessageDecoder decoder;
 
    public ServerMessageCodecFactory() {
        encoder = new ServerMessageEncoder();
        decoder = new ServerMessageDecoder();
    }
 
    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
        return encoder;
    }
 
    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
        return decoder;
    }

}
