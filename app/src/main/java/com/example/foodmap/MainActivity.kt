package com.example.foodmap

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.SimpleCursorAdapter
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.foodmap.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var db: SQLiteDatabase
    private lateinit var productCursor: Cursor
    private lateinit var fodmapCursor: Cursor
    private lateinit var productAdapter: ProductAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(applicationContext)
        databaseHelper.create_db()
    }

    override fun onResume() {
        super.onResume()
        db = databaseHelper.open()

        val queryProducts = "SELECT * FROM " + DatabaseHelper.tableProducts + " INNER JOIN " + DatabaseHelper.tableCategory + " ON " + "Продукты.[Категория продукта] = Категория.[Код категории]"
        productCursor = db.rawQuery(queryProducts, null)

        initial()

        /*
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
        */
    }

    override fun onDestroy() {
        super.onDestroy()
        db.close()
        productCursor.close()
    }

    private fun initial() {
        recyclerView = binding.rvProducts
        productAdapter = ProductAdapter()
        recyclerView.adapter = productAdapter
        productAdapter.setList(myProducts())
    }

    fun myProducts(): ArrayList<ProductModel> {
        val productList = ArrayList<ProductModel>()
        val count: Int = productCursor.count
        var i = 0
        while (i < count) {
            productCursor.moveToPosition(i)
            val nameProduct: String = productCursor.getString(productCursor.getColumnIndexOrThrow(DatabaseHelper.columnProductsName))
            val categoryProduct: String = productCursor.getString(productCursor.getColumnIndexOrThrow(DatabaseHelper.columnCategoryName))
            productList.add(ProductModel(nameProduct, categoryProduct))
            i++
        }
        return productList
    }

    /*
    // Создание Alert Dialog
    private fun createAlertDialog(title: String, olygos: String, fructose: String, polyols: String, lactose: String, gluten: String) {
        val productDialogView = LayoutInflater.from(this).inflate(R.layout.custom_alert_dialog_info_products, null)
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
    } */
}