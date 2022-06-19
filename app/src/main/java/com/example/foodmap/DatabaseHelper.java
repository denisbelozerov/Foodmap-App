package com.example.foodmap;

import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class DatabaseHelper extends SQLiteOpenHelper {
    private static String DB_PATH; // полный путь к базе данных
    private static String DB_NAME = "fodmap.db";
    private static final int SCHEMA = 1; // версия базы данных
    static final String tableProducts = "Продукты"; // название таблицы Продукты в бд
    static final String columnProducts_id = "_id";
    static final String columnProductsName = "Название продукта";
    static final String columnProductsCode = "Код продукта";
    static final String columnProductsCategory = "Категория продукта";
    static final String columnProductsGluten = "Наличие глютена";
    static final String columnProductsProtein = "Содержание белков (на 100 гр)";
    static final String columnProductsFats = "Содержание жиров (на 100 гр)";
    static final String columnProductsCarb = "Содержание углеводов (на 100 гр)";

    static final String tableCategory = "Категория"; // название таблицы Категория в бд
    static final String columnCategoryCode = "Код категории";
    static final String columnCategoryName = "Название категории";

    static final String tableFODMAP = "FODMAP"; // название таблицы FODMAP в бд
    static final String columnFodmapProducts = "Название продукта";
    static final String columnFodmapUnit = "Единицы измерения гр";
    static final String columnFodmapOlygos = "Наличие Olygos (олигосахариды)";
    static final String columnFodmapFructose = "Наличие Fructose (фруктоза)";
    static final String columnFodmapPolyols = "Наличие Polyols (полиолы)";
    static final String columnFodmapLactose = "Наличие Lactose (лактоза)";

    private Context myContext;

    DatabaseHelper(Context context) {
        super(context, DB_NAME, null, SCHEMA);
        this.myContext = context;
        DB_PATH = context.getFilesDir().getPath() + DB_NAME;
    }

    @Override
    public void onCreate(SQLiteDatabase db) { }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) { }

    void create_db(){

        File file = new File(DB_PATH);
        if (!file.exists()) {
            //получаем локальную бд как поток
            try(InputStream myInput = myContext.getAssets().open(DB_NAME);
                // Открываем пустую бд
                OutputStream myOutput = new FileOutputStream(DB_PATH)) {

                // побайтово копируем данные
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }
                myOutput.flush();
            }
            catch(IOException ex){
                Log.d("DatabaseHelper", ex.getMessage());
            }
        }
    }
    public SQLiteDatabase open()throws SQLException {
        return SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
    }
}