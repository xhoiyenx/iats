package com.hoiyen.iats.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class PostModel {
    public String post_id;
    public String username;
    public String avatar;
    public String location;
    public String published_at;
    public String caption;
    public String image;
    public int like_count, liked, comments_count;
    public List<TagModel> tagModels;

    public static List<PostModel> parseList(JSONArray data) {

        if (data.length() == 0) {
            return null;
        }

        JSONObject item;
        PostModel post;
        final List<PostModel> posts = new ArrayList<>(data.length());

        for (int i = 0; i < data.length(); i++) {
            item = data.optJSONObject(i);
            post = new PostModel();
            post.post_id = item.optString("post_id");
            post.username = item.optString("user");
            post.avatar = item.optString("user_avatar");
            post.caption = item.optString("caption");
            post.location = item.optString("location");
            post.published_at = item.optString("created_at");
            post.image = item.optString("image_url");
            post.like_count = item.optInt("like_count");
            post.liked = item.optInt("liked");
            post.comments_count = item.optInt("comments_count");

            // create tags
            post.tagModels = new ArrayList<>();
            JSONArray tags = item.optJSONArray("tags");
            if (tags.length() > 0) {
                for (int j = 0; j < tags.length(); j++) {
                    JSONObject tag = tags.optJSONObject(j);
                    post.tagModels.add(new TagModel(tag.optString("tag_id"), tag.optString("description")));
                }
            }

            posts.add(post);
        }

        return posts;
    }
}
