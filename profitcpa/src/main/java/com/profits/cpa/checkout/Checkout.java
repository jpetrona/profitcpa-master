package com.profits.cpa.checkout;

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
import com.profits.cpa.R;
import com.profits.cpa.RestClient;
import com.profits.cpa.User;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class Checkout extends Fragment implements OnRefreshListener {
	RestClient MyRestClient;
	Context mycontxt;
	MainActivity activity;
	SuperCardToast ProgressToast;
	boolean ispaypal;
	private AsyncHttpClient client = new AsyncHttpClient();
	private RecyclerView.LayoutManager layoutManager;
	private CheckoutAdapter checkoutAdapter;
	private JSONArray jsonArray;
	User UserProfile;

	private RecyclerView recyclerView;
	private SuperCardToast superCardToast;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
			@Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.checkout, container, false);
		final ViewGroup containerx = container;
		UserProfile = ((MainActivity) this.getActivity()).getUserProfile();
		ProgressToast = new SuperCardToast(this.getActivity(), SuperToast.Type.PROGRESS);
		recyclerView = (RecyclerView) v.findViewById(R.id.checkout_v);
		recyclerView.setHasFixedSize(true);
		layoutManager = new LinearLayoutManager(this.getActivity());
		recyclerView.setLayoutManager(layoutManager);

		recyclerView.addOnItemTouchListener(
				new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
					@Override
					public void onItemClick(View view, int position) {
						if (jsonArray != null) {
							try {
								JSONObject json = jsonArray.getJSONObject(position);
								StartRoutine(json.getString("id"));
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}
				}));

		mycontxt = this.getActivity();
		this.getCheckoutList();

		return v;
	}

	public void StartRoutine(final String id) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View layout = inflater.inflate(R.layout.dialog_paypal, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mycontxt, R.style.AlertDialogCustom);
		final EditText editTextEmailx = (EditText) layout.findViewById(R.id.editTextEmail);
		Button RegisterButtonx = (Button) layout.findViewById(R.id.btn_add_ref);

		alertDialogBuilder.setView(layout).setCancelable(true);
		final AlertDialog alertDialog = alertDialogBuilder.create();
		RegisterButtonx.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (!editTextEmailx.getText().toString().matches("")) {
					alertDialog.dismiss();
					ShowProgress(getString(R.string.Checkout));
					client.setTimeout(5000);

					RequestParams params = new RequestParams("user_id", UserProfile.getid());
					params.add("id", id);
					params.add("account", editTextEmailx.getText().toString());

					client.post(Constants.REDEEM_URL, params, new JSONHandler(getActivity(), false) {
						@Override
						public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
							super.onSuccess(statusCode, headers, json);
							ProgressToast.dismiss();
							if (json != null) {
								Log.d("Checkout", json.toString());
								try {
									if (json.getString("SUCCESS") != null) {
										ShowGood(json.getString("message"));
									} else {
										ShowError(json.getString("message"));
									}
								} catch (JSONException e) {
									Log.e(getClass().getCanonicalName(), e.getMessage());
								}
							}
						}
					});
				} else {
					ShowError(getString(R.string.Fillfields));
				}
			}
		});

		// show it
		alertDialog.show();
	}

	private void getCheckoutList() {
		RequestParams rp = new RequestParams();
		rp.add("country", UserProfile.getCountry());

		client.post(Constants.CHECKOUT_URL, rp, new JSONHandler(this.getActivity(), false) {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
				Log.d("Checkout", json.toString());
				jsonArray = json;
				checkoutAdapter = new CheckoutAdapter(json, mycontxt, ((MainActivity) getActivity()));
				recyclerView.setAdapter(checkoutAdapter);

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
			}
		});
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
	public void onRefresh() {
		this.getCheckoutList();
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
