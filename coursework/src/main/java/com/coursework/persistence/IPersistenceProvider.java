package com.coursework.persistence;

import com.coursework.indices.InverseIndex;
import com.coursework.models.IndexItem;

import java.util.HashMap;
import java.util.List;

public interface IPersistenceProvider {
    void persistIndex(InverseIndex index);

    void setPath(String path);

    public HashMap<String, IndexItem> readIndex(String path);
}
