package com.profits.cpa.rank;

import org.apache.http.Header;
import org.json.JSONArray;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.profits.cpa.Constants;
import com.profits.cpa.JSONHandler;
import com.profits.cpa.MainActivity;
import com.profits.cpa.R;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Ranking extends Fragment implements OnRefreshListener {
	Context mycontxt;
	MainActivity activity;
	RankAdapter adapter;
	private RecyclerView recyclerView;
	private RecyclerView.LayoutManager layoutManager;

	private AsyncHttpClient client = new AsyncHttpClient();

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
			@Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.ranking, container, false);
		final ViewGroup containerx = container;
		mycontxt = getActivity().getApplicationContext();
		recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
		recyclerView.setHasFixedSize(true);
		layoutManager = new LinearLayoutManager(this.getActivity());
		recyclerView.setLayoutManager(layoutManager);
		GetRank();
		return v;
	}

	public void GetRank() {
		RequestParams rp = new RequestParams();

		client.post(Constants.RANK_URL, rp, new JSONHandler(this.getActivity(), false) {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONArray json) {

				adapter = new RankAdapter(json, mycontxt, ((MainActivity) getActivity()));
				recyclerView.setAdapter(adapter);

				Log.d("Rank", json.toString());
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
			}
		});
	}

	@Override
	public void onRefresh() {
		GetRank();
	}
}