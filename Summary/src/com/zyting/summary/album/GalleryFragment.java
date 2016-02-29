package com.zyting.summary.album;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.plugin.gallery.GalleryView;
import com.zyting.summary.R;

/**
 * 点击查看大图
 * 
 * @author Zyting
 * 
 */
public class GalleryFragment extends Fragment {
	private GalleryView mGv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View myView = inflater.inflate(R.layout.gallery_layout, null);
		mGv = (GalleryView) myView.findViewById(R.id.galleryview);
		
		mGv.setOnImageClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getActivity().finish();
			}
		});

		List<Bitmap> bmps = new ArrayList<Bitmap>();
		bmps.add(BitmapFactory.decodeResource(getResources(),
				R.drawable.guide_1));
		bmps.add(BitmapFactory.decodeResource(getResources(),
				R.drawable.guide_2));
		bmps.add(BitmapFactory.decodeResource(getResources(),
				R.drawable.guide_3));
		bmps.add(BitmapFactory.decodeResource(getResources(),
				R.drawable.guide_4));
		bmps.add(BitmapFactory.decodeResource(getResources(),
				R.drawable.guide_5));
		bmps.add(BitmapFactory.decodeResource(getResources(),
				R.drawable.icon_addpic_focused));

		mGv.setShowBmps(bmps);
		mGv.setShowPosition(3);
		return myView;
	}

}
