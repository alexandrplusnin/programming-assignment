package com.assignment.programming.ui.file_list;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.assignment.programming.R;
import com.assignment.programming.databinding.FragmentFileListBinding;
import com.assignment.programming.utils.NotificationHelper;

/**
 * A fragment representing a list of Items.
 */
public class FileListFragment extends Fragment {

    @NonNull
    private final Observer<Integer> counterObserver = counter -> {
        if (counter >= 0 && !isResumed()) {
            NotificationHelper.createMatchesFoundNotification(
                    requireContext(),
                    getString(R.string.programming_assignment_matches_found, counter)
            );
        }
    };
    @NonNull
    private final ProgrammingAssignmentRecyclerViewAdapter adapter = new ProgrammingAssignmentRecyclerViewAdapter();
    @Nullable
    private MenuItem oldSelectedItem = null;

    private FileListViewModel viewModel;
    @NonNull
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    viewModel.saveResultsToFile();
                }
            });
    private FragmentFileListBinding viewBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this)
                .get(FileListViewModel.class);
        viewModel.getCountLiveData()
                .observeForever(counterObserver);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        viewModel.getFileLiveData()
                .observe(
                        getViewLifecycleOwner(),
                        adapter::setData
                );
        viewModel.getCountLiveData()
                .observe(
                        getViewLifecycleOwner(),
                        counter -> {
                            if (counter >= 0) {
                                viewBinding.fileListCounter.setText(getString(R.string.programming_assignment_matches_found, counter));
                            }
                        }
                );
        viewModel.getResultFileModel()
                .observe(
                        getViewLifecycleOwner(),
                        fileModel -> NotificationHelper.createResultFileNotification(requireContext(), fileModel)
                );
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_file_list, container, false);
        viewBinding.setLifecycleOwner(getViewLifecycleOwner());
        viewBinding.setViewModel(viewModel);
        viewBinding.fileList.setAdapter(adapter);
        return viewBinding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.getCountLiveData()
                .removeObserver(counterObserver);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull final Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.file_list_menu, menu);
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        final SearchManager searchManager = (SearchManager) activity.getSystemService(Context.SEARCH_SERVICE);
        if (searchManager == null) {
            return;
        }
        MenuItem searchMenu = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchMenu.getActionView();
        searchView.setOnCloseListener(() -> {
            refreshSearch("");
            return false;
        });
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity.getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                refreshSearch(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        setSelected(menu.getItem(1));
    }

    private void refreshSearch(@NonNull String query) {
        adapter.setQuery(query);
        viewModel.setQuery(query);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.sort_by_name) {
            setSelected(item);
            viewModel.sortByName();
            return true;
        } else if (item.getItemId() == R.id.sort_by_date) {
            setSelected(item);
            viewModel.sortByDate();
            return true;
        } else if (item.getItemId() == R.id.sort_by_extension) {
            setSelected(item);
            viewModel.sortByExtension();
            return true;
        } else if (item.getItemId() == R.id.save_to_file) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                viewModel.saveResultsToFile();
            } else {
                if (ContextCompat.checkSelfPermission(
                        requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_GRANTED) {
                    viewModel.saveResultsToFile();
                } else {
                    requestPermission();
                }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setSelected(MenuItem item) {
        if (oldSelectedItem != null) {
            oldSelectedItem.setTitle(oldSelectedItem.getTitle().toString());
        }
        final SpannableStringBuilder builder = new SpannableStringBuilder(item.getTitle());
        builder.setSpan(
                new ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.purple_200)),
                0,
                item.getTitle().length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        item.setTitle(builder);
        oldSelectedItem = item;
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            requestPermissionLauncher.launch(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }
}
