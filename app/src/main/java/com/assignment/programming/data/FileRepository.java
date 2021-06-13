package com.assignment.programming.data;

import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public final class FileRepository {

    private static final String TAG = FileRepository.class.getSimpleName();
    private static FileRepository INSTANCE;


    private final List<FileModel> fileList = new ArrayList<>();

    private FileRepository() {
    }

    public static FileRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FileRepository();
        }
        return INSTANCE;
    }

    @WorkerThread
    @NonNull
    public List<FileModel> getFileList(SortType sortType) {
        final List<FileModel> fileList = new ArrayList<>(getFileList());
        long start = new Date().getTime();
        switch (sortType) {
            case DATE:
                Collections.sort(fileList, Collections.reverseOrder(new FileDateComparator()));
                Log.i(TAG, "sort by date performance: " + (new Date().getTime() - start));
                break;
            case EXT:
                Collections.sort(fileList, new FileExtComparator());
                Log.i(TAG, "sort by ext performance: " + (new Date().getTime() - start));
                break;
            case NAME:
                Collections.sort(fileList, new FileNameComparator());
                Log.i(TAG, "sort by name performance: " + (new Date().getTime() - start));
                break;
        }
        return fileList;
    }

    @WorkerThread
    @NonNull
    public List<FileModel> getFileList() {
        synchronized (fileList) {
            if (fileList.isEmpty()) {
                fileList.addAll(getFileList(Environment.getExternalStorageDirectory()));
            }
            return fileList;
        }
    }

    @WorkerThread
    public int getMatchingFileCount(@NonNull String query) {
        final List<FileModel> fileList = getFileList();
        if (!TextUtils.isEmpty(query)) {
            int count = 0;
            String lowerCasedQuery = query.toLowerCase();
            for (FileModel file : fileList) {
                if (file.getName().toLowerCase().contains(lowerCasedQuery)) {
                    count++;
                }
            }
            return count;
        } else {
            return fileList.size();
        }
    }

    @WorkerThread
    public DetailedFileModel getDetailedFileModel(String filePath) {
        File file = new File(filePath);
        return new DetailedFileModel(file, getCreated(file));
    }

    @WorkerThread
    @NonNull
    private List<FileModel> getFileList(@NonNull File parentDir) {
        final ArrayList<FileModel> inFiles = new ArrayList<>();
        File[] files = parentDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    inFiles.addAll(getFileList(file));
                } else {
                    inFiles.add(new FileModel(file));
                }
            }
        }
        return inFiles;
    }

    @WorkerThread
    private long getCreated(File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                return attr.creationTime().toMillis();
            } catch (IOException e) {
                Log.e(TAG, "Failed to get creation time");
                return file.lastModified();
            }
        }
        return file.lastModified();
    }

    @Nullable
    public FileModel saveResultsToFile() {
        final List<FileModel> fileList = getFileList();
        File reportFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "programming_assignment_report.txt");
        try (FileWriter writer = new FileWriter(reportFile)) {
            for (FileModel fileModel : fileList) {
                writer.write(fileModel.toString() + System.lineSeparator());
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to save results to file");
            return null;
        }
        return new FileModel(reportFile);
    }
}
