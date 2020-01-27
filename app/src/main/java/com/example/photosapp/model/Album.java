package com.example.photosapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.photosapp.model.Photo;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the Album class, used for creating Album objects
 * Photos are contained within Album objects
 * @author Ethan Walker and Denajah Tarver
 */
public class Album implements Comparable, Parcelable, Serializable {

	public String name;
	public ArrayList <Photo> photos =  new ArrayList<>();

	public Album(String n) {
		this.name = n;
	}

	protected Album(Parcel in) {
		name = in.readString();
		in.readTypedList(photos, Photo.CREATOR);
	}

	public static final Creator<Album> CREATOR = new Creator<Album>() {
		@Override
		public Album createFromParcel(Parcel in) {
			return new Album(in);
		}

		@Override
		public Album[] newArray(int size) {
			return new Album[size];
		}
	};

	@Override
	public int compareTo(Object o) {
		Album a = (Album)o;
		return this.name.compareTo(a.name);
	}

	/**
	 * <p>Overridden equals method for the object Album
	 * </p>
	 * @param obj, most likely an Album object
	 * @return boolean, false if objects are not equal, true if they are
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null || (!(obj instanceof Album))) {
			 return false;
		}
		Album other = (Album)obj;
		return this.name.equals(other.name);
	}
	
	/**
	 * <p> Overridden toString method for Album object
	 * </p>
	 * @return a String, which is the name of the Album
	 */
	@Override
	public String toString() { 
	    return this.name;
	} 

	/**
	 * <p> Helper method rename, allows users to edit the names of their albums
	 * </p>
	 * @param s, the string that the user would like to change the album name to
	 */
	public void rename(String s){
		this.name = s;
	}

	public boolean deletePhoto(Photo p){
		return false;
	}


    public String getName() {
        return name;
    }

	public ArrayList<Photo> getPhotos() {
		return photos;
	}
    public int getSize() {
        return photos.size();
    }
    
    public void setName(String name) {
        this.name = name;
    }

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeTypedList(photos);
	}
}
