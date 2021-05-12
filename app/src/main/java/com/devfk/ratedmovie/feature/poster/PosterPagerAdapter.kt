package com.devfk.ratedmovie.feature.poster

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.devfk.ratedmovie.R
import com.devfk.ratedmovie.data.models.Movie
import com.devfk.ratedmovie.data.util.Constant
import com.devfk.ratedmovie.databinding.PosterItemBinding

class PosterPagerAdapter: PagingDataAdapter<Movie, PosterPagerAdapter.FilmViewHolder>(diffCallback) {

    inner class FilmViewHolder(val binding:PosterItemBinding)
        :RecyclerView.ViewHolder(binding.root)

    companion object{
        private val diffCallback = object :DiffUtil.ItemCallback<Movie>(){
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return newItem == oldItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        return FilmViewHolder(PosterItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val currentFilm = getItem(position)

        holder.binding.apply {
            imvPoster.load(Constant.POSTER_BASE_URL+currentFilm?.posterPath){
                crossfade(true)
                crossfade(1000)
                placeholder(R.drawable.ic_placeholder)
                scale(Scale.FILL)
            }
        }
    }

}