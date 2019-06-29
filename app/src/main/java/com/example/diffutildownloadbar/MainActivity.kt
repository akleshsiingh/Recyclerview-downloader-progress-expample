package com.example.diffutildownloadbar

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.alert_start_again.view.*
import java.util.*

class MainActivity : AppCompatActivity() {

    val adapterDownloads = AdapterDownloads()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        listDownload?.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = adapterDownloads
        }
        startT()
    }
   fun startT(){
        val timer = Timer()
        val t = tasker()
        t.list = generateNewList()
        t.progress =0
        t.listner = { progress ->
            if (progress == 100)
                timer.cancel()
            showDialogStartAgain { _startAgain ->
                if (_startAgain) {
                    startT()
                }
            }
        }
        timer.scheduleAtFixedRate(t, 100, 200)
    }


    inner class tasker(var progress: Int = 0) : TimerTask() {
        var list = listOf<ModelDownload>()
        var listner: ((Int) -> Unit)? = null

        override fun run() {
            val newList = mutableListOf<ModelDownload>()
            progress += 1
            for (d in list) {
                newList.add(
                    ModelDownload(
                        id = d.id,
                        title = d.title,
                        fileSize = d.fileSize
                    ).apply { downloadedSize = (progress * d.fileSize / 100) })

            }
            Log.e("PROGRESS", " $progress")
            runOnUiThread {
                if (progress == 100) {
                    listner?.invoke(progress)
                }
                adapterDownloads.submitList(newList)
            }
        }
    }


    private fun generateNewList(): MutableList<ModelDownload> {
        return mutableListOf<ModelDownload>()
            .apply {
                add(ModelDownload(title = "SomeThing Important.pdf", fileSize = 100 * 1000 * 10000))
                add(ModelDownload(title = "statement.pdf", fileSize = 200 * 1000 * 1000))
                add(ModelDownload(title = "Item extra.pdf", fileSize = 300 * 1000 * 1000))
                add(ModelDownload(title = "Season 1 of something.rar", fileSize = 400 * 1000 * 1000))
                add(ModelDownload(title = "Season 2 of something.rar", fileSize = 50 * 1000 * 1000))
                add(ModelDownload(title = "Season 3 of something.rar", fileSize = 600 * 1000 * 1000))
                add(ModelDownload(title = "Season 4 of something.rar", fileSize = 700 * 1000 * 1000))
                add(ModelDownload(title = "Season 5 of something.rar", fileSize = 700 * 1000 * 1000))
                add(ModelDownload(title = "office project.rar", fileSize = 10 * 1000 * 1000))
                add(ModelDownload(title = "presentation.xx", fileSize = 30 * 1000 * 1000))
                add(ModelDownload(title = "Excelfile.xxx", fileSize = 80 * 1000 * 1000))
                add(ModelDownload(title = "cc_statement.pdf", fileSize = 900 * 1000 * 1000))
            }
    }

    private fun Context.showDialogStartAgain(startAgain: (Boolean) -> Unit) {
        val view = LayoutInflater.from(this).inflate(R.layout.alert_start_again, null)
        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .create()
        view.btnStartAgain.setOnClickListener { startAgain.invoke(true); dialog.dismiss() }
        dialog.show()
    }
}
