package com.lvsecoto.bluemine.ui.home.issues

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import com.lvsecoto.bluemine.R
import com.lvsecoto.bluemine.data.vo.Issue
import com.lvsecoto.bluemine.databinding.ViewItemIssueBinding
import com.lvsecoto.bluemine.utils.recyclerview.DataBoundListAdapter

class IssuesAdapter :
    DataBoundListAdapter<IssuesAdapter.Wrapper, ViewItemIssueBinding>(
        diffCallback = object : DiffUtil.ItemCallback<IssuesAdapter.Wrapper>() {
            override fun areItemsTheSame(oldItem: Wrapper, newItem: Wrapper): Boolean =
                oldItem.issue.id == newItem.issue.id

            override fun areContentsTheSame(oldItem: Wrapper, newItem: Wrapper): Boolean =
                oldItem == newItem
        }
    ) {

    var onClickItem : ((Int) -> Unit)? = null

    @get:LayoutRes
    override val layoutId: Int
        get() = R.layout.view_item_issue

    override fun onBindData(dataBinding: ViewItemIssueBinding, item: Wrapper, position: Int) {
        super.onBindData(dataBinding, item, position)
        dataBinding.root.setOnClickListener {
            onClickItem?.invoke(position)
        }
    }

    fun submitIssues(issues: List<Issue>?) {
        submitList((issues ?: emptyList()).map { Wrapper(it) })
    }

    data class Wrapper(val issue: Issue) {
        val subject = issue.subject
        val statusName = issue.statusName
        val meta = issue.meta
    }
}