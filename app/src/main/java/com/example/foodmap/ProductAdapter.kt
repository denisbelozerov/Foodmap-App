package com.example.foodmap

import android.annotation.SuppressLint
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodmap.databinding.ItemProductsLayoutBinding
import com.mikepenz.iconics.Iconics.applicationContext


class MyHolder(val binding: ItemProductsLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
    val name_product = binding.nameProduct
    val name_category = binding.categoryProduct
    val safety_indicator: ImageView = binding.safetyIndicator
    val add_to_favorite: ImageButton = binding.addToFavorite
    var buttonOn = false

    fun bind(product: ProductModel, clickListener: OnItemClickListener) {
        name_product.text = product.product
        name_category.text = product.category
        safety_indicator.setColorFilter(product.safetyIndicator)
        add_to_favorite.setImageResource(product.favouriteProduct)

        itemView.setOnClickListener {
            clickListener.onItemClicked(product)
        }
    }
}

class ProductAdapter(
    private var products: ArrayList<ProductModel>,
    val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<MyHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_products_layout, parent, false)
        return MyHolder(
            ItemProductsLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(myHolder: MyHolder, position: Int) {
        val currentPosition = products.get(position)
        myHolder.bind(currentPosition, itemClickListener)

        myHolder.binding.addToFavorite.setOnClickListener {
            var databaseHelper = DatabaseHelper(applicationContext)

            if (!myHolder.buttonOn) {
                myHolder.buttonOn = true
                myHolder.add_to_favorite.setImageResource(R.drawable.baseline_favorite_24)
                databaseHelper.addToFavourite(position)

            } else {
                myHolder.buttonOn = false
                myHolder.add_to_favorite.setImageResource(R.drawable.baseline_favorite_border_24)
                databaseHelper.deleteToFavourite(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return products.size
    }
}

interface OnItemClickListener {
    fun onItemClicked(product: ProductModel)
}