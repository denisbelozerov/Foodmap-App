package com.example.foodmap

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodmap.databinding.ActivityRecipiesBinding
import com.example.foodmap.databinding.ItemRecipiesBinding
import com.example.foodmap.models.RecipiesModel
import com.mikepenz.iconics.Iconics.applicationContext


class RecipiesHolder(val binding: ItemRecipiesBinding) : RecyclerView.ViewHolder(binding.root) {
    val name_recipies = binding.nameRecipe
    val type_recipe = binding.typeRecipe
    val type_cusine = binding.typeCusine
    val safety_indicator: ImageView = binding.safetyIndicator
    val add_to_favorite: ImageButton = binding.addToFavorite
    var buttonOn = false

    fun bind(recipe: RecipiesModel, clickListener: OnItemClickListenerRecipe) {
        name_recipies.text = recipe.recipe
        type_recipe.text = recipe.type
        type_cusine.text = recipe.cusine
        safety_indicator.setColorFilter(recipe.safetyIndicator)
        add_to_favorite.setImageResource(recipe.favouriteRecipe)

        itemView.setOnClickListener {
            clickListener.onItemClickedRecipe(recipe)
        }
    }
}

class RecipiesAdapter(
    private var recipies: ArrayList<RecipiesModel>,
    private val itemClickListener: RecipiesActivity
) : RecyclerView.Adapter<RecipiesHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipiesHolder {
        return RecipiesHolder(
            ItemRecipiesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(myHolder: RecipiesHolder, position: Int) {
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

interface OnItemClickListenerRecipe {
    fun onItemClickedRecipe(recipies: RecipiesModel)
}