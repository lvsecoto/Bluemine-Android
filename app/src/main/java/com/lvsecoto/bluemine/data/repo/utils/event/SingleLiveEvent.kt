package com.lvsecoto.bluemine.data.repo.utils.event

import androidx.lifecycle.*

/**
 * reference from https://gist.github.com/JoseAlcerreca/5b661f1800e1e654f07cc54fe87441af#gistcomment-2783677
 * supports multiple observer, *I think*
 */
class SingleLiveEvent<T> : MutableLiveData<T>() {

    private val liveDataToObserve: LiveData<T>
    private val pendingMap: MutableMap<Int, Boolean>

    init {
        val outputLiveData = MediatorLiveData<T>()
        outputLiveData.addSource(this) { currentValue ->
            outputLiveData.value = currentValue
        }
        liveDataToObserve = outputLiveData
        pendingMap = HashMap()
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {

        pendingMap[observer.hashCode()] = false

        // Observe the internal MutableLiveData
        liveDataToObserve.observe(owner, Observer { t ->
            if (pendingMap[observer.hashCode()] == true) { // don't trigger if the observer wasn't registered
                observer.onChanged(t)
                pendingMap[observer.hashCode()] = false
            }
        })
    }

    override fun setValue(t: T?) {
        pendingMap.forEach { pendingMap[it.key] = true }
        super.setValue(t)
    }
}

