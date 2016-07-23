package com.profits.cpa.frag;

import java.lang.reflect.Field;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.johnpersano.supertoasts.SuperCardToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.profits.cpa.Constants;
import com.profits.cpa.JSONHandler;
import com.profits.cpa.MainActivity;
import com.profits.cpa.NotifAdapter;
import com.profits.cpa.R;
import com.profits.cpa.RestClient;
import com.profits.cpa.User;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Settings extends Fragment implements OnRefreshListener {

	NotifAdapter adapter;
	Context mycontxt;
	private RecyclerView recyclerView;
	private RecyclerView.LayoutManager layoutManager;
	MainActivity activity;
	RestClient MyRestClient;
	User UserProfile;
	TextView Name, ID, Credits, Referrals;
	ImageView Country;
	SuperCardToast ProgressToast;
	private AsyncHttpClient client = new AsyncHttpClient();

	private Button mBtnAddRef;
	private SuperCardToast superCardToast;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
			@Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.settings, container, false);
		final ViewGroup containerx = container;
		recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
		recyclerView.setHasFixedSize(true);
		layoutManager = new LinearLayoutManager(this.getActivity());
		recyclerView.setLayoutManager(layoutManager);

		Name = (TextView) v.findViewById(R.id.Username);
		ID = (TextView) v.findViewById(R.id.ID);
		Credits = (TextView) v.findViewById(R.id.Credits);
		Referrals = (TextView) v.findViewById(R.id.Referrals);
		Country = (ImageView) v.findViewById(R.id.country);
		UserProfile = ((MainActivity) this.getActivity()).getUserProfile();
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
		String Email = preferences.getString("Email", "");
		String Password = preferences.getString("Password", "");
		if ((Email != "") && (Password != "")) {
			Profile(Email, Password);
		} else {
			Log.e("Login Error", "No Email & Password available");
		}
		Button LogoutButton = (Button) v.findViewById(R.id.LogoutButton);
		LogoutButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				savePreferences("Email", "");
				savePreferences("Password", "");
				savePreferences("UserProfile", "");
				getActivity().finish();
			}
		});

		Name.setText(UserProfile.getname());
		ID.setText(String.valueOf(UserProfile.getid()));
		Credits.setText(String.valueOf(UserProfile.getcredits()));
		Referrals.setText(String.valueOf(UserProfile.getrefs()));

		int resid = getId(UserProfile.getCountry().toLowerCase(), R.drawable.class);
		Country.setImageResource(resid);

		mBtnAddRef = (Button) v.findViewById(R.id.addRefer);

		mBtnAddRef.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				referalDialog();
			}
		});

		if (UserProfile.getrefs() != 0) {
			Referrals.setVisibility(View.GONE);
			mBtnAddRef.setVisibility(View.VISIBLE);
		} else {
			Referrals.setVisibility(View.VISIBLE);
			mBtnAddRef.setVisibility(View.GONE);
		}

		return v;
	}

	public static final Settings newInstance(Context context) {
		Settings f = new Settings();
		f.mycontxt = context;
		Bundle bdl = new Bundle(1);
		f.setArguments(bdl);

		return f;

	}

	public void Profile(final String email, final String password) {
		RequestParams rp = new RequestParams();
		rp.add("email", email);
		rp.add("password", password);

		client.post(Constants.PROFILE_URL, rp, new JSONHandler(getActivity(), false) {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject json) {

				Log.d("Profile", json.toString());

				try {
					String success = json.getString("success");
					if ("1".equalsIgnoreCase(success)) {

						String id = json.getString("id");
						String name = json.getString("username");
						String country = json.getString("country");
						String credits = json.getString("credits");
						String refs = json.getString("ref_id");

						UserProfile.setcredits(Integer.valueOf(credits));
						((MainActivity) getActivity()).usercredits.setText(String.valueOf(UserProfile.getcredits()));

						Name.setText(name);
						ID.setText(id);
						Credits.setText(credits);
						Referrals.setText(refs);

						if (TextUtils.isEmpty(refs) || "0".equals(refs)) {
							Referrals.setVisibility(View.GONE);
							mBtnAddRef.setVisibility(View.VISIBLE);
						} else {
							Referrals.setVisibility(View.VISIBLE);
							mBtnAddRef.setVisibility(View.GONE);
						}

						int resid = getId(country.toLowerCase(), R.drawable.class);
						Country.setImageResource(resid);

						// Get notification
						JSONArray jsonArray = json.getJSONArray("notifications");

						if (jsonArray.length() == 0) {
							Log.e("Notif List Empty", "No notifications at the moment");
						} else {
							adapter = new NotifAdapter(jsonArray, mycontxt, ((MainActivity) getActivity()));
							recyclerView.setAdapter(adapter);
						}

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
			}
		});
	}

	public static int getId(String resourceName, Class<?> c) {
		try {
			Field idField = c.getDeclaredField(resourceName);
			return idField.getInt(idField);
		} catch (Exception e) {
			throw new RuntimeException("No resource ID found for: " + resourceName + " / " + c, e);
		}
	}

	@Override
	public void onRefresh() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
		String Email = preferences.getString("Email", "");
		String Password = preferences.getString("Password", "");
		if ((Email != "") && (Password != "")) {
			Profile(Email, Password);
		} else {
			Log.e("Login Error", "No Email & Password available");
		}

	}

	private void savePreferences(String key, String value) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public void referalDialog() {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View layout = inflater.inflate(R.layout.dialog_refer, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogCustom);
		final EditText referID = (EditText) layout.findViewById(R.id.refer_id);
		Button btnAddRefer = (Button) layout.findViewById(R.id.btn_add_ref);

		alertDialogBuilder.setView(layout).setCancelable(true);
		final AlertDialog alertDialog = alertDialogBuilder.create();
		btnAddRefer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!referID.getText().toString().matches("")) {
					alertDialog.dismiss();
					ShowProgress(getString(R.string.process_addrefer));
					client.setTimeout(5000);

					RequestParams rp = new RequestParams();
					rp.add("user_id", String.valueOf(UserProfile.getid()));
					rp.add("ref_id", referID.getText().toString());

					client.post(Constants.REFER_URL, rp, new JSONHandler(getActivity(), false) {
						@Override
						public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
							ProgressToast.dismiss();
							if (json != null) {
								Log.d("Setting", json.toString());
								try {
									if (json.getString("status") != null) {
										ShowGood(json.getString("msg"));
									} else {
										ShowError(json.getString("msg"));
									}
								} catch (JSONException e) {
									Log.e(getClass().getCanonicalName(), e.getMessage());
								}
							}
						}

						@Override
						public void onFailure(int statusCode, Header[] headers, String responseString,
								Throwable throwable) {
							super.onFailure(statusCode, headers, responseString, throwable);
							Log.e("Setting", responseString);
						}
					});
				} else {
					ShowError(getString(R.string.Fillfields));
				}
			}
		});

		alertDialog.show();

	}

	public void ShowProgress(String msgId) {
		ProgressToast = new SuperCardToast(this.getActivity(), SuperToast.Type.PROGRESS);
		ProgressToast.setText(msgId);
		ProgressToast.setBackground(SuperToast.Background.BLUE);
		ProgressToast.setTextColor(Color.WHITE);
		ProgressToast.setIndeterminate(true);
		ProgressToast.setProgressIndeterminate(true);
		ProgressToast.setSwipeToDismiss(true);
		ProgressToast.setTouchToDismiss(true);
		ProgressToast.show();

	}

	public void ShowError(String Message) {

		superCardToast = new SuperCardToast(this.getActivity());

		superCardToast.setText(Message);
		superCardToast.setDuration(SuperToast.Duration.EXTRA_LONG);
		superCardToast.setBackground(SuperToast.Background.RED);
		superCardToast.setTextColor(Color.WHITE);
		superCardToast.setSwipeToDismiss(true);
		superCardToast.setTouchToDismiss(true);

		superCardToast.show();
	}

	public void ShowGood(String msgId) {
		superCardToast = new SuperCardToast(this.getActivity(), SuperToast.Type.STANDARD);

		superCardToast.setText(msgId);
		superCardToast.setBackground(SuperToast.Background.GREEN);
		superCardToast.setIcon(SuperToast.Icon.Dark.INFO, SuperToast.IconPosition.LEFT);
		superCardToast.setTextColor(Color.WHITE);
		superCardToast.setIndeterminate(true);
		superCardToast.setTouchToDismiss(true);
		superCardToast.setSwipeToDismiss(true);

		superCardToast.show();
	}

	@Override
	public void onPause() {
		super.onPause();
		if (superCardToast != null) {
			superCardToast.dismiss();
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		if (superCardToast != null) {
			superCardToast.dismiss();
		}
	}
}
