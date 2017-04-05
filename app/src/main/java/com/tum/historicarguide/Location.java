package com.tum.historicarguide;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Mati on 01.04.2017.
 */

public class Location implements Parcelable {

    private long id;
    private String name;
    private float latitude;
    private float longitude;
    private float orientation;
    private float middleArchWidth;
    private float middleArchHeight;
    private float zenithX;
    private float zenithY;
    private boolean gateClosed;

    /**
     * Standard basic constructor for non-parcel
     * object creation
     */
    public Location(long id, String name, float latitude, float longitude, float orientation, float middleArchWidth, float middleArchHeight, float zenithX, float zenithY, boolean gateClosed) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.orientation = orientation;
        this.middleArchWidth = middleArchWidth;
        this.middleArchHeight = middleArchHeight;
        this.zenithX = zenithX;
        this.zenithY = zenithY;
        this.gateClosed = gateClosed;
    }

    /**
     *
     * Constructor to use when re-constructing object
     * from a parcel
     *
     * @param in a parcel from which to read this object
     */
    public Location(Parcel in) {
        readFromParcel(in);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getOrientation() {
        return orientation;
    }

    public void setOrientation(float orientation) {
        this.orientation = orientation;
    }

    public float getMiddleArchWidth() {
        return middleArchWidth;
    }

    public void setMiddleArchWidth(float middleArchWidth) {
        this.middleArchWidth = middleArchWidth;
    }

    public float getMiddleArchHeight() {
        return middleArchHeight;
    }

    public void setMiddleArchHeight(float middleArchHeight) {
        this.middleArchHeight = middleArchHeight;
    }

    public float getZenithX() {
        return zenithX;
    }

    public void setZenithX(float zenithX) {
        this.zenithX = zenithX;
    }

    public float getZenithY() {
        return zenithY;
    }

    public void setZenithY(float zenithY) {
        this.zenithY = zenithY;
    }

    public boolean isGateClosed() {
        return gateClosed;
    }

    public void setGateClosed(boolean gateClosed) {
        this.gateClosed = gateClosed;
    }

    @Override
    public String toString(){
        String output = id + ": " + name + " is located at LAT " + latitude + " and LON " + longitude;
        return output;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        // We just need to write each field into the
        // parcel. When we read from parcel, they
        // will come back in the same order
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeFloat(latitude);
        dest.writeFloat(longitude);
        dest.writeFloat(orientation);
        dest.writeFloat(middleArchWidth);
        dest.writeFloat(middleArchHeight);
        dest.writeFloat(zenithX);
        dest.writeFloat(zenithY);
        if (gateClosed) {
            dest.writeInt(1);
        } else {
            dest.writeInt(0);
        }
    }

    /**
     *
     * Called from the constructor to create this
     * object from a parcel.
     *
     * @param in parcel from which to re-create object
     */
    private void readFromParcel(Parcel in) {

        // We just need to read back each
        // field in the order that it was
        // written to the parcel
        id = in.readLong();
        name = in.readString();
        latitude = in.readFloat();
        longitude = in.readFloat();
        orientation = in.readFloat();
        middleArchWidth = in.readFloat();
        middleArchHeight = in.readFloat();
        zenithX = in.readFloat();
        zenithY = in.readFloat();
        int gateIsClosed = in.readInt();
        if (gateIsClosed == 1) {
            gateClosed = true;
        } else {
            gateClosed = false;
        }
    }

    /**
     *
     * This field is needed for Android to be able to
     * create new objects, individually or as arrays.
     *
     * This also means that you can use use the default
     * constructor to create the object and use another
     * method to hyrdate it as necessary.
     *
     * I just find it easier to use the constructor.
     * It makes sense for the way my brain thinks ;-)
     *
     */
    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public Location createFromParcel(Parcel in) {
                    return new Location(in);
                }

                public Location[] newArray(int size) {
                    return new Location[size];
                }
            };
}
