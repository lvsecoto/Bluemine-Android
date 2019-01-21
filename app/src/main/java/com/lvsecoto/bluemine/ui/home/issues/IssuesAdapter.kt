package com.lvsecoto.bluemine.ui.home.issues

import androidx.recyclerview.widget.DiffUtil
import com.lvsecoto.bluemine.R
import com.lvsecoto.bluemine.databinding.ViewItemIssueBinding
import com.lvsecoto.bluemine.utils.recyclerview.DataBoundListAdapter

class IssuesAdapter :
    DataBoundListAdapter<IssuesAdapter.Wrapper, ViewItemIssueBinding>(
        diffCallback = object : DiffUtil.ItemCallback<IssuesAdapter.Wrapper>() {
            override fun areItemsTheSame(oldItem: Wrapper, newItem: Wrapper): Boolean =
                oldItem.title == newItem.title

            override fun areContentsTheSame(oldItem: Wrapper, newItem: Wrapper): Boolean =
                oldItem == newItem
        }
    ) {

    var onClickItem : ((Int) -> Unit)? = null

    override val layoutId: Int
        get() = R.layout.view_item_issue

    override fun onBindData(dataBinding: ViewItemIssueBinding, item: Wrapper, position: Int) {
        super.onBindData(dataBinding, item, position)
        dataBinding.root.setOnClickListener {
            onClickItem?.invoke(position)
        }
    }

    fun submitDemo(demos: List<String>) {
        submitList(demos.map { Wrapper(it) })
    }

    data class Wrapper(val title: String)
}