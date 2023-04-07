package com.kmm.laserstars.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Group implements Serializable {
    @SerializedName("id")
    private final int id;
    @SerializedName("desc")
    private final String desc;
    @SerializedName("name")
    private final String name;
    @SerializedName("desgins")
    private final ArrayList<Design>designs;

    public Group(int id, String desc, String name, ArrayList<Design> designs) {
        this.id = id;
        this.desc = desc;
        this.name = name;
        this.designs = designs;
    }

    public int getId() {
        return id;
    }

    public String getDesc() {
        return desc;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Design> getDesigns() {
        return designs;
    }
}
