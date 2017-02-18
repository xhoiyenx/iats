package com.hoiyen.iats.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public final class ProductModel implements Serializable {

    public String id;
    public String brand;
    public String price;
    public String short_desc;
    public String description;
    public String unit_type;
    public List<ProductMediaModel> media;

    public static ProductModel parseJSON(JSONObject data) {

        ProductModel product = new ProductModel();
        product.id = data.optString("id");
        product.brand = data.optString("brand");
        product.price = data.optString("price");
        product.short_desc = data.optString("short_desc");
        product.description = data.optString("description", "");
        product.unit_type = data.optString("unit_type", "");

        JSONArray medias = data.optJSONArray("media");
        if (medias.length() > 0) {
            product.media = new ArrayList<>(medias.length());
            for (int i = 0; i < medias.length(); i++) {
                ProductMediaModel media = ProductMediaModel.parseJSON(medias.optJSONObject(i));
                product.media.add(media);
            }
        }

        return product;

    }

    public static List<ProductModel> parseJSON(JSONArray data) {

        if (data.length() > 0) {
            List<ProductModel> models = new ArrayList<>();
            for (int i = 0; i < data.length(); i++) {
                ProductModel product = ProductModel.parseJSON(data.optJSONObject(i));
                models.add(product);
            }
            return models;
        }

        return null;

    }

    public String formattedPrice() {
        return "Rp ".concat(price).concat(",-");
    }

}
