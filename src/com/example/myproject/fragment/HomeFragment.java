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
 * 1.listview��item���ò���
 * 2.����listFragment���֣�����Ӧ�ð���listview
 * 3.��ListFragment�����н��м���
 * 
 * ListFragment��ʾһ�����������������Ŀ�б�����SimpleCursorAdapter����������ListActivity��
 * �����ṩ���������б���ͼ�ĺ��������紦�����¼���onListItemClick()�ص�������
 * 
 * ListFragment�Ĳ���Ĭ�ϰ���һ��list view��
        ��ˣ���ListFragment��Ӧ�Ĳ����ļ��У�
        ����ָ��һ�� android:id Ϊ ��@android:id/list�� ��ListView�ؼ�
    ���û����޸�list view�ģ�������onCreateView(LayoutInflater, ViewGroup, Bundle)�н����޸ġ�
    ��Ȼ���û�Ҳ������ListFragment�Ĳ����а��������Ŀؼ���
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
	 * �ڴ���fragmentʱϵͳ����ô˷�����
	 * ��ʵ�ִ����У�����Գ�ʼ����Ҫ��fragment�б��ֵ���Щ��Ҫ�����
	 * ��fragment������ͣ����ֹͣ״̬֮��������������ǡ�
	 * ��ִ��onCreate��Ȼ����ִ��onCreateView
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
	 * �ڵ�һ��Ϊfragment�����û�����ʱϵͳ����ô˷�����
	 * Ϊfragment�����û����棬�����������Ҫ�����������fragment�ĸ�View��
	 * ���fragmentû���û�������Է��ؿա�
	 * Ϊ�˸�fragment�ṩһ�����֣������ʵ��onCreateView()�ص��������ڻ���fragment����ʱAndroidϵͳ�������
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
			progressDialog.setMessage("���ڻ�ȡ���ݣ����Ժ�");
			 progressDialog.show();
		}

		/**
		 * �൱��Handler ����UI�ķ�ʽ�������������ʹ����doInBackground �õ��Ľ���������UI��
		 * �˷��������߳�ִ�У�����ִ�еĽ����Ϊ�˷����Ĳ�������
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
