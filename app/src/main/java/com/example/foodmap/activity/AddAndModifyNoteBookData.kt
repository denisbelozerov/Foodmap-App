package com.example.foodmap.activity

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foodmap.DatabaseHelper
import com.example.foodmap.R
import com.example.foodmap.databinding.ActivityAddModifyNotebookDataBinding
import com.example.foodmap.databinding.ActivityMenuBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class AddAndModifyNoteBookData : AppCompatActivity() {
    /*val mydb = DatabaseHelper(this)
    var title_data: String? = null
    var des_data: String? = null
    var i_action: String? = null
    var i_id: String? = null*/

    private lateinit var binding: ActivityAddModifyNotebookDataBinding
    private lateinit var databaseHelper: DatabaseHelper
    lateinit var db: SQLiteDatabase
    private lateinit var title_data: String
    private lateinit var des_data: String
    private lateinit var i_action: String
    private var i_id: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddModifyNotebookDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseHelper = DatabaseHelper(applicationContext)
        databaseHelper.create_db()
        db = databaseHelper.open()

        val i = intent
        i_action = i.getStringExtra("action").toString()
        if (i_action!!.trim().contentEquals("edit")) {
            supportActionBar!!.setTitle("Udpate")
        } else {
            supportActionBar!!.setTitle("Add New")
        }
        setContentView(R.layout.activity_add_modify_notebook_data)
        var title = findViewById(R.id.title) as TextView
        var description = findViewById(R.id.description) as TextView
        var save_btn = findViewById(R.id.save_btn) as Button
        if (i_action!!.trim().contentEquals("edit")) {
            i_id = i.getStringExtra("id")
            val i_title = i.getStringExtra("title")
            val i_des = i.getStringExtra("description")
            title!!.text  = i_title
            description!!.text = i_des
            save_btn!!.text = "SAVE"
        }
        save_btn.setOnClickListener(View.OnClickListener {
            title_data = title!!.text.toString()
            des_data = description!!.text.toString()
            if (i_action!!.trim { it <= ' ' }.contentEquals("edit") && title_data!!.length > 3) {
                if (title_data!!.trim { it <= ' ' }.length > 0) {
                    databaseHelper!!.updateNoteBookData(i_id!!, title_data, des_data)
                    finish()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Заголовок не заполнен!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            if (i_action!!.trim { it <= ' ' }.contentEquals("add")) {
                val date =
                    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        .format(Date())
                if (title_data!!.trim { it <= ' ' }.length > 0) {
                    databaseHelper!!.insertNoteBookData(title_data, des_data, date.toString())
                    finish()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Заголовок не заполнен!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })


        //mydb.insertNoteBookData(title_data, des_data, date.toString());
    }
}