package com.coursework.persistence;

import com.coursework.indices.InverseIndex;

public interface IPersistenceProvider {
    void persistIndex(InverseIndex index);
}
