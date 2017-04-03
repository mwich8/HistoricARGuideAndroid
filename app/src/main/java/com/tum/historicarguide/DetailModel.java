package com.tum.historicarguide;

/**
 * Created by Mati on 03.04.2017.
 */

public class DetailModel {

    private String key;
    private String value;

    public DetailModel(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString(){
        String output = "Key: " + key + ", Value: " + value;
        return output;
    }
}
