package com.assignment.programming.ui.file_item;

import androidx.core.util.ObjectsCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.assignment.programming.base.BaseViewModel;
import com.assignment.programming.data.FileRepository;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public final class FileItemViewModel extends BaseViewModel {

    private final MutableLiveData<String> name = new MutableLiveData<>("");
    private final MutableLiveData<String> path = new MutableLiveData<>("");
    private final MutableLiveData<String> size = new MutableLiveData<>("");
    private final MutableLiveData<String> created = new MutableLiveData<>("");
    private final MutableLiveData<String> lastModified = new MutableLiveData<>("");
    private final MutableLiveData<String> imagePath = new MutableLiveData<>();
    private String filePath = null;

    public void setFilePath(String filePath) {
        if (!ObjectsCompat.equals(filePath, this.filePath)) {
            this.filePath = filePath;
            subscribe(
                    Single.fromCallable(() -> FileRepository.getInstance().getDetailedFileModel(filePath))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(fileModel -> {
                                        name.setValue(fileModel.getName());
                                        path.setValue(fileModel.getPath());
                                        size.setValue(fileModel.getFormattedLength());
                                        created.setValue(fileModel.getFormattedCreationDate());
                                        lastModified.setValue(fileModel.getFormattedModificationDate());
                                        if (fileModel.isImage()) {
                                            imagePath.setValue(fileModel.getPath());
                                        }
                                    }
                            )
            );
        }
    }

    public LiveData<String> getName() {
        return name;
    }

    public LiveData<String> getPath() {
        return path;
    }

    public LiveData<String> getSize() {
        return size;
    }

    public LiveData<String> getCreated() {
        return created;
    }

    public LiveData<String> getLastModified() {
        return lastModified;
    }

    public LiveData<String> getImagePath() {
        return imagePath;
    }
}
