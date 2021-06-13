package com.assignment.programming.utils;

import android.content.Context;
import android.net.Uri;

public final class UriUtils {
    private UriUtils() {
    }

    public static Uri getAppUri(Context context) {
        return Uri.fromParts("package", context.getPackageName(), null);
    }
}
