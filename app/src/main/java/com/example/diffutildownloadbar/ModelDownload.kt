package com.example.diffutildownloadbar

import java.util.*

data class ModelDownload(
    val id: Long = Date().time,
    val title: String,
    var fileSize: Long,
    var downloadedSize: Long = 0L,
    var timeElapsed: Long = 0,
    var start: Boolean = false,
    val done: Boolean = false
) {
    fun progress() = (downloadedSize * 100 / fileSize).toInt()
}