package com.example.foodmap.activity

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.foodmap.DatabaseHelper
import com.example.foodmap.ListNoteBookAdapter
import com.example.foodmap.R
import com.example.foodmap.databinding.ActivityMenuBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import kotlin.collections.ArrayList

class NotesActivity : AppCompatActivity() {
    /*val mydb = DatabaseHelper(this)
    var dataList =
        ArrayList<HashMap<String, String>>()
    var simpleList: ListView? = null
    var add_button: FloatingActionButton? = null
    var empty_text: TextView? = null*/

    private lateinit var binding: ActivityMenuBinding
    private lateinit var databaseHelper: DatabaseHelper
    lateinit var db: SQLiteDatabase
    private lateinit var dataList: ArrayList<HashMap<String, String>>
    private lateinit var add_button: FloatingActionButton
    private lateinit var empty_text: TextView
    private lateinit var simpleList: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dataList = ArrayList<HashMap<String, String>>()

        databaseHelper = DatabaseHelper(applicationContext)
        databaseHelper.create_db()

        // setContentView(R.layout.activity_menu)
        // empty_text = findViewById<View>(R.id.empty_text) as TextView
        // simpleList = findViewById<View>(R.id.ListView) as ListView

        empty_text = binding.emptyText
        simpleList = binding.ListView

        simpleList!!.emptyView = empty_text
        add_button = binding.addButton
        add_button!!.setOnClickListener(View.OnClickListener {
            val i = Intent(this, AddAndModifyNoteBookData::class.java)
            i.putExtra("action", "add")
            startActivity(i)
        })
    }

    override fun onStart() {
        super.onStart()
        db = databaseHelper.open()
        populateData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.archive -> {
                startActivity(Intent(this@NotesActivity, ArchivedActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        // populateData()
    }

    override fun onDestroy() {
        super.onDestroy()
        db.close()
    }

    fun populateData() {
        runOnUiThread { fetchDataFromDB() }
    }

    fun fetchDataFromDB() {
        dataList.clear()
        val cursor = databaseHelper!!.noteBookData
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
                mapData["Текст заметки"] = cursor.getString(+
                4).toString()
                mapData["Избранное"] = cursor.getString(5).toString()
                mapData["Статус"] = cursor.getString(6).toString()
                List.add(mapData)
                cursor.moveToNext()
            }
            val adapter = ListNoteBookAdapter(this@NotesActivity, List, databaseHelper)
            simpleList!!.adapter = adapter
            simpleList!!.onItemClickListener =
                OnItemClickListener { parent, view, position, id ->
                    val builder =
                        AlertDialog.Builder(this@NotesActivity)
                    builder.setTitle(List[+position]["Заголовок"])
                    builder.setMessage(List[+position]["Текст заметки"])
                    builder.setNeutralButton(
                        "ARCHIVE"
                    ) { dialog, id ->
                        databaseHelper!!.updateNoteBookStatus(List[+position]["id"]!!, 1)
                        populateData()
                    }
                    builder.setNegativeButton(
                        "EDIT"
                    ) { dialog, id ->
                        val i =
                            Intent(this@NotesActivity, AddAndModifyNoteBookData::class.java)
                        i.putExtra("id", List[+position]["id"])
                        i.putExtra("Заголовок", List[+position]["Заголовок"])
                        i.putExtra("Текст заметки", List[+position]["Текст заметки"])
                        i.putExtra("action", "edit")
                        startActivity(i)
                    }
                    builder.setPositiveButton("CANCEL", null)
                    builder.show()
                }
        }
    }
}