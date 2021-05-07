package com.devfk.ratedmovie.feature.film

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.devfk.ratedmovie.R
import com.devfk.ratedmovie.databinding.ActivityFilmBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilmActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFilmBinding
    private val viewModel: FilmViewModel by viewModels()
    private lateinit var filmAdapter: FilmAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRv()
    }

    private fun setupRv(){
        filmAdapter = FilmAdapter()

        binding.rvFilm.apply {
            adapter = filmAdapter
            layoutManager = LinearLayoutManager(
                this@FilmActivity, LinearLayoutManager.HORIZONTAL,
                false
            )
            setHasFixedSize(true)
        }

        viewModel.responseFilm.observe(this, {listFilm->
            filmAdapter.film = listFilm
        })
    }
}