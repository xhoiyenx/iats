package com.hoiyen.iats.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class ChatModel {

    public String member_id;
    public String message;
    public String time;
    public String date;
    public String avatar;
    public int type = 0;

    public static List<ChatModel> parseJSON(JSONArray data) {
        List<ChatModel> models = new ArrayList<>();
        if (data != null && data.length() > 0) {
            for (int i = 0; i < data.length(); i++) {
                JSONObject item = data.optJSONObject(i);
                ChatModel model = parseJSON(item);
                models.add(model);
            }
        }
        return models;
    }

    public static ChatModel parseJSON(JSONObject item) {
        ChatModel model = new ChatModel();
        model.member_id = item.optString("member_id");
        model.message = item.optString("message");
        model.avatar = item.optString("avatar");
        model.type = item.optInt("type");
        model.date = item.optString("date");
        model.time = item.optString("time");
        return model;
    }

}