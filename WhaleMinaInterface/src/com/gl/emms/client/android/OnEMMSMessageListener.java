 
package com.gl.emms.client.android;

import android.net.NetworkInfo;

import com.gl.emms.nio.mutual.ServerMessage;
import com.gl.emms.nio.mutual.ReplyBody;
import com.gl.emms.nio.mutual.RequestBody;

/**
 *CIM ��Ҫ�¼��ӿ�
 * �����ƣ�OnCIMMessageListener
 * ��������
 * �����ˣ� 3979434
 * �޸��ˣ� 3979434
 * �޸�ʱ�䣺 2014-4-28 ����5:07:47
 * �޸ı�ע��
 * @version 1.0.0
 *
 */
public interface OnEMMSMessageListener
{


    /**
     * ���յ���������͹�������Ϣʱ����
     * @param message
     */
    public abstract void onMessageReceived(ServerMessage message);

    /**
     * ������CIMPushManager.sendRequest()�����˷������󣬻����Ӧʱ����
     * @param replybody
     */
    public abstract void onReplyReceived(ReplyBody replybody);

    /**
     * ������CIMPushManager.sendRequest()�����˷������󣬻����Ӧʱ����
     * @param replybody
     */
    public abstract void onRequestReceived(RequestBody requestBody);
    
    
    /**
     * ���ֻ����緢���仯ʱ����
     * @param networkinfo
     */
    public abstract void onNetworkChanged(NetworkInfo networkinfo);
    
    /**
     * ��ȡ���Ƿ����ӵ������
     * ͨ������CIMPushManager.detectIsConnected()���첽��ȡ
     * 
     */
    public abstract void onConnectionStatus(boolean  isConnected);
    
    /**
     * ���ӷ���˳ɹ�
     */
	public abstract void onConnectionSucceed();

	public abstract void onSendHeartBeat();
	
	
}

