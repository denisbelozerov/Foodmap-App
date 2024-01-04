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
import com.example.foodmap.activity.MainActivity
import com.example.foodmap.activity.ProfileUserActivity
import com.example.foodmap.databinding.ActivitySelectedProductsBinding
import com.example.foodmap.models.ProductModel
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem

class SelectedProducts : AppCompatActivity(), OnItemClickListenerProduct {
    private lateinit var binding: ActivitySelectedProductsBinding
    private lateinit var databaseHelper: DatabaseHelper
    lateinit var db: SQLiteDatabase
    private lateinit var favouriteProductCursor: Cursor
    private lateinit var fodmapCursor: Cursor
    private lateinit var favouriteProductAdapter: FavouriteProductAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var mDrawer: Drawer
    private lateinit var mHeader: AccountHeader
    private lateinit var mToolbar: Toolbar


    private fun initial() {
        recyclerView = binding.rvSelectedProducts
        favouriteProductAdapter = FavouriteProductAdapter(myProducts(), this)
        recyclerView.adapter = favouriteProductAdapter
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
                        .withName("Все продукты")
                        .withSelectable(false),
                    PrimaryDrawerItem().withIdentifier(2)
                        .withIconTintingEnabled(true)
                        .withName("Все рецепты")
                        .withSelectable(false),
                    PrimaryDrawerItem().withIdentifier(3)
                        .withIconTintingEnabled(true)
                        .withName("Профиль пользователя")
                        .withSelectable(false),
                    PrimaryDrawerItem().withIdentifier(4)
                        .withIconTintingEnabled(true)
                        .withName("Мой дневник")
                        .withSelectable(false),
                    PrimaryDrawerItem().withIdentifier(5)
                        .withIconTintingEnabled(true)
                        .withName("Мои продукты")
                        .withSelectable(false),
                    PrimaryDrawerItem().withIdentifier(6)
                        .withIconTintingEnabled(true)
                        .withName("Мои рецепты")
                        .withSelectable(false),
                    PrimaryDrawerItem().withIdentifier(7)
                        .withIconTintingEnabled(true)
                        .withName("Выход")
                        .withSelectable(false)
                ).withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                    override fun onItemClick(
                        view: View?,
                        position: Int,
                        drawerItem: IDrawerItem<*>
                    ): Boolean {
                        val intentMyProducts = Intent(this@SelectedProducts, SelectedProducts::class.java)
                        val intentMyRecipies = Intent(this@SelectedProducts, SelectedRecipies::class.java)
                        val intentAllProducts = Intent(this@SelectedProducts, MainActivity::class.java)
                        val intentAllRecipies = Intent(this@SelectedProducts, RecipiesActivity::class.java)
                        val intentUserInfo = Intent(this@SelectedProducts, ProfileUserActivity::class.java)

                        when (position) {
                            1 -> startActivity(intentAllProducts)
                            2 -> startActivity(intentAllRecipies)
                            3 -> startActivity(intentUserInfo)
                            5 -> startActivity(intentMyProducts)
                            6 -> startActivity(intentMyRecipies)
                        }
                        return false
                    }
                }).build()
    }

    private fun createHeader() {
        val userInfo = databaseHelper.getInfoUser()
        mHeader = AccountHeaderBuilder()
            .withActivity(this)
            .withHeaderBackground(R.drawable.header)
            .addProfiles(
                ProfileDrawerItem()
                    .withName("${userInfo[1]} ${userInfo[0]}")
                    .withIcon(R.drawable._avatar180)
                    .withEmail("${userInfo[3]}")
            )
            .build()
    }

    private fun myProducts(): ArrayList<ProductModel> {
        val productList = ArrayList<ProductModel>()
        val count: Int = favouriteProductCursor.count

        var i = 0
        while (i < count) {
            var colorSafety = getColor(R.color.green)
            var favouriteProduct = R.drawable.baseline_favorite_border_24

            favouriteProductCursor.moveToPosition(i)
            val nameProduct: String =
                favouriteProductCursor.getString(favouriteProductCursor.getColumnIndexOrThrow(DatabaseHelper.columnProductsName))
            val categoryProduct: String =
                favouriteProductCursor.getString(favouriteProductCursor.getColumnIndexOrThrow(DatabaseHelper.columnCategoryName))
            val safetyIndicator: String =
                favouriteProductCursor.getString(favouriteProductCursor.getColumnIndexOrThrow(DatabaseHelper.columnSafetyIndicator))
            val favouriteIndicator: Int =
                favouriteProductCursor.getInt(favouriteProductCursor.getColumnIndexOrThrow(DatabaseHelper.columnFavouriteProduct))

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectedProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseHelper = DatabaseHelper(applicationContext)
        databaseHelper.create_db()
    }

    override fun onStart() {
        super.onStart()
        db = databaseHelper.open()
        favouriteProductCursor = db.rawQuery(DatabaseHelper.Companion.queryFavouriteProducts, null)
        fodmapCursor = db.rawQuery(DatabaseHelper.Companion.queryFodmap, null)
        initial()
    }


    override fun onDestroy() {
        super.onDestroy()
        favouriteProductCursor.close()
        db.close()
    }

    override fun onItemClickedProduct(product: ProductModel) {
        fodmapCursor = db.rawQuery(
            DatabaseHelper.queryFodmap + "WHERE Продукты.[Название продукта] = \"${product.product}\"",
            null
        )
        fodmapCursor.moveToFirst()
        val itemOlygos =
            fodmapCursor.getString(fodmapCursor.getColumnIndexOrThrow(DatabaseHelper.columnFodmapOlygos))
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
}