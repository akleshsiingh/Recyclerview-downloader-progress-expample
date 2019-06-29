package com.example.diffutildownloadbar

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.single_row_download.view.*
import java.nio.file.Files.size
import java.text.DecimalFormat


class AdapterDownloads : RecyclerView.Adapter<BaseViewHolder>() {
    private val list = mutableListOf<ModelDownload>()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int) = MyViewHolder(p0.inflateView(R.layout.single_row_download))
    override fun getItemCount() = list.size
    override fun onBindViewHolder(p0: BaseViewHolder, p1: Int) = p0.onBind()
    inner class MyViewHolder(itemView: View) : BaseViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        override fun onBind() {
            val single = list[adapterPosition]
            itemView.tvTitle.text = single.title
            itemView.progressBar.progress = single.progress()
            itemView.tvProgress.text = single.downloadedSize.getFileSize()
            itemView.tvFileSize.text = "/ " + single.fileSize.getFileSize()
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int, payloads: MutableList<Any>) {

        if (payloads.isEmpty()) super.onBindViewHolder(holder, position, payloads)
        else {
            val bundle = payloads[0] as Bundle
            for (b in bundle.keySet()) {
                Log.e("$b ", " ${bundle.get(b)}")
                if (b.equals("PROGRESS", true)) {
                    holder.itemView.progressBar.progress = bundle.getLong(b).toInt()
                }

                if (b.equals("DOWNLOADED", true)) {
                    holder.itemView.tvProgress.text = bundle.getLong(b).getFileSize()
                }
            }
        }
    }

    fun submitList(newList: List<ModelDownload>) {
        val diffResult = DiffUtil.calculateDiff(DiffUtilDownload(list, newList))
        diffResult.dispatchUpdatesTo(this)
        list.clear()
        list.addAll(newList)
    }

}

private fun Long.getFileSize(): String {
    var bytes = this
    if (this <= 0)
        return "0"

    if (bytes < 1024)
        return "$bytes bytes"

    bytes /= 1024
    if (bytes < 1024)
        return "$bytes KB"

    bytes /= 1024
    if (bytes < 1024)
        return "$bytes MB"

    bytes /= 1024
    if (bytes < 1024)
        return "$bytes GB"

    return "0"
}

// extension function
private fun ViewGroup.inflateView(layout: Int) = LayoutInflater.from(this.context).inflate(layout, this, false)


abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun onBind()
}