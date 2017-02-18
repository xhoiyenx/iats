package com.hoiyen.iats.models;

import org.json.JSONObject;

import java.io.Serializable;

public final class ProductMediaModel implements Serializable {

    public String type;
    public String name;
    public String path;

    public static ProductMediaModel parseJSON(JSONObject data) {
        ProductMediaModel media = new ProductMediaModel();
        media.type = data.optString("type");
        media.name = data.optString("name");
        media.path = data.optString("path");

        return media;
    }

    public String getThumb() {
        if (type.equals("image")) {
            return path;
        }
        // youtube video thumbnail
        else {
            return "http://img.youtube.com/vi/".concat(name).concat("/sddefault.jpg");
        }
    }

}
