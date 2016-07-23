package com.profits.cpa;

import java.io.Serializable;
import java.lang.reflect.Field;

public class User implements Serializable {
	int id;
	String name;
	String email;
	int credits;
	String country;
	int countrypic;
	int refs;
	String paypal;
	int notifs;
	String ip;

	public User(int idx, String namex, String emailx, int creditx, String countryx, int refsx, String paypalx,
			int notifsx, String ipx) {

		id = idx;
		name = namex;
		email = emailx;
		credits = creditx;
		country = countryx;
		refs = refsx;
		paypal = paypalx;
		notifs = notifsx;
		ip = ipx;
	}

	public int getid() {
		return this.id;
	}

	public void setid(int idx) {
		this.id = idx;
	}

	public String getip() {
		return this.ip;
	}

	public void setip(String ipx) {
		this.ip = ipx;
	}

	public int getrefs() {
		return this.refs;
	}

	public void setrefs(int refsx) {
		this.refs = refsx;
	}

	public String getpaypal() {
		return this.paypal;
	}

	public void setpaypal(String paypalx) {
		this.paypal = paypalx;
	}

	public String getname() {
		return this.name;
	}

	public void setname(String namex) {
		this.name = namex;
	}

	public String getemail() {
		return this.email;
	}

	public void setemail(String emailx) {
		this.email = emailx;
	}

	public void setcountry(String countryx) {
		this.country = countryx;
	}
	
	public String getCountry(){
		return country;
	}

	public int getcredits() {
		return this.credits;
	}

	public void setcredits(int creditx) {
		this.credits = creditx;
	}

	public int getnotifs() {
		return this.notifs;
	}

	public void setnotifs(int notifsx) {
		this.notifs = notifsx;
	}

	public int getcountrypic() {
		int resid;
		if (this.country.toLowerCase() == "do") {
			resid = getId("dx", R.drawable.class);

		} else {
			resid = getId(this.country.toLowerCase(), R.drawable.class);

		}
		return resid;
	}

	public static int getId(String resourceName, Class<?> c) {
		try {
			Field idField = c.getDeclaredField(resourceName);
			return idField.getInt(idField);
		} catch (Exception e) {
			throw new RuntimeException("No resource ID found for: " + resourceName + " / " + c, e);
		}
	}
}
