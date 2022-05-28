package com.example.foodmap

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.SimpleCursorAdapter
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.foodmap.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var db: SQLiteDatabase
    private lateinit var userCursor: Cursor
    private lateinit var fodmapCursor: Cursor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(applicationContext)
        databaseHelper.create_db()
    }

    override fun onResume() {
        super.onResume()

        db = databaseHelper.open() // открываем подключение
        userCursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.tableProducts, null) // получаем все данные из БД в виде курсора
        val queryFodmap = "SELECT Продукты._id, Продукты.[Название продукта], Продукты.[Наличие глютена], FODMAP.[Единицы измерения гр], FODMAP.[Наличие Olygos (олигосахариды)], FODMAP.[Наличие Fructose (фруктоза)], FODMAP.[Наличие Polyols (полиолы)], FODMAP.[Наличие Lactose (лактоза)]\n" +
                "FROM Продукты INNER JOIN FODMAP ON Продукты.[Название продукта] = FODMAP.[Название продукта]" // запрос к БД на получение информации о продуктах
        fodmapCursor = db.rawQuery(queryFodmap,  null) // совершаем запрос и сохраняем в курсор
        val headers = arrayOf(DatabaseHelper.columnProductsName) // определяем какие столбцы будут показываться в ListView
        val userAdapter = SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, userCursor, headers, intArrayOf(android.R.id.text1),0) // создаем адаптер
        binding.lvProducts.adapter = userAdapter

        // обработка нажатия по элементу списка
        binding.lvProducts.setOnItemClickListener { parent, view, position, id ->
            userCursor.moveToPosition(position) // перемещаемся на нужную позицию курсора
            val itemName = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.columnProductsName)) // извлекаем название продукта и передаем в тайтл AlertDialog
            val itemGluten = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.columnProductsGluten))

            fodmapCursor.moveToPosition(position)
            val itemOlygos = fodmapCursor.getString(fodmapCursor.getColumnIndexOrThrow(DatabaseHelper.columnFodmapOlygos)) // извлекаем характеристики продукта и передаем в AlertDialog
            val itemFructose = fodmapCursor.getString(fodmapCursor.getColumnIndexOrThrow(DatabaseHelper.columnFodmapFructose))
            val itemPolyols = fodmapCursor.getString(fodmapCursor.getColumnIndexOrThrow(DatabaseHelper.columnFodmapPolyols))
            val itemLactose = fodmapCursor.getString(fodmapCursor.getColumnIndexOrThrow(DatabaseHelper.columnFodmapLactose))

            createAlertDialog(itemName, itemOlygos, itemFructose, itemPolyols, itemLactose, itemGluten)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        db.close()
        userCursor.close()
    }

    // Создание Alert Dialog
    private fun createAlertDialog(title: String, olygos: String, fructose: String, polyols: String, lactose: String, gluten: String) {
        val productDialogView = LayoutInflater.from(this).inflate(R.layout.custom_alert_dialog_info_products, null) // inflate Dialog with custom view
        val builder = AlertDialog.Builder(this).setView(productDialogView).setTitle(title) // AlertDialogBuilder

        val textViewOlygos: TextView = productDialogView.findViewById(R.id.Olygos)
        textViewOlygos.setText("Olygos: " + olygos)

        val textViewFructose: TextView = productDialogView.findViewById(R.id.Fructose)
        textViewFructose.setText("Fructose: " + fructose)

        val textViewPolyols: TextView = productDialogView.findViewById(R.id.Polyols)
        textViewPolyols.setText("Polyols: " + polyols)

        val textViewLactose: TextView = productDialogView.findViewById(R.id.Lactose)
        textViewLactose.setText("Lactose: " + lactose)

        val textViewGluten: TextView = productDialogView.findViewById(R.id.Gluten)
        textViewGluten.setText("Наличие глютена: " + gluten)

        builder.setPositiveButton("OK") { dialog, which ->
        }
        builder.show()
    }
}