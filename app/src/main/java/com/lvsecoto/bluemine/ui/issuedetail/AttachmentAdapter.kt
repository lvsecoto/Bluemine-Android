package com.lvsecoto.bluemine.ui.issuedetail

import androidx.recyclerview.widget.DiffUtil
import com.lvsecoto.bluemine.R
import com.lvsecoto.bluemine.data.vo.IssueDetail
import com.lvsecoto.bluemine.databinding.ViewItemAttachmentPicBinding
import com.lvsecoto.bluemine.utils.recyclerview.DataBoundListAdapter

class AttachmentAdapter : DataBoundListAdapter<AttachmentAdapter.Wrapper, ViewItemAttachmentPicBinding>(
    diffCallback = object : DiffUtil.ItemCallback<AttachmentAdapter.Wrapper>() {
        override fun areItemsTheSame(oldItem: Wrapper, newItem: Wrapper): Boolean =
            oldItem.title == newItem.title

        override fun areContentsTheSame(oldItem: Wrapper, newItem: Wrapper): Boolean =
            oldItem == newItem

    }
) {
    override val layoutId: Int
        get() = R.layout.view_item_attachment_pic

    fun submitAttachments(attachments: List<IssueDetail.Attachment>?) {
        submitList(attachments?.map { Wrapper(it) })
    }

    data class Wrapper(
        private val attachment: IssueDetail.Attachment
    ) {
        val title: String = attachment.fileName
        val isImage = attachment.contentType.matches("image/.+".toRegex())
        val imageUrl = attachment.contentUrl
    }
}
