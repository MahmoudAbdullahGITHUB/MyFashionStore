package com.example.myfashionstore.data;

public class ImageModel {
    private String iName;
    private String iUrl;

    public ImageModel() {
    }

    public ImageModel(String iName, String iUrl) {
        if(iName.trim().equals("")){
            iName="No Name";
        }
        this.iName = iName;
        this.iUrl = iUrl;
    }

    public String getName() {
        return iName;
    }

    public void setName(String iName) {
        this.iName = iName;
    }

    public String getUrl() {
        return iUrl;
    }

    public void setUrl(String iUrl) {
        this.iUrl = iUrl;
    }
}
