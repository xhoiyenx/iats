package com.hoiyen.iats.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class CartModel {

    public String id;
    public String image;
    public int price;
    public String brand, article, color, formattedPrice, quantity;

    public static List<CartModel> parseJSON(JSONArray data) {
        List<CartModel> models = new ArrayList<>();

        if (data.length() > 0) {
            for (int i = 0; i < data.length(); i++) {
                JSONObject item = data.optJSONObject(i);
                CartModel model = new CartModel();
                model.id = item.optString("id");
                model.image = item.optString("image");
                model.brand = item.optString("brand");
                model.article = item.optString("article");
                model.color = item.optString("color");
                model.price = item.optInt("price");
                model.quantity = item.optString("quantity");
                model.formattedPrice = item.optString("formatted");

                models.add(model);
            }
        }

        return models;
    }

    public String getName() {
        return this.brand.concat(" ").concat(this.article).concat(" ").concat(this.color);
    }
}
