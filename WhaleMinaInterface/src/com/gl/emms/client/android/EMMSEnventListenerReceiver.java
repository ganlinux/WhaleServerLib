package com.gl.emms.client.android;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.gl.emms.nio.constant.EMMSConstant;
import com.gl.emms.nio.mutual.ServerMessage;
import com.gl.emms.nio.mutual.ReplyBody;
import com.gl.emms.nio.mutual.RequestBody;
import com.gl.emms.nio.mutual.SentBody;
/**
 *  消息入口，所有消息都会经过这里
 * @author ganlinux
 *
 */
public  abstract  class EMMSEnventListenerReceiver extends BroadcastReceiver implements OnEMMSMessageListener {


	public Context context;//上下文
	
	@Override
	public void onReceive(Context ctx, Intent it) {//事件接受处理

		context = ctx;
		Log.v("whale", "CIMEnventListenerReceiver action:"+it.getAction());
		
		if(it.getAction().equals(EMMSConnectorManager.ACTION_NETWORK_CHANGED))//网络变化
		{
			Log.v("whale", "ACTION_NETWORK_CHANGED");
			ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService("connectivity");
			android.net.NetworkInfo info = connectivityManager.getActiveNetworkInfo();

			onDevicesNetworkChanged(info);
		}
		
		if(it.getAction().equals(EMMSConnectorManager.ACTION_CONNECTION_CLOSED))//连接关闭
		{
			Log.v("whale", "ACTION_CONNECTION_CLOSED");
			onConnectionClosed();
		}

		if(it.getAction().equals(EMMSConnectorManager.ACTION_SEND_HEARTBEAT))//心跳
		{
			Log.v("whale", "ACTION_SEND_HEARTBEAT");
			onSendHeartBeat();
		}

		if(it.getAction().equals(EMMSConnectorManager.ACTION_CONNECTION_FAILED))//连接失败
		{
			Log.v("whale", "ACTION_CONNECTION_FAILED");
			onConnectionFailed((Exception) it.getSerializableExtra("exception"));
		}

		if(it.getAction().equals(EMMSConnectorManager.ACTION_CONNECTION_SUCCESS))//连接成功
		{
			Log.v("whale", "ACTION_CONNECTION_SUCCESS");
			onDispatchConnectionSucceed();
		}

		if(it.getAction().equals(EMMSConnectorManager.ACTION_MESSAGE_RECEIVED))//消息接收
		{
			Log.v("whale", "ACTION_MESSAGE_RECEIVED");
			filterType999Message((ServerMessage)it.getSerializableExtra("message"));
		}

		if(it.getAction().equals(EMMSConnectorManager.ACTION_REPLY_RECEIVED))//回复接收
		{
			Log.v("whale", "ACTION_REPLY_RECEIVED");
			onReplyReceived((ReplyBody)it.getSerializableExtra("replyBody"));
		}

		if(it.getAction().equals(EMMSConnectorManager.ACTION_REQUEST_RECEIVED))//请求接收
		{
			Log.v("whale", "ACTION_REQUEST_RECEIVED");
			onRequestReceived((RequestBody)it.getSerializableExtra("requestBody"));
		}


		if(it.getAction().equals(EMMSConnectorManager.ACTION_SENT_FAILED))//发送失败
		{
			Log.v("whale", "ACTION_SENT_FAILED");
			onSentFailed((Exception) it.getSerializableExtra("exception"),(SentBody)it.getSerializableExtra("sentBody"));
		}

		if(it.getAction().equals(EMMSConnectorManager.ACTION_SENT_SUCCESS))//发送成功
		{
			Log.v("whale", "ACTION_SENT_SUCCESS");
			onSentSucceed((SentBody)it.getSerializableExtra("sentBody"));
		}

		if(it.getAction().equals(EMMSConnectorManager.ACTION_UNCAUGHT_EXCEPTION))//异常
		{
			Log.v("whale", "ACTION_UNCAUGHT_EXCEPTION");
			onUncaughtException((Exception)it.getSerializableExtra("exception"));
		}

		if(it.getAction().equals(EMMSConnectorManager.ACTION_CONNECTION_STATUS))//连接状态
		{
			Log.v("whale", "ACTION_CONNECTION_STATUS");
			onConnectionStatus(it.getBooleanExtra(EMMSPushManager.KEY_CIM_CONNECTION_STATUS, false));
		}


	}

	protected boolean isInBackground(Context context) {//判断是否在后台
		List<RunningTaskInfo> tasksInfo = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningTasks(1);
		if (tasksInfo.size() > 0) {

			if (context.getPackageName().equals(
					tasksInfo.get(0).topActivity.getPackageName())) {

				return false;
			}
		}
		return true;
	}

	private  void onConnectionClosed(){//连接关闭

		//是否后台关闭
		//boolean  isManualStop  = EMMSDataConfig.getBoolean(context,EMMSDataConfig.KEY_MANUAL_STOP);

		//boolean  isManualDestory  = EMMSDataConfig.getBoolean(context,EMMSDataConfig.KEY_CIM_DESTORYED);

		//if(EMMSConnectorManager.netWorkAvailable(context) && !isManualStop && !isManualDestory)

		if(EMMSConnectorManager.netWorkAvailable(context))
		{
			Log.v("whale", "close reconnect");
			EMMSPushManager.reconnect(context);
		}

	}

	private   void onConnectionFailed(Exception e){//连接失败
		if(EMMSConnectorManager.netWorkAvailable(context))
		{
			/*
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			 */
			EMMSPushManager.reconnect(context);
		}
	}


	private void onUncaughtException(Throwable arg0) {
		EMMSPushManager.reconnect(context);
	}//异常



	private  void onDevicesNetworkChanged(NetworkInfo info) {//设备网络改变

		if(info !=null)
		{
			EMMSPushManager.reconnect(context);
		} 

		onNetworkChanged(info);
	}

	private void filterType999Message(com.gl.emms.nio.mutual.ServerMessage message)//过滤消息
	{
		Log.v("whale", "filterType999Message");
//		if(EMMSConstant.MessageType.TYPE_999.equals(message.getType()))
//		{
//			EMMSDataConfig.putBoolean(context,EMMSDataConfig.KEY_MANUAL_STOP,true);
//		}

		onMessageReceived(message);
	}

	private   void onSentFailed(Exception e, SentBody body){//发送失败

		//与服务端端开链接，重新连接
		if(e instanceof EMMSSessionDisableException)
		{
			EMMSPushManager.reconnect(context);
		}else
		{
			//发送失败 重新发送
			EMMSPushManager.sendRequest(context, body);
		}

	}


	private  void  onDispatchConnectionSucceed(){//连接成功。获取id

		//CIMPushManager.setAccount(context);
		//CIMDataConfig.putBoolean(context,CIMDataConfig.KEY_MANUAL_STOP,false);
		
		onConnectionSucceed();
	}


	private  void onSentSucceed(SentBody body){}//发送

	@Override
	public abstract void onMessageReceived(com.gl.emms.nio.mutual.ServerMessage message);//消息接收处理
	@Override
	public abstract void onReplyReceived(ReplyBody body);//回复接收处理

	public abstract void onNetworkChanged(NetworkInfo info);//网络改变处理

	@Override
	public abstract void onSendHeartBeat();//发送心跳

}
