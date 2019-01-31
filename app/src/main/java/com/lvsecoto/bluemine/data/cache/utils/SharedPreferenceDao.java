package com.lvsecoto.bluemine.data.cache.utils;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * 对SharedPreference提供类似数据库CRUD操作，并且可以生成LiveData进行观察
 */
public class SharedPreferenceDao {

    @SuppressWarnings("SpellCheckingInspection")
    private static Gson mGSON = new Gson();

    /**
     * 创建LiveData用于观察对象
     */
    @NonNull
    public static <T> LiveData<T> objectLiveData(
            SharedPreferences pref, String key, Class<T> tClass) {
        return new PrefLiveData<T>(pref, key, null) {
            @Override
            protected T getPrefValue(SharedPreferences pref, String key, T defValue) {
                return getObject(pref, key, tClass);
            }
        };
    }

    /**
     * 创建LiveData用于观察对象列表
     */
    @NonNull
    public static <T> LiveData<List<T>> objectListLiveData(
            SharedPreferences pref, String key, Class<T> type) {
        return new PrefLiveData<List<T>>(pref, key, null) {
            @Override
            protected List<T> getPrefValue(SharedPreferences pref, String key, List<T> defValue) {
                return getObjectList(pref, key, type);
            }
        };
    }

    /**
     * 直接获取Pref中的对象
     */
    @Nullable
    public static <T> T getObject(SharedPreferences pref, String key, Class<T> tClass) {
        String json = pref.getString(key, null);
        if (json == null) {
            return null;
        }

        try {
            return mGSON.fromJson(json, tClass);
        } catch (JsonSyntaxException ignored) {
            return null;
        }
    }

    /**
     * 直接获取Pref中的对象列表
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public static <T> List<T> getObjectList(SharedPreferences pref, String key, Class<T> type) {
        String json = pref.getString(key, null);
        if (json == null) {
            return null;
        }

        try {
            return mGSON.fromJson(json, new GenericOf(type));
        } catch (JsonSyntaxException ignored) {
            return null;
        }
    }

    /**
     * 尝试获取列表，当列表不存在，则创建列表。
     * <p>如果可以取得一个对象，则用这个对象来创建列表
     */
    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> List<T> getOrCreateObjectList(
            SharedPreferences pref, String key, Class<T> type) {
        List<T> objectList = getObjectList(pref, key, type);

        if (objectList == null) {
            objectList = new ArrayList<>();

            T object = getObject(pref, key, type);
            if (object != null) {
                objectList.add(object);
            }
        }

        return objectList;
    }

    /**
     * 将一个对象写入pref, 替换原来那个
     */
    @SuppressLint("ApplySharedPref")
    @WorkerThread
    public static <T> void putObjectOrObjectList(SharedPreferences preferences, String key, T value) {
        preferences.edit().putString(key, mGSON.toJson(value)).commit();
    }

    /**
     * 同{@link #putObjectOrObjectList(SharedPreferences, String, Object)}, 为了兼容旧版本
     */
    @SuppressLint("ApplySharedPref")
    @WorkerThread
    public static <T> void putObject(SharedPreferences preferences, String key, T value) {
        putObjectOrObjectList(preferences, key, value);
    }


    /**
     * 添加{@code object}, 到列表位置{@code index}，如果位置为负数，则表示倒数第几位，-1代表倒数第一位，-2代表倒数第二位
     *
     * @return 如果索引不在范围，或者{@code key}对应的位置没有列表存在，返回false，否则为true
     */
    @WorkerThread
    public static <T> boolean insert(
            SharedPreferences pref, String key, Class<T> type, T object, int index) {
        List<T> objectList = getOrCreateObjectList(pref, key, type);

        int actualIndex = getActualIndex(index, objectList.size());

        if (actualIndex < 0 || actualIndex > objectList.size()) {
            return false;
        }

        objectList.add(actualIndex, object);

        putObjectOrObjectList(pref, key, objectList);
        return true;
    }

    /**
     * 添加对象到列表最前面
     *
     * @see #insert(SharedPreferences, String, Class, Object, int)
     */
    @WorkerThread
    public static <T> boolean insertFirst(
            SharedPreferences pref, String key, Class<T> type, T object) {
        return insert(pref, key, type, object, 0);
    }

    /**
     * 添加对象到列表最后面
     *
     * @see #insert(SharedPreferences, String, Class, Object, int)
     */
    @WorkerThread
    public static <T> boolean insertLast(
            SharedPreferences pref, String key, Class<T> type, T object) {
        return insert(pref, key, type, object, -1);
    }

    /**
     * 添加{@code object}, 到列表位置{@code index}，如果位置为负数，则表示倒数第几位，-1代表倒数第一位，-2代表倒数第二位
     *
     * @return 如果索引不在范围，或者{@code key}对应的位置没有列表存在，添加失败, 返回false，否则为true
     */
    @WorkerThread
    public static <T> boolean insertAll(
            SharedPreferences pref, String key, Class<T> type, List<T> objects, int index) {
        List<T> objectList = getOrCreateObjectList(pref, key, type);

        int actualIndex = getActualIndex(index, objectList.size());

        if (actualIndex < 0 || actualIndex > objectList.size()) {
            return false;
        }

        objectList.addAll(actualIndex, objects);
        putObjectOrObjectList(pref, key, objectList);

        return true;
    }

    /**
     * 当条件符合{@code where}, 则执行{@code update}
     *
     * @param count 指定要更新的数目
     * @return 多少行更新了
     */
    @WorkerThread
    public static <T> int update(
            SharedPreferences pref,
            String key,
            Class<T> type,
            Update<T> update,
            Where<T> where,
            int count) {
        List<T> objectList = getObjectList(pref, key, type);
        if (objectList == null || objectList.isEmpty()) {
            return 0;
        }

        int updateCount = 0;
        for (int index = 0; index < objectList.size(); index++) {
            T object = objectList.get(index);

            if (where.where(object)) {
                objectList.set(index, update.update(object));
                updateCount++;
            }

            if (updateCount >= count) {
                break;
            }
        }

        if (updateCount > 0) {
            putObjectOrObjectList(pref, key, objectList);
        }
        return updateCount;
    }

    /**
     * 仅更新一次，优化性能
     *
     * @see #update(SharedPreferences, String, Class, Update, Where, int) 仅更新一次, 性能优化
     */
    @WorkerThread
    public static <T> int updateOnce(
            SharedPreferences pref, String key, Class<T> type, Update<T> update, Where<T> where) {
        return update(pref, key, type, update, where, 1);
    }

    /**
     * 更新任何一个符合条件
     *
     * @see #update(SharedPreferences, String, Class, Update, Where, int) 仅更新一次, 性能优化
     */
    @WorkerThread
    public static <T> int updateAny(
            SharedPreferences pref, String key, Class<T> type, Update<T> update, Where<T> where) {
        return update(pref, key, type, update, where, Integer.MAX_VALUE);
    }

    /**
     * 删除符合条件{@code where}的条目
     *
     * @return 删除的数量
     */
    @WorkerThread
    public static <T> int delete(
            SharedPreferences pref, String key, Class<T> type, Where<T> where, int count) {
        List<T> objectList = getObjectList(pref, key, type);
        if (objectList == null || objectList.isEmpty()) {
            return 0;
        }

        int deleteCount = 0;

        for (Iterator<T> iterator = objectList.iterator(); iterator.hasNext(); ) {
            T object = iterator.next();

            if (where.where(object)) {
                deleteCount++;
                iterator.remove();
            }

            if (deleteCount >= count) {
                break;
            }
        }

        if (deleteCount > 0) {
            putObjectOrObjectList(pref, key, objectList);
        }
        return deleteCount;
    }

    /**
     * 仅删除符合条件的条目一次，优化性能
     *
     * @return 删除的数量
     */
    @WorkerThread
    public static <T> int deleteOnce(
            SharedPreferences pref, String key, Class<T> type, Where<T> where) {
        return delete(pref, key, type, where, 1);
    }

    /**
     * 删除所有符合条件的条目
     *
     * @return 删除的数量
     */
    @WorkerThread
    public static <T> int deleteAny(
            SharedPreferences pref, String key, Class<T> type, Where<T> where) {
        return delete(pref, key, type, where, Integer.MAX_VALUE);
    }

    /**
     * 删除所有条目
     *
     * @return 删除的数量
     */
    @WorkerThread
    public static <T> int deleteAll(SharedPreferences pref, String key, Class<T> type) {
        List<T> objectList = getObjectList(pref, key, type);

        putObjectOrObjectList(pref, key, Collections.emptyList());

        if (objectList == null || objectList.isEmpty()) {
            return 0;
        } else {
            return objectList.size();
        }
    }

    /**
     * 转换可正可负的索引，当索引为负几，代表倒数第几个，-1是倒数第一个
     */
    private static int getActualIndex(int index, int size) {
        int actualIndex;
        if (index < 0) {
            actualIndex = size - (-index) + 1;
        } else {
            actualIndex = index;
        }
        return actualIndex;
    }

    /**
     * 创建一个可以观察SharedPreference Key 对应的数据的变化的LiveData
     *
     * @param <T>
     */
    abstract static class PrefLiveData<T> extends LiveData<T> {

        private SharedPreferences mPref;

        private String mKey;

        private T mDefValue;

        private SharedPreferences.OnSharedPreferenceChangeListener mListener =
                (pref, key) -> {
                    if (Objects.equals(key, mKey)) {
                        setValue(getPrefValue(mPref, mKey, mDefValue));
                    }
                };

        /**
         * @param sharedPreferences SharedPreference对象
         * @param key               对应的SharedPreference Key
         * @param defValue          默认指
         */
        PrefLiveData(SharedPreferences sharedPreferences, String key, T defValue) {
            mPref = sharedPreferences;
            mKey = key;
            mDefValue = defValue;
        }

        @Override
        protected void onActive() {
            super.onActive();
            setValue(getPrefValue(mPref, mKey, mDefValue));
            mPref.registerOnSharedPreferenceChangeListener(mListener);
            Log.e("PREF", "act:" + mKey);
        }

        @Override
        protected void onInactive() {
            super.onInactive();
            mPref.unregisterOnSharedPreferenceChangeListener(mListener);
            Log.e("PREF", "inact:" + mKey);
        }

        /**
         * 如何从SharedPreference获取数据
         */
        protected abstract T getPrefValue(SharedPreferences pref, String key, T defValue);
    }

    /**
     * 帮助GSON解析列表范型数据
     */
    static class GenericOf<T> implements ParameterizedType {

        private final Class<T> type;

        GenericOf(Class<T> type) {
            this.type = type;
        }

        @Override
        @NonNull
        public Type[] getActualTypeArguments() {
            return new Type[]{type};
        }

        @Override
        @NonNull
        public Type getRawType() {
            return List.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }

    /**
     * where条件测试
     */
    public interface Where<T> {
        boolean where(T object);
    }

    /**
     * 更新操作
     */
    public interface Update<T> {
        T update(T object);
    }
}

