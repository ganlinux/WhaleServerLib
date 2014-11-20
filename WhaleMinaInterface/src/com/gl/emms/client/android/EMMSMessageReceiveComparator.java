package com.gl.emms.client.android;

import java.util.Comparator;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;

import com.gl.emms.nio.constant.EMMSConstant;


/**
 * ��Ϣ����activity�Ľ���˳������CIM_RECEIVE_ORDER����
 * @author 3979434
 *
 */
public class EMMSMessageReceiveComparator  implements Comparator<OnEMMSMessageListener>{

	Context mcontext;
	public EMMSMessageReceiveComparator(Context ctx)
	{
		mcontext = ctx;
	}
	
	@Override
	public int compare(OnEMMSMessageListener arg1, OnEMMSMessageListener arg2) {
		 
		Integer order1  = EMMSConstant.EMMS_DEFAULT_MESSAGE_ORDER;
		Integer order2  = EMMSConstant.EMMS_DEFAULT_MESSAGE_ORDER;
		ActivityInfo info;
		if (arg1 instanceof Activity ) {
			
			try {
				 info = mcontext.getPackageManager() .getActivityInfo(((Activity)(arg1)).getComponentName(), PackageManager.GET_META_DATA);
				 if(info.metaData!=null)
				 {
					 order1 = info.metaData.getInt("CIM_RECEIVE_ORDER");
				 }
				 
		     } catch (Exception e) {}
		}
		
		if (arg1 instanceof Activity ) {
			try {
				 info = mcontext.getPackageManager() .getActivityInfo(((Activity)(arg2)).getComponentName(), PackageManager.GET_META_DATA);
				 if(info.metaData!=null)
				 {
					 order2 = info.metaData.getInt("CIM_RECEIVE_ORDER");
				 }
				 
		     } catch (Exception e) {}
		}
		
		return order2.compareTo(order1);
	}
	 

}
