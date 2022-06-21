package com.example.foodmap

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.SimpleCursorAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.foodmap.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnItemClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var db: SQLiteDatabase
    private lateinit var productCursor: Cursor
    private lateinit var fodmapCursor: Cursor
    private lateinit var productAdapter: ProductAdapter
    private lateinit var recyclerView: RecyclerView

    private fun initial() {
        recyclerView = binding.rvProducts
        productAdapter = ProductAdapter(myProducts(), this)
        recyclerView.adapter = productAdapter
    }

    private fun myProducts(): ArrayList<ProductModel> {
        val productList = ArrayList<ProductModel>()
        val count: Int = productCursor.count
        var i = 0
        while (i < count) {
            productCursor.moveToPosition(i)
            val nameProduct: String = productCursor.getString(productCursor.getColumnIndexOrThrow(DatabaseHelper.columnProductsName))
            val categoryProduct: String = productCursor.getString(productCursor.getColumnIndexOrThrow(DatabaseHelper.columnCategoryName))
            productList.add(ProductModel(nameProduct, categoryProduct)) // сюда надо ссылку на название продукта и категорию по нужной строке
            i++
        }
        return productList
    }

    override fun onItemClicked(product: ProductModel) {
        val queryFodmap = "SELECT Продукты._id, Продукты.[Название продукта], Продукты.[Наличие глютена], FODMAP.[Единицы измерения гр], FODMAP.[Наличие Olygos (олигосахариды)], FODMAP.[Наличие Fructose (фруктоза)], FODMAP.[Наличие Polyols (полиолы)], FODMAP.[Наличие Lactose (лактоза)] FROM Продукты INNER JOIN FODMAP ON Продукты.[Название продукта] = FODMAP.[Название продукта] WHERE Продукты.[Название продукта] = \"${product.product}\""
        fodmapCursor = db.rawQuery(queryFodmap,  null)
        fodmapCursor.moveToFirst()
        val itemOlygos = fodmapCursor.getString(fodmapCursor.getColumnIndexOrThrow(DatabaseHelper.columnFodmapOlygos)) // извлекаем характеристики продукта и передаем в AlertDialog
        val itemFructose = fodmapCursor.getString(fodmapCursor.getColumnIndexOrThrow(DatabaseHelper.columnFodmapFructose))
        val itemPolyols = fodmapCursor.getString(fodmapCursor.getColumnIndexOrThrow(DatabaseHelper.columnFodmapPolyols))
        val itemLactose = fodmapCursor.getString(fodmapCursor.getColumnIndexOrThrow(DatabaseHelper.columnFodmapLactose))
        val itemGluten = fodmapCursor.getString(fodmapCursor.getColumnIndexOrThrow(DatabaseHelper.columnProductsGluten))
        createAlertDialog("${product.product}", itemOlygos, itemFructose, itemPolyols, itemLactose, itemGluten)
    }

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

        val queryFodmap = "SELECT Продукты._id, Продукты.[Название продукта], Продукты.[Наличие глютена], FODMAP.[Единицы измерения гр], FODMAP.[Наличие Olygos (олигосахариды)], FODMAP.[Наличие Fructose (фруктоза)], FODMAP.[Наличие Polyols (полиолы)], FODMAP.[Наличие Lactose (лактоза)]\n" +
                "FROM Продукты INNER JOIN FODMAP ON Продукты.[Название продукта] = FODMAP.[Название продукта]" // запрос к БД на получение информации о продуктах
        fodmapCursor = db.rawQuery(queryFodmap,  null)

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
    }
}