package com.santeh.rjhonsl.fishtaordering.Pojo;

/**
 * Created by Globe on 10/3/2016.
 */

public class BLBO_itemPojo {

    private String id;
    private String storeID;
    private String actualItemID;
    private String actualDescription;
    private String actualItemUnit;
    private String actualItemUnitOptions;
    private String actualQty;
    private String actualunits;
    private String expectedQTY;
    private String receivedUnits;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public String getActualItemID() {
        return actualItemID;
    }

    public void setActualItemID(String actualItemID) {
        this.actualItemID = actualItemID;
    }

    public String getActualQty() {
        return actualQty;
    }

    public void setActualQty(String actualQty) {
        this.actualQty = actualQty;
    }

    public String getActualunits() {
        return actualunits;
    }

    public void setActualunits(String actualunits) {
        this.actualunits = actualunits;
    }

    public String getExpectedQTY() {
        return expectedQTY;
    }

    public void setExpectedQTY(String expectedQTY) {
        this.expectedQTY = expectedQTY;
    }

    public String getReceivedUnits() {
        return receivedUnits;
    }

    public void setReceivedUnits(String receivedUnits) {
        this.receivedUnits = receivedUnits;
    }

    public String getActualDescription() {
        return actualDescription;
    }

    public void setActualDescription(String actualDescription) {
        this.actualDescription = actualDescription;
    }

    public String getActualItemUnit() {
        return actualItemUnit;
    }

    public void setActualItemUnit(String actualItemUnit) {
        this.actualItemUnit = actualItemUnit;
    }

    public String getActualItemUnitOptions() {
        return actualItemUnitOptions;
    }

    public void setActualItemUnitOptions(String actualItemUnitOptions) {
        this.actualItemUnitOptions = actualItemUnitOptions;
    }
}
