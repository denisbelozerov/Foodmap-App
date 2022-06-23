package com.example.foodmap

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
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
            productList.add(ProductModel(nameProduct, categoryProduct))
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
        val itemWeight = fodmapCursor.getString(fodmapCursor.getColumnIndexOrThrow(DatabaseHelper.columnFodmapUnit))
        createAlertDialog("${product.product}", itemOlygos, itemFructose, itemPolyols, itemLactose, itemWeight)
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
        val queryFodmap = "SELECT Продукты._id, Продукты.[Название продукта], Продукты.[Наличие глютена], FODMAP.[Единицы измерения гр], FODMAP.[Наличие Olygos (олигосахариды)], FODMAP.[Наличие Fructose (фруктоза)], FODMAP.[Наличие Polyols (полиолы)], FODMAP.[Наличие Lactose (лактоза)]\n" +
                "FROM Продукты INNER JOIN FODMAP ON Продукты.[Название продукта] = FODMAP.[Название продукта]" // запрос к БД на получение информации о продуктах

        productCursor = db.rawQuery(queryProducts, null)
        fodmapCursor = db.rawQuery(queryFodmap,  null)

        initial()
    }

    override fun onDestroy() {
        super.onDestroy()
        db.close()
        productCursor.close()
    }

    // Создание Alert Dialog
    private fun createAlertDialog(title: String, olygos: String, fructose: String, polyols: String, lactose: String, weight: String) {
        val productDialogView = LayoutInflater.from(this).inflate(R.layout.custom_alert_dialog_info_products_1, null)
        val builder = AlertDialog.Builder(this).setView(productDialogView).setTitle(title)

        val textViewWeight: TextView = productDialogView.findViewById(R.id.Product_weight_1_1_1)
        val textViewOlygos: TextView = productDialogView.findViewById(R.id.Olygos_1_1_2)
        val textViewFructose: TextView = productDialogView.findViewById(R.id.Fructose_1_1_3)
        val textViewPolyols: TextView = productDialogView.findViewById(R.id.Polyols_1_1_4)
        val textViewLactose: TextView = productDialogView.findViewById(R.id.Lactose_1_1_5)

        textViewWeight.setText(weight + " гр")
        textViewOlygos.setText(olygos)
        textViewFructose.setText(fructose)
        textViewPolyols.setText(polyols)
        textViewLactose.setText(lactose)

        builder.setPositiveButton("OK") { dialog, which ->
        }
        builder.show()
    }
}