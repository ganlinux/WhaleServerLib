package com.gl.emms.client.android;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.gl.emms.nio.filter.ClientMessageCodecFactory;
import com.gl.emms.nio.mutual.ServerMessage;
import com.gl.emms.nio.mutual.ReplyBody;
import com.gl.emms.nio.mutual.RequestBody;
import com.gl.emms.nio.mutual.SentBody;
/**
 * 连接服务端管理，emms核心处理类，管理连接，以及消息处理
 * 
 * @author ganlinux
 */
class EMMSConnectorManager  {

	private  NioSocketConnector connector;//异步socket连接
	private  ConnectFuture connectFuture;//异步socket连接
	private  IoSession session;//会话

	Context context;//上下文，句柄

	static EMMSConnectorManager manager;//系统socket连接管理器

	// 请求接收广播action 
	public static final String ACTION_REQUEST_RECEIVED = "com.gl.emms.REQUEST_RECEIVED";

	// 消息接收广播action
	public static final String ACTION_MESSAGE_RECEIVED = "com.gl.emms.MESSAGE_RECEIVED";

	// 发送sendbody失败广播
	public static final String ACTION_SENT_FAILED = "com.gl.emms.SENT_FAILED";

	// 发送sendbody成功广播
	public static final String ACTION_SENT_SUCCESS = "com.gl.emms.SENT_SUCCESS";

	// 发送心跳
	public static final String ACTION_SEND_HEARTBEAT = "com.gl.emms.SEND_HEARTBEAT";

	// 链接意外关闭广播
	public static final String ACTION_CONNECTION_CLOSED = "com.gl.emms.CONNECTION_CLOSED";
	// 链接失败广播
	public static final String ACTION_CONNECTION_FAILED = "com.gl.emms.CONNECTION_FAILED";
	// 链接成功广播
	public static final String ACTION_CONNECTION_SUCCESS = "com.gl.emms.CONNECTION_SUCCESS";
	// 发送sendbody成功后获得replaybody回应广播
	public static final String ACTION_REPLY_RECEIVED = "com.gl.emms.REPLY_RECEIVED";
	// 网络变化广播
	public static final String ACTION_NETWORK_CHANGED = "android.net.conn.CONNECTIVITY_CHANGE";

	// 未知异常
	public static final String ACTION_UNCAUGHT_EXCEPTION = "com.gl.emms.UNCAUGHT_EXCEPTION";

	// CIM连接状态
	public static final String ACTION_CONNECTION_STATUS = "com.gl.emms.CONNECTION_STATUS";

	//30秒后超时 
	//private static final int IDELTIMEOUT = 30;
	private static final int IDELTIMEOUT = 30;//测试用5 发送心跳时间，如果不发送心跳，服务器会关掉连接
	private ExecutorService executor;//线程池对象


	private EMMSConnectorManager(Context ctx) {
		context = ctx;
		executor = Executors.newFixedThreadPool(3);//创建3个线程池
		
		connector = new NioSocketConnector();//创建异步连接
		connector.setConnectTimeoutMillis(10 * 1000);//设置连接超时时间
		connector.getSessionConfig().setBothIdleTime(IDELTIMEOUT);//设置空闲时间
		// 设置接收缓存区大小  
		connector.getSessionConfig().setReadBufferSize(2048);  
		// 设置接收缓存区大小  
		connector.getSessionConfig().setReceiveBufferSize(2048);  
		//connector.getSessionConfig().setKeepAlive(true);
		//connector.getFilterChain().addLast("logger", new LoggingFilter());
		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ClientMessageCodecFactory()));//设置客户端编解码处理
		connector.setHandler(iohandler);//设置事件处理适配器
		Log.v("whale", "22");
	}

	public synchronized static EMMSConnectorManager getManager(Context context) {//得到连接管理器
		Log.v("whale", "11");
		if (manager == null) {

			manager = new EMMSConnectorManager(context);
		}
		Log.v("whale", "EMMSConnectorManager getManager");
		return manager;

	}

	private synchronized void  syncConnection(final String cimServerHost,final int cimServerPort) {//连接服务器
		try {
			Log.v("whale", "连接");
			if(isConnected()){
				return ;
			}

			InetSocketAddress remoteSocketAddress = new InetSocketAddress(cimServerHost, cimServerPort);//获得IP地址和端口
			connectFuture = connector.connect(remoteSocketAddress);//连接
			connectFuture.awaitUninterruptibly();
			session = connectFuture.getSession();
			Log.v("whale", "连接"+cimServerHost+":"+cimServerPort+"成功");

		} catch (Exception e) {

			Intent intent = new Intent();
			intent.setAction(ACTION_CONNECTION_FAILED);
			intent.putExtra("exception", e);
			context.sendBroadcast(intent);

			Log.v("whale", "连接"+cimServerHost+":"+cimServerPort+"失败");

		}

	}

	public  void connect(final String cimServerHost, final int cimServerPort) {

		if (!netWorkAvailable(context)) {
			//如果连接不可用，连接失败
			Intent intent = new Intent();
			intent.setAction(ACTION_CONNECTION_FAILED);
			intent.putExtra("exception", new NetWorkDisableException());
			context.sendBroadcast(intent);

			return;
		}

		executor.execute(new Runnable() {
			@Override
			public void run() {
				syncConnection(cimServerHost, cimServerPort);
			}
		});
	}

	public void send(final SentBody body) {//消息发送 函数（最底层）
		executor.execute(new Runnable() {
			@Override
			public void run() {
				android.os.Message msg = new android.os.Message();
				msg.getData().putSerializable("body", body);
				if(session!=null && session.isConnected())
				{
					WriteFuture wf = session.write(body);
					// 消息发送超时 5秒
					wf.awaitUninterruptibly(5, TimeUnit.SECONDS);

					if (!wf.isWritten()) {
						//发送失败
						Intent intent = new Intent();
						intent.setAction(ACTION_SENT_FAILED);
						intent.putExtra("exception", new WriteToClosedSessionException());
						intent.putExtra("sentBody", body);
						context.sendBroadcast(intent);
					}
				}else
				{
					//发送失败
					Intent intent = new Intent();
					intent.setAction(ACTION_SENT_FAILED);
					intent.putExtra("exception", new EMMSSessionDisableException());
					intent.putExtra("sentBody", body);
					context.sendBroadcast(intent);
				}
			}
		});
	}

	public   void destroy() {//连接取消
		if (manager.session != null) {
			manager.session.close(false);
			manager.session.removeAttribute("account");
		}

		if (manager.connector != null && !manager.connector.isDisposed()) {
			manager.connector.dispose();
		}
		manager = null;
	}

	public boolean isConnected() {//判断是否连接
		if (session == null || connector == null) {
			return false;
		}
		return session.isConnected() ;
	}

	public void deliverIsConnected() {//分发是否连接的消息
		Intent intent = new Intent();
		intent.setAction(ACTION_CONNECTION_FAILED);
		intent.putExtra(EMMSPushManager.KEY_CIM_CONNECTION_STATUS, isConnected());
		context.sendBroadcast(intent);
	}



	public void closeSession()//关闭对话
	{
		if(session!=null)
		{
			session.close(false);
		}
	}


	IoHandlerAdapter iohandler = new IoHandlerAdapter() {//session事件处理适配器

		@Override
		public void sessionCreated(IoSession session) throws Exception {//会话创建处理函数

			Log.v("whale", "连接服务器成功:"+session.getLocalAddress());
			Intent intent = new Intent();
			intent.setAction(ACTION_CONNECTION_SUCCESS);
			context.sendBroadcast(intent);

		}

		@Override
		public void sessionOpened(IoSession session) throws Exception {//会话打开处理函数
			session.getConfig().setBothIdleTime(IDELTIMEOUT);
		}

		@Override
		public void sessionClosed(IoSession session) throws Exception {//会话关闭处理函数

			Log.v("whale", "与服务器断开连接:"+session.getLocalAddress());
			if(EMMSConnectorManager.this.session.getId()==session.getId())
			{

				Intent intent = new Intent();
				intent.setAction(ACTION_CONNECTION_CLOSED);
				context.sendBroadcast(intent);

			}
		}

		@Override
		public void sessionIdle(IoSession session, IdleStatus status)//对话空闲处理函数：发送心跳
				throws Exception {
			Intent intent = new Intent();
			intent.setAction(ACTION_SEND_HEARTBEAT);
			context.sendBroadcast(intent);

		}

		@Override
		public void exceptionCaught(IoSession session, Throwable cause)//会话遗产处理函数
				throws Exception {

			Intent intent = new Intent();
			intent.setAction(ACTION_UNCAUGHT_EXCEPTION);
			intent.putExtra("exception", cause);
			context.sendBroadcast(intent);
		}

		@Override
		public void messageReceived(IoSession session, Object obj)//消息接收处理函数
				throws Exception {
			Log.v("whale", "CIMConnectorManager:messageReceived");
			if (obj instanceof ServerMessage) {

				Intent intent = new Intent();
				intent.setAction(ACTION_MESSAGE_RECEIVED);
				intent.putExtra("message", (ServerMessage) obj);
				context.sendBroadcast(intent);
				Log.v("whale", "CIMConnectorManager接收服务器message:"+obj.toString());

			}
			if (obj instanceof ReplyBody) {


				Intent intent = new Intent();
				intent.setAction(ACTION_REPLY_RECEIVED);
				intent.putExtra("replyBody", (ReplyBody) obj);
				context.sendBroadcast(intent);
				Log.v("whale", "CIMConnectorManager接收服务器reply:"+obj.toString());

			}
			if (obj instanceof RequestBody) {


				Intent intent = new Intent();
				intent.setAction(ACTION_REQUEST_RECEIVED);
				intent.putExtra("requestBody", (RequestBody) obj);
				context.sendBroadcast(intent);
				Log.v("whale", "CIMConnectorManager接收服务器request:"+obj.toString());

			}
		}

		@Override
		public void messageSent(IoSession session, Object message)//消息发送处理函数
				throws Exception {

			Intent intent = new Intent();
			intent.setAction(ACTION_SENT_SUCCESS);
			intent.putExtra("sentBody", (SentBody) message);
			context.sendBroadcast(intent);


		}
	};

	public static boolean netWorkAvailable(Context context) {//网络可用性检查函数
		try {
			ConnectivityManager nw = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = nw.getActiveNetworkInfo();
			return networkInfo != null;
		} catch (Exception e) {}

		return false;
	}


}