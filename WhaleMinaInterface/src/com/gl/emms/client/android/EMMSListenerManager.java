package com.gl.emms.client.android;
import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
 

/**
 * CIM ��Ϣ����������
 * 
 * @author 3979434@qq.com
 */
public class EMMSListenerManager  {

	private static ArrayList<OnEMMSMessageListener> cimListeners = new ArrayList<OnEMMSMessageListener>();
	
	

	public static void registerMessageListener(OnEMMSMessageListener listener,Context mcontext) {

		if (!cimListeners.contains(listener)) {
			cimListeners.add(listener);
			// ���ս���˳����
			Collections.sort(cimListeners, new EMMSMessageReceiveComparator(mcontext));
		}
	}

	
	public static void removeMessageListener(OnEMMSMessageListener listener) {
		for (int i = 0; i < cimListeners.size(); i++) {
			if (listener.getClass() == cimListeners.get(i).getClass()) {
				cimListeners.remove(i);
			}
		}
	}
	
	public static ArrayList<OnEMMSMessageListener> getCIMListeners() {
		return cimListeners;
	}
}