package com.coursework.models;

import java.util.List;

public record FileEntry(String fileName, List<Integer> positions,
                        Integer totalCount) {
}
