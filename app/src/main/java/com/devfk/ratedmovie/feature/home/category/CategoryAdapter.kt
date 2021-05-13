package com.devfk.ratedmovie.feature.home.category

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.devfk.ratedmovie.data.models.Category
import com.devfk.ratedmovie.databinding.CategoryHomeItemBinding

class CategoryAdapter(private val context: Context) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(val binding:CategoryHomeItemBinding)
        :RecyclerView.ViewHolder(binding.root)

    companion object{
        private val diffCallback = object :DiffUtil.ItemCallback<Category>(){
            override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
                return newItem == oldItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(CategoryHomeItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var listItem : List<Category> get() = differ.currentList
    set(value) {
        differ.submitList(value)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val currentFilm = listItem[position]

        holder.binding.apply {
            currentFilm.background.let { relSeries.setBackgroundResource(it) }
            currentFilm.icon.let { icon.setImageResource(it) }
            tvTitle.text = currentFilm.title
            relSeries.setOnClickListener {

            }
        }

        if (position == 0){
            val params = holder.itemView.layoutParams as RecyclerView.LayoutParams
            params.marginStart = 35
            holder.itemView.layoutParams = params
        }else{
            val params = holder.itemView.layoutParams as RecyclerView.LayoutParams
            params.bottomMargin = 0
            holder.itemView.layoutParams = params
        }
    }
    override fun getItemCount(): Int = listItem.size
}