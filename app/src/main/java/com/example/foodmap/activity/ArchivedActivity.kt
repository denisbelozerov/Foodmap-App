package com.example.foodmap.activity

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
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
import com.example.foodmap.databinding.ActivityAddModifyNotebookDataBinding
import com.example.foodmap.databinding.ActivityArchivedBinding
import java.util.*

class ArchivedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArchivedBinding
    private lateinit var databaseHelper: DatabaseHelper
    lateinit var db: SQLiteDatabase
    private lateinit var emptyText: TextView
    private lateinit var simpleList: ListView


    /*val mydb = DatabaseHelper(this)*/
    var dataList =
        ArrayList<HashMap<String, String>>()
    /*var simpleList: ListView? = null
    var empty_text: TextView? = null*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArchivedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseHelper = DatabaseHelper(applicationContext)
        databaseHelper.create_db()
        db = databaseHelper.open()

        simpleList = binding.ListView
        emptyText = binding.emptyText
        simpleList!!.emptyView = emptyText
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
        val cursor = databaseHelper!!.archivedData
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
                mapData["Дата создания"] = cursor.getString(1).toString()
                mapData["Заголовок"] = cursor.getString(3).toString()
                mapData["Текст заметки"] = cursor.getString(4).toString()
                mapData["Дата создания"] = cursor.getString(3).toString()
                mapData["Избранное"] = cursor.getString(5).toString()
                mapData["Статус"] = cursor.getString(6).toString()
                List.add(mapData)
                cursor.moveToNext()
            }
            val adapter = ListNoteBookAdapter(this, List, databaseHelper)
            simpleList!!.adapter = adapter
            simpleList!!.onItemClickListener =
                OnItemClickListener { parent, view, position, id ->
                    val builder =
                        AlertDialog.Builder(this@ArchivedActivity)
                    builder.setTitle(List[+position]["Заголовок"])
                    builder.setMessage(List.get(+position).get("Текст заметки"));
                    builder.setNeutralButton(
                        "Удалить"
                    ) { dialog, id ->
                        databaseHelper!!.deleteNoteBookData(List[+position]["id"]!!)
                        populateData()
                    }
                    builder.setNegativeButton(
                        "Убрать из архива"
                    ) { dialog, id ->
                        databaseHelper!!.updateNoteBookStatus(List[+position]["id"]!!, 0)
                        populateData()
                    }
                    builder.setPositiveButton("Отмена", null)
                    builder.show()
                }
        }
    }
}