package com.coursework.models;

import java.util.List;
import java.util.Objects;

public final class FileEntry {
    private  String fileName;
    private  List<Integer> positions;
    private  Integer totalCount;

    public FileEntry(){}

    public FileEntry(String fileName, List<Integer> positions,
                     Integer totalCount) {
        this.fileName = fileName;
        this.positions = positions;
        this.totalCount = totalCount;
    }

    public String fileName() {
        return fileName;
    }

    public List<Integer> positions() {
        return positions;
    }

    public Integer totalCount() {
        return totalCount;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (FileEntry) obj;
        return Objects.equals(this.fileName, that.fileName) &&
                Objects.equals(this.positions, that.positions) &&
                Objects.equals(this.totalCount, that.totalCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName, positions, totalCount);
    }

    @Override
    public String toString() {
        return "FileEntry[" +
                "fileName=" + fileName + ", " +
                "positions=" + positions + ", " +
                "totalCount=" + totalCount + ']';
    }

}
