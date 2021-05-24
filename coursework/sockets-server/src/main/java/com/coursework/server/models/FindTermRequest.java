package com.coursework.server.models;

public class FindTermRequest {
    private String searchTerm;

    public String getSearchTerm() {
        return this.searchTerm;
    }

    public String setSearchTerm() {
        return this.searchTerm;
    }

    public FindTermRequest() {

    }

    public FindTermRequest(String s){
        this.searchTerm = s;
    }
}
