package com.profits.cpa;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;
import com.github.johnpersano.supertoasts.SuperCardToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.OnClickWrapper;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

	Toolbar toolbar;
	MyPageAdapter pageAdapter;

	int Numboftabs = 4;
	RestClient MyRestClient;
	User UserProfile;
	public TextView usercredits;
	ViewPager pager;
	private SyncHttpClient client = new SyncHttpClient();
	private Timer timer;

	private AHBottomNavigation bottomNavigation;
	private ArrayList<AHBottomNavigationItem> bottomNavigationItems = new ArrayList<>();
	private int[] tabColors;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		SuperCardToast.onRestoreState(savedInstanceState, this);

		ProfitCPAApplication.getInstance().trackScreenView("MainActivity");

		Intent intent = getIntent();
		UserProfile = (User) intent.getSerializableExtra("UserProfile");

		MyRestClient = new RestClient();

		CharSequence Titles[] = getResources().getStringArray(R.array.tabs);
		pageAdapter = new MyPageAdapter(getSupportFragmentManager(), Titles, Numboftabs);

		pager = (ViewPager) findViewById(R.id.viewpager);
		pager.setOffscreenPageLimit(5);
		usercredits = (TextView) findViewById(R.id.Credits);
		usercredits.setText(String.valueOf(UserProfile.getcredits()));
		pager.setAdapter(pageAdapter);
		initUI();

		/*SlidingTabLayout tabs = (SlidingTabLayout) findViewById(R.id.tabs);
		tabs.setDistributeEvenly(true);

		tabs.setCustomTabView(R.layout.tab_layout, R.id.tabtext);
		tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
			@Override
			public int getIndicatorColor(int position) {
				return getResources().getColor(R.color.tabselector);
			}
		});

		tabs.setViewPager(pager);*/

		if (UserProfile.getnotifs() > 0) {
			LaunchNotif(UserProfile.getnotifs());
		}

		timer = new Timer();

		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				RequestParams rp = new RequestParams();
				rp.add("email", UserProfile.getemail());
				rp.add("password", "123456");

				client.post(Constants.PROFILE_URL, rp, new JSONHandler(MainActivity.this, false) {
					@Override
					public void onSuccess(int statusCode, Header[] headers, JSONObject json) {

						Log.d("Load point", json.toString());

						try {
							String success = json.getString("success");
							if ("1".equalsIgnoreCase(success)) {
								final String credits = json.getString("credits");
								runOnUiThread(new Runnable() {

									@Override
									public void run() {
										if (credits != null) {
											UserProfile.setcredits(Integer.valueOf(credits));
											usercredits.setText(credits);
										}
									}
								});

							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers, String responseString,
							Throwable throwable) {
						super.onFailure(statusCode, headers, responseString, throwable);
					}
				});
			}
		}, 5000, 5000);
	}

	public void LaunchNotif(int numberofnotifs) {
		SuperCardToast superCardToast = new SuperCardToast(MainActivity.this, SuperToast.Type.BUTTON);
		superCardToast.setBackground(SuperToast.Background.ORANGE);
		superCardToast.setIndeterminate(true);
		superCardToast.setTextColor(Color.WHITE);
		superCardToast.setText(getString(R.string.NewNotifications1) + " " + String.valueOf(numberofnotifs) + " "
				+ getString(R.string.NewNotifications2));
		superCardToast.setButtonIcon(SuperToast.Icon.Dark.UNDO, getString(R.string.shownotifs));
		OnClickWrapper onClickWrapper = new OnClickWrapper("supercardtoast", new SuperToast.OnClickListener() {
			@Override
			public void onClick(View view, Parcelable token) {
				pager.setCurrentItem(3);
			}
		});

		superCardToast.setOnClickWrapper(onClickWrapper);
		superCardToast.show();
	}

	public User getUserProfile() {
		return UserProfile;
	}

	@Override
	protected void onStop() {
		super.onStop();
		timer.cancel();
	}



	private void initUI() {

		bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);


			AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.tab_1, R.drawable.mycreditdark, R.color.color_tab_1);
			AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.tab_2, R.drawable.mycheckoutdark, R.color.color_tab_2);
			AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.tab_3, R.drawable.myrankingdark, R.color.color_tab_3);
			AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.tab_4, R.drawable.mysettingdark, R.color.color_tab_4);

			bottomNavigationItems.add(item1);
			bottomNavigationItems.add(item2);
			bottomNavigationItems.add(item3);
			bottomNavigationItems.add(item4);

			bottomNavigation.addItems(bottomNavigationItems);

		bottomNavigation.setAccentColor(Color.parseColor("#FFFFFF"));
		bottomNavigation.setInactiveColor(Color.parseColor("#747474"));

		bottomNavigation.setColored(true);
		bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
			@Override
			public boolean onTabSelected(int position, boolean wasSelected) {

				/*if (currentFragment == null) {
					currentFragment = adapter.getCurrentFragment();
				}

				if (wasSelected) {
					currentFragment.refresh();
					return true;
				}

				if (currentFragment != null) {
					currentFragment.willBeHidden();
				}*/

				pager.setCurrentItem(position, false);
				/*currentFragment = adapter.getCurrentFragment();
				currentFragment.willBeDisplayed();

				if (position == 1) {
					bottomNavigation.setNotification("", 1);

					floatingActionButton.setVisibility(View.VISIBLE);
					floatingActionButton.setAlpha(0f);
					floatingActionButton.setScaleX(0f);
					floatingActionButton.setScaleY(0f);
					floatingActionButton.animate()
							.alpha(1)
							.scaleX(1)
							.scaleY(1)
							.setDuration(300)
							.setInterpolator(new OvershootInterpolator())
							.setListener(new Animator.AnimatorListener() {
								@Override
								public void onAnimationStart(Animator animation) {

								}

								@Override
								public void onAnimationEnd(Animator animation) {
									floatingActionButton.animate()
											.setInterpolator(new LinearOutSlowInInterpolator())
											.start();
								}

								@Override
								public void onAnimationCancel(Animator animation) {

								}

								@Override
								public void onAnimationRepeat(Animator animation) {

								}
							})
							.start();

				} else {
					if (floatingActionButton.getVisibility() == View.VISIBLE) {
						floatingActionButton.animate()
								.alpha(0)
								.scaleX(0)
								.scaleY(0)
								.setDuration(300)
								.setInterpolator(new LinearOutSlowInInterpolator())
								.setListener(new Animator.AnimatorListener() {
									@Override
									public void onAnimationStart(Animator animation) {

									}

									@Override
									public void onAnimationEnd(Animator animation) {
										floatingActionButton.setVisibility(View.GONE);
									}

									@Override
									public void onAnimationCancel(Animator animation) {
										floatingActionButton.setVisibility(View.GONE);
									}

									@Override
									public void onAnimationRepeat(Animator animation) {

									}
								})
								.start();
					}
				}*/

				return true;
			}
		});

		bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
			@Override public void onPositionChange(int y) {
				Log.d("DemoActivity", "BottomNavigation Position: " + y);
			}
		});

		pager.setOffscreenPageLimit(4);
//		adapter = new DemoViewPagerAdapter(getSupportFragmentManager());
//		pager.setAdapter(adapter);

//		currentFragment = adapter.getCurrentFragment();

		/*final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				bottomNavigation.setNotification("16", 1);
				Snackbar.make(bottomNavigation, "Snackbar with bottom navigation",
						Snackbar.LENGTH_SHORT).show();
			}
		}, 3000);*/
	}

}
