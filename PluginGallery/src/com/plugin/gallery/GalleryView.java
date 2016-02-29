package com.plugin.gallery;

import java.util.ArrayList;
import java.util.List;

import com.plugin.photozoom.PhotoView;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 全屏查看多张图片
 * 
 * @author zyting
 * 
 */
public class GalleryView extends LinearLayout implements OnPageChangeListener {
	private MyPageAdapter mAdapter;
	private ViewPagerFixed mPager;
	private TextView mNumTv;

	private List<View> mListViews = null;
	private OnClickListener mImgClickListener;

	// 获取传过来的position
	private int mLocation = 0;

	public GalleryView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public GalleryView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	/**
	 * 设置图片点击事件监听器
	 * 
	 * @param listener
	 */
	public void setOnImageClickListener(OnClickListener listener) {
		this.mImgClickListener = listener;
		if (mListViews != null && mListViews.size() > 0) {
			for (View view : mListViews) {
				if (view != null)
					view.setOnClickListener(listener);
			}
		}
	}

	private void setLocation(int location) {
		if (mLocation >= 0 && mListViews.size() > mLocation) {
			View v = mListViews.get(mLocation);
			if (v != null && v instanceof PhotoView) {
				((PhotoView) v).zoomToDefault();
			}
		}

		this.mLocation = location;
	}

	/**
	 * 设置显示图片位置
	 * 
	 * @param position
	 *            从0开始
	 */
	public void setShowPosition(int position) {
		setLocation(position);
		if (mPager != null && mAdapter != null && position < mAdapter.size) {
			mPager.setCurrentItem(mLocation);
			setNum();
		}
	}

	/**
	 * 获取显示图片的位置
	 * 
	 * @return
	 */
	public int getShowPosition() {
		return mLocation;
	}

	/**
	 * 设置是否显示页数信息
	 * 
	 * @param visibility
	 */
	public void setNumVisiblity(int visibility) {
		if (mNumTv != null) {
			mNumTv.setVisibility(visibility);
		}
	}

	/**
	 * 设置显示图片的列表
	 * 
	 * @param bmps
	 */
	public void setShowBmps(List<Bitmap> bmps) {
		if (bmps != null) {
			for (int i = 0; i < bmps.size(); i++) {
				initListViews(bmps.get(i));
			}
			if (mAdapter != null) {
				mAdapter.setListViews(mListViews);
				mAdapter.notifyDataSetChanged();

				setNum();
			}
		}
	}

	private void setNum() {
		if (mNumTv.getVisibility() == View.VISIBLE && mAdapter != null) {
			mNumTv.setText((mLocation + 1) + "/" + mAdapter.size);
		}
	}

	private void init(Context context) {
		String pkgName = context.getPackageName();
		Resources resources = context.getResources();
		int layoutId = resources.getIdentifier("plugin_gallery_layout",
				"layout", pkgName);
		View layoutView = View.inflate(context, layoutId, null);
		this.addView(layoutView);

		int galleryId = resources.getIdentifier("plugin_gallery_vpf", "id",
				pkgName);
		int numTvId = resources.getIdentifier("plugin_gallery_num_tv", "id",
				pkgName);

		mPager = (ViewPagerFixed) layoutView.findViewById(galleryId);
		mPager.setOnPageChangeListener(this);
		mAdapter = new MyPageAdapter(mListViews);
		mPager.setAdapter(mAdapter);

		mNumTv = (TextView) layoutView.findViewById(numTvId);
	}

	private void initListViews(Bitmap bmp) {
		if (mListViews == null)
			mListViews = new ArrayList<View>();

		PhotoView img = new PhotoView(getContext());
		img.setBackgroundColor(0xff000000);
		img.setImageBitmap(bmp);
		img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));

		if (mImgClickListener != null)
			img.setOnClickListener(mImgClickListener);
		mListViews.add(img);
	}

	class MyPageAdapter extends PagerAdapter {
		private List<View> listViews;
		private int size;

		public MyPageAdapter(List<View> listviews) {
			setListViews(listviews);
		}

		public void setListViews(List<View> listviews) {
			this.listViews = listviews;
			size = listviews == null ? 0 : listviews.size();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return size;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			((ViewPagerFixed) container).removeView(listViews.get(position
					% size));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			try {
				((ViewPagerFixed) container).addView(listViews.get(position
						% size));
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return listViews.get(position % size);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		setLocation(arg0);
		setNum();
	}
}
