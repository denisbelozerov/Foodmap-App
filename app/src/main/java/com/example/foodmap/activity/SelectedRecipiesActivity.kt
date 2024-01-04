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
import com.example.foodmap.databinding.ActivityRecipiesBinding
import com.example.foodmap.databinding.ActivitySelectedRecipiesBinding
import com.example.foodmap.models.RecipiesModel
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem

class SelectedRecipies : AppCompatActivity(), OnItemClickListenerFavouriteRecipe {
    private lateinit var binding: ActivitySelectedRecipiesBinding
    private lateinit var databaseHelper: DatabaseHelper
    lateinit var db: SQLiteDatabase
    private lateinit var recipiesCursor: Cursor
    private lateinit var descriptionRecipeCursor: Cursor
    private lateinit var favouriteRecipiesAdapter: FavouriteRecipiesAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var mDrawer: Drawer
    private lateinit var mHeader: AccountHeader
    private lateinit var mToolbar: Toolbar


    private fun initial() {
        recyclerView = binding.rvRecipes
        favouriteRecipiesAdapter = FavouriteRecipiesAdapter(myRecipies(), this)
        recyclerView.adapter = favouriteRecipiesAdapter
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
                        val intentMyProducts = Intent(this@SelectedRecipies, SelectedProducts::class.java)
                        val intentMyRecipies = Intent(this@SelectedRecipies, SelectedRecipies::class.java)
                        val intentAllProducts = Intent(this@SelectedRecipies, MainActivity::class.java)
                        val intentAllRecipies = Intent(this@SelectedRecipies, RecipiesActivity::class.java)
                        val intentUserInfo = Intent(this@SelectedRecipies, ProfileUserActivity::class.java)

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

    private fun myRecipies(): ArrayList<RecipiesModel> {
        val recipiesList = ArrayList<RecipiesModel>()
        val count: Int = recipiesCursor.count

        var i = 0
        while (i < count) {
            var colorSafety = getColor(R.color.green)
            var favouriteProduct = R.drawable.baseline_favorite_border_24

            recipiesCursor.moveToPosition(i)
            val nameRecipe: String =
                recipiesCursor.getString(recipiesCursor.getColumnIndexOrThrow(DatabaseHelper.columnRecipiesName))
            val typeRecipe: String =
                recipiesCursor.getString(recipiesCursor.getColumnIndexOrThrow(DatabaseHelper.columnRecipiesType))
            val typeCusine: String =
                recipiesCursor.getString(recipiesCursor.getColumnIndexOrThrow(DatabaseHelper.columnRecipiesCusine))
            val recipeSafetyIndicator: String =
                recipiesCursor.getString(recipiesCursor.getColumnIndexOrThrow(DatabaseHelper.columnSafetyIndicator))
            val favouriteRecipeIndicator: Int =
                recipiesCursor.getInt(recipiesCursor.getColumnIndexOrThrow(DatabaseHelper.columnFavouriteRecipies))

            when (recipeSafetyIndicator) {
                "Безопасен" -> colorSafety = getColor(R.color.green)
                "Небезопасен" -> colorSafety = getColor(R.color.red)
                "Умеренно безопасен" -> colorSafety = getColor(R.color.yellow)
            }

            when (favouriteRecipeIndicator) {
                0 -> favouriteProduct = R.drawable.baseline_favorite_border_24
                1 -> favouriteProduct = R.drawable.baseline_favorite_24
            }

            recipiesList.add(RecipiesModel(nameRecipe, typeRecipe, typeCusine, colorSafety, favouriteProduct))
            i++
        }
        return recipiesList
    }

    private fun createAlertDialog(
        title: String,
        descriptionRecipe: String,
    ) {
        val productDialogView =
            LayoutInflater.from(this).inflate(R.layout.custom_alert_dialog_info_recipe, null)
        val builder = AlertDialog.Builder(this).setView(productDialogView).setTitle(title)

        val textViewRecipeDescription: TextView = productDialogView.findViewById(R.id.recipe_description)

        textViewRecipeDescription.setText(descriptionRecipe)

        builder.setPositiveButton("OK") { dialog, which ->
        }
        builder.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectedRecipiesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseHelper = DatabaseHelper(applicationContext)
        databaseHelper.create_db()
    }

    override fun onStart() {
        super.onStart()
        db = databaseHelper.open()
        recipiesCursor = db.rawQuery(DatabaseHelper.Companion.queryFavouriteRecipies, null)
        initial()
    }


    override fun onDestroy() {
        super.onDestroy()
        recipiesCursor.close()
        db.close()
    }


    override fun onItemClickedFavouriteRecipe(recipies: RecipiesModel) {
        descriptionRecipeCursor = db.rawQuery(
            DatabaseHelper.queryAllRecipies + "WHERE Рецепты.[Название] = \"${recipies.recipe}\"",
            null
        )
        descriptionRecipeCursor.moveToFirst()
        val descriptionRecipe =
            descriptionRecipeCursor.getString(descriptionRecipeCursor.getColumnIndexOrThrow(DatabaseHelper.columnRecipiesDescription))
        createAlertDialog(
            "${recipies.recipe}",
            descriptionRecipe,
        )
    }
}