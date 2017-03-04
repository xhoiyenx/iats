package com.hoiyen.iats.models;

public final class TagModel {

    public String id;
    public String name;

    public TagModel(String name) {
        this.name = name;
    }

    public TagModel(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
