package com.example.foodmap

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.example.foodmap.databinding.ActivityMainBinding
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem

class MainActivity : AppCompatActivity(), OnItemClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var databaseHelper: DatabaseHelper
    lateinit var db: SQLiteDatabase
    private lateinit var productCursor: Cursor
    private lateinit var fodmapCursor: Cursor
    private lateinit var productAdapter: ProductAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var mDrawer: Drawer
    private lateinit var mHeader: AccountHeader
    private lateinit var mToolbar: Toolbar

    private fun initial() {
        recyclerView = binding.rvProducts
        productAdapter = ProductAdapter(myProducts(), this)
        recyclerView.adapter = productAdapter
        mToolbar = binding.mainToolbar

        setSupportActionBar(mToolbar)
        createHeader()
        createDrawer()
    }

    private fun createDrawer() {
        mDrawer =
            DrawerBuilder()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withActionBarDrawerToggle(true)
                .withSelectedItem(-1)
                .withAccountHeader(mHeader)
                .addDrawerItems(
                    PrimaryDrawerItem().withIdentifier(1)
                        .withIconTintingEnabled(true)
                        .withName("Профиль пользователя")
                        .withSelectable(false),
                    PrimaryDrawerItem().withIdentifier(2)
                        .withIconTintingEnabled(true)
                        .withName("Мой дневник")
                        .withSelectable(false),
                    PrimaryDrawerItem().withIdentifier(3)
                        .withIconTintingEnabled(true)
                        .withName("Мои продукты")
                        .withSelectable(false),
                    PrimaryDrawerItem().withIdentifier(4)
                        .withIconTintingEnabled(true)
                        .withName("Мои рецепты")
                        .withSelectable(false),
                    PrimaryDrawerItem().withIdentifier(5)
                        .withIconTintingEnabled(true)
                        .withName("Выход")
                        .withSelectable(false)
                ).withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                    override fun onItemClick(
                        view: View?,
                        position: Int,
                        drawerItem: IDrawerItem<*>
                    ): Boolean {
                        val newIntent = Intent(this@MainActivity, SelectedProducts::class.java)

                        when (position) {
                            3 -> startActivity(newIntent)
                        }
                        return false
                    }
                }).build()
    }

    private fun createHeader() {
        mHeader = AccountHeaderBuilder()
            .withActivity(this)
            .withHeaderBackground(R.drawable.header)
            .addProfiles(
                ProfileDrawerItem()
                    .withName("Денис Белозёров")
                    .withIcon(R.drawable._avatar180)
                    .withEmail("denis.belozerov@edu.hse.ru")
            )
            .build()
    }

    private fun myProducts(): ArrayList<ProductModel> {
        val productList = ArrayList<ProductModel>()
        val count: Int = productCursor.count

        var i = 0
        while (i < count) {
            var colorSafety = getColor(R.color.green) // храним цвет каждого продукта
            var favouriteProduct = R.drawable.baseline_favorite_border_24

            productCursor.moveToPosition(i)
            val nameProduct: String =
                productCursor.getString(productCursor.getColumnIndexOrThrow(DatabaseHelper.columnProductsName))
            val categoryProduct: String =
                productCursor.getString(productCursor.getColumnIndexOrThrow(DatabaseHelper.columnCategoryName))
            val safetyIndicator: String =
                productCursor.getString(productCursor.getColumnIndexOrThrow(DatabaseHelper.columnSafetyIndicator))
            val favouriteIndicator: Int =
                productCursor.getInt(productCursor.getColumnIndexOrThrow(DatabaseHelper.columnFavouriteProduct))

            when (safetyIndicator) {
                "Безопасен" -> colorSafety = getColor(R.color.green)
                "Небезопасен" -> colorSafety = getColor(R.color.red)
                "Умеренно безопасен" -> colorSafety = getColor(R.color.yellow)
            }

            when (favouriteIndicator) {
                0 -> favouriteProduct = R.drawable.baseline_favorite_border_24
                1 -> favouriteProduct = R.drawable.baseline_favorite_24
            }

            productList.add(ProductModel(nameProduct, categoryProduct, colorSafety, favouriteProduct))
            i++
        }
        return productList
    }

    private fun createAlertDialog(
        title: String,
        olygos: String,
        fructose: String,
        polyols: String,
        lactose: String,
        weight: String
    ) {
        val productDialogView =
            LayoutInflater.from(this).inflate(R.layout.custom_alert_dialog_info_products_1, null)
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

    override fun onItemClicked(product: ProductModel) {
        fodmapCursor = db.rawQuery(
            DatabaseHelper.Companion.queryFodmap + "WHERE Продукты.[Название продукта] = \"${product.product}\"",
            null
        )
        fodmapCursor.moveToFirst()
        val itemOlygos =
            fodmapCursor.getString(fodmapCursor.getColumnIndexOrThrow(DatabaseHelper.columnFodmapOlygos)) // извлекаем характеристики продукта и передаем в AlertDialog
        val itemFructose =
            fodmapCursor.getString(fodmapCursor.getColumnIndexOrThrow(DatabaseHelper.columnFodmapFructose))
        val itemPolyols =
            fodmapCursor.getString(fodmapCursor.getColumnIndexOrThrow(DatabaseHelper.columnFodmapPolyols))
        val itemLactose =
            fodmapCursor.getString(fodmapCursor.getColumnIndexOrThrow(DatabaseHelper.columnFodmapLactose))
        val itemWeight =
            fodmapCursor.getString(fodmapCursor.getColumnIndexOrThrow(DatabaseHelper.columnFodmapUnit))
        createAlertDialog(
            "${product.product}",
            itemOlygos,
            itemFructose,
            itemPolyols,
            itemLactose,
            itemWeight
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(applicationContext)
        databaseHelper.create_db()
    }

    override fun onStart() {
        super.onStart()
        db = databaseHelper.open()
        productCursor = db.rawQuery(DatabaseHelper.Companion.queryProducts, null)
        fodmapCursor = db.rawQuery(DatabaseHelper.Companion.queryFodmap, null)
        initial()
    }

    override fun onDestroy() {
        super.onDestroy()
        db.close()
        productCursor.close()
    }
}