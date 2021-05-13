package com.devfk.ratedmovie.feature.movie

import android.annotation.SuppressLint
import android.graphics.PointF
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
import com.devfk.ratedmovie.feature.poster.PosterPagerAdapter
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.EdgeTreatment
import com.google.android.material.shape.OffsetEdgeTreatment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat


@AndroidEntryPoint
class MovieDetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMovieDetailBinding
    private lateinit var mSimilarAdapter: PosterPagerAdapter
    private val viewModel : MovieDetailViewModel by viewModels()

    override fun onNightModeChanged(mode: Int) {
        Log.d("TAG","*** DEBUG $mode")
        binding.imgPlay.setBackgroundResource(R.drawable.btn_play_dark)
        super.onNightModeChanged(mode)
    }


    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = intent
        val id = intent.getIntExtra("id", 0)
        setupRV()
        viewModel.movieResp(id).observe(this, {
            binding.apply {

                imgPoster.load(Constant.POSTER_BASE_URL + it.posterPath) {
                    crossfade(true)
                    crossfade(1000)
                    placeholder(R.drawable.ic_placeholder)
                    scale(Scale.FILL)
                }

                tvTitle.text = it.title
                tvGenre.text = it.genres.joinToString {
                    it.name
                }
                ratebar.rating = (it.voteAverage / 2).toFloat()
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val date = sdf.parse(it.releaseDate)
                val df = SimpleDateFormat("yyyy")
                val year = df.format(date!!)
                tvYear.text = "($year)"
                tvCountry.text = if (it.productionCountries.isNotEmpty())
                    it.productionCountries.first().iso31661
                else
                    "-"
                tvLength.text = "${it.runtime} min"
                tvDescription.text = it.overview
            }
            loading(it)

            mSimilarAdapter.addLoadStateListener {combinedLoadStates->
                if (combinedLoadStates.refresh != LoadState.Loading && mSimilarAdapter.itemCount < 1) {
                    binding.clSimilar.visibility = View.GONE
                    binding.rvSimilar.visibility = View.GONE
                }
            }
        })
    }

    private fun loading(it: Movie?) {
        lifecycleScope.launch {
            if (it != null) {
                viewModel.similarListMovie(it.id, it.spokenLanguages.first().iso6391).collectLatest {
                    mSimilarAdapter.submitData(it)
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