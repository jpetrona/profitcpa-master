package com.profits.cpa.frag;

import java.util.ArrayList;
import java.util.List;

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
import com.profits.cpa.OfferAdapter;
import com.profits.cpa.OfferEntity;
import com.profits.cpa.R;
import com.profits.cpa.User;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Home extends Fragment implements OnRefreshListener {
	private RecyclerView recyclerView;
	private RecyclerView.LayoutManager layoutManager;
	SuperCardToast ProgressToast;
	String userip, userid, completed, extraparams;
	String OffersUrl;
	OfferAdapter adapter;
	User UserProfile;
	Context mycontxt;
	MainActivity activity;

	private SharedPreferences preferences;

	private ArrayList<OfferEntity> offerList;

	private AsyncHttpClient client = new AsyncHttpClient();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
			@Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.home, container, false);
		final ViewGroup containerx = container;
		client.setTimeout(10000);

		UserProfile = ((MainActivity) this.getActivity()).getUserProfile();
		ProgressToast = new SuperCardToast(this.getActivity(), SuperToast.Type.PROGRESS);
		recyclerView = (RecyclerView) v.findViewById(R.id.recycler_v);
		recyclerView.setHasFixedSize(true);
		layoutManager = new LinearLayoutManager(this.getActivity());
		recyclerView.setLayoutManager(layoutManager);
		mycontxt = getActivity().getApplicationContext();
		userip = UserProfile.getip();
		userid = String.valueOf(UserProfile.getid());
		ShowProgress(getString(R.string.OfferString));

		offerList = new ArrayList<OfferEntity>();

		offerList.add(new OfferEntity());
		offerList.add(new OfferEntity());

		preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());

		getOffer();

		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	private void getOffer() {
		RequestParams rp = new RequestParams();
		client.post(Constants.OFFER_URL, rp, new JSONHandler(this.getActivity(), false) {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONArray json) {

				if (ProgressToast.isShowing()) {
					ProgressToast.dismiss();
				}
				if (json != null) {

					OfferEntity offerEntity = null;
					for (int i = 0; i < json.length(); i++) {
						try {
							JSONObject row = json.getJSONObject(i);
							offerEntity = new OfferEntity(row.getString("offer_name"), row.getString("payout"),
									Integer.valueOf(row.getString("offer_id")), row.getString("offer_file"),
									row.getString("offer_desc"), row.getString("banner_url"));
							offerList.add(offerEntity);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}

				adapter = new OfferAdapter(offerList, mycontxt, ((MainActivity) getActivity()));
				recyclerView.setAdapter(adapter);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
			}
		});
	}

	@Override
	public void onRefresh() {
		getOffer();
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

		SuperCardToast superCardToast = new SuperCardToast(this.getActivity());
		superCardToast.setText(Message);
		superCardToast.setDuration(SuperToast.Duration.EXTRA_LONG);
		superCardToast.setBackground(SuperToast.Background.RED);
		superCardToast.setTextColor(Color.WHITE);
		superCardToast.setSwipeToDismiss(true);
		superCardToast.setTouchToDismiss(true);
		superCardToast.show();
	}

	public void ShowGood(String msgId) {
		final SuperCardToast superCardToast = new SuperCardToast(this.getActivity(), SuperToast.Type.STANDARD);
		superCardToast.setText(msgId);
		superCardToast.setBackground(SuperToast.Background.GREEN);
		superCardToast.setIcon(SuperToast.Icon.Dark.INFO, SuperToast.IconPosition.LEFT);
		superCardToast.setTextColor(Color.WHITE);
		superCardToast.setDuration(SuperToast.Duration.LONG);
		superCardToast.setTouchToDismiss(true);
		superCardToast.show();
	}
}
