package com.santeh.rjhonsl.fishtaordering.Util;

import android.app.Application;

/**
 * Created by rjhonsl on 8/12/2015.
 */
public class VarFishtaOrdering extends Application {


    private String item_id;
    private String item_class;
    private String item_code;
    private String item_description;
    private String item_oldcode;
    private String item_units;
    private String item_isActive;

    private String order_unit;
    private String order_qty;
    private String order_code;
    private String order_description;

    private String hst_id;
    private String hst_timesent;
    private String hst_sendTo;
    private String hst_message;
    private String hst_isMultipart;
    private String hst_isSent;
    private String hst_isStillSending;

    private String set_servernum;
    private String set_increCount;
    private String set_storeName;
    private String set_setPin;
    private String set_id;

    private String oi_id;
    private String oi_itemid;
    private String oi_orderid;

    private String cust_id;
    private String cust_code;
    private String cust_name;
    private String cust_type;
    private String cust_isactive;
    private String cust_AssignedStores;

    private String usr_id;
    private String usr_fname;
    private String usr_lname;
    private String usr_deviceid;
    private String usr_devicename;
    private String usr_isactive;
    private String usr_username;
    private String usr_password;
    private String usr_assignedCust;

    private String ds_skey;
    private String ds_value;

    private String ap_id;
    private String ap_custidFk;
    private String ap_prodsarray;

    public VarFishtaOrdering() {
    }


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

    public String getItem_units() {
        return item_units;
    }

    public void setItem_units(String item_units) {
        this.item_units = item_units;
    }

    public String getHst_id() {
        return hst_id;
    }

    public void setHst_id(String hst_id) {
        this.hst_id = hst_id;
    }

    public String getHst_sendTo() {
        return hst_sendTo;
    }

    public void setHst_sendTo(String hst_sendTo) {
        this.hst_sendTo = hst_sendTo;
    }

    public String getHst_message() {
        return hst_message;
    }

    public void setHst_message(String hst_message) {
        this.hst_message = hst_message;
    }

    public String getHst_isMultipart() {
        return hst_isMultipart;
    }

    public void setHst_isMultipart(String hst_isMultipart) {
        this.hst_isMultipart = hst_isMultipart;
    }

    public String getHst_isSent() {
        return hst_isSent;
    }

    public void setHst_isSent(String hst_isSent) {
        this.hst_isSent = hst_isSent;
    }

    public String getHst_isStillSending() {
        return hst_isStillSending;
    }

    public void setHst_isStillSending(String hst_isStillSending) {
        this.hst_isStillSending = hst_isStillSending;
    }

    public String getHst_timesent() {
        return hst_timesent;
    }

    public void setHst_timesent(String hst_timesent) {
        this.hst_timesent = hst_timesent;
    }

    public String getSet_servernum() {
        return set_servernum;
    }

    public void setSet_servernum(String set_servernum) {
        this.set_servernum = set_servernum;
    }

    public String getSet_increCount() {
        return set_increCount;
    }

    public void setSet_increCount(String set_increCount) {
        this.set_increCount = set_increCount;
    }

    public String getSet_storeName() {
        return set_storeName;
    }

    public void setSet_storeName(String set_storeName) {
        this.set_storeName = set_storeName;
    }

    public String getSet_setPin() {
        return set_setPin;
    }

    public void setSet_setPin(String set_setPin) {
        this.set_setPin = set_setPin;
    }

    public String getSet_id() {
        return set_id;
    }

    public void setSet_id(String set_id) {
        this.set_id = set_id;
    }

    public String getOi_id() {
        return oi_id;
    }

    public void setOi_id(String oi_id) {
        this.oi_id = oi_id;
    }

    public String getOi_itemid() {
        return oi_itemid;
    }

    public void setOi_itemid(String oi_itemid) {
        this.oi_itemid = oi_itemid;
    }

    public String getOi_orderid() {
        return oi_orderid;
    }

    public void setOi_orderid(String oi_orderid) {
        this.oi_orderid = oi_orderid;
    }

    public String getCust_id() {
        return cust_id;
    }

    public void setCust_id(String cust_id) {
        this.cust_id = cust_id;
    }

    public String getCust_code() {
        return cust_code;
    }

    public void setCust_code(String cust_code) {
        this.cust_code = cust_code;
    }

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    public String getCust_type() {
        return cust_type;
    }

    public void setCust_type(String cust_type) {
        this.cust_type = cust_type;
    }

    public String getCust_isactive() {
        return cust_isactive;
    }

    public void setCust_isactive(String cust_isactive) {
        this.cust_isactive = cust_isactive;
    }

    public String getItem_isActive() {
        return item_isActive;
    }

    public void setItem_isActive(String item_isActive) {
        this.item_isActive = item_isActive;
    }

    public String getUsr_id() {
        return usr_id;
    }

    public void setUsr_id(String usr_id) {
        this.usr_id = usr_id;
    }

    public String getUsr_fname() {
        return usr_fname;
    }

    public void setUsr_fname(String usr_fname) {
        this.usr_fname = usr_fname;
    }

    public String getUsr_deviceid() {
        return usr_deviceid;
    }

    public void setUsr_deviceid(String usr_deviceid) {
        this.usr_deviceid = usr_deviceid;
    }

    public String getUsr_lname() {
        return usr_lname;
    }

    public void setUsr_lname(String usr_lname) {
        this.usr_lname = usr_lname;
    }

    public String getUsr_devicename() {
        return usr_devicename;
    }

    public void setUsr_devicename(String usr_devicename) {
        this.usr_devicename = usr_devicename;
    }

    public String getUsr_isactive() {
        return usr_isactive;
    }

    public void setUsr_isactive(String usr_isactive) {
        this.usr_isactive = usr_isactive;
    }

    public String getUsr_username() {
        return usr_username;
    }

    public void setUsr_username(String usr_username) {
        this.usr_username = usr_username;
    }

    public String getUsr_password() {
        return usr_password;
    }

    public void setUsr_password(String usr_password) {
        this.usr_password = usr_password;
    }

    public String getUsr_assignedCust() {
        return usr_assignedCust;
    }

    public void setUsr_assignedCust(String usr_assignedCust) {
        this.usr_assignedCust = usr_assignedCust;
    }

    public String getAp_id() {
        return ap_id;
    }

    public void setAp_id(String ap_id) {
        this.ap_id = ap_id;
    }

    public String getAp_custidFk() {
        return ap_custidFk;
    }

    public void setAp_custidFk(String ap_custidFk) {
        this.ap_custidFk = ap_custidFk;
    }

    public String getAp_prodsarray() {
        return ap_prodsarray;
    }

    public void setAp_prodsarray(String ap_prodsarray) {
        this.ap_prodsarray = ap_prodsarray;
    }

    public String getCust_AssignedStores() {
        return cust_AssignedStores;
    }

    public void setCust_assignedItems(String cust_AssignedStores) {
        this.cust_AssignedStores = cust_AssignedStores;
    }

    public String getDs_skey() {
        return ds_skey;
    }

    public void setDs_skey(String ds_skey) {
        this.ds_skey = ds_skey;
    }

    public String getDs_value() {
        return ds_value;
    }

    public void setDs_value(String ds_value) {
        this.ds_value = ds_value;
    }

    public String getItem_class() {
        return item_class;
    }

    public void setItem_class(String item_class) {
        this.item_class = item_class;
    }
}
