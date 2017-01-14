package com.hoiyen.iats.models;

import org.json.JSONObject;

public final class UserModel {

    public String userid;
    public String username;
    public String usermail;
    public String usercell;
    public String token;
    public String fcmtoken;

    public static UserModel parseJSON(JSONObject json) {
        UserModel user = new UserModel();
        user.userid = json.optString("id");
        user.username = json.optString("username");
        user.usermail = json.optString("usermail");
        user.usercell = json.optString("usercell");
        user.token = json.optString("token");
        return user;
    }

}
