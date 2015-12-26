package com.example.myproject.fragment;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myproject.R;
import com.example.myproject.activity.MainActivity;
import com.example.myproject.adapter.MyAdapter;
import com.example.myproject.entity.Content;
import com.example.myproject.parameter.Parameter;
import com.example.myproject.util.MyHttpUtils;
import com.example.myproject.view.XListView;
import com.example.myproject.view.XListView.IXListViewListener;

public class HomeFragment4XListView extends ListFragment implements IXListViewListener{

	private XListView mListView;
	private ArrayAdapter<String> mAdapter;
	private ArrayList<String> items = new ArrayList<String>();
	private Handler mHandler;
	//初始化3条
	private int start = 3;
	//每次加载2条
	private int step = 2;
	private static int refreshCnt = 0;
	ProgressDialog progressDialog;
	
	public static HttpClient client = new DefaultHttpClient();
	List<Content> result = new ArrayList<Content>();
	MyAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	/**
	 * onActivityCreated顾名思义，是在activity完成onCreate()后被调用。
	 * 我们可以在此处理activity的其他UI操作，而其他的fragment已经与activity关联，
	 * 我们可以进行相互间处理。这也是用户看到UI界面之前的最后一个状态，
	 * 对于从saved状态中重新创建activity及fragment来讲是重要状态。
	 * 
	 * 注意：this.getListView();必须在onActivityCreated才能取到值！！！
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//获取listview对象
		mListView = (XListView) this.getListView();
		//启用或禁用打开负载更多的特性
		mListView.setPullLoadEnable(true);
		//为适配器设置activity
		adapter = new MyAdapter(this.getActivity());
		//适配器设置数据
		adapter.setData(result);
		//listview添加适配器
//		mListView.setAdapter(adapter);
		//适配器添加监听
		mListView.setXListViewListener(this);
		
		//添加listview事件
		mListView.setOnItemClickListener(new OnItemClickListener() {

			/**
			 * arg0???
			 * arg1是当前item的view，通过它可以获得该项中的各个组件。例如arg1.textview.settext("abc");
			 * arg2是当前item的ID。这个id根据你在适配器中的写法可以自己定义。例如：list.get(arg2).equals("abc"){//Do Something...}
			 * arg3是当前的item在listView中的相对位置！
			 */
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				TextView contentIdTextView = (TextView) view.findViewById(R.id.contentId);
				String contentId = contentIdTextView.getText().toString();
//				Toast.makeText(getActivity(), "我要吃:" + titleTextView.getText() , Toast.LENGTH_SHORT).show();
				Intent contentIntent = new Intent("com.example.myproject.activity.CONTENT_START");
				contentIntent.putExtra("contentId", contentId);
				startActivity(contentIntent);
			}
		});
		
		//设置handler进行刷新回调
		mHandler = new Handler();
		setListAdapter(adapter);
		geneItems();
		progressDialog = new ProgressDialog(this.getActivity());
		progressDialog.setMessage("正在获取数据，请稍后");
		progressDialog.show();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.home_fragment_xlistview, container, false);
		return view;
	}
	
	class MyTask extends AsyncTask<String, Void, List<Content>> {

		/**
		 * 后台执行，比较耗时的操作都可以放在这里。注意这里不能直接操作UI。 此方法在后台线程执行，完成任务的主要工作，通常需要较长的时间。
		 * 在执行过程中可以调用publicProgress(Progress…)来更新任务的进度。
		 */
		@Override
		protected List<Content> doInBackground(String... params) {

			String url = params[0];
			String tempResult = "";

			try {
				tempResult = MyHttpUtils.getContext(client, url);
			} catch (Exception e) {
				e.printStackTrace();
			}

			JSONArray jsonArray;
			List<Content> contentList = new ArrayList<Content>();
			try {
				jsonArray = new JSONObject(tempResult).getJSONArray("list");

				for (int i = 0; i < jsonArray.length(); i++) { // 遍历数据
					Content content = new Content();
					JSONObject object = jsonArray.getJSONObject(i); // 从JSONArray里面获取一个JSONObject对象
					Integer id = (Integer) object.get("id");
					String temp = (String) object.get("content");
					String img = (String) object.get("img");
					String addDate = (String) object.get("addDate");
					String title = (String) object.get("title");
					content.setId(id);
					content.setContent(temp);
					content.setImg(img);
					content.setAddDate(addDate);
					content.setTitle(title);
					
					//测试数据
					ArrayList<String> urls_3 = new ArrayList<String>();
					urls_3.add("http://img.my.csdn.net/uploads/201410/19/1413698837_7507.jpg");
					urls_3.add("http://img.my.csdn.net/uploads/201410/19/1413698865_3560.jpg");
					urls_3.add("http://img.my.csdn.net/uploads/201410/19/1413698867_8323.jpg");
					urls_3.add("http://img.my.csdn.net/uploads/201410/19/1413698837_5654.jpg");
					urls_3.add("http://img.my.csdn.net/uploads/201410/19/1413698883_5877.jpg");
					urls_3.add("http://img.my.csdn.net/uploads/201410/19/1413698839_2302.jpg");
					urls_3.add("http://img.my.csdn.net/uploads/201410/19/1413698837_7507.jpg");
					urls_3.add("http://img.my.csdn.net/uploads/201410/19/1413698865_3560.jpg");
					urls_3.add("http://img.my.csdn.net/uploads/201410/19/1413698867_8323.jpg");
					content.setImageUrls(urls_3);
					
					contentList.add(content);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return contentList;
		}

		/**
		 * 这里是最终用户调用Excute时的接口， 当任务执行之前开始调用此方法， 可以在这里显示进度对话框。
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
//			progressDialog.setTitle("处理中");
			
		}

		/**
		 * 相当于Handler 处理UI的方式，在这里面可以使用在doInBackground 得到的结果处理操作UI。
		 * 此方法在主线程执行，任务执行的结果作为此方法的参数返回
		 */
		@Override
		protected void onPostExecute(List<Content> result) {
			super.onPostExecute(result);
			adapter.setData(result);
			adapter.notifyDataSetChanged();
			progressDialog.dismiss();
		}

	}

	/**
	 * 还灭有灌数据
	 */
	private void geneItems() {
//		for (int i = 0; i <= 20; ++i) {
//			items.add("refresh cnt " + (++start));
//			System.out.println(start);
//		}
		new MyTask().execute(Parameter.URL + "?operate=queryJson&count=" + start);
	}

	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime("刚刚");
	}
	
	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				items.clear();
				geneItems();
				onLoad();
			}
		}, 2000);
	}

	/**
	 * 加载更多时对调
	 */
	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				start += step;
				geneItems();
				onLoad();
			}
		}, 2000);
	}
	
}
