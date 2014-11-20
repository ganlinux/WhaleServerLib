 
package com.gl.emms.nio.filter;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/** 
 * android�ͻ��˶���Ϣ ����������� ������
 * ������Ϣ��������ܣ� ���� encoderʱ������Ϣ���ܣ���ServerMessageCodecFactory�� decoderʱ����Ϣ����
 * @author 3979434@qq.com
 */
public class ClientMessageCodecFactory implements ProtocolCodecFactory {

    private final ClientMessageEncoder encoder;

    private final ClientMessageDecoder decoder;

    /**
     * Constructor.
     */
    public ClientMessageCodecFactory() {
        encoder = new ClientMessageEncoder();
        decoder = new ClientMessageDecoder();
    }

    /**
     * Returns a new (or reusable) instance of ProtocolEncoder.
     */
    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
        return encoder;
    }

    /**
     * Returns a new (or reusable) instance of ProtocolDecoder.
     */
    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
        return decoder;
    }

}
