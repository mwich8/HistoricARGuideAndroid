package com.tum.historicarguide;

/**
 * Created by Mati on 01.04.2017.
 */

public class Location {

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
}
