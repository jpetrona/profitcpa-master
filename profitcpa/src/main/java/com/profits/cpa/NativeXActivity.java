package com.profits.cpa;

import com.nativex.monetization.MonetizationManager;
import com.nativex.monetization.enums.AdEvent;
import com.nativex.monetization.enums.NativeXAdPlacement;
import com.nativex.monetization.listeners.OnAdEventV2;
import com.nativex.monetization.listeners.SessionListener;
import com.nativex.monetization.mraid.AdInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class NativeXActivity extends Activity implements SessionListener, OnAdEventV2 {
	private String appId = "86230";
	User UserProfile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		ProfitCPAApplication.getInstance().trackScreenView("NativeActivity");
		Intent intent = getIntent();
		UserProfile = (User) intent.getSerializableExtra("UserProfile");
	}

	@Override
	protected void onResume() {
		super.onResume();
		MonetizationManager.createSession(getApplicationContext(), appId, String.valueOf(UserProfile.getid()), this);
	}

	@Override
	public void createSessionCompleted(boolean arg0, boolean arg1, String arg2) {
		MonetizationManager.showAd(NativeXActivity.this, NativeXAdPlacement.Game_Launch);
	}

	@Override
	public void onEvent(AdEvent event, AdInfo arg1, String arg2) {
		switch (event) {
		case FETCHED:
			MonetizationManager.showReadyAd(this, NativeXAdPlacement.Game_Launch, this);
			break;
		case NO_AD:
			break;
		case BEFORE_DISPLAY:
			break;
		case DISMISSED:
			MonetizationManager.fetchAd(this, NativeXAdPlacement.Game_Launch, this);
			break;
		case ERROR:
			break;
		default:
			break;
		}
	}
}
