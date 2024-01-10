package com.example.foodmap

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import java.util.*

class ListNoteBookAdapter(
    private val context: Context,
    private val data: ArrayList<HashMap<String, String>>,
    private val database: DatabaseHelper
) :
    BaseAdapter() {
    private val inflater: LayoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    override fun getCount(): Int { return data.size }
    override fun getItem(position: Int): Int { return position }
    override fun getItemId(position: Int): Long { return position.toLong() }

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var dataitem = data[position]

        val rowView = inflater.inflate(R.layout.activity_notebook_list_items, parent, false)
        rowView.findViewById<TextView>(R.id.title).text = dataitem.get("Заголовок")
        rowView.findViewById<TextView>(R.id.description).text = dataitem.get("Текст заметки")
        rowView.findViewById<TextView>(R.id.date).text = dataitem.get("Дата создания")
        var fevourite = rowView.findViewById(R.id.fev) as ImageView

        var fevourite_data = dataitem.get("Избранное")
        if (fevourite_data!!.trim().contentEquals("0")) {
            fevourite.setImageResource(R.drawable.baseline_favorite_border_24)
        } else {
            fevourite.setImageResource(R.drawable.baseline_favorite_24)
        }

        fevourite.setOnClickListener {
            val cursor = database.getSingleNoteBookData(dataitem.get("id")!!)
            cursor.moveToFirst()
            if (cursor.getString(4).toString().contentEquals("0")) {
                database.updateNoteBookFev(dataitem.get("id")!!, 1)
                fevourite.setImageResource(R.drawable.baseline_favorite_24)
            } else {
                database.updateNoteBookFev(dataitem.get("id")!!, 0)
                fevourite.setImageResource(R.drawable.baseline_favorite_border_24)
            }
        }

        rowView.tag = position
        return rowView
    }
}