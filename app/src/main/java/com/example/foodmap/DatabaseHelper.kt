package com.example.foodmap

import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class DatabaseHelper(private val myContext: Context) : SQLiteOpenHelper(
    myContext, DATABASE_NAME, null, DATABASE_VERSION
) {
    init {
        DATABASE_PATH = myContext.filesDir.path + DATABASE_NAME
    }

    companion object {
        private lateinit var DATABASE_PATH: String
        private const val DATABASE_NAME = "fodmap_ver3.db"
        private const val DATABASE_VERSION = 2
        const val tableProducts = "Продукты"
        const val tableRecipies = "Рецепты"
        const val tableUsers = "Пользователь"

        const val columnProducts_id = "_id"
        const val columnRecipies_id = "id"
        const val columnProductsName = "Название продукта"
        const val columnRecipiesName = "Название"
        const val columnFamily = "Фамилия"
        const val columnRecipiesDescription = "Описание"
        const val columnRecipiesCategory = "Название категории"
        const val columnRecipiesCusine = "Название кухни"
        const val columnRecipiesType = "Название категории"

        const val columnSafetyIndicator = "Индикатор безопасности"
        const val columnCategoryName = "Название категории"
        const val columnFodmapUnit = "Единицы измерения гр"
        const val columnFodmapOlygos = "Наличие Olygos (олигосахариды)"
        const val columnFodmapFructose = "Наличие Fructose (фруктоза)"
        const val columnFodmapPolyols = "Наличие Polyols (полиолы)"
        const val columnFodmapLactose = "Наличие Lactose (лактоза)"
        const val columnFavouriteProduct = "Избранное"
        const val columnFavouriteRecipies = "Избранное"

        const val columnUser_id = "id"
        const val columnUserFamily = "Фамилия"
        const val columnUserName = "Имя"
        const val columnUserMiddleName = "Отчество"
        const val columnUserCity = "Город"
        const val columnUserEmail = "Email"

        const val queryFodmap =
            "SELECT Продукты._id, Продукты.[Название продукта], Продукты.[Наличие глютена], FODMAP.[Единицы измерения гр], FODMAP.[Наличие Olygos (олигосахариды)], FODMAP.[Наличие Fructose (фруктоза)], FODMAP.[Наличие Polyols (полиолы)], FODMAP.[Наличие Lactose (лактоза)] FROM Продукты INNER JOIN FODMAP ON Продукты.[Название продукта] = FODMAP.[Название продукта]"
        const val queryProducts =
            "SELECT * FROM Продукты INNER JOIN Категория ON Продукты.[Категория продукта] = Категория.[Код категории]"
        const val queryFavouriteProducts =
            "SELECT * FROM Продукты INNER JOIN Категория ON Продукты.[Категория продукта] = Категория.[Код категории] WHERE Продукты.[Избранное] = 1"
        const val queryAllRecipies =
            "SELECT Рецепты.[id], Рецепты.[Название], Рецепты.[Описание], Рецепты.[Индикатор безопасности], \"Категория рецептов\".[Название категории], \"Тип кухни\".[Название кухни], Рецепты.[Избранное] FROM \"Категория рецептов\" INNER JOIN Рецепты ON \"Категория рецептов\".[id] = Рецепты.[Категория] INNER JOIN \"Тип кухни\" ON Рецепты.[Национальная кухня] = \"Тип кухни\".[id]"
        const val queryFavouriteRecipies = queryAllRecipies + "WHERE Рецепты.[Избранное] = 1"
        const val queryInfoUser = "SELECT * FROM Пользователь WHERE Пользователь.[id] = 1"
        const val queryIdeology = "SELECT * FROM Идеология"
    }

    override fun onCreate(db: SQLiteDatabase?) {}

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    fun create_db() {
        val file = File(DATABASE_PATH)
        if (!file.exists()) {
            try {
                myContext.assets.open(DATABASE_NAME).use { myInput ->
                    FileOutputStream(DATABASE_PATH).use { myOutput ->
                        val buffer = ByteArray(1024)
                        var length: Int
                        while (myInput.read(buffer).also { length = it } > 0) {
                            myOutput.write(buffer, 0, length)
                        }
                        myOutput.flush()
                    }
                }
            } catch (ex: IOException) {
                Log.d("DatabaseHelper", ex.message!!)
            }
        }
    }

    @Throws(SQLException::class)
    fun open(): SQLiteDatabase {
        return SQLiteDatabase.openDatabase(DATABASE_PATH, null, SQLiteDatabase.OPEN_READWRITE)
    }

    fun addProductToFavourite(position: Int) {
        var db: SQLiteDatabase = this.open()
        var newPosition = position + 1
        db.execSQL("UPDATE $tableProducts SET $columnFavouriteProduct = 1 WHERE $columnProducts_id = $newPosition")
        db.close()
    }

    fun deleteProductFromFavourite(position: Int) {
        var db: SQLiteDatabase = this.open()
        var newPosition = position + 1
        db.execSQL("UPDATE $tableProducts SET $columnFavouriteProduct = 0 WHERE $columnProducts_id = $newPosition")
        db.close()
    }

    fun addRecipeToFavourite(position: Int) {
        var db: SQLiteDatabase = this.open()
        var newPosition = position + 1
        db.execSQL("UPDATE $tableRecipies SET $columnFavouriteRecipies = 1 WHERE $columnRecipies_id = $newPosition")
        db.close()
    }

    fun deleteRecipeFromFavourite(position: Int) {
        var db: SQLiteDatabase = this.open()
        var newPosition = position + 1
        db.execSQL("UPDATE $tableRecipies SET $columnFavouriteRecipies = 0 WHERE $columnRecipies_id = $newPosition")
        db.close()
    }

    fun updateInfoUser(arrayList: List<String>) {
        var db: SQLiteDatabase = this.open()
        db.execSQL("INSERT OR REPLACE INTO $tableUsers ($columnUser_id, $columnUserFamily, $columnUserName, $columnUserMiddleName, $columnUserEmail, $columnUserCity) VALUES (1, '${arrayList[0]}', '${arrayList[1]}', '${arrayList[2]}', '${arrayList[3]}', '${arrayList[4]}')")
        db.close()
    }

    fun getInfoUser(): List<String> {
        var db: SQLiteDatabase = this.open()
        val userCursor = db.rawQuery(DatabaseHelper.queryInfoUser, null)
        userCursor.moveToFirst()
        val userFamily =
            userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.columnUserFamily))
        val userName =
            userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.columnUserName))
        val userMiddleName =
            userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.columnUserMiddleName))
        val userEmail =
            userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.columnUserEmail))
        val userCity =
            userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.columnUserCity))
        val userInfoList = listOf<String>(
            "${userFamily}",
            "${userName}", "${userMiddleName}", "${userEmail}", "${userCity}"
        )
        db.close()
        return userInfoList
    }
}