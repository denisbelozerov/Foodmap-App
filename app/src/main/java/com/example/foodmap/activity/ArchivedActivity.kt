package com.example.foodmap.activity

import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.foodmap.DatabaseHelper
import com.example.foodmap.ListNoteBookAdapter
import com.example.foodmap.R
import java.util.*

class ArchivedActivity : AppCompatActivity() {
    val mydb = DatabaseHelper(this)
    var dataList =
        ArrayList<HashMap<String, String>>()
    var simpleList: ListView? = null
    var empty_text: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.title = "Архивные заметки"
        setContentView(R.layout.activity_archived)
        empty_text = findViewById<View>(R.id.empty_text) as TextView
        simpleList = findViewById<View>(R.id.ListView) as ListView
        simpleList!!.emptyView = empty_text
    }

    public override fun onResume() {
        super.onResume()
        populateData()
    }

    fun populateData() {
        runOnUiThread { fetchDataFromDB() }
    }

    fun fetchDataFromDB() {
        dataList.clear()
        val cursor = mydb!!.archivedData
        loadDataList(cursor, dataList)
    }

    fun loadDataList(
        cursor: Cursor?,
        List: ArrayList<HashMap<String, String>>
    ) {
        if (cursor != null) {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val mapData =
                    HashMap<String, String>()
                mapData["id"] = cursor.getString(0).toString()
                mapData["Заголовок"] = cursor.getString(1).toString()
                mapData["Текст заметки"] = cursor.getString(2).toString()
                mapData["Дата создания"] = cursor.getString(3).toString()
                mapData["Избранное"] = cursor.getString(4).toString()
                mapData["Статус"] = cursor.getString(5).toString()
                List.add(mapData)
                cursor.moveToNext()
            }
            val adapter = ListNoteBookAdapter(this, List, mydb)
            simpleList!!.adapter = adapter
            simpleList!!.onItemClickListener =
                OnItemClickListener { parent, view, position, id ->
                    val builder =
                        AlertDialog.Builder(this@ArchivedActivity)
                    builder.setTitle(List[+position]["Заголовок"])
                    //builder.setMessage(List.get(+position).get("description"));
                    builder.setNeutralButton(
                        "DELETE"
                    ) { dialog, id ->
                        mydb!!.deleteNoteBookData(List[+position]["id"]!!)
                        populateData()
                    }
                    builder.setNegativeButton(
                        "MOVE TO LIST"
                    ) { dialog, id ->
                        mydb!!.updateNoteBookStatus(List[+position]["id"]!!, 0)
                        populateData()
                    }
                    builder.setPositiveButton("CANCEL", null)
                    builder.show()
                }
        }
    }
}