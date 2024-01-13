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

        var title = binding.title
        var description = binding.description
        var save_btn = binding.saveBtn

        val i = intent
        i_action = i.getStringExtra("action").toString()
        if (i_action!!.trim().contentEquals("edit")) {
            supportActionBar!!.setTitle("Изменить заметку")
        } else {
            supportActionBar!!.setTitle("Add New")
        }

        if (i_action!!.trim().contentEquals("edit")) {
            i_id = i.getStringExtra("id")
            val i_title = i.getStringExtra("Заголовок")
            val i_des = i.getStringExtra("Текст")

            title!!.setText("$i_title")
            description!!.setText("$i_des")
            save_btn!!.setText("Сохранить")
        }

        save_btn.setOnClickListener(View.OnClickListener {
            title_data = title!!.text.toString()
            des_data = description!!.text.toString()
            if (i_action!!.trim { it <= ' ' }.contentEquals("edit") && title_data!!.length > 3) {
                if (title_data!!.trim { it <= ' ' }.length > 0) {
                    databaseHelper!!.updateNoteBookData(id = i_id!!, title = title_data, description = des_data)
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
                    databaseHelper!!.insertNoteBookData(
                        title = title_data,
                        description = des_data,
                        created_at = date.toString()
                    )
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
    }
}