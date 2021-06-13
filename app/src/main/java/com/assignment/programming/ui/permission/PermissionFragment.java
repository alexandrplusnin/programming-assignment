package com.assignment.programming.ui.permission;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.assignment.programming.R;
import com.assignment.programming.utils.UriUtils;

public class PermissionFragment extends Fragment {

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    navigateToFileList();
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_permission, container, false);
        view.findViewById(R.id.permission_grant_access)
                .setOnClickListener(v -> requestPermission());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                navigateToFileList();
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED) {
                navigateToFileList();
            }
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            startActivity(
                    new Intent(
                            Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                            UriUtils.getAppUri(requireContext())
                    )
            );
        } else {
            requestPermissionLauncher.launch(
                    Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    private void navigateToFileList() {
        final View view = getView();
        if (view != null) {
            Navigation.findNavController(view).navigate(R.id.action_permissionFragment_to_fileListFragment);
        }
    }
}
