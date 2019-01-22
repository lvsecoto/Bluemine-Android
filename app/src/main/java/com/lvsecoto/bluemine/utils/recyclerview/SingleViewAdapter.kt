package com.lvsecoto.bluemine.utils.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class SingleViewAdapter<V : ViewDataBinding>(
    @LayoutRes private var layoutId: Int
) : RecyclerView.Adapter<SingleViewAdapter.ViewHolder<V>>() {

    private var onBindData: ((V) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<V> {
        val binding = DataBindingUtil.inflate<V>(
            LayoutInflater.from(parent.context), layoutId, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder<V>, position: Int) {
        onBindData?.invoke(holder.binding)
    }

    override fun getItemViewType(position: Int): Int {
        return layoutId
    }

    override fun getItemCount(): Int {
        return 1
    }

    fun submit(onBindData: (V) -> Unit) {
        this.onBindData = onBindData
        notifyDataSetChanged()
    }

    class ViewHolder<V : ViewDataBinding> constructor(val binding: V) :
        RecyclerView.ViewHolder(binding.root)
}
