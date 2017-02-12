package com.hoiyen.iats.utils;

import com.hoiyen.iats.models.PostModel;

import java.util.ArrayList;
import java.util.List;

public final class SampleData {

    public static List<PostModel> loadPosts() {
        List<PostModel> posts = new ArrayList<>();
        PostModel post;

        post = new PostModel();
        post.username = "art.devie";
        post.caption = "No matter how good a concept is, it will always be a concept. We could transform your concept and dreams into sketches and visualise it, even before we built it! We brought future to present, to bring present into future. ";
        post.published_at = "05 Feb 2017";
        post.image = "http://indonesiatrimmer.com/001.png";

        posts.add(post);

        post = new PostModel();
        post.username = "art.devie";
        post.caption = "No matter how good a concept is, it will always be a concept. We could transform your concept and dreams into sketches and visualise it, even before we built it! We brought future to present, to bring present into future. ";
        post.published_at = "05 Feb 2017";
        post.image = "http://indonesiatrimmer.com/002.png";
        posts.add(post);

        post = new PostModel();
        post.username = "art.devie";
        post.caption = "Another ADV's Project";
        post.published_at = "06 Feb 2017";
        post.image = "http://indonesiatrimmer.com/003.png";
        posts.add(post);

        post = new PostModel();
        post.username = "Carlex.Design";
        post.caption = "GTR Alcantara";
        post.published_at = "07 Feb 2017";
        post.image = "http://indonesiatrimmer.com/004.jpg";
        posts.add(post);

        return posts;
    }

}
