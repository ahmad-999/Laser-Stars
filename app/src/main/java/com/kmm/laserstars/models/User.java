package com.kmm.laserstars.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {
    @SerializedName("id")
    private final int id;
    @SerializedName("name")
    private final String name;
    @SerializedName("phone")
    private final String phone;
    @SerializedName("avatar")
    private final String avatar;
    @SerializedName("created_at")
    private final String createdAt;
    @SerializedName("type")
    private final UserType type;
    @SerializedName("group")
    private final Group group;
    @SerializedName("token")
    private final String token;


    public User(int id, String name, String phone,
                String avatar, String createdAt,
                UserType type, Group group, String token) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.avatar = avatar;
        this.createdAt = createdAt;
        this.type = type;
        this.group = group;
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public UserType getType() {
        return type;
    }

    public Group getGroup() {
        return group;
    }

    public String getToken() {
        return token;
    }
}
