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
 * start��stop֮��ѭ��������Ӧ���ǣ�reset()-->setDataSource(path)-->prepare()-->start()-->
 * stop()--reset()-->������������һ�顣
 * 
 * @author Administrator ��ͣ�󲥷�������
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
	 * ��ʼ��
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
			mediaPlayer.prepareAsync();// prepare֮���Զ�����
		} catch (Exception e) {
			Log.e("mediaPlayer", "error", e);
		}

		// ÿ��һ�����һ��
		// �ڶ����������û����� schedule() ������Ҫ�ȴ���ô����ʱ��ſ��Ե�һ��ִ�� run() ������
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
	 * ͨ����ʱ����Handler�����½�����
	 ******************************************************/
	public class MTimerTask extends TimerTask {
		@Override
		public void run() {
			try {
				if (mediaPlayer != null && mediaPlayer.isPlaying()
						&& skbProgress.isPressed() == false) {
					/*
					 * sendMessage()�����㴦��Message����(Message����԰�������,)��
					 * sendEmptyMessage()ֻ�ܷ����� �����������ĸ������
					 */
					handleProgress.sendEmptyMessage(0);
				}
			}
			// ����ֵ����Ѿ��ͷŵ���ԴmediaPlayer��������б���
			catch (IllegalStateException ex) {
				Log.e("mediaPlayer", ex.toString());
			}
		}

	}

	/*******************************************************
	 * �����ڲ�����ʽʵ�֣�ͨ����ʱ����Handler�����½�����
	 ******************************************************/
	/*
	 * TimerTask mTimerTask = new TimerTask() {
	 * 
	 * @Override public void run() { if (mediaPlayer == null) return; if
	 * (mediaPlayer.isPlaying() && skbProgress.isPressed() == false) {
	 * 
	 * sendMessage()�����㴦��Message����(Message����԰�������,)�� sendEmptyMessage()ֻ�ܷ�����
	 * �����������ĸ������
	 * 
	 * handleProgress.sendEmptyMessage(0); } } };
	 */

	Handler handleProgress = new Handler() {
		public void handleMessage(Message msg) {
			if (mediaPlayer != null) {
				// ��ȡ��ǰλ��
				int position = mediaPlayer.getCurrentPosition();
				// �õ���ʱ��
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
	 * ͨ��onPrepared����
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
