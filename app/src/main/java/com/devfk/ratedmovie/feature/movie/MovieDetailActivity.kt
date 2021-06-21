package com.devfk.ratedmovie.feature.movie

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.size.Scale
import com.devfk.ratedmovie.R
import com.devfk.ratedmovie.data.models.Movie
import com.devfk.ratedmovie.data.util.Constant
import com.devfk.ratedmovie.databinding.ActivityMovieDetailBinding
import com.devfk.ratedmovie.feature.poster.PosterActivity
import com.devfk.ratedmovie.feature.poster.PosterPagerAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat


@AndroidEntryPoint
class MovieDetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMovieDetailBinding
    private lateinit var mSimilarAdapter: PosterPagerAdapter
    private lateinit var movie: Movie
    private val viewModel : MovieDetailViewModel by viewModels()
    private var isSaved:Boolean = false
    private var spokenLang:String = ""

    override fun onNightModeChanged(mode: Int) {
        Log.d("TAG", "*** DEBUG $mode")
        binding.imgPlay.setBackgroundResource(R.drawable.btn_play_dark)
        super.onNightModeChanged(mode)
    }

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = intent
        val id = intent.getIntExtra("id", 0)
        onActionClick(id)
        setupRV()
        loadDetailMovie(id)
    }

    private fun onActionClick(id: Int) {

        binding.linkSimilar.setOnClickListener {
            val intent = Intent(this, PosterActivity::class.java)
            intent.putExtra(Constant.CATEGORY_PARAMS, Constant.CATEGORY_SIMILAR)
            intent.putExtra(Constant.ID_PARAMS, id)
            intent.putExtra(Constant.LANG_PARAMS, spokenLang)
            startActivity(intent)
        }
        binding.relBack.setOnClickListener {
            onBackPressed()
        }

        binding.relBookmark.setOnClickListener {
            bookmarkState(it)
        }

        binding.relShare.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "https://www.imdb.com/title/${movie.imdbId}/")
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        binding.relPlay.setOnClickListener {

        }
    }

    private fun bookmarkState(view: View) {
        if(isSaved){
            viewModel.deleteMovie(movie)
            binding.imgBookmark.setBackgroundResource(R.drawable.ic_bookmark_detail)
        }else{
            viewModel.insertMovie(movie)
            Snackbar.make(
                    view,
                    "Movie Saved",
                    Snackbar.LENGTH_LONG
            ).show()
            binding.imgBookmark.setBackgroundResource(R.drawable.ic_bookmark_detail_filled)
        }
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    @InternalCoroutinesApi
    private fun loadDetailMovie(id: Int) {
        viewModel.isMovieSaved(id).observe(this, {
            if (it.isNotEmpty()) {
                Log.d("TAG", "MOVIE IS AWESOME HERE ")
                isSaved = true
                binding.imgBookmark.setBackgroundResource(R.drawable.ic_bookmark_detail_filled)
            }
        })

        viewModel.movieResp(id).observe(this, {
            movie = it
            binding.apply {
                spokenLang = it.spokenLanguages?.first()?.iso6391.toString()
                imgPoster.load(Constant.POSTER_BASE_URL + it.posterPath) {
                    crossfade(true)
                    crossfade(1000)
                    placeholder(R.drawable.ic_placeholder)
                    scale(Scale.FILL)
                }
                tvTitle.text = it.title
                tvGenre.text = it.genres?.joinToString {
                    it.name
                }
                ratebar.rating = (it.voteAverage?.div(2))?.toFloat()!!
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val date = sdf.parse(it.releaseDate)
                val df = SimpleDateFormat("yyyy")
                val year = df.format(date!!)
                tvYear.text = "($year)"
                tvCountry.text = if (it.productionCountries?.isNotEmpty() == true)
                    it.productionCountries?.first()?.iso31661
                else
                    "-"
                tvLength.text = "${it.runtime} min"
                tvDescription.text = it.overview
            }
            loadSimilarMovies(it)
            mSimilarAdapter.addLoadStateListener { combinedLoadStates ->
                if (combinedLoadStates.refresh != LoadState.Loading && mSimilarAdapter.itemCount < 1) {
                    binding.clSimilar.visibility = View.GONE
                    binding.rvSimilar.visibility = View.GONE
                }
            }
        })

    }


    private fun loadSimilarMovies(it: Movie?) {
        lifecycleScope.launch {
            if (it != null) {
                it.id?.let { it1 ->
                    it.spokenLanguages?.first()?.iso6391?.let { it2 ->
                        viewModel.similarListMovie(it1, it2).collectLatest {
                            mSimilarAdapter.submitData(it)
                        }
                    }
                }
            }
        }
    }

    private fun setupRV() {
        mSimilarAdapter = PosterPagerAdapter(this)
        binding.rvSimilar.apply {
            adapter = mSimilarAdapter
            layoutManager = LinearLayoutManager(
                    this@MovieDetailActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
            )
            setHasFixedSize(true)
        }

    }
}