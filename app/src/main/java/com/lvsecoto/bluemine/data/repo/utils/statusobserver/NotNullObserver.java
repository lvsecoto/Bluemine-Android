package com.lvsecoto.bluemine.data.repo.utils.statusobserver;

import androidx.annotation.NonNull;

/**
 * 不同于{@link android.arch.lifecycle.Observer}, {@link
 * com.jbangit.base.repo.Resource#data}为空的时候,这个接口不会回调
 */
public interface NotNullObserver<T> {

    void onChanged(@NonNull T data);
}
