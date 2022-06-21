package com.example.foodmap

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_products_layout.view.*

class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val name_product = itemView.name_product
    val name_category = itemView.category_product


    fun bind(product: ProductModel, clickListener: OnItemClickListener) {
        name_product.text = product.product
        name_category.text = product.category

        itemView.setOnClickListener {
            clickListener.onItemClicked(product)
        }
    }
}

class ProductAdapter(private var products: ArrayList<ProductModel>, val itemClickListener: OnItemClickListener): RecyclerView.Adapter<MyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_products_layout, parent, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(myHolder: MyHolder, position: Int) {
        val currentPosition = products.get(position)
        myHolder.bind(currentPosition, itemClickListener)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: ArrayList<ProductModel>) {
        products = list
        notifyDataSetChanged()
    }
}

interface OnItemClickListener{
    fun onItemClicked(product: ProductModel)
}