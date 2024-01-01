package com.example.foodmap.activity

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.foodmap.DatabaseHelper
import com.example.foodmap.R
import com.example.foodmap.RecipiesActivity
import com.example.foodmap.SelectedProducts
import com.example.foodmap.SelectedRecipies
import com.example.foodmap.databinding.ActivityProfileUserBinding
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem

class ProfileUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileUserBinding
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var btnChangeData: Button
    lateinit var db: SQLiteDatabase
    private lateinit var mDrawer: Drawer
    private lateinit var mHeader: AccountHeader
    private lateinit var mToolbar: Toolbar
    private lateinit var infoUserCursor: Cursor

    private fun initial() {
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
                        val intentMyProducts =
                            Intent(this@ProfileUserActivity, SelectedProducts::class.java)
                        val intentMyRecipies =
                            Intent(this@ProfileUserActivity, SelectedRecipies::class.java)
                        val intentAllProducts =
                            Intent(this@ProfileUserActivity, MainActivity::class.java)
                        val intentAllRecipies =
                            Intent(this@ProfileUserActivity, RecipiesActivity::class.java)
                        val intentUserInfo =
                            Intent(this@ProfileUserActivity, ProfileUserActivity::class.java)

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
        mHeader = AccountHeaderBuilder()
            .withActivity(this)
            .withHeaderBackground(R.drawable.header)
            .addProfiles(
                ProfileDrawerItem()
                    .withName("Денис Белозёров")
                    .withIcon(R.drawable._avatar180)
                    .withEmail("denis.belozeroov@edu.hse.ru")
            )
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileUserBinding.inflate(layoutInflater)
        val myFamily = binding.myFamily
        val myName = binding.myName
        val myMiddleName = binding.myMiddleName
        val myEmail = binding.myEmail
        val myCity = binding.myCity
        val myFullName = binding.fullName

        setContentView(binding.root)
        databaseHelper = DatabaseHelper(applicationContext)
        databaseHelper.create_db()

        btnChangeData = binding.btnEditProfile
        btnChangeData.setOnClickListener {
            if (btnChangeData.text == "Изменить") {
                btnChangeData.setText("Сохранить")

                myFamily.isFocusable = true
                myFamily.isClickable = true
                myFamily.isCursorVisible = true
                myFamily.isEnabled = true
                myName.isFocusable = true
                myName.isClickable = true
                myName.isCursorVisible = true
                myName.isEnabled = true
                myMiddleName.isFocusable = true
                myMiddleName.isClickable = true
                myMiddleName.isCursorVisible = true
                myMiddleName.isEnabled = true
                myEmail.isFocusable = true
                myEmail.isClickable = true
                myEmail.isCursorVisible = true
                myEmail.isEnabled = true
                myCity.isFocusable = true
                myCity.isClickable = true
                myCity.isCursorVisible = true
                myCity.isEnabled = true

            } else {
                var fields = listOf<String>(
                    myFamily.text.toString(),
                    myName.text.toString(),
                    myMiddleName.text.toString(),
                    myEmail.text.toString(),
                    myCity.text.toString()
                )
                databaseHelper.updateInfoUser(fields)

                btnChangeData.setText("Изменить")
                myFamily.isFocusable = false
                myFamily.isClickable = false
                myFamily.isCursorVisible = false
                myFamily.isEnabled = false
                myName.isFocusable = false
                myName.isClickable = false
                myName.isCursorVisible = false
                myName.isEnabled = false
                myMiddleName.isFocusable = false
                myMiddleName.isClickable = false
                myMiddleName.isCursorVisible = false
                myMiddleName.isEnabled = false
                myEmail.isFocusable = false
                myEmail.isClickable = false
                myEmail.isCursorVisible = false
                myEmail.isEnabled = false
                myCity.isFocusable = false
                myCity.isClickable = false
                myCity.isCursorVisible = false
                myCity.isEnabled = false


            }
        }
    }

    override fun onStart() {
        super.onStart()
        db = databaseHelper.open()
        infoUserCursor = db.rawQuery(DatabaseHelper.queryInfoUser, null)
        initial()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}