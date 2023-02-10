package com.example.smsdemo.models;

import java.io.File;
import java.util.ArrayList;

public class Assignment {

    private String ID, topic;
    private ArrayList<File> attachments;

    public Assignment(){

    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public ArrayList<File> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<File> attachments) {
        this.attachments = attachments;
    }


}
