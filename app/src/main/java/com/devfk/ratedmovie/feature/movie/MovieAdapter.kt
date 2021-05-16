package com.devfk.ratedmovie.feature.movie

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


class MovieAdapter(private val context: Context) : RecyclerView.Adapter<MovieAdapter.FilmViewHolder>(){

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

    val differ = AsyncListDiffer(this, diffCallback)

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val currentFilm = differ.currentList[position]
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
            holder.binding.cvParent.setOnClickListener {
                val intent = Intent(context, MovieDetailActivity::class.java)
                intent.putExtra("id", currentFilm.id)
                context.startActivity(intent)
        }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size
}