package com.santeh.rjhonsl.fishtaordering.Pojo;

/**
 * Created by Globe on 10/3/2016.
 */

public class BLBO_recordPojo {

    private String id;
    private String BLBO_ID;
    private String sender;
    private String content;
    private String timeSent;
    private String custID;
    private String arrangedItem;
    private String isSent;
    private String batchNumber;
    private String isRead;
    private String type;
    private String timeOpened;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getAllitems() {
        return allitems;
    }

    public void setAllitems(String allitems) {
        this.allitems = allitems;
    }

    private String allitems;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
