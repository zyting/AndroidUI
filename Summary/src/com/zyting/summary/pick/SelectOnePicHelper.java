package com.zyting.summary.pick;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

/**
 * 从相册中取一张照片，或拍一张新的照片，可以设置选取完照片后的缩放属性<br>
 * 
 * 需要权限：<br>
 * android.permission.READ_EXTERNAL_STORAGE
 * android.permission.WRITE_EXTERNAL_STORAGE
 * android.permission.MOUNT_UNMOUNT_FILESYSTEMS
 * 
 * 
 * @author zyting
 * 
 */
public class SelectOnePicHelper {

	/**
	 * 拍照对应onActivityResult的requestCode
	 */
	public static final int IMAGE_CAPTURE_REQUEST_CODE = 1;
	/**
	 * 从相册中择时对应onActivityResult的requestCode
	 */
	public static final int IMAGE_GALLERY_REQUEST_CODE = 2;
	/**
	 * 选择完照片后图片的缩放对应onActivityResult的requestCode
	 */
	public static final int IMAGE_ZOOM_REQUEST_CODE = 3;

	private Activity mActivity = null;
	private OnHanleImageCompletedListener mListener = null;

	// 拍照时保存图片的路径
	private String mSaveFilePath = null;

	// 是否选择完后进行缩放
	private boolean mNeedZoomImage = false;
	// 缩放后图片的最终宽高
	private int zoomOutputWidth = 100;
	private int zoomOutputHeight = 100;

	public SelectOnePicHelper(Activity act) {
		this.mActivity = act;
	}

	public void setOnHanleImageCompletedListener(
			OnHanleImageCompletedListener listener) {
		this.mListener = listener;
	}

	/**
	 * 是否进行缩放
	 * 
	 * @param needZoom
	 */
	public void setNeedZoomImage(boolean needZoom) {
		this.mNeedZoomImage = needZoom;
	}

	/**
	 * 缩放后最终的图片宽高，默认为100x100
	 * 
	 * @param width
	 *            宽度
	 * @param height
	 *            高度
	 */
	public void setZoomInfo(int width, int height) {
		this.zoomOutputWidth = width;
		this.zoomOutputHeight = height;
	}

	/**
	 * 拍照
	 * 
	 * @param filePath
	 *            拍照后照片保存文件夹路径,例如："/ycz/pictures"
	 * @param fileName
	 *            照片保存名称，如果为null，则按当前时间命名
	 */
	public void gotoImageCapture(String filePath, String fileName) {
		mSaveFilePath = null;
		if (hasSdcard()) {
			File dir = new File(Environment.getExternalStorageDirectory()
					+ filePath);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			if (fileName == null || fileName.equals("")) {
				fileName = new SimpleDateFormat("yyyyMMdd_HHmmss")
						.format(new Date()) + ".png";
			}

			File file = new File(dir, fileName);
			mSaveFilePath = file.getAbsolutePath();
			Log.e("", mSaveFilePath);
			Intent intentFromCapture = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);
			intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(file));
			mActivity.startActivityForResult(intentFromCapture,
					IMAGE_CAPTURE_REQUEST_CODE);
		} else {
			Toast.makeText(mActivity, "未找到sd卡，不能保存照片!", Toast.LENGTH_SHORT)
					.show();
		}
	}

	/**
	 * 跳转到相册
	 */
	public void gotoGallery() {
		Intent intentFromGallery = new Intent();
		intentFromGallery.setType("image/*");
		// intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
		intentFromGallery.setAction(Intent.ACTION_PICK);
		intentFromGallery
				.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		mActivity.startActivityForResult(intentFromGallery,
				IMAGE_GALLERY_REQUEST_CODE);
	}

	public void onActivityResult(int requestCode, Intent data) {
		// if (data == null || data.getData() == null)
		// return;
		switch (requestCode) {
		case IMAGE_CAPTURE_REQUEST_CODE:
			handleImageCaptureResule(mActivity, data, mSaveFilePath);
			break;
		case IMAGE_GALLERY_REQUEST_CODE:
			if (data != null && data.getData() != null) {
				if (mNeedZoomImage) {
					startPhotoZoom(mActivity, data.getData());
				} else if (mListener != null) {
					ContentResolver cr = mActivity.getContentResolver();
					try {
						Bitmap bmp = BitmapFactory.decodeStream(cr
								.openInputStream(data.getData()));
						mListener.onPickImageEnd(bmp);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			break;
		case IMAGE_ZOOM_REQUEST_CODE:
			if (data != null && mListener != null) {
				// 对图片进行缩放处理
				Bitmap bm = data.getParcelableExtra("data");
				mListener.onPickImageEnd(bm);
			}
			break;
		}
	}

	/**
	 * 拍照返回后的处理
	 * 
	 * @param act
	 * @param data
	 * @param requestCode
	 * @param filePath
	 */
	private void handleImageCaptureResule(Activity act, Intent data,
			String filePath) {
		if (filePath != null && !filePath.equals("")) {
			if (mNeedZoomImage) {
				File tempFile = new File(filePath);
				startPhotoZoom(act, Uri.fromFile(tempFile));
			} else if (mListener != null) {
				// Bitmap bmp = BitmapFactory.decodeFile(filePath);
				// mListener.onPickImageEnd(bmp);
				mListener.onPickImageEnd(filePath);
			}
		} else if (data != null) {
			Bundle bundle = data.getExtras();
			if (bundle != null) {
				Bitmap photo = (Bitmap) bundle.get("data");
				if (mNeedZoomImage) {
					Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(
							act.getContentResolver(), photo, null, null));
					startPhotoZoom(act, uri);
				} else if (mListener != null) {
					mListener.onPickImageEnd(photo);
				}
			} else {
				Toast.makeText(act, "未找到sd卡，不能保存照片!", Toast.LENGTH_LONG).show();
			}
		}
	}

	/**
	 * 对图片进行缩放
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Activity act, Uri uri) {
		if (uri == null) {
			Log.i("tag", "The uri is not exist.");
			return;
		}
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", zoomOutputWidth);
		intent.putExtra("aspectY", zoomOutputHeight);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", zoomOutputWidth);
		intent.putExtra("outputY", zoomOutputHeight);
		intent.putExtra("scale", true);
		intent.putExtra("scaleUpIfNeeded", true);
		intent.putExtra("return-data", true);
		act.startActivityForResult(intent, IMAGE_ZOOM_REQUEST_CODE);
	}

	public boolean hasSdcard() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	public static interface OnHanleImageCompletedListener {
		/** 获取完图片后的通知 **/
		public void onPickImageEnd(Bitmap bmp);

		public void onPickImageEnd(String filePath);
	}

}
