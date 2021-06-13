package com.assignment.programming.base;

import androidx.lifecycle.ViewModel;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseViewModel extends ViewModel {
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCleared() {
        super.onCleared();
        unsubscribeAll();
    }

    protected void subscribe(final Disposable event) {
        if (compositeDisposable == null || compositeDisposable.isDisposed()) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(event);
    }

    private void unsubscribeAll() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
            compositeDisposable = null;
        }
    }
}
