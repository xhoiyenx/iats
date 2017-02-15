package com.hoiyen.iats.models;
public final class CommentModel {

    public String username;
    public String avatar;
    public String comment;
    public String date;

    public CommentModel(String username, String avatar, String comment, String date) {
        this.username = username;
        this.avatar = avatar;
        this.comment = comment;
        this.date = date;
    }
}
