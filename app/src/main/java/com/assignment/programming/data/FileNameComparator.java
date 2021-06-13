package com.assignment.programming.data;

import java.util.Comparator;

public final class FileNameComparator implements Comparator<FileModel> {
    @Override
    public int compare(FileModel o1, FileModel o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
