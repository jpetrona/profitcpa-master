package com.profits.cpa;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

public class Utils {

	public static String getPhoneId(Context context) {
		return ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
	}

	public static boolean isConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean NisConnected = activeNetwork != null && activeNetwork.isConnected();
		if (NisConnected) {
			if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
				return true;
			} else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
				return true;
			else
				return false;
		}
		return false;
	}

	public static String getAndroidId(Context context) {
		return Settings.Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
	}
}
