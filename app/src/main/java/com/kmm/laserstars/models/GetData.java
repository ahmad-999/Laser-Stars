package com.kmm.laserstars.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class GetData<T extends Serializable> implements Serializable {
    @SerializedName("code")
    private final int code;
    @SerializedName("msg")
    private final String msg;
    @SerializedName(value = "data", alternate = {"designs",
            "token", "tags", "me",
            "distributors", "user",
            "distributor", "genres",
            "tagGenre", "tag"})
    private T data;

    @SerializedName("errors")
    private final ArrayList<Errors> errors;

    public GetData(int code, String msg, T data, ArrayList<Errors> errors) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.errors = errors;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ArrayList<Errors> getErrors() {
        return errors;
    }
}
