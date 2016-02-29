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
	 * �������������ҳ��
	 * 
	 * @param total
	 * @return
	 */
	public static long getTotalPage(String total) {
		if (!TextUtils.isEmpty(total)) {
			// �����ܶ���ҳ
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
	 * ����ת������ �� ��
	 * 
	 * @param date
	 * @return
	 */
	public static String dataFomat(String date) {
		if (!TextUtils.isEmpty(date)) {
			String[] split = date.split(" ");
			String[] day = split[0].split("-");
			return day[0] + "��" + day[1] + "��" + day[2] + "��";
		}
		return "";
	}

	/**
	 * ������λ����
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
	 * ����0λ����
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
	 * ��������.
	 * 
	 * @param number
	 *            ԭ��
	 * @param decimal
	 *            ������λС��
	 * @return ����������ֵ
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
	 * ���ת��Ϊȫ�� ˵�������TextView�������Ű�β�������
	 * ��textview�е��ַ�ȫ�ǻ����������е����֡���ĸ�����ȫ��תΪȫ���ַ���ʹ�����뺺��ͬռ�����ֽ� by:liubing
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
	 * ȥ�������ַ����������ı���滻ΪӢ�ı�� ˵�������TextView�������Ű�β������� by:liubing
	 * 
	 * @param str
	 * @return
	 */
	public static String StringFilter(String str) {
		str = str.replaceAll("��", "[").replaceAll("��", "]")
				.replaceAll("��", "!").replaceAll("��", ":").replace("��", ",")
				.replace("��", ".").replace("����", "......");// �滻���ı��
		String regEx = "[����]"; // ����������ַ�
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

	/**
	 * �����Ƿ����
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

	// ����С�������λС��
	public static double Number2(double pDouble) {
		BigDecimal bd = new BigDecimal(pDouble);
		BigDecimal bd1 = bd.setScale(2, bd.ROUND_HALF_UP);
		pDouble = bd1.doubleValue();
		long ll = Double.doubleToLongBits(pDouble);

		return pDouble;
	}

	// �ж�email��ʽ�Ƿ���ȷ
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);

		return m.matches();
	}

	/**
	 * �ֻ�����֤
	 * 
	 * @param str
	 * @return ��֤ͨ������true
	 */
	public static boolean isMobile(String str) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // ��֤�ֻ���
		m = p.matcher(str);
		b = m.matches();
		return b;
	}

	/**
	 * �绰������֤
	 * 
	 * @param str
	 * @return ��֤ͨ������true
	 */
	public static boolean isMobileOrTel(String str) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^[0-9]{7,8}$|^1[3,4,5,7,8][0-9]{9}$"); // ��֤�ֻ���
																	// ��������
		m = p.matcher(str);
		b = m.matches();
		return b;
	}

	public static boolean isPsw(String str) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("\\d{9-16}$"); // ��֤�ֻ���
		m = p.matcher(str);
		b = m.matches();
		return b;
	}

	/**
	 * �绰������֤
	 * 
	 * @param str
	 * @return ��֤ͨ������true
	 */
	public static boolean isPhone(String str) {
		Pattern p1 = null, p2 = null;
		Matcher m = null;
		boolean b = false;
		p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$"); // ��֤�����ŵ�
		p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$"); // ��֤û�����ŵ�
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
	 * �ж��Ƿ�Ϊ�����ַ���
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str) {
		// +��ʾ1����������"3"��"225"����*��ʾ0��������[0-9]*������""��"1"��"22"����?��ʾ0����1��([0-9]?)(��""��"7")
		boolean isNum = str.matches("[0-9]+");
		return isNum;
	}

	public static boolean isChinese(String str) {
		Pattern idNumPat = Pattern.compile("[\u4e00-\u9fa5]+");
		// ͨ��Pattern���Matcher
		Matcher idNumMat = idNumPat.matcher(str);
		return idNumMat.matches();
	}

	/**
	 * ����ַ���Ϊ��
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
	 * �ַ�����������
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
	 * ��ȡGPS��γ��
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
	 * �����·����
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
		// null).show();//��ʾwifi��������״̬
		State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();
		if (mobile == State.CONNECTED)
			return "3G";
		// new
		// AlertDialog.Builder(context).setMessage(mobile.toString()).setPositiveButton("3G",
		// null).show();//��ʾ3G��������״̬
		return "";
	}


	/**
	 * �����ļ�
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

		// ��Ļ�����ĳ���
		DisplayMetrics outMetrics = new DisplayMetrics();
		// ��ȡ����Ļ�Ŀ�͸�
		// manager.getDefaultDisplay().getHeight();
		manager.getDefaultDisplay().getMetrics(outMetrics);// ����������������
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
			// ����
			ctx.startActivity(phoneIntent);
		}
	}

	public static String getCurrentTime(Context context) {
		// ˢ��ʱ��
		String time = DateUtils.formatDateTime(context,
				System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
						| DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL);
		return time;
	}

	/**
	 * �л�����̵�״̬ �統ǰΪ�����Ϊ����,����ǰΪ������Ϊ����
	 */
	public static void toggleInput(Context context) {
		InputMethodManager inputMethodManager = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.toggleSoftInput(0,
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * ������Ƿ񵯳�
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
	 * ���ص�ǰ����汾��
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

	// ��SD�е��ļ�
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
