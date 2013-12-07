package com.example.jfinal_blog;

import com.example.jfinal_blog.view.LeftAndRightView;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {
	private ListView contentList;
	private LeftAndRightView larView;
	/**
	 * ListViewµƒ  ≈‰∆˜
	 */
	private ArrayAdapter<String> contentListAdapter;
	private String[] contentItems = { "Content Item 1", "Content Item 2", "Content Item 3",
			"Content Item 4", "Content Item 5", "Content Item 6", "Content Item 7",
			"Content Item 8", "Content Item 9", "Content Item 10", "Content Item 11",
			"Content Item 12", "Content Item 13", "Content Item 14", "Content Item 15",
			"Content Item 16" };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item);
		final int proc=Runtime.getRuntime().availableProcessors();
		Log.i("JFinal_Blog", proc+"");
		
		larView = (LeftAndRightView) findViewById(R.id.larView);
		contentList = (ListView) findViewById(R.id.contentList);
		contentListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
				contentItems);
		contentList.setAdapter(contentListAdapter);
		larView.setScrollEventView(contentList);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void initWidget() {
		//listView = (ListView) findViewById(R.id.listView);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
