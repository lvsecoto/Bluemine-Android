package com.lvsecoto.bluemine.ui.issuedetail

import androidx.recyclerview.widget.DiffUtil
import com.lvsecoto.bluemine.R
import com.lvsecoto.bluemine.databinding.ViewItemAttachmentPicBinding
import com.lvsecoto.bluemine.utils.recyclerview.DataBoundListAdapter

class AttachmentPicAdapter : DataBoundListAdapter<AttachmentPicAdapter.Wrapper, ViewItemAttachmentPicBinding>(
    diffCallback = object : DiffUtil.ItemCallback<AttachmentPicAdapter.Wrapper>() {
        override fun areItemsTheSame(oldItem: Wrapper, newItem: Wrapper): Boolean =
            oldItem.title == newItem.title

        override fun areContentsTheSame(oldItem: Wrapper, newItem: Wrapper): Boolean =
            oldItem == newItem

    }
) {
    override val layoutId: Int
        get() = R.layout.view_item_attachment_pic

    fun submitDemos(demos: List<String>) {
        submitList(demos.map { Wrapper(it) })
    }

    class Wrapper(val title: String)
}
