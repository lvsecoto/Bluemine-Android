package com.lvsecoto.bluemine.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.lvsecoto.bluemine.data.repo.utils.event.SingleLiveEvent

/**
 * 每当输入一个参数[.input]，就回调[OnGetLiveData]，生成一个LiveData
 */
class ActionLiveData<InputType, ResultType>(
    private var action: (InputType) -> LiveData<ResultType>
) {

    private val input = SingleLiveEvent<InputType>()

    private val result: LiveData<ResultType>

    init {
        result = Transformations.switchMap(input) {
            action.invoke(it)
        }
    }

    /**
     * 调用此函数触发[OnGetLiveData]创建新的LiveData
     * @param input
     */
    fun input(input: InputType) {
        this.input.value = input
    }

    fun call() {
        this.input.value = null
    }

    fun asLiveData(): LiveData<ResultType> {
        return result
    }

    companion object {

        /**
         * 使用此静态方法方便创建[ActionLiveData], 也可以使用[.ActionLiveData]
         * @param onGetLiveData 回调函数，用于创建新的LiveData
         */
        fun <InputType, ResultType> create(
            action: (InputType) -> LiveData<ResultType>
        ): ActionLiveData<InputType, ResultType> {
            return ActionLiveData(action)
        }
    }
}