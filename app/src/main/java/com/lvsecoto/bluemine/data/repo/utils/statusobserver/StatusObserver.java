package com.lvsecoto.bluemine.data.repo.utils.statusobserver;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import com.lvsecoto.bluemine.data.repo.utils.Resource;

/**
 * 会感知数据状态的{@link Observer}
 */
@SuppressWarnings("unused")
public class StatusObserver {

    private StatusObserver() {
    }

    /** 只有当状态从 loading 到 success 才回调 */
    @NonNull
    public static <T> Observer<Resource<T>> onSuccess(Observer<Resource<T>> onSuccess) {
        return new StatusChangeObserver<>(null, onSuccess, null);
    }

    /** 曾经success过 */
    @NonNull
    public static <T> Observer<Resource<T>> hasSuccess(Observer<Resource<T>> hasSuccess) {
        return new StatusStateObserver<>(null, hasSuccess, null);
    }

    /** 只有当状态从 loading 到 error 才回调 */
    @NonNull
    public static <T> Observer<Resource<T>> onError(Observer<Resource<T>> onError) {
        return new StatusChangeObserver<>(null, null, onError);
    }

    /**
     * 曾经error过
     */
    @NonNull
    public static <T> Observer<Resource<T>> hasError(Observer<Resource<T>> hasError) {
        return new StatusStateObserver<>(null, null, hasError);
    }

    /**
     * 只有当状态从 loading 到 success 或者 error 才回调
     */
    public static <T> Observer<Resource<T>> onResult(Observer<Resource<T>> onResult) {
        return new StatusChangeObserver<>(null, onResult, onResult);
    }

    /**
     * 当曾经有过success或者error时会回调
     */
    public static <T> Observer<Resource<T>> hasResult(Observer<Resource<T>> hasResult) {
        return new StatusStateObserver<>(null, hasResult, hasResult);
    }

    /**
     * 对整个数据的变化周期进行观察
     */
    public static <T> Observer<Resource<T>> on(
            Observer<Resource<T>> isLoading,
            Observer<Resource<T>> onSuccess,
            Observer<Resource<T>> onError) {
        return new StatusChangeObserver<>(isLoading, onSuccess, onError);
    }

    /**
     * 仅观察数据
     */
    public static <T> Observer<Resource<T>> data(Observer<T> data) {
        return new DataObserver<>(data);
    }

    /**
     * 仅观察非空数据
     */
    public static <T> Observer<Resource<T>> notNullData(NotNullObserver<T> data) {
        return new NotNullDataObserver<>(data);
    }
}
