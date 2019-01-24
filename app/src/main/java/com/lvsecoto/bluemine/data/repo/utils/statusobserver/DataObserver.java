package com.lvsecoto.bluemine.data.repo.utils.statusobserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import com.lvsecoto.bluemine.data.repo.utils.Resource;

import java.util.Objects;

/**
 * 数据观察者, 忽略{@link Resource#status}, 只观察{@link Resource#data}
 */
class DataObserver<T> implements Observer<Resource<T>> {

    @NonNull
    private final Observer<T> mDataObserver;

    DataObserver(@NonNull Observer<T> dataObserver) {
        mDataObserver = dataObserver;
    }

    @Override
    public void onChanged(@Nullable Resource<T> result) {
        Objects.requireNonNull(result);
        mDataObserver.onChanged(result.getData());
    }
}
