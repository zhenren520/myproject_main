package com.example.myproject.activity;

import java.util.ArrayList;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.example.myproject.R;
import com.example.myproject.entity.Content;
import com.example.myproject.music.Player;
import com.example.myproject.parameter.Parameter;
import com.example.myproject.picbrowse.ImagePagerActivity;
import com.example.myproject.picbrowse.NoScrollGridAdapter;
import com.example.myproject.picbrowse.NoScrollGridView;
import com.example.myproject.util.DownImage;
import com.example.myproject.util.DownImage.ImageCallBack;
import com.example.myproject.util.MyHttpUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

/**
 * ��ϸ����ҳ�棬��Ҫͨ����ѯ����������ݣ�������Ҫ�첽��ѯ
 * 
 * @author Administrator
 * 
 */
public class ContentActivity extends Activity {

	public static HttpClient client = new DefaultHttpClient();

	private String contentId;
	private Handler contentHandler;

	private TextView addDateTextView;
	private TextView titleTextView;
	private ImageView imageView;
	private TextView contentTextView;
	private TextView authorTextView;
	
	private SeekBar skbProgress;
	private Player player;
	private ImageButton playButton;
	private NoScrollGridView gridview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.content_activity);
		Intent intent = getIntent();
		contentId = intent.getStringExtra("contentId");
		Toast.makeText(ContentActivity.this, "id:" + contentId,
				Toast.LENGTH_LONG).show();
		
		addDateTextView = (TextView) findViewById(R.id.addDate);
		titleTextView = (TextView) findViewById(R.id.title);
//		imageView = (ImageView) findViewById(R.id.imageView);
		gridview = (NoScrollGridView) findViewById(R.id.gridview);
		contentTextView = (TextView) findViewById(R.id.contentTextView);
		authorTextView = (TextView) findViewById(R.id.author);
		
		skbProgress = (SeekBar) this.findViewById(R.id.skbProgress);
		skbProgress.setOnSeekBarChangeListener(new SeekBarChangeEvent());

		playButton = (ImageButton) findViewById(R.id.button_play);
		//���ð�ť����͸��
		playButton.getBackground().setAlpha(0);
		// playButton.setEnabled(false);
		playButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				if (player.getMediaPlayer().isPlaying()) {
					player.getMediaPlayer().pause();
					playButton.setImageResource(R.drawable.button_play);
				} else {
					player.getMediaPlayer().start();
					playButton.setImageResource(R.drawable.button_pause);
				}
			}
		});
		
		// ʹ��ImageLoader��������ͼƬ
				DisplayImageOptions options = new DisplayImageOptions.Builder()//
						.showImageOnLoading(R.drawable.ic_launcher) // ��������ʾ��Ĭ��ͼƬ
						.showImageOnFail(R.drawable.ic_launcher) // ���ü���ʧ�ܵ�Ĭ��ͼƬ
						.cacheInMemory(true) // �ڴ滺��
						.cacheOnDisk(true) // sdcard����
						.bitmapConfig(Bitmap.Config.RGB_565)// �����������
						.build();//

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
				
				final ArrayList<String> imageUrls = urls_3;
				if (imageUrls == null || imageUrls.size() == 0) { // û��ͼƬ��Դ������GridView
					gridview.setVisibility(View.GONE);
				} else {
					gridview.setAdapter(new NoScrollGridAdapter(ContentActivity.this,
							imageUrls));
				}

				// ��������Ź��񣬲鿴��ͼ
				gridview.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						imageBrower(position, imageUrls);
					}
				});
		
		contentHandler = new ContentHandler();
		new ContentThread().start();
	}

	/**
	 * ��ͼƬ�鿴��
	 * 
	 * @param position
	 * @param urls2
	 */
	protected void imageBrower(int position, ArrayList<String> urls2) {
		Intent intent = new Intent(ContentActivity.this, ImagePagerActivity.class);
		// ͼƬurl,Ϊ����ʾ����ʹ�ó�����һ������ݿ��л������л�ȡ
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		startActivity(intent);
	}
	
	/**
	 * �û�������Ⱦ���֮���Զ���������
	 */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if (hasFocus) {
			String url = Parameter.WEB_URL + "/beautiful.mp3";
			player = new Player(skbProgress, url);
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (player.getMediaPlayer() != null) {
			player.getMediaPlayer().stop();
			player.getMediaPlayer().release();
			player.setMediaPlayer(null);
			player.getTimerTask().cancel();
		}
	}
	
	/**
	 * SeekBar.OnSeekBarChangeListener�ӿڡ� ��SeekBar����Ҫ����3���¼���
	 * �ֱ��ǣ���ֵ�ĸı�(onProgressChanged)�� ��ʼ�϶�(onStartTrackingTouch)��
	 * ֹͣ�϶�(onStopTrackingTouch)�� ��onProgressChanged �����ǿ��Եõ���ǰ��ֵ�Ĵ�С
	 * 
	 * @author Administrator
	 * 
	 */
	class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {
		int progress;

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// ԭ����(progress/seekBar.getMax())*player.mediaPlayer.getDuration()
			this.progress = progress * player.mediaPlayer.getDuration()
					/ seekBar.getMax();
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// seekTo()�Ĳ����������ӰƬʱ������֣���������seekBar.getMax()��Ե�����
			player.mediaPlayer.seekTo(progress);
		}
	}
	
	class ContentThread extends Thread {
		String url = Parameter.URL + "?operate=queryContent&contentId="
				+ contentId;

		@Override
		public void run() {
			try {
				String jsonResult = MyHttpUtils.getContext(client, url);
				JSONObject obj = new JSONObject(jsonResult)
						.getJSONObject("content");
				String id = obj.getString("id");
				String strContent = obj.getString("content");
				String img = obj.getString("img");
				String addDate = obj.getString("addDate");
				String title = obj.getString("title");
				String author = obj.getString("author");
				Content content = new Content();
				content.setId(Integer.parseInt(id));
				content.setAddDate(addDate);
				content.setAuthor(author);
				content.setContent(strContent);
				content.setImg(img);
				content.setTitle(title);

				Message msg = contentHandler.obtainMessage();
				msg.obj = content;
				contentHandler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	class ContentHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			Content content = (Content) msg.obj;
			addDateTextView.setText(content.getAddDate());
			titleTextView.setText(content.getTitle());
			contentTextView.setText(content.getContent());
			authorTextView.setText("����:" + content.getAuthor());

			// �ӿڻص��ķ��������ͼƬ�Ķ�ȡ;
			/*DownImage downImage = new DownImage(content.getImg());
			downImage.loadImage(new ImageCallBack() {

				@Override
				public void getDrawable(Drawable drawable) {
					// TODO Auto-generated method stub
					imageView.setImageDrawable(drawable);
				}
			});*/
		}
	}
}
