package com.profits.cpa;

public interface Constants {

	public String BASE_URL = "http://manager.mediagrouplive.com/";
	// public String BASE_URL = "http://192.168.1.230/multiapp/";

	public static String INIT_URL = BASE_URL + "loading";

	/* User Url */
	public static String REGISTER_URL = BASE_URL + "users/regiter";
	public static String LOGIN_URL = BASE_URL + "users/init";
	public static String PROFILE_URL = BASE_URL + "users/profile";
	public static String REFER_URL = BASE_URL + "users/add_refer";

	/* Offer Url */
	public static String OFFER_URL = BASE_URL + "offers/android";

	public static String RANK_URL = BASE_URL + "ranking";

	public static String CHECKOUT_URL = BASE_URL + "giftcard/json";

	public static String REDEEM_URL = BASE_URL + "withdraw/create";
}
