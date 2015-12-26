package com.example.myproject.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myproject.R;
import com.example.myproject.entity.Content;
import com.example.myproject.picbrowse.ImagePagerActivity;
import com.example.myproject.picbrowse.NoScrollGridAdapter;
import com.example.myproject.picbrowse.NoScrollGridView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class MyAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater layoutInflater;
	private List<Content> list;
	private int resourceId;

	public MyAdapter(Context context) {
		this.context = context;
		// ��һ��Context�У����һ�������������������Ϳ���ʹ��������������xml�����ļ�תΪView�����ˡ�
		// ���滹��һ�� layoutInflater.inflate(R.layout.content_item, null);������ִ��
		layoutInflater = layoutInflater.from(context);
	}

	public List getData() {
		return list;
	}

	public void setData(List<Content> data) {
		this.list = data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = null;
		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.content_item, null);
			viewHolder = new ViewHolder();
			viewHolder.content = (TextView) convertView
					.findViewById(R.id.textViewContent);
//			viewHolder.img = (ImageView) convertView
//					.findViewById(R.id.imageView);
			viewHolder.addDate = (TextView) convertView
					.findViewById(R.id.addDate);
			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
			viewHolder.contentId = (TextView) convertView
					.findViewById(R.id.contentId);
			viewHolder.gridview = (NoScrollGridView) convertView
					.findViewById(R.id.gridview);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.content.setText(list.get(position).getContent());
		viewHolder.addDate.setText(list.get(position).getAddDate());
		viewHolder.title.setText(list.get(position).getTitle());
		viewHolder.contentId.setText(list.get(position).getId() + "");

		// ʹ��ImageLoader��������ͼƬ
		DisplayImageOptions options = new DisplayImageOptions.Builder()//
				.showImageOnLoading(R.drawable.ic_launcher) // ��������ʾ��Ĭ��ͼƬ
				.showImageOnFail(R.drawable.ic_launcher) // ���ü���ʧ�ܵ�Ĭ��ͼƬ
				.cacheInMemory(true) // �ڴ滺��
				.cacheOnDisk(true) // sdcard����
				.bitmapConfig(Bitmap.Config.RGB_565)// �����������
				.build();//

		final ArrayList<String> imageUrls = list.get(position).getImageUrls();
		if (imageUrls == null || imageUrls.size() == 0) { // û��ͼƬ��Դ������GridView
			viewHolder.gridview.setVisibility(View.GONE);
		} else {
			viewHolder.gridview.setAdapter(new NoScrollGridAdapter(context,
					imageUrls));
		}

		// ��������Ź��񣬲鿴��ͼ
		viewHolder.gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				imageBrower(position, imageUrls);
			}
		});

		// �ӿڻص��ķ��������ͼƬ�Ķ�ȡ;
		/*
		 * DownImage downImage = new DownImage(list.get(position).getImg());
		 * downImage.loadImage(new ImageCallBack() {
		 * 
		 * @Override public void getDrawable(Drawable drawable) { // TODO
		 * Auto-generated method stub viewHolder.img.setImageDrawable(drawable);
		 * } });
		 */

		return convertView;
	}

	/**
	 * ��ͼƬ�鿴��
	 * 
	 * @param position
	 * @param urls2
	 */
	protected void imageBrower(int position, ArrayList<String> urls2) {
		Intent intent = new Intent(context, ImagePagerActivity.class);
		// ͼƬurl,Ϊ����ʾ����ʹ�ó�����һ������ݿ��л������л�ȡ
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		context.startActivity(intent);
	}

	class ViewHolder {
		ImageView img;
		TextView content;
		TextView title;
		TextView addDate;
		TextView contentId;
		NoScrollGridView gridview;
	}
}