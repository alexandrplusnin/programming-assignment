package com.assignment.programming.base;

import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

public final class BindingAdapters {

    private BindingAdapters() {
    }

    @BindingAdapter({"app:filePath"})
    public static void setFilePath(ImageView view, String filePath) {
        Glide.with(view.getContext())
                .as(Bitmap.class)
                .load(filePath)
                .into(view);
    }
}
