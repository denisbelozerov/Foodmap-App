package com.example.foodmap.activity

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.example.foodmap.DatabaseHelper
import com.example.foodmap.ProductAdapter
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
                    .withEmail("denis.belozerov@edu.hse.ru")
            )
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseHelper = DatabaseHelper(applicationContext)
        databaseHelper.create_db()

        btnChangeData = binding.btnEditProfile
        btnChangeData.setOnClickListener {
            if (btnChangeData.text == "Изменить") {
                btnChangeData.setText("Сохранить")
                binding.myFamily.isFocusable = true
                binding.myFamily.isClickable = true
                binding.myFamily.isCursorVisible = true
                binding.myFamily.isEnabled = true
                binding.myName.isFocusable = true
                binding.myName.isClickable = true
                binding.myName.isCursorVisible = true
                binding.myName.isEnabled = true
                binding.myMiddleName.isFocusable = true
                binding.myMiddleName.isClickable = true
                binding.myMiddleName.isCursorVisible = true
                binding.myMiddleName.isEnabled = true
                binding.myEmail.isFocusable = true
                binding.myEmail.isClickable = true
                binding.myEmail.isCursorVisible = true
                binding.myEmail.isEnabled = true
                binding.myCity.isFocusable = true
                binding.myCity.isClickable = true
                binding.myCity.isCursorVisible = true
                binding.myCity.isEnabled = true

            } else {
                btnChangeData.setText("Изменить")
                binding.myFamily.isFocusable = false
                binding.myFamily.isClickable = false
                binding.myFamily.isCursorVisible = false
                binding.myFamily.isEnabled = false
                binding.myName.isFocusable = false
                binding.myName.isClickable = false
                binding.myName.isCursorVisible = false
                binding.myName.isEnabled = false
                binding.myMiddleName.isFocusable = false
                binding.myMiddleName.isClickable = false
                binding.myMiddleName.isCursorVisible = false
                binding.myMiddleName.isEnabled = false
                binding.myEmail.isFocusable = false
                binding.myEmail.isClickable = false
                binding.myEmail.isCursorVisible = false
                binding.myEmail.isEnabled = false
                binding.myCity.isFocusable = false
                binding.myCity.isClickable = false
                binding.myCity.isCursorVisible = false
                binding.myCity.isEnabled = false
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