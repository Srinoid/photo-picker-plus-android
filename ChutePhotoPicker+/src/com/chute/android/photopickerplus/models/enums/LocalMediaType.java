package com.chute.android.photopickerplus.models.enums;

public enum LocalMediaType {
	CAMERA_SHOTS("camera shots"), ALL_PHOTOS("all photos"), LAST_PHOTO_TAKEN("last photo taken"), TAKE_PHOTO(
			"take photo");

	private final String name;

	private LocalMediaType(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	};

	public String getName() {
		return name;
	}
}