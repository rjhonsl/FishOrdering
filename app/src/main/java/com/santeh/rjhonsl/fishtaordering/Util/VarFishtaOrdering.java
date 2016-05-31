package com.santeh.rjhonsl.fishtaordering.Util;

import android.app.Application;

/**
 * Created by rjhonsl on 8/12/2015.
 */
public class VarFishtaOrdering extends Application {


    private String item_id;
    private String item_code;
    private String item_description;
    private String item_oldcode;

    private String order_unit;
    private String order_qty;
    private String order_code;
    private String order_description;

    /**
     * GETTER AND SETTER
    **/


    public String getItem_oldcode() {
        return item_oldcode;
    }

    public void setItem_oldcode(String item_oldcode) {
        this.item_oldcode = item_oldcode;
    }

    public String getItem_description() {
        return item_description;
    }

    public void setItem_description(String item_description) {
        this.item_description = item_description;
    }

    public String getItem_code() {
        return item_code;
    }

    public void setItem_code(String item_code) {
        this.item_code = item_code;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getOrder_unit() {
        return order_unit;
    }

    public void setOrder_unit(String order_unit) {
        this.order_unit = order_unit;
    }

    public String getOrder_qty() {
        return order_qty;
    }

    public void setOrder_qty(String order_qty) {
        this.order_qty = order_qty;
    }

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    public String getOrder_description() {
        return order_description;
    }

    public void setOrder_description(String order_description) {
        this.order_description = order_description;
    }
}
