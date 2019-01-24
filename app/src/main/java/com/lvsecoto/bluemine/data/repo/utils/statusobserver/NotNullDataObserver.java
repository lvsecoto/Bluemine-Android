package com.lvsecoto.bluemine.data.repo.utils.statusobserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import com.lvsecoto.bluemine.data.repo.utils.Resource;

import java.util.Objects;

/**
 * 同{@link DataObserver}, 但回调的数据不为空
 */
class NotNullDataObserver<T> implements Observer<Resource<T>> {

    @NonNull
    private final NotNullObserver<T> mNotNullObserver;

    NotNullDataObserver(@NonNull NotNullObserver<T> notNullObserver) {
        mNotNullObserver = notNullObserver;
    }

    @Override
    public void onChanged(@Nullable Resource<T> result) {
        Objects.requireNonNull(result);
        T data = result.getData();
        if (data != null) {
            mNotNullObserver.onChanged(data);
        }
    }
}
