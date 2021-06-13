package com.assignment.programming.ui.file_list;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.assignment.programming.R;
import com.assignment.programming.data.FileModel;
import com.assignment.programming.databinding.ItemListFileBinding;
import com.assignment.programming.placeholder.PlaceholderContent.PlaceholderItem;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ProgrammingAssignmentRecyclerViewAdapter extends RecyclerView.Adapter<ProgrammingAssignmentRecyclerViewAdapter.ViewHolder> {

    private final List<FileModel> itemList = new ArrayList<>();
    @NonNull
    private String query = "";

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemListFileBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final FileModel fileModel = itemList.get(position);
        holder.mItem = fileModel;
        final String name = fileModel.getName();
        SpannableStringBuilder builder = new SpannableStringBuilder(name);
        if (!TextUtils.isEmpty(query) && !TextUtils.isEmpty(name)) {
            final int queryStart = name.toLowerCase().indexOf(query.toLowerCase());
            if (queryStart >= 0) {
                builder.setSpan(
                        new ForegroundColorSpan(
                                ContextCompat.getColor(holder.itemView.getContext(), R.color.purple_200)),
                        queryStart,
                        queryStart + query.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );
            }
        }
        holder.mIdView.setText(builder);
        holder.mContentView.setText(fileModel.getPath());
        holder.itemView.setOnClickListener(v -> {
            final Bundle bundle = new Bundle();
            bundle.putString("filePath", fileModel.getPath());
            Navigation.findNavController(holder.itemView)
                    .navigate(
                            R.id.action_fileListFragment_to_fileItemFragment,
                            bundle
                    );
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setData(List<FileModel> itemList) {
        synchronized (this) {
            this.itemList.clear();
            this.itemList.addAll(itemList);
            notifyDataSetChanged();
        }
    }

    public void setQuery(@NonNull String query) {
        this.query = query;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public FileModel mItem;

        public ViewHolder(ItemListFileBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mContentView = binding.content;
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
