package com.example.myproject.music;

import java.util.Timer;
import java.util.TimerTask;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.SeekBar;

/**
 * start和stop之间循环的流程应该是：reset()-->setDataSource(path)-->prepare()-->start()-->
 * stop()--reset()-->重来上述流程一遍。
 * 
 * @author Administrator 暂停后播放有问题
 */
public class Player implements OnBufferingUpdateListener, OnCompletionListener,
		MediaPlayer.OnPreparedListener {
	public MediaPlayer mediaPlayer;

	private SeekBar skbProgress;
	private Timer mTimer;
	public MTimerTask timerTask;

	public Player(SeekBar skbProgress, String url) {
		this.skbProgress = skbProgress;
		timerTask = new MTimerTask();
		init(url);
	}

	/**
	 * 初始化
	 */
	private void init(String videoUrl) {
		try {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnBufferingUpdateListener(this);
			mediaPlayer.setOnPreparedListener(this);

			mediaPlayer.reset();
			mediaPlayer.setLooping(true);
			mediaPlayer.setDataSource(videoUrl);
			mediaPlayer.prepareAsync();// prepare之后自动播放
		} catch (Exception e) {
			Log.e("mediaPlayer", "error", e);
		}

		// 每隔一秒更新一次
		// 第二个参数：用户调用 schedule() 方法后，要等待这么长的时间才可以第一次执行 run() 方法。
		mTimer = new Timer();
		mTimer.schedule(timerTask, 0, 1000);
	}

	public MTimerTask getTimerTask() {
		return timerTask;
	}

	public void setTimerTask(MTimerTask timerTask) {
		this.timerTask = timerTask;
	}

	public MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}

	public void setMediaPlayer(MediaPlayer mediaPlayer) {
		this.mediaPlayer = mediaPlayer;
	}

	public void setmTimerTask(MTimerTask mTimerTask) {
		this.timerTask = mTimerTask;
	}

	/*******************************************************
	 * 通过定时器和Handler来更新进度条
	 ******************************************************/
	public class MTimerTask extends TimerTask {
		@Override
		public void run() {
			try {
				if (mediaPlayer != null && mediaPlayer.isPlaying()
						&& skbProgress.isPressed() == false) {
					/*
					 * sendMessage()允许你处理Message对象(Message里可以包含数据,)。
					 * sendEmptyMessage()只能放数据 用于区别是哪个传入的
					 */
					handleProgress.sendEmptyMessage(0);
				}
			}
			// 会出现调用已经释放的资源mediaPlayer，这里进行保护
			catch (IllegalStateException ex) {
				Log.e("mediaPlayer", ex.toString());
			}
		}

	}

	/*******************************************************
	 * 匿名内部类形式实现，通过定时器和Handler来更新进度条
	 ******************************************************/
	/*
	 * TimerTask mTimerTask = new TimerTask() {
	 * 
	 * @Override public void run() { if (mediaPlayer == null) return; if
	 * (mediaPlayer.isPlaying() && skbProgress.isPressed() == false) {
	 * 
	 * sendMessage()允许你处理Message对象(Message里可以包含数据,)。 sendEmptyMessage()只能放数据
	 * 用于区别是哪个传入的
	 * 
	 * handleProgress.sendEmptyMessage(0); } } };
	 */

	Handler handleProgress = new Handler() {
		public void handleMessage(Message msg) {
			if (mediaPlayer != null) {
				// 获取当前位置
				int position = mediaPlayer.getCurrentPosition();
				// 得到总时长
				int duration = mediaPlayer.getDuration();

				if (duration > 0) {
					long pos = skbProgress.getMax() * position / duration;
					skbProgress.setProgress((int) pos);
				}
			}
		};
	};

	// *****************************************************

	public void play() {
		mediaPlayer.start();
	}

	/*
	 * public void playUrl(String videoUrl) { try { if(mediaPlayer != null &&
	 * mediaPlayer.isPlaying()){ init(); }
	 * 
	 * mediaPlayer.start(); } catch (IllegalArgumentException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } catch
	 * (IllegalStateException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } }
	 */

	public void pause() {
		mediaPlayer.pause();
	}

	public void stop() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			// mediaPlayer = null;
			// mTimer.cancel();
		}
	}

	@Override
	/**
	 * 通过onPrepared播放
	 */
	public void onPrepared(MediaPlayer arg0) {
		arg0.start();
		Log.e("mediaPlayer", "onPrepared");
	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		Log.e("mediaPlayer", "onCompletion");
	}

	@Override
	public void onBufferingUpdate(MediaPlayer arg0, int bufferingProgress) {
		skbProgress.setSecondaryProgress(bufferingProgress);
		int currentProgress = skbProgress.getMax()
				* mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration();
		Log.e(currentProgress + "% play", bufferingProgress + "% buffer");
	}

}
