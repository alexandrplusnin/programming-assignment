package com.assignment.programming.data;

import java.util.Comparator;

public class FileDateComparator implements Comparator<FileModel> {
    @Override
    public int compare(FileModel o1, FileModel o2) {
        return Long.compare(o1.getLastModified(), o2.getLastModified());
    }
}
