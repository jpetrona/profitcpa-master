package com.profits.cpa;

import android.app.ProgressDialog;
import android.content.Context;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

public class JSONHandler extends JsonHttpResponseHandler {
	private Context context;
	private ProgressDialog myDialog;
	private boolean ishowDialog;

	public JSONHandler(Context context, boolean ishowDialog) {
		this.context = context;
		this.ishowDialog = ishowDialog;
	}

	@Override
	public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
		super.onFailure(statusCode, headers, responseString, throwable);
	}

	public void onFinish() {
		if (this.myDialog != null) {
			this.myDialog.cancel();
		}
		super.onFinish();
	}

	public void onStart() {
		if (this.ishowDialog) {
			this.myDialog = new ProgressDialog(context);
			this.myDialog.setMessage("Loading...");
			this.myDialog.show();
		}
		super.onStart();
	}

	public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
		super.onSuccess(statusCode, headers, response);
	}

	@Override
	public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
		super.onSuccess(statusCode, headers, response);
	}
}
