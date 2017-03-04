package com.hoiyen.iats.models;


import java.io.Serializable;
import java.util.HashMap;

public final class CartItemModel implements Serializable {

    public String product_id, product_detail_id;
    public String unit_type;
    public String brand, article, color;
    public String code;
    public HashMap<String, Double> unit_codes;

    public double quantity;
    public int price;
    public String subtotal;

}
