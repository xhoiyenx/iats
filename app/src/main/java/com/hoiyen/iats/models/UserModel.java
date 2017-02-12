package com.hoiyen.iats.models;

import org.json.JSONObject;

import java.io.Serializable;

public final class UserModel implements Serializable {

    public String userid;
    public String username;
    public String usermail;
    public String usercell;
    public String apitoken;
    public String fcmtoken;
    public String avatar_url;

    public static UserModel parseJSON(JSONObject json) {
        UserModel user = new UserModel();
        user.userid = json.optString("id");
        user.username = json.optString("username");
        user.usermail = json.optString("usermail");
        user.usercell = json.optString("usercell");
        user.apitoken = json.optString("api_token");
        user.avatar_url = json.optString("avatar_url");
        return user;
    }

}
