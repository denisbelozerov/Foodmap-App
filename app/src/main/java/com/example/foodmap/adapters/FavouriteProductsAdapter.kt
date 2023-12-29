package com.example.foodmap

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodmap.databinding.ItemSelectedProductsLayoutBinding
import com.example.foodmap.models.ProductModel
import com.mikepenz.iconics.Iconics.applicationContext


class FavouriteProductHolder(val binding: ItemSelectedProductsLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
    val name_product = binding.nameProduct
    val name_category = binding.categoryProduct
    val safety_indicator: ImageView = binding.safetyIndicator
    val add_to_favorite: ImageButton = binding.addToFavorite
    var buttonOn = false

    fun bind(product: ProductModel, clickListener: OnItemClickListenerProduct) {
        name_product.text = product.product
        name_category.text = product.category
        safety_indicator.setColorFilter(product.safetyIndicator)
        add_to_favorite.setImageResource(product.favouriteProduct)

        itemView.setOnClickListener {
            clickListener.onItemClickedProduct(product)
        }
    }
}

class FavouriteProductAdapter(
    private var products: ArrayList<ProductModel>,
    private val itemClickListener: SelectedProducts
) : RecyclerView.Adapter<FavouriteProductHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteProductHolder {
        return FavouriteProductHolder(
            ItemSelectedProductsLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(myHolder: FavouriteProductHolder, position: Int) {
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

interface OnItemClickListenerProduct {
    fun onItemClickedProduct(product: ProductModel)
}