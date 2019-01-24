package com.lvsecoto.bluemine.data.repo.utils.statusobserver;


import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import com.lvsecoto.bluemine.data.repo.utils.Resource;

import java.util.Objects;

/**
 * 监测数据状态改变的观察者
 *
 * @param <T>
 */
class StatusChangeObserver<T> implements Observer<Resource<T>> {

    /**
     * 这个标志在Loading状态下设为True, 在成功或者失败设置为False
     */
    private boolean mIsPending = false;

    private final Observer<Resource<T>> mIsLoading;

    private final Observer<Resource<T>> mOnChangeToSuccess;

    private final Observer<Resource<T>> mOnChangeToError;

    public StatusChangeObserver(Observer<Resource<T>> isLoading,
                                Observer<Resource<T>> onChangeToSuccess,
                                Observer<Resource<T>> onChangeToError) {
        mIsLoading = isLoading;
        mOnChangeToSuccess = onChangeToSuccess;
        mOnChangeToError = onChangeToError;
    }

    @MainThread
    @Override
    public void onChanged(@Nullable Resource<T> resource) {
        Objects.requireNonNull(resource);

        switch (resource.getStatus()) {
            case SUCCESS:
                if (mIsPending) {
                    mIsPending = false;
                    if (mOnChangeToSuccess != null) {
                        mOnChangeToSuccess.onChanged(resource);
                    }
                }
                break;
            case ERROR:
                if (mIsPending) {
                    mIsPending = false;
                    if (mOnChangeToError != null) {
                        mOnChangeToError.onChanged(resource);
                    }
                }
                break;
            case LOADING:
                mIsPending = true;
                if (mIsLoading != null) {
                    mIsLoading.onChanged(resource);
                }
                break;
        }

    }
}
