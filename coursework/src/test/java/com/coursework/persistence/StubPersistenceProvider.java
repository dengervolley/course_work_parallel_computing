package com.coursework.persistence;

import com.coursework.indices.InverseIndex;

public class StubPersistenceProvider implements IPersistenceProvider{
    @Override
    public void persistIndex(InverseIndex index) {
        System.out.println(index.toString());
    }
}
