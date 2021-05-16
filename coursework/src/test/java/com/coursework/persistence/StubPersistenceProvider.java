package com.coursework.persistence;

import com.coursework.indices.InverseIndex;
import com.coursework.models.IndexItem;

import java.util.ArrayList;
import java.util.List;

public class StubPersistenceProvider implements IPersistenceProvider {
    @Override
    public void persistIndex(InverseIndex index) {
        System.out.println(index.toString());
    }

    @Override
    public void setPath(String path) {
    }

    @Override
    public List<IndexItem> readIndex(String path) {
        return new ArrayList<>();
    }

}
