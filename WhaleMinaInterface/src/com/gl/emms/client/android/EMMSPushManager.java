package com.gl.emms.client.android;
import org.apache.mina.filter.reqres.Response;

import android.R.string;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.gl.emms.nio.constant.EMMSConstant;
import com.gl.emms.nio.mutual.ReplyBody;
import com.gl.emms.nio.mutual.SentBody;

/**
 *  ���ܽӿ�
 * 
 * @author ganlinux
 */
public class EMMSPushManager  {


	static String  ACTION_CONNECTION ="ACTION_CONNECTION";

	static String  ACTION_CONNECTION_STATUS ="ACTION_CONNECTION_STATUS";

	static String  ACTION_SENDREQUEST ="ACTION_SENDREQUEST";

	static String  ACTION_SENDRESPONSE ="ACTION_SENDRESPONSE";//��

	static String  ACTION_SENDHEARTBEAT ="ACTION_SENDHEARTBEAT";//����

	static String  ACTION_DISCONNECTION ="ACTION_DISSENDREQUEST";

	static String  ACTION_DESTORY ="ACTION_DESTORY";

	static String  SERVICE_ACTION ="SERVICE_ACTION";

	static String  KEY_SEND_BODY ="KEY_SEND_BODY";

	static String  KEY_CIM_CONNECTION_STATUS ="KEY_CIM_CONNECTION_STATUS";

	/**
	 * ��ʼ��,���ӷ���ˣ��ڳ�������ҳ���� ��Application�����
	 * @param context
	 * @param ip
	 * @param port
	 */
	public static  void connect(Context context,String ip,int port){

		//EMMSDataConfig.putBoolean(context,EMMSDataConfig.KEY_CIM_DESTORYED, false);

		Intent serviceIntent  = new Intent(context, EMMSPushService.class);
		serviceIntent.putExtra(EMMSDataConfig.KEY_CIM_SERVIER_HOST, ip);
		serviceIntent.putExtra(EMMSDataConfig.KEY_CIM_SERVIER_PORT, port);
		serviceIntent.putExtra(SERVICE_ACTION, ACTION_CONNECTION);
		context.startService(serviceIntent);

		EMMSDataConfig.putString(context, EMMSDataConfig.KEY_CIM_SERVIER_HOST, ip);
		EMMSDataConfig.putInt(context, EMMSDataConfig.KEY_CIM_SERVIER_PORT, port);
	}

	public static  void reconnect(Context context){

		String host = EMMSDataConfig.getString(context, EMMSDataConfig.KEY_CIM_SERVIER_HOST);
		int port =EMMSDataConfig.getInt(context, EMMSDataConfig.KEY_CIM_SERVIER_PORT);

		connect(context,host,port);

	}


	/**
	 * ����һ���˺ŵ�¼�������
	 * @param account �û�ΨһID
	 */
	public static  void requestLogin(Context context,String ID,String account,String pwd){

		if(account==null || account.trim().length()==0)
		{
			return ;
		}
		//EMMSDataConfig.putString(context,EMMSDataConfig.KEY_ACCOUNT, ID);
		//EMMSDataConfig.putString(context,EMMSDataConfig.KEY_ACCOUNT, account);
		//EMMSDataConfig.putString(context,EMMSDataConfig.KEY_PASSWORD, pwd);

		//boolean  isManualDestory  = EMMSDataConfig.getBoolean(context,EMMSDataConfig.KEY_CIM_DESTORYED);
		//if(isManualDestory || account==null){
		//	return ;
		//}


		String imei = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();

		SentBody sent = new SentBody();
		sent.setType("login_request");
		sent.setSender(ID);
		sent.setReciever("0");
		sent.put("username",account);
		sent.put("password",pwd);
		sendRequest(context,sent);


	}

	public static  void requestLoginSync(Context context,String id,String username){
		SentBody sent = new SentBody();
		sent.setType("loginsync_request");
		sent.setSender(id);
		sent.setReciever("0");
		sent.put("username",username);
		sendRequest(context,sent);
	}

	public static  void requestRegister(Context context,String id,String username,String password,String phoneNum,String roomNum,String cardNum,String wxNum){

		SentBody sent = new SentBody();
		sent.setType("register_request");
		sent.setSender(id);
		sent.setReciever("0");
		sent.put("username",username);
		sent.put("password",password);
		sent.put("phonenum",phoneNum);
		sent.put("roomnum",roomNum);
		sent.put("cardnum",cardNum);
		sent.put("wxnum",wxNum);
		sendRequest(context,sent);
	}

	/**
	 * ����һ���˺ŵ�¼�������
	 * @param account �û�ΨһID
	 */
	public static  void requestId(Context context,String idip,String id,boolean isOnline,String user){

		SentBody sent = new SentBody();
		sent.setType("id_request");
		sent.setSender(idip);
		sent.setReciever("0");
		sent.put("idip",idip);
		sent.put("userid",id);
		if (isOnline) {
			sent.put("isonline","1");
		}else {
			sent.put("isonline","0");
		}
		sent.put("user",user);
		sendRequest(context,sent);
		
	}

	/**
	 * ����ˢ������
	 * @param account �û�ΨһID
	 */
	public static  void setRefreshRoomsData(Context context,String ID,String roomnum){
		SentBody sent = new SentBody();
		sent.setType("roomview_request");
		sent.setSender(ID);
		sent.setReciever("0");
		sent.put("username",roomnum);
		sendRequest(context,sent);
	}

	/**
	 * ����ˢ������
	 * @param account �û�ΨһID
	 */
	public static  void setNlbDataRequest(Context context,String id,String starttime,String endtime){
		SentBody sent = new SentBody();
		sent.setType("nlbdata_request");
		sent.setSender(id);
		sent.setReciever("0");
		sent.put("starttime",starttime);
		sent.put("endtime",endtime);
		sendRequest(context,sent);


	}


	public static  void requestForceControl(Context context,String id,String room,String controltype){

		SentBody sent = new SentBody();
		sent.setType("forcecontrol_request");
		sent.setSender(id);
		sent.setReciever("0");
		sent.put("room",room);
		sent.put("controltype","3");
		sent.put("controlmetertype","0");
		sent.put("op",controltype);
		sent.put("sn","0");
		sendRequest(context,sent);
	}
	public static  void requestAutoForceControl(Context context,String id,String room,boolean[] controlEnable,String []controlTime,String [] controlType){

		SentBody sent = new SentBody();
		sent.setType("timingcontrol_request");
		sent.setSender(id);
		sent.setReciever("0");

		sent.put("room", room);
		sent.put("controlenable1", (controlEnable[0])?"1":"0");
		sent.put("controltime1", controlTime[0]);
		sent.put("controltype1", controlType[0]);

		sent.put("controlenable2", (controlEnable[1])?"1":"0");
		sent.put("controltime2", controlTime[1]);
		sent.put("controltype2", controlType[1]);

		sent.put("controlenable3", (controlEnable[2])?"1":"0");
		sent.put("controltime3", controlTime[2]);
		sent.put("controltype3", controlType[2]);

		sent.put("controlenable4", (controlEnable[3])?"1":"0");
		sent.put("controltime4", controlTime[3]);
		sent.put("controltype4", controlType[3]);

		sendRequest(context,sent);
	}

	public static  void requestHeartBeat(Context context,String id,boolean isOnline,String user){

		SentBody sent = new SentBody();
		sent.setType("heartbeat_request");
		sent.setSender(id);
		sent.setReciever("0");
		if (isOnline) {
			sent.put("isonline","1");
		}else {
			sent.put("isonline","0");
		}
		sent.put("user",user);
		sendRequest(context,sent);
	}
	public static  void requestReportRepair(Context context,String id,String room,String reportType,String reportContent){

		SentBody sent = new SentBody();
		sent.setType("reportrepair_request");
		sent.setSender(id);
		sent.setReciever("0");
		sent.put("reporttype",reportType);
		sent.put("reportcontent",reportContent);
		sent.put("room",room);
		sendRequest(context,sent);
	}
	public static  void requestRecharge(Context context,String id,String username,String room,String bill,String money,String balance,String cardNum){

		SentBody sent = new SentBody();
		sent.setType("recharge_request");
		sent.setSender(id);
		sent.setReciever("0");
		sent.put("username",username);
		sent.put("room",room);
		sent.put("bill",bill);
		sent.put("money",money);
		sent.put("lastcharge",balance);
		sent.put("cardnum",cardNum);
		sendRequest(context,sent);
	}
	public static  void requestRechargeParameter(Context context,String id){

		SentBody sent = new SentBody();
		sent.setType("rechargepara_request");
		sent.setSender(id);
		sent.setReciever("0");

		sendRequest(context,sent);
	}
	public static  void requestSelectRecharge(Context context,String id,String room){

		SentBody sent = new SentBody();
		sent.setType("selectcharge_request");
		sent.setSender(id);
		sent.setReciever("0");
		sent.put("room",room);
		sendRequest(context,sent);
	}
	public static  void requestRechargeSync(Context context,String id,String username,String room,String money,String bill){

		SentBody sent = new SentBody();
		sent.setType("recharge_request");
		sent.setSender(id);
		sent.setReciever("0");
		sent.put("username",username);
		sent.put("room",room);
		sent.put("money",money);
		sent.put("bill",bill);
		sendRequest(context,sent);
	}
	public static  void requestChangePassword(Context context,String id ,String username,String oldPassword,String newPassword){

		SentBody sent = new SentBody();
		sent.setType("changepwd_request");
		sent.setSender(id);
		sent.setReciever("0");
		sent.put("username",username);
		sent.put("oldpassword",oldPassword);
		sent.put("newpassword",newPassword);
		sendRequest(context,sent);
	}
	/*
	protected static  void setAccount(Context context){

		String ID = EMMSDataConfig.getString(context,EMMSDataConfig.KEY_ID);
		String account = EMMSDataConfig.getString(context,EMMSDataConfig.KEY_ACCOUNT);
		String password = EMMSDataConfig.getString(context,EMMSDataConfig.KEY_PASSWORD);
		setAccount(context,ID,account,password);

	}
	 */
	protected static  void clearAccount(Context context){

		//EMMSDataConfig.putString(context,EMMSDataConfig.KEY_ACCOUNT, null);
	}

	/**
	 * ����һ��CIM����
	 * @param context
	 * @body
	 */
	public static  void sendRequest(Context context,SentBody body){

		//boolean  isManualDestory  = EMMSDataConfig.getBoolean(context,EMMSDataConfig.KEY_CIM_DESTORYED);
		//if(isManualDestory){
		//	return ;
		//}

		Intent serviceIntent  = new Intent(context, EMMSPushService.class);
		serviceIntent.putExtra(KEY_SEND_BODY, body);
		serviceIntent.putExtra(SERVICE_ACTION, ACTION_SENDREQUEST);
		context.startService(serviceIntent);

	}

	/**
	 * ����һ���ظ�
	 * @param context
	 * @body
	 */
	public static  void sendReply(Context context,SentBody body){

		//boolean  isManualDestory  = EMMSDataConfig.getBoolean(context,EMMSDataConfig.KEY_CIM_DESTORYED);
		//if(isManualDestory){
		//	return ;
		//}

		Intent serviceIntent  = new Intent(context, EMMSPushService.class);
		serviceIntent.putExtra(KEY_SEND_BODY, body);
		serviceIntent.putExtra(SERVICE_ACTION, ACTION_SENDRESPONSE);
		context.startService(serviceIntent);

	}


	/**
	 * ֹͣ�������ͣ������˳���ǰ�˺ŵ�¼���˿������˵�����
	 * @param context
	 */
	public static  void stop(Context context){

		//boolean  isManualDestory  = EMMSDataConfig.getBoolean(context,EMMSDataConfig.KEY_CIM_DESTORYED);
		//if(isManualDestory){
		//	return ;
		//}

		//EMMSDataConfig.putBoolean(context,EMMSDataConfig.KEY_MANUAL_STOP, true);

		Intent serviceIntent  = new Intent(context, EMMSPushService.class);
		serviceIntent.putExtra(SERVICE_ACTION, ACTION_DISCONNECTION);
		context.startService(serviceIntent);

	}


	/**
	 * ��ȫ����CIM��һ��������ȫ�˳����򣬵���resume�����ָܻ�
	 * @param context
	 */
	public static  void destory(Context context){


		//EMMSDataConfig.putBoolean(context,EMMSDataConfig.KEY_CIM_DESTORYED, true);
		//EMMSDataConfig.putString(context,EMMSDataConfig.KEY_ACCOUNT, null);

		Intent serviceIntent  = new Intent(context, EMMSPushService.class);
		serviceIntent.putExtra(SERVICE_ACTION, ACTION_DESTORY);
		context.startService(serviceIntent);

	}


	/**
	 * ���»ָ��������ͣ��������ӷ���ˣ�����¼��ǰ�˺�
	 * @param context
	 */
	public static  void resume(Context context){

		//boolean  isManualDestory  = EMMSDataConfig.getBoolean(context,EMMSDataConfig.KEY_CIM_DESTORYED);
		//if(isManualDestory){
		//	return ;
		//}

		//setAccount(context);
	}


	/**
	 * �첽��ȡ����������״̬,�����ڹ㲥���յ�onConnectionStatus��boolean f��
	 * @param context
	 */
	public static void detectIsConnected(Context context){
		Intent serviceIntent  = new Intent(context, EMMSPushService.class);
		serviceIntent.putExtra(SERVICE_ACTION, ACTION_CONNECTION_STATUS);
		context.startService(serviceIntent);
	}

}