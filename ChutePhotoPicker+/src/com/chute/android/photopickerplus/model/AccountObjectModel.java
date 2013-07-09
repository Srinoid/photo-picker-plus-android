package com.chute.android.photopickerplus.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * {@link AccountObjectModel} class represents the concept of an object for a
 * specific account. Each object contains ID and name.
 * 
 */
public class AccountObjectModel implements Parcelable {

	@SuppressWarnings("unused")
	private static final String TAG = AccountObjectModel.class.getSimpleName();

	/**
	 * The unique identifier of the object.
	 */
	@JsonProperty("id")
	private String id;
	/**
	 * Name of the object.
	 */
	@JsonProperty("name")
	private String name;

	/**
	 * Default non-args constructor.
	 */
	public AccountObjectModel() {
		super();
	}

	/**
	 * Constructor using fields.
	 * 
	 * @param id
	 * @param name
	 */
	public AccountObjectModel(String id, String name) {
		super();
		this.id = id;
		this.name = name;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AccountObjectModel(Parcel in) {
		id = in.readString();
		name = in.readString();
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
		dest.writeString(name);
	}

	public static final Parcelable.Creator<AccountObjectModel> CREATOR = new Parcelable.Creator<AccountObjectModel>() {

		@Override
		public AccountObjectModel createFromParcel(Parcel in) {
			return new AccountObjectModel(in);
		}

		@Override
		public AccountObjectModel[] newArray(int size) {
			return new AccountObjectModel[size];
		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GCAccountObjectModel [id=" + id + ", name=" + name + "]";
	}

}
