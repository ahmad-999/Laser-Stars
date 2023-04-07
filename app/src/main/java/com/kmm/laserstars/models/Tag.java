package com.kmm.laserstars.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class Tag implements Serializable, Comparable<Tag> {
    @SerializedName("id")
    private final int id;
    @SerializedName("name")
    private final String name;
    @SerializedName("genere_id")
    private final int genreId;

    private boolean isChecked;

    public Tag(int id, String name, int genreId, boolean isChecked) {
        this.id = id;
        this.name = name;
        this.genreId = genreId;
        this.isChecked = isChecked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(Tag tag) {
        return this.id - tag.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return id == tag.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public int getGenreId() {
        return genreId;
    }

    public Tag copy() {
        return new Tag(getId(), getName(), getGenreId(), isChecked());
    }
}
