package com.santeh.rjhonsl.fishtaordering.Pojo;

/**
 * Created by Globe on 10/3/2016.
 */

public class DeliveryConfirmationPojo {

    private String id;
    private String drNumber;
    private String sender;
    private String items_received;
    private String items_sent;
    private String content_received;
    private String content_sent ;
    private String timeReceived ;
    private String timeSent ;
    private String allitems;
    private String batchnumber;
    private String custID;
    private String isSent ;
    private String isRead ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDrNumber() {
        return drNumber;
    }

    public void setDrNumber(String drNumber) {
        this.drNumber = drNumber;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getItems_received() {
        return items_received;
    }

    public void setItems_received(String items_received) {
        this.items_received = items_received;
    }

    public String getItems_sent() {
        return items_sent;
    }

    public void setItems_sent(String items_sent) {
        this.items_sent = items_sent;
    }

    public String getContent_received() {
        return content_received;
    }

    public void setContent_received(String content_received) {
        this.content_received = content_received;
    }

    public String getContent_sent() {
        return content_sent;
    }

    public void setContent_sent(String content_sent) {
        this.content_sent = content_sent;
    }

    public String getTimeReceived() {
        return timeReceived;
    }

    public void setTimeReceived(String timeReceived) {
        this.timeReceived = timeReceived;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(String timeSent) {
        this.timeSent = timeSent;
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

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public String getBatchnumber() {
        return batchnumber;
    }

    public void setBatchnumber(String batchnumber) {
        this.batchnumber = batchnumber;
    }

    public String getAllitems() {
        return allitems;
    }

    public void setAllitems(String allitems) {
        this.allitems = allitems;
    }
}
