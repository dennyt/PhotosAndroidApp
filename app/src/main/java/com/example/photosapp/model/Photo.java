package com.example.photosapp.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * This is the Photo class, used for creating Photo objects which are contained within Album objects
 * Photos hold their filepath, date, caption, name, and an ArrayList of Strings which operate as tags
 * @author Ethan Walker and Denajah Tarver
 */

public class Photo implements Parcelable, Serializable {

	private Uri filePath;
	private String image_title;
	public ArrayList<String> locationtag = new ArrayList<>();
	public ArrayList<String> persontag = new ArrayList<>();

	public Photo() {

	}

	protected Photo(Parcel in) {
		filePath = in.readParcelable(Uri.class.getClassLoader());
		image_title = in.readString();
		locationtag = in.createStringArrayList();
		persontag = in.createStringArrayList();
	}

	public static final Creator<Photo> CREATOR = new Creator<Photo>() {
		@Override
		public Photo createFromParcel(Parcel in) {
			return new Photo(in);
		}

		@Override
		public Photo[] newArray(int size) {
			return new Photo[size];
		}
	};

	@Override
	public String toString() {
		return this.image_title;
	}

	public String getImage_title() {
		return image_title;
	}

	public void setImage_title(String android_version_name) {
		this.image_title = android_version_name;
	}

	public void setUri (Uri uri) {
		this.filePath = uri;
	}

	public Uri getUri() {
		return this.filePath;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(filePath, flags);
		dest.writeString(image_title);
		dest.writeStringList(locationtag);
		dest.writeStringList(persontag);
	}
}

