package com.chute.android.photopickerplus.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * The {@link AccountMediaModel} class represents the concept of a media item
 * for a specific object that belongs to an account. Each media item contains
 * URL, thumbnail URL and large image URL.
 * 
 */
public class AccountMediaModel implements Parcelable {

	@SuppressWarnings("unused")
	private static final String TAG = AccountMediaModel.class.getSimpleName();

	/**
	 * The unique identifier of the media item.
	 */
	@JsonProperty("id")
	private String id;
	/**
	 * The URL that shows the location of the thumbnail.
	 */
	@JsonProperty("thumbUrl")
	private String thumbUrl;
	/**
	 * The URL that shows the location of the large image.
	 */
	private String largeUrl;
	/**
	 * The URl that shows the location of the media item.
	 */
	private String url;

	/**
	 * Photo dimensions
	 */
	private String dimensions;

	/**
	 * Default non-args constructor.
	 */
	public AccountMediaModel() {
		super();
	}

	/**
	 * Getter and setter methods.
	 */
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getThumbUrl() {
		return thumbUrl;
	}

	public void setThumbUrl(String thumbUrl) {
		this.thumbUrl = thumbUrl;
	}

	public String getLargeUrl() {
		return largeUrl;
	}

	public void setLargeUrl(String largeUrl) {
		this.largeUrl = largeUrl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDimensions() {
		return dimensions;
	}

	public void setDimensions(String dimensions) {
		this.dimensions = dimensions;
	}

	public AccountMediaModel(Parcel in) {
		id = in.readString();
		thumbUrl = in.readString();
		largeUrl = in.readString();
		url = in.readString();
		dimensions = in.readString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.Parcelable#describeContents()
	 */
	@Override
	public int describeContents() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(thumbUrl);
		dest.writeString(largeUrl);
		dest.writeString(url);
		dest.writeString(dimensions);
	}

	public static final Parcelable.Creator<AccountMediaModel> CREATOR = new Parcelable.Creator<AccountMediaModel>() {

		@Override
		public AccountMediaModel createFromParcel(Parcel in) {
			return new AccountMediaModel(in);
		}

		@Override
		public AccountMediaModel[] newArray(int size) {
			return new AccountMediaModel[size];
		}

	};


	@Override
	public String toString() {
		return "GCAccountMediaModel [id=" + id + ", thumbUrl=" + thumbUrl
				+ ", largeUrl=" + largeUrl + ", url=" + url + ", dimensions="
				+ dimensions + "]";
	}

}
