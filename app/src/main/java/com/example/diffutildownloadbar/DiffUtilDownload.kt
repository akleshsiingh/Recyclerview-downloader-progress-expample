package com.example.diffutildownloadbar

import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.util.Log

class DiffUtilDownload(private val oldList: List<ModelDownload>, private val newList: List<ModelDownload>) :
    DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(p0: Int, p1: Int) = oldList[p0].id == newList[p1].id

    override fun areContentsTheSame(p0: Int, p1: Int): Boolean {
        val oldD = oldList[p0]
        val newD = newList[p1]
        return oldD.downloadedSize == newD.downloadedSize
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldD = oldList[oldItemPosition]
        val newD = newList[newItemPosition]
        val bundle = Bundle()
            .apply {
                if (oldD.downloadedSize != newD.downloadedSize)
                    putLong("DOWNLOADED", newD.downloadedSize)

                if (oldD.progress() != newD.progress())
                    putLong("PROGRESS", newD.progress().toLong())
            }
        if (bundle.isEmpty) return null
        return bundle
    }
}