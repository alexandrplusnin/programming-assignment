package com.assignment.programming.data.models;

import androidx.core.util.ObjectsCompat;

import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public final class DetailedFileModel extends FileModel {

    private static final String[] SIZE_UNITS = new String[]{"B", "kB", "MB", "GB", "TB"};
    private static final String[] IMAGE_EXTENSION = new String[]{"jpeg", "jpg", "png"};
    private final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
    private final long length;
    private final long created;

    public DetailedFileModel(File file, long created) {
        super(file);
        this.length = file.length();
        this.created = created;
    }

    public String getFormattedLength() {
        if (length <= 0) {
            return "0";
        }
        int digitGroups = (int) (Math.log10(length) / Math.log10(1024));
        return new DecimalFormat("#,##0.#")
                .format(length / Math.pow(1024, digitGroups)) + " " + SIZE_UNITS[digitGroups];
    }

    public String getFormattedModificationDate() {
        return dateFormat.format(getLastModified());
    }

    public String getFormattedCreationDate() {
        return dateFormat.format(created);
    }

    public boolean isImage() {
        for (String ext : IMAGE_EXTENSION) {
            if (ObjectsCompat.equals(ext, getExt().toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
