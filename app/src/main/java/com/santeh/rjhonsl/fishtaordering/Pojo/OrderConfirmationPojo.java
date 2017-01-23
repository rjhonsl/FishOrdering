package com.santeh.rjhonsl.fishtaordering.Pojo;

/**
 * Created by Globe on 10/3/2016.
 */

public class OrderConfirmationPojo {

    private String id;
    private String ORnumber;
    private String sender;
    private String content;
    private String timeReceived;
    private String custID;
    private String arrangedItem;
    private String isSent;
    private String batchNumber;
    private String items;
    private String allitems;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getORnumber() {
        return ORnumber;
    }

    public void setORnumber(String ORnumber) {
        this.ORnumber = ORnumber;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimeReceived() {
        return timeReceived;
    }

    public void setTimeReceived(String timeReceived) {
        this.timeReceived = timeReceived;
    }

    public String getCustID() {
        return custID;
    }

    public void setCustID(String custID) {
        this.custID = custID;
    }

    public String getIsSent() {
        return isSent;
    }

    public void setIsSent(String isSent) {
        this.isSent = isSent;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getArrangedItem() {
        return arrangedItem;
    }

    public void setArrangedItem(String arrangedItem) {
        this.arrangedItem = arrangedItem;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getAllitems() {
        return allitems;
    }

    public void setAllitems(String allitems) {
        this.allitems = allitems;
    }
}
