package com.zyting.summary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

@SuppressLint("NewApi")
public class DetailActivity extends Activity {
	private Fragment mFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		mFragment = JumpHelper.getFragment();

		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();
		transaction.add(R.id.id_content, mFragment);
		transaction.commit();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (mFragment != null)
			mFragment.onActivityResult(requestCode, resultCode, data);
	}
}
