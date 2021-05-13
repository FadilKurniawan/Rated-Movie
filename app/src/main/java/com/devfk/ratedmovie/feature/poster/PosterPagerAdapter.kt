package com.devfk.ratedmovie.feature.poster

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.devfk.ratedmovie.R
import com.devfk.ratedmovie.data.models.Movie
import com.devfk.ratedmovie.data.util.Constant
import com.devfk.ratedmovie.databinding.PosterItemBinding
import com.devfk.ratedmovie.feature.movie.MovieDetailActivity
import java.text.SimpleDateFormat


class PosterPagerAdapter(private val context: Context) : PagingDataAdapter<Movie, PosterPagerAdapter.FilmViewHolder>(
    diffCallback
) {

    inner class FilmViewHolder(val binding: PosterItemBinding)
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
        return FilmViewHolder(
            PosterItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }


    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val currentFilm = getItem(position)
        holder.binding.apply {
            tvTitle.text = currentFilm?.title
            if(currentFilm?.releaseDate!=null){
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val date = sdf.parse(currentFilm.releaseDate)
                val df = SimpleDateFormat("yyyy")
                val year = df.format(date!!)
                tvYear.text = "($year)"
            }
            ratebar.numStars = 5
            ratebar.rating = (currentFilm?.voteAverage?.div(2))?.toFloat()!!
            imvPoster.load(Constant.POSTER_BASE_URL + currentFilm.posterPath){
                crossfade(true)
                crossfade(1000)
                placeholder(R.drawable.ic_placeholder)
                scale(Scale.FILL)
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
            holder.binding.cvParent.setOnClickListener {
                val intent = Intent(context, MovieDetailActivity::class.java)
                intent.putExtra("id", currentFilm.id)
                context.startActivity(intent)
        }
        }
    }
}