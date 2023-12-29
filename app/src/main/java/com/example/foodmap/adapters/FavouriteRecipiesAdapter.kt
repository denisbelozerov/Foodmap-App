package com.example.foodmap

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodmap.databinding.ItemSelectedProductsLayoutBinding
import com.example.foodmap.databinding.ItemSelectedRecipiesLayoutBinding
import com.example.foodmap.models.ProductModel
import com.example.foodmap.models.RecipiesModel
import com.mikepenz.iconics.Iconics.applicationContext


class FavouriteRecipiesHolder(val binding: ItemSelectedRecipiesLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
    val name_recipies = binding.nameRecipe
    val type_recipe = binding.typeRecipe
    val type_cusine = binding.typeCusine
    val safety_indicator: ImageView = binding.safetyIndicator
    val add_to_favorite: ImageButton = binding.addToFavorite
    var buttonOn = false

    fun bind(recipe: RecipiesModel, clickListener: OnItemClickListenerFavouriteRecipe) {
        name_recipies.text = recipe.recipe
        type_recipe.text = recipe.type
        type_cusine.text = recipe.cusine
        safety_indicator.setColorFilter(recipe.safetyIndicator)
        add_to_favorite.setImageResource(recipe.favouriteRecipe)

        itemView.setOnClickListener {
            clickListener.onItemClickedFavouriteRecipe(recipe)
        }
    }
}

class FavouriteRecipiesAdapter(
    private var recipies: ArrayList<RecipiesModel>,
    private val itemClickListener: SelectedRecipies
) : RecyclerView.Adapter<FavouriteRecipiesHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteRecipiesHolder {
        return FavouriteRecipiesHolder(
            ItemSelectedRecipiesLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(myHolder: FavouriteRecipiesHolder, position: Int) {
        val currentPosition = recipies.get(position)
        myHolder.bind(currentPosition, itemClickListener)

        myHolder.binding.addToFavorite.setOnClickListener {
            val databaseHelper = DatabaseHelper(applicationContext)

            if (!myHolder.buttonOn) {
                myHolder.buttonOn = true
                myHolder.add_to_favorite.setImageResource(R.drawable.baseline_favorite_24)
                databaseHelper.addRecipeToFavourite(position)

            } else {
                myHolder.buttonOn = false
                myHolder.add_to_favorite.setImageResource(R.drawable.baseline_favorite_border_24)
                databaseHelper.deleteRecipeFromFavourite(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return recipies.size
    }
}

interface OnItemClickListenerFavouriteRecipe {
    fun onItemClickedFavouriteRecipe(product: RecipiesModel)
}