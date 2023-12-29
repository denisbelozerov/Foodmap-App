package com.example.foodmap

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodmap.activity.MainActivity
import com.example.foodmap.databinding.ItemProductsLayoutBinding
import com.example.foodmap.models.ProductModel
import com.mikepenz.iconics.Iconics.applicationContext


class ProductHolder(val binding: ItemProductsLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
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
    private val itemClickListener: MainActivity
) : RecyclerView.Adapter<ProductHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        return ProductHolder(
            ItemProductsLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(myHolder: ProductHolder, position: Int) {
        val currentPosition = products.get(position)
        myHolder.bind(currentPosition, itemClickListener)

        myHolder.binding.addToFavorite.setOnClickListener {
            val databaseHelper = DatabaseHelper(applicationContext)

            if (!myHolder.buttonOn) {
                myHolder.buttonOn = true
                myHolder.add_to_favorite.setImageResource(R.drawable.baseline_favorite_24)
                databaseHelper.addProductToFavourite(position)

            } else {
                myHolder.buttonOn = false
                myHolder.add_to_favorite.setImageResource(R.drawable.baseline_favorite_border_24)
                databaseHelper.deleteProductFromFavourite(position)
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