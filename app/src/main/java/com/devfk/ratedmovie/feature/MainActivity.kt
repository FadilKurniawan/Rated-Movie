package com.devfk.ratedmovie.feature

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.devfk.ratedmovie.data.api.APIService
import com.devfk.ratedmovie.data.api.BaseService
import com.devfk.ratedmovie.data.repository.NetworkState
import com.devfk.ratedmovie.databinding.ActivityMainBinding
import com.devfk.ratedmovie.feature.movies.MoviePagedListAdapter
import com.devfk.ratedmovie.feature.movies.MoviePagedListRepo
import com.devfk.ratedmovie.feature.movies.MovieViewModel

class MainActivity : AppCompatActivity() {


    private lateinit var binding:ActivityMainBinding
    private lateinit var viewModel: MovieViewModel
    lateinit var moviePagedListRepo: MoviePagedListRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val apiService: APIService = BaseService.getClient()

        moviePagedListRepo = MoviePagedListRepo(apiService)

        viewModel = getViewModel()
        val movieAdapter = MoviePagedListAdapter(this)
        val gridLayoutManager = GridLayoutManager(this,3)

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                val viewType = movieAdapter.getItemViewType(position)
                return if(viewType == movieAdapter.MOVIE_VIEW_TYPE){ 1 }else{ 3 }
            }
        }

        binding.rvMovieList.layoutManager = gridLayoutManager
        binding.rvMovieList.setHasFixedSize(true)
        binding.rvMovieList.adapter = movieAdapter

        viewModel.moviePagedList.observe(this, Observer {
            movieAdapter.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {
            binding.progressBarPopular.visibility = if(viewModel.listEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            binding.txtErrorPopular.visibility = if(viewModel.listEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE
            if(!viewModel.listEmpty()){
                movieAdapter.setNetWorkState(it)
            }
        })

    }

    private fun getViewModel(): MovieViewModel {
        return ViewModelProviders.of(this, object: ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MovieViewModel(moviePagedListRepo) as T
            }
        })[MovieViewModel::class.java]
    }
}