package com.coursework.server.responses;

import com.coursework.models.IndexItem;

public class FoundTermResponse {
    private String jsonIndexItem;

    public String getIndexItem(){
        return this.jsonIndexItem;
    }

    public void setIndexItem(IndexItem indexItem){
        this.jsonIndexItem = indexItem.toString();
    }

    public FoundTermResponse(IndexItem indexItem){
        this.jsonIndexItem = indexItem == null ? "No entries found" : indexItem.toString();
    }

    public FoundTermResponse(){

    }

}
