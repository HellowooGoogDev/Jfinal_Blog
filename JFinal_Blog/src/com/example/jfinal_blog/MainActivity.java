package com.example.jfinal_blog;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.jfinal_blog.view.LeftAndRightView;
/**
 * 
 *     
 * 项目名称：Blog    
 * 类名称：MainActivity    
 * 类描述：    
 * 创建人：yxtao    
 * 创建时间：2014-6-20 下午4:20:30    
 * 修改人：Administrator    
 * 修改时间：2014-6-20 下午4:20:30    
 * 修改备注：    
 * @version     
 *
 */
public class MainActivity extends Activity {
	private ListView contentList;
	private LeftAndRightView larView;
	
	private ArrayAdapter<String> contentListAdapter;
	private String[] contentItems = { "Content Item 1", "Content Item 2",
			"Content Item 3", "Content Item 4", "Content Item 5",
			"Content Item 6", "Content Item 7", "Content Item 8",
			"Content Item 9", "Content Item 10", "Content Item 11",
			"Content Item 12", "Content Item 13", "Content Item 14",
			"Content Item 15", "Content Item 16" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item);
		final int proc = Runtime.getRuntime().availableProcessors();
		Log.i("JFinal_Blog", proc + "");

		larView = (LeftAndRightView) findViewById(R.id.larView);
		contentList = (ListView) findViewById(R.id.contentList);
		contentListAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, contentItems);
		contentList.setAdapter(contentListAdapter);
		larView.setScrollEventView(contentList);
		initWidget();
		DisplayMetrics mMetrics = this.getResources().getDisplayMetrics();
		float desity = mMetrics.density;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	/**
	 * 
	   
	 * initWidget      
	 * @param             
	 * @return    
	 * @Exception     
	 * @since
	 */
	private void initWidget() {
		// listView = (ListView) findViewById(R.id.listView);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
