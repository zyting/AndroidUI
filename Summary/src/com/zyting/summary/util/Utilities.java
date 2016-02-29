package com.zyting.summary.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class Utilities {

	/**
	 * 将总数量计算成页数
	 * 
	 * @param total
	 * @return
	 */
	public static long getTotalPage(String total) {
		if (!TextUtils.isEmpty(total)) {
			// 返回总多少页
			long Pager = 0;
			long totalCount = Integer.parseInt(total);
			long currentPager = 1;
			if (totalCount - 5 > 0) {
				currentPager = totalCount / 5;
				Pager = totalCount % 5;
			}
			return currentPager + Pager;
		}
		return 1;
	}

	/**
	 * 日期转换成年 月 日
	 * 
	 * @param date
	 * @return
	 */
	public static String dataFomat(String date) {
		if (!TextUtils.isEmpty(date)) {
			String[] split = date.split(" ");
			String[] day = split[0].split("-");
			return day[0] + "年" + day[1] + "月" + day[2] + "日";
		}
		return "";
	}

	/**
	 * 保留两位精度
	 * 
	 * @param param
	 * @return
	 */
	public static double float2(Double param) {
		double f = param;
		BigDecimal b = new BigDecimal(f);
		double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return f1;
	}

	/**
	 * 保留0位精度
	 * 
	 * @param param
	 * @return
	 */
	public static double float0(Double param) {
		double f = param;
		BigDecimal b = new BigDecimal(f);
		double f1 = b.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
		return f1;
	}

	/**
	 * 四舍五入.
	 * 
	 * @param number
	 *            原数
	 * @param decimal
	 *            保留几位小数
	 * @return 四舍五入后的值
	 */
	public static BigDecimal round(double number, int decimal) {
		return new BigDecimal(number).setScale(decimal,
				BigDecimal.ROUND_HALF_UP);
	}

	public static String toMd5(byte[] bytes) {
		try {
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(bytes);
			return toHexString(algorithm.digest(), "");
		} catch (NoSuchAlgorithmException e) {
			Log.v("util", "toMd5():" + e);
			throw new RuntimeException(e);
		}
	}

	private static String toHexString(byte[] bytes, String separator) {
		StringBuilder hexString = new StringBuilder();
		for (byte b : bytes) {
			String hex = Integer.toHexString(0xFF & b);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex).append(separator);
		}
		return hexString.toString();
	}

	/**
	 * 半角转换为全角 说明：解决TextView中文字排版参差不齐的问题
	 * 将textview中的字符全角化。即将所有的数字、字母及标点全部转为全角字符，使它们与汉字同占两个字节 by:liubing
	 * 
	 * @param input
	 * @return
	 */
	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	/**
	 * 去除特殊字符或将所有中文标号替换为英文标号 说明：解决TextView中文字排版参差不齐的问题 by:liubing
	 * 
	 * @param str
	 * @return
	 */
	public static String StringFilter(String str) {
		str = str.replaceAll("【", "[").replaceAll("】", "]")
				.replaceAll("！", "!").replaceAll("：", ":").replace("，", ",")
				.replace("。", ".").replace("……", "......");// 替换中文标号
		String regEx = "[『』]"; // 清除掉特殊字符
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

	/**
	 * 网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager mgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] info = mgr.getAllNetworkInfo();
		if (info != null) {
			for (int i = 0; i < info.length; i++) {
				if (info[i].getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;
	}

	// 保留小数点后两位小数
	public static double Number2(double pDouble) {
		BigDecimal bd = new BigDecimal(pDouble);
		BigDecimal bd1 = bd.setScale(2, bd.ROUND_HALF_UP);
		pDouble = bd1.doubleValue();
		long ll = Double.doubleToLongBits(pDouble);

		return pDouble;
	}

	// 判断email格式是否正确
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);

		return m.matches();
	}

	/**
	 * 手机号验证
	 * 
	 * @param str
	 * @return 验证通过返回true
	 */
	public static boolean isMobile(String str) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
		m = p.matcher(str);
		b = m.matches();
		return b;
	}

	/**
	 * 电话号码验证
	 * 
	 * @param str
	 * @return 验证通过返回true
	 */
	public static boolean isMobileOrTel(String str) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^[0-9]{7,8}$|^1[3,4,5,7,8][0-9]{9}$"); // 验证手机号
																	// 和座机号
		m = p.matcher(str);
		b = m.matches();
		return b;
	}

	public static boolean isPsw(String str) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("\\d{9-16}$"); // 验证手机号
		m = p.matcher(str);
		b = m.matches();
		return b;
	}

	/**
	 * 电话号码验证
	 * 
	 * @param str
	 * @return 验证通过返回true
	 */
	public static boolean isPhone(String str) {
		Pattern p1 = null, p2 = null;
		Matcher m = null;
		boolean b = false;
		p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$"); // 验证带区号的
		p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$"); // 验证没有区号的
		if (str.length() > 9) {
			m = p1.matcher(str);
			b = m.matches();
		} else {
			m = p2.matcher(str);
			b = m.matches();
		}
		return b;
	}

	/**
	 * 判断是否为整型字符串
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str) {
		// +表示1个或多个（如"3"或"225"），*表示0个或多个（[0-9]*）（如""或"1"或"22"），?表示0个或1个([0-9]?)(如""或"7")
		boolean isNum = str.matches("[0-9]+");
		return isNum;
	}

	public static boolean isChinese(String str) {
		Pattern idNumPat = Pattern.compile("[\u4e00-\u9fa5]+");
		// 通过Pattern获得Matcher
		Matcher idNumMat = idNumPat.matcher(str);
		return idNumMat.matches();
	}

	/**
	 * 检查字符串为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str == null || "".equals(str)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 字符串插入数组
	 * 
	 * @param arr
	 * @param str
	 * @return
	 */
	public static String[] insert(String[] arr, String str) {
		int size = arr.length;
		String[] tmp = new String[size + 1];
		System.arraycopy(arr, 0, tmp, 0, size);
		tmp[size] = str;
		return tmp;
	}

	/**
	 * 获取GPS经纬度
	 * 
	 * @param context
	 * @return
	 */
	public static List<Double> getLocation(Context context) {
		// String position = "";
		double latitude = 0;
		double longitude = 0;
		List<Double> locationList = new ArrayList<Double>();
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		Location location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location == null) {
			location = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}

		if (location != null) {
			latitude = location.getLatitude();
			longitude = location.getLongitude();
			// position = latitude + "," + longitude;
		}
		locationList.add(latitude);
		locationList.add(longitude);

		return locationList;
	}


	/**
	 * 检测网路类型
	 * 
	 * @param context
	 * @return
	 */
	public static String checkNetworkInfo(Context context) {
		// TODO Auto-generated method stub
		ConnectivityManager conMan = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE); // mobile 3G
																	// Data
																	// Network
		State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		if (wifi == State.CONNECTED)
			return "WIFI";
		// new
		// AlertDialog.Builder(context).setMessage(wifi.toString()).setPositiveButton("WIFI",
		// null).show();//显示wifi网络连接状态
		State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();
		if (mobile == State.CONNECTED)
			return "3G";
		// new
		// AlertDialog.Builder(context).setMessage(mobile.toString()).setPositiveButton("3G",
		// null).show();//显示3G网络连接状态
		return "";
	}


	/**
	 * 拷贝文件
	 * 
	 * @param context
	 * @param fileName
	 * @param path
	 * @return
	 */
	public static boolean copyApkFromAssets(Context context, String fileName,
			String path) {
		boolean copyIsFinish = false;
		try {
			InputStream is = context.getAssets().open(fileName);
			File file = new File(path);
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			byte[] temp = new byte[1024];
			int i = 0;
			while ((i = is.read(temp)) > 0) {
				fos.write(temp, 0, i);
			}
			fos.close();
			is.close();
			copyIsFinish = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return copyIsFinish;
	}

	public static int[] getScreenSize(Context ctx) {
		WindowManager manager = (WindowManager) ctx
				.getSystemService(ctx.WINDOW_SERVICE);

		// 屏幕测量的尺子
		DisplayMetrics outMetrics = new DisplayMetrics();
		// 获取到屏幕的款和高
		// manager.getDefaultDisplay().getHeight();
		manager.getDefaultDisplay().getMetrics(outMetrics);// 尺子里面有数据了
		int screenWidth = outMetrics.widthPixels;
		int screenHeigh = outMetrics.heightPixels;
		int[] screenSize = { screenWidth, screenHeigh };
		return screenSize;
	}
	public static boolean hasSdcard() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	public static void showToastShort(Context context, String info) {
		Toast toast = Toast.makeText(context, info, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}


	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	public static void call(Context ctx, String tel) {
		if (!isEmpty(tel)) {
			Intent phoneIntent = new Intent("android.intent.action.CALL",
					Uri.parse("tel:" + tel));
			// 启动
			ctx.startActivity(phoneIntent);
		}
	}

	public static String getCurrentTime(Context context) {
		// 刷新时间
		String time = DateUtils.formatDateTime(context,
				System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
						| DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL);
		return time;
	}

	/**
	 * 切换软键盘的状态 如当前为收起变为弹出,若当前为弹出变为收起
	 */
	public static void toggleInput(Context context) {
		InputMethodManager inputMethodManager = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.toggleSoftInput(0,
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * 软键盘是否弹出
	 */
	public static boolean isShowSoftInput(Context context) {
		if (((Activity) context).getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {
			return true;
		}
		return false;
	}


	public static String[] getHMSByTime(long time){
		int h = 0;
		int m = 0;
		int s = 0;
		int curTime = (int) (time);
		h = curTime / (60 * 60);
		m = (curTime / 60) % 60;
		s = curTime % 60;
		String[] arr = {h < 10 ? ("0" + h) : (h + ""), m < 10 ? ("0" + m) : (m + ""), s < 10 ? ("0" + s) : (s + "")};
		return arr;
	}
	
	/**
	 * 返回当前程序版本名
	 */
	public static String getAppVersionName(Context context) {
		String versionName = "";
		try {
			// ---get the package info---
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			// versioncode = pi.versionCode;
			if (versionName == null || versionName.length() <= 0) {
				return "";
			}
		} catch (Exception e) {
			Log.e("VersionInfo", "Exception", e);
		}
		return versionName;
	}

	public static byte[] readFromUri(Context ctx, String uri) {
		try {
			byte[] mContent = readStream(ctx.getContentResolver()
					.openInputStream(Uri.parse(uri)));
			return mContent;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] readStream(InputStream inStream) throws Exception {
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inStream.close();
		return data;
	}

	// 读SD中的文件
	public static byte[] readFileSdcardFile(String fileName) {
		try {
			FileInputStream fin = new FileInputStream(fileName);

			int length = fin.available();

			byte[] data = new byte[length];
			fin.read(data);

			fin.close();
			return data;
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getSDCardPath() {
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}

	public static boolean createDir(String path) {
		File file = new File(path);
		if (!file.exists())
			return file.mkdirs();
		return true;
	}



	public static String encodeByBase64(String content) {
		if (!Utilities.isEmpty(content)) {
			content = Base64.encodeToString(content.getBytes(), Base64.DEFAULT);
			if (content.endsWith("\n")) {
				content = content.substring(0, content.length() - 1);
			}
		}
		return content;
	}


}
