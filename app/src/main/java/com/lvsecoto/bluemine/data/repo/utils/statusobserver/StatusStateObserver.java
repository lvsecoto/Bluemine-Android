package com.lvsecoto.bluemine.data.repo.utils.statusobserver;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import com.lvsecoto.bluemine.data.repo.utils.Resource;

import java.util.Objects;

/**
 * 观察当前数据状态的观察者
 *
 * @param <T>
 */
class StatusStateObserver<T> implements Observer<Resource<T>> {

    private final Observer<Resource<T>> mIsLoading;

    private final Observer<Resource<T>> mHasSuccess;

    private final Observer<Resource<T>> mHasError;

    StatusStateObserver(Observer<Resource<T>> isLoading,
                        Observer<Resource<T>> hasSuccess,
                        Observer<Resource<T>> hasError) {
        mIsLoading = isLoading;
        mHasSuccess = hasSuccess;
        mHasError = hasError;
    }

    @Override
    public void onChanged(@Nullable Resource<T> result) {
        Objects.requireNonNull(result);

        switch (result.getStatus()) {
            case SUCCESS:
                if (mHasSuccess != null) {
                    mHasSuccess.onChanged(result);
                }
                break;
            case ERROR:
                if (mHasError != null) {
                    mHasError.onChanged(result);
                }
                break;
            case LOADING:
                if (mIsLoading != null) {
                    mIsLoading.onChanged(result);
                }
                break;
        }
    }
}
