package com.devfk.ratedmovie.feature.film

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.devfk.ratedmovie.data.models.Movie
import com.devfk.ratedmovie.data.util.Constant
import com.devfk.ratedmovie.databinding.FilmItemBinding

class FilmAdapter: RecyclerView.Adapter<FilmAdapter.FilmViewHolder>() {
    inner class FilmViewHolder(val binding:FilmItemBinding)
        :RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object :DiffUtil.ItemCallback<Movie>(){
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return newItem == oldItem
        }
    }
    private val differ = AsyncListDiffer(this,diffCallback)
    var film:List<Movie>
    get() =  differ.currentList
    set(value) {
        differ.submitList(value)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        return FilmViewHolder(FilmItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val currentFilm = film[position]

        holder.binding.apply {
            textView.text = currentFilm.title
            imageView.load(Constant.POSTER_BASE_URL+currentFilm.posterPath){
                crossfade(true)
                crossfade(1000)
            }
        }
    }

    override fun getItemCount() = film.size


}