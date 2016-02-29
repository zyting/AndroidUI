package com.zyting.summary.pick;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zyting.summary.R;
import com.zyting.summary.pick.SelectOnePicHelper.OnHanleImageCompletedListener;

public class SelectOnePicFragment extends Fragment implements OnClickListener,
		OnHanleImageCompletedListener {
	private ImageView iv;
	private Bitmap bmp;

	private SelectOnePicHelper mHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mHelper = new SelectOnePicHelper(getActivity());
		mHelper.setOnHanleImageCompletedListener(this);
		 mHelper.setNeedZoomImage(true);
		 mHelper.setZoomInfo(300, 300);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.pick_picture_layout, null);
		iv = (ImageView) v.findViewById(R.id.iv);

		v.findViewById(R.id.galleryBtn).setOnClickListener(this);
		v.findViewById(R.id.captureBtn).setOnClickListener(this);
		return v;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.galleryBtn:// �����ѡ��ͼƬ
			mHelper.gotoGallery();
			break;
		case R.id.captureBtn:// ���ջ�ȡ�µ�ͼƬ
			mHelper.gotoImageCapture("/zyting/pictures", null);
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		mHelper.onActivityResult(requestCode, data);
	}

	@Override
	public void onPickImageEnd(Bitmap bmp) {
		// TODO Auto-generated method stub
		// if(this.bmp != null)
		// this.bmp.recycle();
		// this.bmp = bmp;
		iv.setImageBitmap(bmp);
	}

	@Override
	public void onPickImageEnd(String filePath) {
		// TODO Auto-generated method stub
		Bitmap bmp = BitmapFactory.decodeFile(filePath);
		onPickImageEnd(bmp);
	}
}
