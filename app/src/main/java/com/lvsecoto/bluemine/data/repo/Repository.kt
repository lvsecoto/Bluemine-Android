package com.lvsecoto.bluemine.data.repo

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.lvsecoto.bluemine.AppExecutors
import com.lvsecoto.bluemine.data.cache.utils.SharedPreferenceDao
import com.lvsecoto.bluemine.data.network.response.ProjectResponse
import com.lvsecoto.bluemine.data.network.service.RedMineService
import com.lvsecoto.bluemine.data.network.utils.ApiResponse
import com.lvsecoto.bluemine.data.repo.utils.NetworkBoundResource
import com.lvsecoto.bluemine.data.repo.utils.RateLimiter
import com.lvsecoto.bluemine.data.repo.utils.Resource
import com.lvsecoto.bluemine.data.vo.Project
import java.util.concurrent.TimeUnit

private const val KEY_PROJECTS = "KEY_PROJECTS"

class Repository(
    private val executes: AppExecutors,
    private val service: RedMineService,
    private val pref: SharedPreferences
) {
    val limiter = RateLimiter<String>(5, TimeUnit.MINUTES)

    fun getProject(): LiveData<Resource<List<Project>>> {
        return object : NetworkBoundResource<List<Project>, ProjectResponse>(executes) {
            override fun saveCallResult(item: ProjectResponse) {
                pref.putObjectOrObjectList(KEY_PROJECTS, item.projects)
            }

            override fun shouldFetch(data: List<Project>?): Boolean {
                return data == null || data.isEmpty() || limiter.shouldFetch(KEY_PROJECTS)
            }

            override fun loadFromDb(): LiveData<List<Project>> =
                pref.objectListData(KEY_PROJECTS)

            override fun createCall(): LiveData<ApiResponse<ProjectResponse>> =
                service.getProject()

        }.asLiveData()
    }

    inline fun <reified T> SharedPreferences.objectListData(key: String): LiveData<List<T>> =
        SharedPreferenceDao.objectListLiveData(this, key, T::class.java)

    inline fun <reified T> SharedPreferences.putObjectOrObjectList(key: String, data: T) =
        SharedPreferenceDao.putObjectOrObjectList(this, key, data)
}