package com.lvsecoto.bluemine.utils.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lvsecoto.bluemine.BR

abstract class DataBoundListAdapter<T, V : ViewDataBinding> protected constructor(diffCallback: DiffUtil.ItemCallback<T>) :
    ListAdapter<T, DataBoundListAdapter.ViewHolder<V>>(diffCallback) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ViewHolder<V> {
        val itemBinding = DataBindingUtil.inflate<V>(
            LayoutInflater.from(viewGroup.context),
            getItemViewType(position),
            viewGroup,
            false
        )
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder<V>, position: Int) {
        onBindData(viewHolder.mDataBinding, getItem(position), position)
    }

    override fun getItemViewType(position: Int): Int {
        return layoutId
    }

    protected abstract val layoutId :Int

    protected open fun onBindData(dataBinding: V, item: T, position: Int) {
        dataBinding.setVariable(BR.item, item)
        dataBinding.executePendingBindings()
    }

    class ViewHolder<V : ViewDataBinding>(val mDataBinding: V) : RecyclerView.ViewHolder(mDataBinding.root)
}
