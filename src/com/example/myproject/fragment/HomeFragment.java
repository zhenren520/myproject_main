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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myproject.R;
import com.example.myproject.adapter.MyAdapter;
import com.example.myproject.entity.Content;
import com.example.myproject.parameter.Parameter;
import com.example.myproject.util.MyHttpUtils;

/**
 * 1.listview中item设置布局
 * 2.设置listFragment布局，其中应该包含listview
 * 3.在ListFragment子类中进行加载
 * 
 * ListFragment显示一个由适配器管理的条目列表（例如SimpleCursorAdapter），类似于ListActivity。
 * 并且提供了许多管理列表视图的函数，例如处理点击事件的onListItemClick()回调函数。
 * 
 * ListFragment的布局默认包含一个list view。
        因此，在ListFragment对应的布局文件中，
        必须指定一个 android:id 为 “@android:id/list” 的ListView控件
    若用户向修改list view的，可以在onCreateView(LayoutInflater, ViewGroup, Bundle)中进行修改。
    当然，用户也可以在ListFragment的布局中包含其它的控件。
 * @author Administrator
 *
 */
public class HomeFragment extends ListFragment {

	public static HttpClient client = new DefaultHttpClient();
	List<Content> contentList = new ArrayList<Content>();
	MyAdapter adapter;
	List<Content> result = new ArrayList<Content>();
	
	ProgressDialog progressDialog;
	
	/**
	 * 在创建fragment时系统会调用此方法。
	 * 在实现代码中，你可以初始化想要在fragment中保持的那些必要组件，
	 * 当fragment处于暂停或者停止状态之后可重新启用它们。
	 * 先执行onCreate，然后再执行onCreateView
	 * @param savedInstanceState
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		adapter = new MyAdapter(this.getActivity());
		adapter.setData(result);
		setListAdapter(adapter);
	}
	
	/**
	 * 在第一次为fragment绘制用户界面时系统会调用此方法。
	 * 为fragment绘制用户界面，这个函数必须要返回所绘出的fragment的根View。
	 * 如果fragment没有用户界面可以返回空。
	 * 为了给fragment提供一个布局，你必须实现onCreateView()回调函数，在绘制fragment布局时Android系统会调用它
	 * @param inflater
	 * @param container
	 * @param savedInstanceState
	 * @return
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.home_fragment, container, false);
		progressDialog = new ProgressDialog(this.getActivity());
		new MyTask().execute(Parameter.URL + "?operate=queryJson");
		return view;
		
//		return inflater.inflate(R.layout.home_fragment, container, false);
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
			progressDialog.setMessage("正在获取数据，请稍后");
			 progressDialog.show();
		}

		/**
		 * 相当于Handler 处理UI的方式，在这里面可以使用在doInBackground 得到的结果处理操作UI。
		 * 此方法在主线程执行，任务执行的结果作为此方法的参数返回
		 */
		@Override
		protected void onPostExecute(List<Content> temp_result) {
			super.onPostExecute(temp_result);
			result = temp_result;
			adapter.setData(result);
			adapter.notifyDataSetChanged();
			progressDialog.dismiss();
		}

	}
	
}
