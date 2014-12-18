package com.truly.helloword;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.truly.models.EventLogModel;
import com.truly.util.MyUtils;
import com.truly.util.SoapService;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class DisplayEventLogActivity extends ListActivity {

	private static final String TAG = "EventLogList";
	private Handler mHandler;
	private List<EventLogModel> list;
	private String[] segements = new String[] { "username", "event" };
	private int[] toViewField = new int[] { R.id.event_log_item_username,
			R.id.event_log_item_event };
	private static final int MENUITEM1 = Menu.FIRST;
	private static final int MENUITEM2 = Menu.FIRST + 1;
	private static final int MENUITEM3 = Menu.FIRST + 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Toast.makeText(this, "Waiting...", Toast.LENGTH_LONG).show();
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				list = JSON.parseObject(msg.obj.toString(),
						new TypeReference<List<EventLogModel>>() {
						});

				SimpleAdapter adapter = new SimpleAdapter(getBaseContext(),
						new MyUtils().ConvertToMapList(list, segements),
						R.layout.eventlog_item, segements, toViewField);

				setListAdapter(adapter);
				super.handleMessage(msg);
			}
		};
		// 启动另外的线程获取数据
		new GetDataThread().start();

		// 注册上下文菜单
		// getListView().setOnCreateContextMenuListener(this);
		registerForContextMenu(getListView());
	}

	public class GetDataThread extends Thread {

		@Override
		public void run() {
			SoapService soap = new SoapService();
			TreeMap<String, Object> filter = new TreeMap<String, Object>();
			filter.put("userName", "liyh");
			filter.put("fromDateStr", "2014-10-14");
			filter.put("toDateStr", "2014-11-1");
			try {
				String result = soap.getSoapStringResult("GetEventLogList",
						filter);
				Message ms = mHandler.obtainMessage();
				ms.obj = result;
				mHandler.sendMessage(ms);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		Toast.makeText(this,
				String.valueOf(position) + "," + list.get(position).toString(),
				Toast.LENGTH_LONG).show();
		super.onListItemClick(l, v, position, id);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.setHeaderTitle("Choose what you need");
		/*menu.add(0, MENUITEM1, 0, "check sysNum");
		menu.add(0, MENUITEM2, 0, "check event");
		menu.add(0, MENUITEM3, 0, "check all");*/
		
		MenuInflater inflater=getMenuInflater();
		inflater.inflate(R.menu.eventlog_item_menu, menu);
		
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		EventLogModel event = list
				.get(((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position);
		switch (item.getItemId()) {
		case MENUITEM1:
		case R.id.eventlog_menu_item_1:
			Toast.makeText(this, event.getSysNum(), Toast.LENGTH_LONG).show();
			break;
		case MENUITEM2:
		case R.id.eventlog_menu_item_2:
			Toast.makeText(this, event.getEvent(), Toast.LENGTH_LONG).show();
			break;
		case MENUITEM3:
		case R.id.eventlog_menu_item_3:
			Toast.makeText(this, event.toString(), Toast.LENGTH_LONG).show();
			break;
		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
}
