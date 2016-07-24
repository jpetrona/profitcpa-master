package com.profits.cpa;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.johnpersano.supertoasts.SuperCardToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import android.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

public class SplashActivity extends Activity {
	private ProgressBar mProgressLoading;

	SuperCardToast ProgressToast;

	private AsyncHttpClient client = new AsyncHttpClient();
	private Gson gson = new Gson();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

		setContentView(R.layout.activity_splash);

		ProfitCPAApplication.getInstance().trackScreenView("SplashActivity");
		SuperCardToast.onRestoreState(savedInstanceState, SplashActivity.this);

		mProgressLoading = (ProgressBar) findViewById(R.id.progessLoading);

		if (!Utils.isConnected(this)) {
			Toast.makeText(SplashActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
			mProgressLoading.setVisibility(View.GONE);
			return;
		}
		
		/*

		String email = preferences.getString("Email", "");
		if (TextUtils.isEmpty(email)) {
			init();
		} else {
			Intent i = new Intent(SplashActivity.this, MainActivity.class);
			String json = preferences.getString("UserProfile", "");
			User user = gson.fromJson(json, User.class);

			i.putExtra("UserProfile", user);
			startActivity(i);
			finish();
		}
		*/
		if (Build.VERSION.SDK_INT >= 23) {
			// Marshmallow+
			insertDummyContactWrapper();
		} else {
			// Pre-Marshmallow
			init();
		}
	}

	public void init() {

		RequestParams rp = new RequestParams();
		rp.add("android_id", Utils.getAndroidId(getApplicationContext()));
		rp.add("phone_id", Utils.getPhoneId(getApplicationContext()));
		rp.add("os_version", System.getProperty("os.version"));
		rp.add("package_info", getApplicationContext().getPackageName());
		rp.add("model", Build.MODEL);
		rp.add("password", "123456");

		client.post(Constants.LOGIN_URL, rp, new JSONHandler(SplashActivity.this, false) {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
				if (json == null) {
					ShowError("003", getString(R.string.ourerror));
					return;
				}

				Log.d("SplashActivity", json.toString());

				try {
					String success = json.getString("success");
					if ("1".equalsIgnoreCase(success)) {

						String id = json.getString("id");
						String name = json.getString("username");
						String email = json.getString("email");
						String country = json.getString("country");
						String credits = json.getString("credits");
						String refs = json.getString("ref_id");
						String ip = json.getString("ip");

						User UserProfile = new User(Integer.parseInt(id), name, email, Integer.parseInt(credits),
								country, Integer.parseInt(refs), "paypal", 0, ip);
						ProfitCPAApplication.userInfo = UserProfile;
						String json1 = gson.toJson(ProfitCPAApplication.userInfo);
						savePreferences("UserProfile", json1);
						savePreferences("Email", email);
						savePreferences("Password", "123456");

						Intent intent = new Intent(SplashActivity.this, MainActivity.class);
						intent.putExtra("UserProfile", UserProfile);
						startActivity(intent);
						finish();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					ShowError("003", getString(R.string.ourerror));
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
				System.out.println(responseString);
				Log.d("SplashActivity", responseString);
			}
		});
	}

	private void savePreferences(String key, String value) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public void ShowError(String StatusCode, String Message) {
		String ErrorPhrase = Message;
		Log.e("ShowError", ErrorPhrase);
		SuperCardToast superCardToast = new SuperCardToast(SplashActivity.this);
		superCardToast.setText(Message);
		superCardToast.setDuration(SuperToast.Duration.EXTRA_LONG);
		superCardToast.setBackground(SuperToast.Background.RED);
		superCardToast.setTextColor(Color.WHITE);
		superCardToast.setSwipeToDismiss(true);
		superCardToast.show();
	}

	public void ShowGood(String msgId) {
		final SuperCardToast superCardToast = new SuperCardToast(this, SuperToast.Type.STANDARD);
		superCardToast.setText(msgId);
		superCardToast.setBackground(SuperToast.Background.GREEN);
		superCardToast.setIcon(SuperToast.Icon.Dark.INFO, SuperToast.IconPosition.LEFT);
		superCardToast.setTextColor(Color.WHITE);
		superCardToast.setDuration(SuperToast.Duration.LONG);
		superCardToast.setTouchToDismiss(true);
		superCardToast.setSwipeToDismiss(true);
		superCardToast.show();
	}

	final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

	private void insertDummyContactWrapper() {
		int hasWriteContactsPermission = checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE);
		if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
			if (!shouldShowRequestPermissionRationale(android.Manifest.permission.READ_PHONE_STATE)) {
				showMessageOKCancel("You need to allow access to Contacts",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								requestPermissions(new String[] {android.Manifest.permission.READ_PHONE_STATE},
										REQUEST_CODE_ASK_PERMISSIONS);
							}
						});
				return;
			}
			requestPermissions(new String[] {android.Manifest.permission.READ_PHONE_STATE},
					REQUEST_CODE_ASK_PERMISSIONS);
			return;
		}
		init();
	}
	private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
		new AlertDialog.Builder(SplashActivity.this)
				.setMessage(message)
				.setPositiveButton("OK", okListener)
				.setNegativeButton("Cancel", null)
				.create()
				.show();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		switch (requestCode) {
			case REQUEST_CODE_ASK_PERMISSIONS:
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					// Permission Granted
					init();
				} else {
					// Permission Denied
					Toast.makeText(SplashActivity.this, "READ PHONE STATE Denied", Toast.LENGTH_SHORT)
							.show();
				}
				break;
			default:
				super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}
}