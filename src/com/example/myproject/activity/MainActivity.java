package com.example.myproject.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RadioButton;

import com.example.myproject.R;
import com.example.myproject.fragment.HomeFragment;
import com.example.myproject.fragment.HomeFragment4XListView;
import com.example.myproject.fragment.My1Fragment;
import com.example.myproject.fragment.My2Fragment;
import com.example.myproject.fragment.My3Fragment;
import com.example.myproject.fragment.My4Fragment;

public class MainActivity extends Activity implements OnClickListener {

	RadioButton homeRadioButton;
	RadioButton my1RadioButton;
	RadioButton my2RadioButton;
	RadioButton my3RadioButton;
	RadioButton my4RadioButton;
	
	HomeFragment4XListView home;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_activity);

		homeRadioButton = (RadioButton) findViewById(R.id.home_button);
		my1RadioButton = (RadioButton) findViewById(R.id.my1_button);
		my2RadioButton = (RadioButton) findViewById(R.id.my2_button);
		my3RadioButton = (RadioButton) findViewById(R.id.my3_button);
		my4RadioButton = (RadioButton) findViewById(R.id.my4_button);

		homeRadioButton.setOnClickListener(this);
		my1RadioButton.setOnClickListener(this);
		my2RadioButton.setOnClickListener(this);
		my3RadioButton.setOnClickListener(this);
		my4RadioButton.setOnClickListener(this);

		init();
	}

	private void init() {
//		HomeFragment home = new HomeFragment();
		home = new HomeFragment4XListView();
		changeFragment(R.id.content_layout, home, "homeFragment");
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.home_button:
//			HomeFragment home = new HomeFragment();
//			HomeFragment4XListView home = new HomeFragment4XListView();
			changeFragment(R.id.content_layout, home, "homeFragment");
			break;
		case R.id.my1_button:
			My1Fragment my1 = new My1Fragment();
			changeFragment(R.id.content_layout, my1, "my1");
			break;
		case R.id.my2_button:
			My2Fragment my2 = new My2Fragment();
			changeFragment(R.id.content_layout, my2, "my2");
			break;
		case R.id.my3_button:
			My3Fragment my3 = new My3Fragment();
			changeFragment(R.id.content_layout, my3, "my3");
			break;
		case R.id.my4_button:
			My4Fragment my4 = new My4Fragment();
			changeFragment(R.id.content_layout, my4, "my4");
			break;
		default:
			break;
		}
	}

	/*
	 * 监听Back键按下事件,方法1: 注意: super.onBackPressed()会自动调用finish()方法,关闭 当前Activity.
	 * 若要屏蔽Back键盘,注释该行代码即可
	 * 
	 * 到主页在点击返回时会退回到最初的空页面上，需要在回退上做处理
	 * 如果当前是homeFragment则直接退出，否则回到上个fragment
	 */
	@Override
	public void onBackPressed() {
		FragmentManager fragmentManager = getFragmentManager();
		Fragment homefFragment = fragmentManager.findFragmentByTag("homeFragment");
		if(homefFragment.isVisible()){
			MainActivity.this.finish();
		}
		else{
			super.onBackPressed();
		}
	}

	private void changeFragment(int fragmentLayout, Fragment fragment, String fragmentName) {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(fragmentLayout, fragment, fragmentName);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}

}
