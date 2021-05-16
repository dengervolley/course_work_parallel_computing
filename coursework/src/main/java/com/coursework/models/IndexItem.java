package com.coursework.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IndexItem {
    private final String value;

    private final List<HashMap<String, List<Integer>>> entries;

    public IndexItem(){
        this(null);
    }

    public IndexItem(String value) {
        this.value = value;
        this.entries = new ArrayList<>();
    }

    public String getValue() {
        return value;
    }

    public List<HashMap<String, List<Integer>>> getEntries() {
        return entries;
    }

    public Integer getTotalCount() {
        int totalCount = 0;
        for (var fileEntry : entries) {
            totalCount += entriesCount(fileEntry);
        }
        return totalCount;
    }


    private <K, V> Integer entriesCount(Map<K, List<V>> map) {
        int count = 0;
        for (var pair : map.entrySet()) {
            count += pair.getValue().size();
        }
        return count;
    }

    @Override
    public String toString() {
        var mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }
}
