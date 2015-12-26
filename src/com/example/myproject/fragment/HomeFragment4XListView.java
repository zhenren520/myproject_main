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
	//��ʼ��3��
	private int start = 3;
	//ÿ�μ���2��
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
	 * onActivityCreated����˼�壬����activity���onCreate()�󱻵��á�
	 * ���ǿ����ڴ˴���activity������UI��������������fragment�Ѿ���activity������
	 * ���ǿ��Խ����໥�䴦����Ҳ���û�����UI����֮ǰ�����һ��״̬��
	 * ���ڴ�saved״̬�����´���activity��fragment��������Ҫ״̬��
	 * 
	 * ע�⣺this.getListView();������onActivityCreated����ȡ��ֵ������
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//��ȡlistview����
		mListView = (XListView) this.getListView();
		//���û���ô򿪸��ظ��������
		mListView.setPullLoadEnable(true);
		//Ϊ����������activity
		adapter = new MyAdapter(this.getActivity());
		//��������������
		adapter.setData(result);
		//listview���������
//		mListView.setAdapter(adapter);
		//��������Ӽ���
		mListView.setXListViewListener(this);
		
		//���listview�¼�
		mListView.setOnItemClickListener(new OnItemClickListener() {

			/**
			 * arg0???
			 * arg1�ǵ�ǰitem��view��ͨ�������Ի�ø����еĸ������������arg1.textview.settext("abc");
			 * arg2�ǵ�ǰitem��ID�����id���������������е�д�������Լ����塣���磺list.get(arg2).equals("abc"){//Do Something...}
			 * arg3�ǵ�ǰ��item��listView�е����λ�ã�
			 */
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				TextView contentIdTextView = (TextView) view.findViewById(R.id.contentId);
				String contentId = contentIdTextView.getText().toString();
//				Toast.makeText(getActivity(), "��Ҫ��:" + titleTextView.getText() , Toast.LENGTH_SHORT).show();
				Intent contentIntent = new Intent("com.example.myproject.activity.CONTENT_START");
				contentIntent.putExtra("contentId", contentId);
				startActivity(contentIntent);
			}
		});
		
		//����handler����ˢ�»ص�
		mHandler = new Handler();
		setListAdapter(adapter);
		geneItems();
		progressDialog = new ProgressDialog(this.getActivity());
		progressDialog.setMessage("���ڻ�ȡ���ݣ����Ժ�");
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
		 * ��ִ̨�У��ȽϺ�ʱ�Ĳ��������Է������ע�����ﲻ��ֱ�Ӳ���UI�� �˷����ں�̨�߳�ִ�У�����������Ҫ������ͨ����Ҫ�ϳ���ʱ�䡣
		 * ��ִ�й����п��Ե���publicProgress(Progress��)����������Ľ��ȡ�
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

				for (int i = 0; i < jsonArray.length(); i++) { // ��������
					Content content = new Content();
					JSONObject object = jsonArray.getJSONObject(i); // ��JSONArray�����ȡһ��JSONObject����
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
					
					//��������
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
		 * �����������û�����Excuteʱ�Ľӿڣ� ������ִ��֮ǰ��ʼ���ô˷����� ������������ʾ���ȶԻ���
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
//			progressDialog.setTitle("������");
			
		}

		/**
		 * �൱��Handler ����UI�ķ�ʽ�������������ʹ����doInBackground �õ��Ľ���������UI��
		 * �˷��������߳�ִ�У�����ִ�еĽ����Ϊ�˷����Ĳ�������
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
	 * �����й�����
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
		mListView.setRefreshTime("�ո�");
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
	 * ���ظ���ʱ�Ե�
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
