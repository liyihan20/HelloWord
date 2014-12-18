package com.truly.util;

import java.util.Iterator;
import java.util.TreeMap;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.util.Log;

public class SoapService {

	public String getSoapStringResult(String methodName,
			TreeMap<String, Object> Args)  throws Exception{

		SoapObject obj = getSoapObject(methodName, Args);
		if (obj == null)
			return null;
		Log.d("SOAP", obj.getProperty(0).toString());
		return obj.getProperty(0).toString();

	}	
	
	private SoapObject getSoapObject(String methodName,
			TreeMap<String, Object> Args) throws Exception {

		String nameSpace = "http://truly.com.cn/ic";
		// 调用的方法名称
		// String methodName = "getMobileCodeInfo";
		// EndPoint
		String endPoint = "http://192.168.51.37:80/SaleOrderService/SOService.asmx";
		// SOAP Action
		String soapAction = nameSpace + "/" + methodName;

		// 指定WebService的命名空间和调用的方法名
		SoapObject rpc = new SoapObject(nameSpace, methodName);

		// 设置需调用WebService接口需要传入的参数
		Iterator<String> iterator = Args.keySet().iterator();
		while (iterator.hasNext()) {
			Object key = iterator.next();
			rpc.addProperty(key.toString(), Args.get(key));
		}
		
		// 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);

		envelope.bodyOut = rpc;
		// 设置是否调用的是dotNet开发的WebService
		envelope.dotNet = true;
		// 等价于envelope.bodyOut = rpc;
		//envelope.setOutputSoapObject(rpc);

		HttpTransportSE transport = new HttpTransportSE(endPoint);
		try {
			// 调用WebService
			transport.call(soapAction, envelope);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		// 获取返回的数据		
		if (envelope.bodyIn instanceof SoapFault) {
			String str = ((SoapFault) envelope.bodyIn).faultstring;			
			return null;
		} else {
			// 获取返回的结果
			// String result = object.getProperty("33").toString();
			SoapObject obj = (SoapObject) envelope.bodyIn;
			Log.d("SOAP", obj.toString());
			return obj;
		}
	}

}
