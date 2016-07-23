package com.profits.cpa;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

import com.google.gson.JsonElement;

public interface RestRequest {

	@POST("/email")
	public void reset(@Query("email") String username, Callback<JsonElement> calback);

	@POST("/checkout")
	public void Checkout(@Query("cost") String cost, @Query("dollars") String dollars, @Query("id") String userid,
			@Query("paypal") String paypal, Callback<JsonElement> calback);

	@GET("/ranking")
	public void GetRank(Callback<JsonElement> calback);

	@POST("/profile")
	public void profile(@Query("email") String username, @Query("password") String password,
			Callback<JsonElement> calback);

	@POST("/login")
	public void login(@Query("email") String username, @Query("password") String password,
			Callback<JsonElement> calback);

}
