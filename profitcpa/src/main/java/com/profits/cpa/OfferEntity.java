package com.profits.cpa;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class OfferEntity implements Parcelable {
	private String offerName;
	private String payoutCost;
	private Integer offerID;
	private String fileName;
	private String offerDesc;
	private String bannerUrl;

	public OfferEntity() {

	}

	public OfferEntity(String offerName, String payoutCost, Integer offerID, String fileName, String offerDesc,
			String bannerUrl) {
		this.offerID = offerID;
		this.offerName = offerName;
		this.payoutCost = payoutCost;
		this.setFileName(fileName);
		this.offerDesc = offerDesc;
		this.bannerUrl = bannerUrl;
	}

	public String getOfferName() {
		return offerName;
	}

	public void setOfferName(String offerName) {
		this.offerName = offerName;
	}

	public String getPayoutCost() {
		return payoutCost;
	}

	public void setPayoutCost(String payoutCost) {
		this.payoutCost = payoutCost;
	}

	public Integer getOfferID() {
		return offerID;
	}

	public void setOfferID(Integer offerID) {
		this.offerID = offerID;
	}

	public String getOfferDesc() {
		return offerDesc;
	}

	public void setOfferDesc(String offerDesc) {
		this.offerDesc = offerDesc;
	}

	public String getUrl(Integer userId) {
		return Constants.BASE_URL + "clicks?u_id=" + userId + "&off_id=" + this.offerID;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getBannerUrl() {
		return bannerUrl;
	}

	public void setBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}

	public String getOfferImage() {
		if (!TextUtils.isEmpty(fileName) && !"null".equals(fileName)) {
			return Constants.BASE_URL + "uploads/" + fileName;
		} else {
			return bannerUrl;
		}
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(offerName);
		dest.writeString(payoutCost);
		dest.writeString(fileName);
		dest.writeString(offerDesc);
		dest.writeString(bannerUrl);
		dest.writeInt(offerID);

	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public OfferEntity createFromParcel(Parcel in) {
			return new OfferEntity(in);
		}

		public OfferEntity[] newArray(int size) {
			return new OfferEntity[size];
		}
	};

	public OfferEntity(Parcel in) {
		offerName = in.readString();
		payoutCost = in.readString();
		fileName = in.readString();
		offerDesc = in.readString();
		bannerUrl = in.readString();
		offerID = in.readInt();
	}
}
