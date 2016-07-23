package com.profits.cpa;

import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.android.AndroidLog;
import retrofit.client.Response;

public class RestClient implements Serializable {
	RestAdapter restAdapter;
	public RestRequest APICONTROL;
	private static String cookies;
	Response finalresponse;

	public RestClient() {
		restAdapter = new RestAdapter.Builder().setLogLevel(LogLevel.FULL).setLog(new AndroidLog("Retrofit Tag"))
				.setEndpoint(Constants.BASE_URL) // call your base url
				.setRequestInterceptor(COOKIES_REQUEST_INTERCEPTOR).build();

		APICONTROL = restAdapter.create(RestRequest.class);

	}

	public static String getCookies() {
		return cookies;
	}

	public static void setCookies(String cookies) {
		RestClient.cookies = cookies;
	}

	private static final RequestInterceptor COOKIES_REQUEST_INTERCEPTOR = new RequestInterceptor() {
		@Override
		public void intercept(RequestFacade request) {
			if (null != cookies && cookies.length() > 0) {
				request.addHeader("Cookie", cookies);
			}
		}
	};

	public String StatusParser(String in) {
		String status = null;
		if (isJSONValid(in)) {

			JSONObject reader;
			try {
				reader = new JSONObject(in);

				JSONObject response = reader.getJSONObject("request");
				status = response.getString("statuscode");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return status;
		} else {
			return null;
		}
	}

	public String MessageParser(String in) {
		String status = null;
		if (isJSONValid(in)) {

			JSONObject reader;
			try {
				reader = new JSONObject(in);

				JSONObject response = reader.getJSONObject("request");
				status = response.getString("message");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return status;
		} else {
			return null;
		}
	}

	public String XParser(String in, String fieldname) {
		String status = null;
		if (isJSONValid(in)) {

			JSONObject reader;
			try {
				reader = new JSONObject(in);

				JSONObject response = reader.getJSONObject("request");
				status = response.getString(fieldname);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return status;
		} else {
			return null;
		}
	}

	public String YParser(String in, String fieldname) {
		String status = null;
		if (isJSONValid(in)) {

			JSONObject reader;
			try {
				reader = new JSONObject(in);

				// JSONObject response = reader.getJSONObject("request");
				status = reader.getString(fieldname);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return status;
		} else {
			return null;
		}
	}

	public boolean isJSONValid(String test) {
		try {
			new JSONObject(test);
		} catch (JSONException ex) {
			// edited, to include @Arthur's comment
			// e.g. in case JSONArray is valid as well...
			try {
				new JSONArray(test);
			} catch (JSONException ex1) {
				return false;
			}
		}
		return true;
	}

}
