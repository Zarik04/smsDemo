package com.example.smsdemo.models.courseSources;

import java.io.File;

public class Attachment {
    private String ID, resourceSectionID, text;
    private File file;
    private double size;

    public Attachment() {
    }

    public String getID() {
        String result = ID;
        while (result.length()<7){
            result = "0"+result;
        }
        return result;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getResourceSectionID() {
        return resourceSectionID;
    }

    public void setResourceSectionID(String resourceSectionID) {
        this.resourceSectionID = resourceSectionID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }
}
