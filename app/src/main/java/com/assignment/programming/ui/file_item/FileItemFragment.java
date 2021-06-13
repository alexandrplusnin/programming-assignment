package com.assignment.programming.ui.file_item;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.assignment.programming.R;
import com.assignment.programming.databinding.FragmentFileItemBinding;

public class FileItemFragment extends Fragment {

    private FileItemViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentFileItemBinding viewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_file_item, container, false);
        viewBinding.setLifecycleOwner(getViewLifecycleOwner());
        viewBinding.setViewModel(viewModel);
        return viewBinding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle arguments = getArguments();
        if (arguments != null) {
            String filePath = arguments.getString("filePath");
            viewModel = new ViewModelProvider(this)
                    .get(FileItemViewModel.class);
            viewModel.setFilePath(filePath);
        } else {
            throw new IllegalStateException("Invalid arguments");
        }
    }
}
