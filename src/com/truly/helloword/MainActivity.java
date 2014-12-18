package com.truly.helloword;

import java.util.List;
import java.util.TreeMap;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.truly.models.EventLogModel;
import com.truly.util.SoapService;

public class MainActivity extends ActionBarActivity {

	public final static String Extra_mess = "Com.Truly.MainActivity.message";
	private Handler mHandler;
	private EventLogModel eventLog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Toast.makeText(this, "you just hit the action_setting menu", Toast.LENGTH_LONG).show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void sendMessage(View view) {

		Intent intent = new Intent(this, DisplayMessageActivity.class);
		EditText editText = (EditText) findViewById(R.id.EditText);
		String message = editText.getText().toString();
		intent.putExtra(Extra_mess, message);
		startActivity(intent);

	}

	public void TestSOAP(View view) {

		Toast.makeText(this, "start..", Toast.LENGTH_LONG).show();
		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				try {
					eventLog=JSON.parseObject(msg.obj.toString(), EventLogModel.class);
					JSON.parseObject(msg.obj.toString(),new TypeReference<List<EventLogModel>>(){});
					Toast.makeText(getApplicationContext(), eventLog.toString(),
							Toast.LENGTH_LONG).show();
				} catch (Exception e) {
					
					Log.e("SOAP", e.getMessage());
				}				
				super.handleMessage(msg);
			};
		};
		
		new MyThread().start();
	}

	public class MyThread extends Thread {
		@Override
		public void run() {
			SoapService soap = new SoapService();
			TreeMap<String, Object> filter = new TreeMap<String, Object>();
			filter.put("userName", "liyh");
			filter.put("fromDateStr", "2014-11-4");
			filter.put("toDateStr", "2014-11-8");
			try {
				String result = soap.getSoapStringResult(
						"GetEventLog", filter);
				Message ms = mHandler.obtainMessage();
				ms.obj = result;
				mHandler.sendMessage(ms);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	public void getEventLog(View view) {
		Intent intent=new Intent(this,DisplayEventLogActivity.class);
		startActivity(intent);
	}
}
