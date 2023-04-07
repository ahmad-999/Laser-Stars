package com.kmm.laserstars.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Errors implements Serializable {
    @SerializedName("errors")
    private final ArrayList<String>errors;
    @SerializedName("key")
    private final String key;

    public Errors(ArrayList<String> errors, String key) {
        this.errors = errors;
        this.key = key;
    }

    public ArrayList<String> getErrors() {
        return errors;
    }

    public String getKey() {
        return key;
    }
}
