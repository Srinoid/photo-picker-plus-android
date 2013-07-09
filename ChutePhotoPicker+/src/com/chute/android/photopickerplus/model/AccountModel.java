package com.chute.android.photopickerplus.model;

import com.chute.sdk.v2.model.UserModel;
import com.fasterxml.jackson.annotation.JsonProperty;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * The {@link AccountModel} class represents the concept of an account. Each
 * account contains type, name, user ID, status and which user it belongs to.
 * 
 */
public class AccountModel implements Parcelable {

	@SuppressWarnings("unused")
	private static final String TAG = AccountModel.class.getSimpleName();

	/**
	 * The unique identifier of the account.
	 */
	@JsonProperty("id")
	private String id;
	/**
	 * Type of the account. It can be: Facebook, Twitter, Flickr, Instagram,
	 * Picasa etc.
	 */
	@JsonProperty("type")
	private String type;
	/**
	 * Account name.
	 */
	@JsonProperty("name")
	private String name;
	/**
	 * Account user unique identifier.
	 */
	@JsonProperty("uid")
	private String uid;
	/**
	 * Status of the account.
	 */
	@JsonProperty("status")
	private int status;
	/**
	 * Flag indicating if account notifications are enabled or no.
	 */
	@JsonProperty("notifications_enabled")
	private boolean notificationsEnabled;
	/**
	 * The {@link GCUserModel} which the account belongs to.
	 */
	@JsonProperty("user")
	public UserModel user = new UserModel();

	/**
	 * Default non-args constructor.
	 */
	public AccountModel() {
		super();
	}

	/**
	 * Constructor using fields.
	 * 
	 * @param id
	 * @param type
	 * @param name
	 * @param uid
	 * @param status
	 * @param notificationsEnabled
	 * @param user
	 */
	public AccountModel(String id, String type, String name, String uid, int status, boolean notificationsEnabled,
			UserModel user) {
		super();
		this.id = id;
		this.type = type;
		this.name = name;
		this.uid = uid;
		this.status = status;
		this.notificationsEnabled = notificationsEnabled;
		this.user = user;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public boolean isNotificationsEnabled() {
		return notificationsEnabled;
	}

	public void setNotificationsEnabled(boolean notificationsEnabled) {
		this.notificationsEnabled = notificationsEnabled;
	}

	public UserModel getUser() {
		return user;
	}

	public void setUser(UserModel user) {
		this.user = user;
	}

	public AccountModel(Parcel in) {
		id = in.readString();
		type = in.readString();
		name = in.readString();
		uid = in.readString();
		status = in.readInt();
		notificationsEnabled = in.readInt() == 1;
		user = in.readParcelable(UserModel.class.getClassLoader());
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
		dest.writeString(type);
		dest.writeString(name);
		dest.writeString(uid);
		dest.writeInt(status);
		dest.writeInt(notificationsEnabled ? 1 : 0);
		dest.writeParcelable(user, flags);
	}

	public static final Parcelable.Creator<AccountModel> CREATOR = new Parcelable.Creator<AccountModel>() {

		@Override
		public AccountModel createFromParcel(Parcel in) {
			return new AccountModel(in);
		}

		@Override
		public AccountModel[] newArray(int size) {
			return new AccountModel[size];
		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GCAccountModel [id=" + id + ", type=" + type + ", name=" + name + ", uid=" + uid + ", status=" + status
				+ ", notificationsEnabled=" + notificationsEnabled + ", user=" + user + "]";
	}

}
