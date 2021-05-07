package com.devfk.ratedmovie.feature.movies.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.devfk.ratedmovie.data.api.APIService
import com.devfk.ratedmovie.data.api.BaseService
import com.devfk.ratedmovie.data.repository.NetworkState
import com.devfk.ratedmovie.data.util.Constant
import com.devfk.ratedmovie.data.models.Movie
import com.devfk.ratedmovie.databinding.ActivityMovieDetailBinding
import java.text.NumberFormat
import java.util.*

class MovieDetail : AppCompatActivity() {

    private lateinit var viewModel: MovieDetailViewModel
    private lateinit var movieDetailRepo: MovieDetailRepo

    private lateinit var binding: ActivityMovieDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val movieId = intent.getIntExtra("id",1)
        networkSetting(movieId)

    }

    private fun networkSetting(movieId:Int){
        val apiService:APIService = BaseService.getClient()
        movieDetailRepo = MovieDetailRepo(apiService)
        viewModel = getViewModel(movieId)

        viewModel.movieDetails.observe(this, Observer {
            bindUI(it)
        })

        viewModel.networkState.observe(this, Observer {
            binding.progressBar.visibility = if(it== NetworkState.LOADING) View.VISIBLE else View.GONE
            binding.txtError.visibility = if(it== NetworkState.ERROR) View.VISIBLE else View.GONE
        })
    }

    private fun bindUI(it:Movie){
        binding.movieTitle.text = it.title
        binding.movieTagline.text = it.tagline
        binding.movieReleaseDate.text = it.releaseDate
        binding.movieRating.text = it.voteAverage.toString()
        (it.runtime.toString() + " minutes").also { binding.movieRuntime.text = it }
        binding.movieOverview.text = it.overview

        val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)
        binding.movieBudget.text = formatCurrency.format(it.budget)
        binding.movieRevenue.text = formatCurrency.format(it.revenue)

        val moviePosterURL = Constant.POSTER_BASE_URL+it.posterPath
        Glide.with(this)
            .load(moviePosterURL)
            .into(binding.ivMoviePoster)
    }

    private fun getViewModel(movieId:Int):MovieDetailViewModel{
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MovieDetailViewModel(movieDetailRepo,movieId) as T
            }
        })[MovieDetailViewModel::class.java]
    }
}