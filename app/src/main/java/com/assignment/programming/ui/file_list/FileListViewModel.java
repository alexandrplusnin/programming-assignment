package com.assignment.programming.ui.file_list;

import androidx.annotation.UiThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.assignment.programming.base.BaseViewModel;
import com.assignment.programming.data.models.FileModel;
import com.assignment.programming.data.FileRepository;
import com.assignment.programming.data.SortType;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public final class FileListViewModel extends BaseViewModel {
    private final MutableLiveData<List<FileModel>> fileLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> countLiveData = new MutableLiveData<>(-1);
    private final MutableLiveData<FileModel> resultFileModel = new MutableLiveData<>();

    private SortType sortType = SortType.NAME;

    @UiThread
    public FileListViewModel() {
        populateList();
    }

    public LiveData<List<FileModel>> getFileLiveData() {
        return fileLiveData;
    }

    public LiveData<Integer> getCountLiveData() {
        return countLiveData;
    }

    public LiveData<FileModel> getResultFileModel() {
        return resultFileModel;
    }

    @UiThread
    private void populateList() {
        subscribe(
                Single.fromCallable(() -> FileRepository.getInstance().getFileList(sortType))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(list -> {
                                    fileLiveData.setValue(list);
                                    countLiveData.setValue(list.size());
                                }
                        )
        );
    }

    public void setQuery(String query) {
        subscribe(
                Single.fromCallable(() -> FileRepository.getInstance().getMatchingFileCount(query))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(countLiveData::setValue)
        );
    }

    public void sortByName() {
        sortType = SortType.NAME;
        populateList();
    }

    public void sortByDate() {
        sortType = SortType.DATE;
        populateList();
    }

    public void sortByExtension() {
        sortType = SortType.EXT;
        populateList();
    }

    public void saveResultsToFile() {
        subscribe(
                Maybe.fromCallable(() -> FileRepository.getInstance().saveResultsToFile())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(resultFileModel::setValue)
        );
    }
}
