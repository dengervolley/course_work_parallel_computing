package com.coursework.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ArrayUtils {
    public static <T> List<List<T>> nChunks(List<T> list, int numChunks) {
        if (list == null)
            return Collections.emptyList();
        var chunks = new ArrayList<List<T>>(numChunks);
        var pageSize = list.size() / numChunks;
        if (pageSize == 0) {
            chunks.add(list);
            return chunks;
        }
        for (int page = 1; page <= numChunks; page++) {
            var singlePage = list.stream().skip((long) (page - 1) * pageSize).limit(pageSize).toList();
            chunks.add(singlePage);
        }
        return chunks;
    }
}
