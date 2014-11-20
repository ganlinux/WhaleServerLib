
package com.gl.emms.client.android;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.gl.emms.nio.mutual.SentBody;


/**
 * 与服务端连接服务
 * @author 3979434
 *
 */
public class EMMSPushService extends Service {

	EMMSConnectorManager manager;
	//EMMSEnventListenerReceiver mNetworkStateReceiver = null;
	private IBinder binder=new EMMSPushService.LocalBinder();

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
        	
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                Log.d("whale", "网络状态已经改变");
                EMMSPushManager.reconnect(context);
            }
        }
    };
	
	@Override
	public void onCreate()
	{
		Log.v("whale", "CIMPushService oncreate");
		manager = EMMSConnectorManager.getManager(this.getApplicationContext());
		Log.v("whale", "CIMPushService oncreate success");
		
		IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, mFilter);
	}
/*
	@Override
    public void onCreate() {
        super.onCreate();
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, mFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
*/
	
	
	@Override
	public int onStartCommand(Intent intent,int flags, int startId) {

		Log.v("whale", "CIMPushService");
		if(intent==null)
		{
			return super.onStartCommand(intent, flags, startId);
		}

		String action = intent.getStringExtra(EMMSPushManager.SERVICE_ACTION);

		if(EMMSPushManager.ACTION_CONNECTION.equals(action))
		{
			String host = intent.getStringExtra(EMMSDataConfig.KEY_CIM_SERVIER_HOST);
			int port = intent.getIntExtra(EMMSDataConfig.KEY_CIM_SERVIER_PORT, 9015);
			Log.v("whale", "sever connect");
			manager.connect(host,port);
		}

		if(EMMSPushManager.ACTION_SENDREQUEST.equals(action))
		{
			Log.v("whale", "请求报文："+(SentBody) intent.getSerializableExtra(EMMSPushManager.KEY_SEND_BODY));
			manager.send((SentBody) intent.getSerializableExtra(EMMSPushManager.KEY_SEND_BODY));
		}
		if(EMMSPushManager.ACTION_SENDRESPONSE.equals(action))
		{
			Log.v("whale", "响应报文："+(SentBody) intent.getSerializableExtra(EMMSPushManager.KEY_SEND_BODY));
			manager.send((SentBody) intent.getSerializableExtra(EMMSPushManager.KEY_SEND_BODY));
		}
		/*
    	if(CIMPushManager.ACTION_SENDHEARTBEAT.equals(action))
    	{
    		Log.v("whale", "请求心跳");
    		//manager.send((SentBody) intent.getSerializableExtra(CIMPushManager.KEY_SEND_BODY));
    	}
		 */


		if(EMMSPushManager.ACTION_DISCONNECTION.equals(action))
		{
			manager.closeSession();
		}

		if(EMMSPushManager.ACTION_DESTORY.equals(action))
		{
			manager.destroy();
			//this.stopSelf();
			//unregisterReceiver(mNetworkStateReceiver);// 取消注册Broadcast Receiver
			Log.v("whale", "EMMSPushService ACTION_DESTORY");
			stopSelf();//这样可以终止servise运行,以后可以修改此句
		}

		if(EMMSPushManager.ACTION_CONNECTION_STATUS.equals(action))
		{
			manager.deliverIsConnected();
		}

		return Service.START_REDELIVER_INTENT;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return binder;
	}
	@Override
    public void onDestroy() {
		Log.v("whale", "EMMSPushService onDestroy");
		super.onDestroy();
        unregisterReceiver(mReceiver);
		//getApplicationContext().unregisterReceiver(mNetworkStateReceiver);
		//stopSelf();
        //super.onDestroy();
    }
	public class LocalBinder extends Binder{

		public EMMSPushService getService()
		{
			return EMMSPushService.this;
		}
	}
}
