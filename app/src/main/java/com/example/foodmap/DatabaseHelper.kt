package com.example.foodmap

import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.ContactsContract.Data
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
        private const val DATABASE_NAME = "fodmap_ver2.db"
        private const val DATABASE_VERSION = 2
        const val tableProducts = "Продукты"
        const val columnProducts_id = "_id"
        const val columnProductsName = "Название продукта"
        const val columnProductsCode = "Код продукта"
        const val columnProductsCategory = "Категория продукта"
        const val columnProductsGluten = "Наличие глютена"
        const val columnProductsProtein = "Содержание белков (на 100 гр)"
        const val columnProductsFats = "Содержание жиров (на 100 гр)"
        const val columnProductsCarb = "Содержание углеводов (на 100 гр)"
        const val columnSafetyIndicator = "Индикатор безопасности"
        const val tableCategory = "Категория"
        const val columnCategoryCode = "Код категории"
        const val columnCategoryName = "Название категории"
        const val tableFODMAP = "FODMAP"
        const val columnFodmapProducts = "Название продукта"
        const val columnFodmapUnit = "Единицы измерения гр"
        const val columnFodmapOlygos = "Наличие Olygos (олигосахариды)"
        const val columnFodmapFructose = "Наличие Fructose (фруктоза)"
        const val columnFodmapPolyols = "Наличие Polyols (полиолы)"
        const val columnFodmapLactose = "Наличие Lactose (лактоза)"
        const val columnFavouriteProduct = "Избранное"

        const val queryFodmap =
            "SELECT Продукты._id, Продукты.[Название продукта], Продукты.[Наличие глютена], FODMAP.[Единицы измерения гр], FODMAP.[Наличие Olygos (олигосахариды)], FODMAP.[Наличие Fructose (фруктоза)], FODMAP.[Наличие Polyols (полиолы)], FODMAP.[Наличие Lactose (лактоза)] FROM Продукты INNER JOIN FODMAP ON Продукты.[Название продукта] = FODMAP.[Название продукта]"
        const val queryProducts =
            "SELECT * FROM Продукты INNER JOIN Категория ON Продукты.[Категория продукта] = Категория.[Код категории]"
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

    fun addToFavourite(position: Int) {
        var db: SQLiteDatabase = this.open()
        var newPosition = position + 1
        db.execSQL("UPDATE $tableProducts SET $columnFavouriteProduct = 1 WHERE $columnProducts_id = $newPosition")
        db.close()
    }

    fun deleteToFavourite(position: Int) {
        var db: SQLiteDatabase = this.open()
        var newPosition = position + 1
        db.execSQL("UPDATE $tableProducts SET $columnFavouriteProduct = 0 WHERE $columnProducts_id = $newPosition")
        db.close()
    }
}