package com.zyting.summary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;

import com.zyting.summary.album.AlbumFragment;
import com.zyting.summary.album.GalleryFragment;
import com.zyting.summary.pick.SelectOnePicFragment;

public class JumpHelper {
	private static int mGotoIdx = -1;
	private static List<String> mShowNames;
	private static Map<Integer, Class> mJumpDatas;

	private static void initDatas() {
		if (mShowNames == null) {
			mShowNames = new ArrayList<String>();
			mJumpDatas = new HashMap<Integer, Class>();

			addData("从相册中获取图片，或拍一张照片", SelectOnePicFragment.class);
			addData("选取多张图片", AlbumFragment.class);
			addData("点击查看大图(PluginGallery项目)", GalleryFragment.class);
		}
	}

	private static void addData(String name, Class fragmentClass) {
		if (fragmentClass != null) {
			mShowNames.add(name);
			mJumpDatas.put(mShowNames.size() - 1, fragmentClass);
		}
	}

	public static List<String> getShowNames() {
		initDatas();
		return mShowNames;
	}

	public static void jump(Context context, int gotoIdx) {
		mGotoIdx = gotoIdx;
		context.startActivity(new Intent(context, DetailActivity.class));
	}

	public static Fragment getFragment() {
		initDatas();
		try {
			Class cls = mJumpDatas.get(mGotoIdx);
			mGotoIdx = -1;
			if (cls != null) {
				return (Fragment) cls.newInstance();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
}
