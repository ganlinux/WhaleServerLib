 
package com.gl.emms.client.android;

import android.net.NetworkInfo;

import com.gl.emms.nio.mutual.ServerMessage;
import com.gl.emms.nio.mutual.ReplyBody;
import com.gl.emms.nio.mutual.RequestBody;

/**
 *CIM 主要事件接口
 * 类名称：OnCIMMessageListener
 * 类描述：
 * 创建人： 3979434
 * 修改人： 3979434
 * 修改时间： 2014-4-28 下午5:07:47
 * 修改备注：
 * @version 1.0.0
 *
 */
public interface OnEMMSMessageListener
{


    /**
     * 当收到服务端推送过来的消息时调用
     * @param message
     */
    public abstract void onMessageReceived(ServerMessage message);

    /**
     * 当调用CIMPushManager.sendRequest()向服务端发送请求，获得相应时调用
     * @param replybody
     */
    public abstract void onReplyReceived(ReplyBody replybody);

    /**
     * 当调用CIMPushManager.sendRequest()向服务端发送请求，获得相应时调用
     * @param replybody
     */
    public abstract void onRequestReceived(RequestBody requestBody);
    
    
    /**
     * 当手机网络发生变化时调用
     * @param networkinfo
     */
    public abstract void onNetworkChanged(NetworkInfo networkinfo);
    
    /**
     * 获取到是否连接到服务端
     * 通过调用CIMPushManager.detectIsConnected()来异步获取
     * 
     */
    public abstract void onConnectionStatus(boolean  isConnected);
    
    /**
     * 连接服务端成功
     */
	public abstract void onConnectionSucceed();

	public abstract void onSendHeartBeat();
	
	
}

