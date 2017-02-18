package com.hoiyen.iats.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class ProductUnitModel {

    public String product_id;
    public String product_detail_id;
    public String product_unit_id;
    public String base_price;
    public String price;
    public Double size;
    public String code;
    public String unit_type;

    public static ProductUnitModel parseJSON(JSONObject data) {

        ProductUnitModel model = new ProductUnitModel();
        model.code = data.optString("code");
        model.size = data.optDouble("size");
        model.price = data.optString("total_price");
        model.base_price = data.optString("base_price");

        return model;

    }

    public static List<ProductUnitModel> parseJSON(JSONArray data) {

        List<ProductUnitModel> models = new ArrayList<>();
        if (data.length() > 0) {
            for (int i = 0; i < data.length(); i++) {
                models.add(parseJSON(data.optJSONObject(i)));
            }
        }

        return models;

    }

}
