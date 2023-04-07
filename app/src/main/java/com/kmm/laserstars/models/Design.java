package com.kmm.laserstars.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Design implements Serializable {
    @SerializedName("id")
    private final int id;
    @SerializedName("name")
    private final String name;
    @SerializedName("desc")
    private final String desc;
    @SerializedName("url")
    private final String url;
    @SerializedName("video")
    private final String videoURL;
    @SerializedName("created_at")
    private final String createdAt;
    @SerializedName("owner")
    private final User user;
    @SerializedName("tags")
    private final ArrayList<Tag> tags;

    public Design(int id, String name, String desc, String url, String videoURL,
                  String createdAt, User user, ArrayList<Tag> tags) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.url = url;
        this.videoURL = videoURL;
        this.createdAt = createdAt;
        this.user = user;
        this.tags = tags;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getUrl() {
        return url;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getOwner() {
        return user;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public String getVideoURL() {
        return videoURL;
    }
}
