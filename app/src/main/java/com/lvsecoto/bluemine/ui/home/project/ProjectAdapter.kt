package com.lvsecoto.bluemine.ui.home.project

import androidx.recyclerview.widget.DiffUtil
import com.lvsecoto.bluemine.R
import com.lvsecoto.bluemine.databinding.ViewItemProjectBinding
import com.lvsecoto.bluemine.utils.recyclerview.DataBoundListAdapter

class ProjectAdapter : DataBoundListAdapter<ProjectAdapter.Wrapper, ViewItemProjectBinding>(
    diffCallback = object : DiffUtil.ItemCallback<Wrapper>() {
        override fun areItemsTheSame(oldItem: Wrapper, newItem: Wrapper): Boolean =
            oldItem.title == newItem.title

        override fun areContentsTheSame(oldItem: Wrapper, newItem: Wrapper): Boolean =
            oldItem == newItem
    }
) {
    override val layoutId: Int
        get() = R.layout.view_item_project

    fun submitDemos(demos: List<String>) {
        submitList(demos.map { Wrapper(it) })
    }

    data class Wrapper(var title: String)
}
