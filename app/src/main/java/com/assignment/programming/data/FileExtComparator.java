package com.assignment.programming.data;

import java.util.Comparator;

public final class FileExtComparator implements Comparator<FileModel> {
    @Override
    public int compare(FileModel o1, FileModel o2) {
        return o1.getExt().compareTo(o2.getExt());
    }
}
