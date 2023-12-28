package com.example.foodmap

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodmap.databinding.ActivityRecipiesBinding
import com.example.foodmap.databinding.ItemProductsLayoutBinding
import com.example.foodmap.databinding.ItemSelectedProductsLayoutBinding
import com.mikepenz.iconics.Iconics.applicationContext


class RecipiesHolder(val binding: RecipiesActivity) : RecyclerView.ViewHolder(binding.root) {
    val name_recipies = binding.nameProduct
    val type_recipe = binding.categoryProduct
    val type_cusine = binding.categoryProduct
    val safety_indicator: ImageView = binding.safetyIndicator
    val add_to_favorite: ImageButton = binding.addToFavorite
    var buttonOn = false

    fun bind(recipe: RecipiesModel, clickListener: OnItemClickListenerProduct) {
        name_product.text = product.product
        name_category.text = product.category
        safety_indicator.setColorFilter(product.safetyIndicator)
        add_to_favorite.setImageResource(product.favouriteProduct)

        itemView.setOnClickListener {
            clickListener.onItemClickedProduct(product)
        }
    }
}

class RecipiesAdapter(
    private var products: ArrayList<RecipiesModel>,
    private val itemClickListener: RecipiesActivity
) : RecyclerView.Adapter<RecipiesHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipiesHolder {
        return RecipiesHolder(
            ActivityRecipiesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(myHolder: RecipiesHolder, position: Int) {
        val currentPosition = products.get(position)
        myHolder.bind(currentPosition, itemClickListener)

        myHolder.binding.addToFavorite.setOnClickListener {
            val databaseHelper = DatabaseHelper(applicationContext)

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
        return recipies.size
    }
}

interface OnItemClickListenerProduct {
    fun onItemClickedProduct(recipies: RecipiesModel)
}