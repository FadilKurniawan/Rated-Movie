package com.devfk.ratedmovie.feature.poster

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.devfk.ratedmovie.data.util.Constant
import com.devfk.ratedmovie.databinding.ActivityPosterBinding
import com.devfk.ratedmovie.feature.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PosterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPosterBinding
    private lateinit var mPosterPagerAdapter: PosterPagerAdapter
    private val viewModel: HomeViewModel by viewModels()
    private var category:Int = 0
    private var id:Int = 0
    private var lang:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPosterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupAppBar()
        setupRV()
        loadData()
    }


    private fun setupAppBar() {
        val intent = intent
        category = intent.getIntExtra(Constant.CATEGORY_PARAMS, Constant.CATEGORY_POPULAR)
        id = intent.getIntExtra(Constant.ID_PARAMS, 0)
        lang = intent.getStringExtra(Constant.LANG_PARAMS).toString()
        binding.tvTitleBar.text = getTitles(category)
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getTitles(category: Int): String {
        return when(category){
            Constant.CATEGORY_POPULAR->"Popular"
            Constant.CATEGORY_TOP_RATED->"Top Rate"
            Constant.CATEGORY_NOW_PLAYING->"Now Playing"
            Constant.CATEGORY_LATEST_MOVIE->"Latest Release"
            Constant.CATEGORY_SIMILAR->"Similar Movie"
            else->" "
        }
    }

    private fun setupRV() {
        mPosterPagerAdapter = PosterPagerAdapter(this)
        binding.rvItems.apply {
            adapter = mPosterPagerAdapter
            layoutManager = GridLayoutManager(
                this@PosterActivity,
                2,
                LinearLayoutManager.VERTICAL,
                false)
        }
    }

    private fun loadData() {
        if (category == Constant.CATEGORY_POPULAR) {
            lifecycleScope.launch {
                viewModel.popularListMovie.collect { pagingData ->
                    mPosterPagerAdapter.submitData(pagingData)
                }
            }
        }
        if(category==Constant.CATEGORY_TOP_RATED) {
            lifecycleScope.launch {
                viewModel.topRatedListMovie.collect { pagingData ->
                    mPosterPagerAdapter.submitData(pagingData)
                }
            }
        }

        if(category==Constant.CATEGORY_NOW_PLAYING) {
            lifecycleScope.launch {
                viewModel.nowPlayingListMovie.collect { pagingData ->
                    mPosterPagerAdapter.submitData(pagingData)
                }
            }
        }
        if(category==Constant.CATEGORY_LATEST_MOVIE) {
            lifecycleScope.launch {
                viewModel.newListMovie.collect { pagingData ->
                    mPosterPagerAdapter.submitData(pagingData)
                }
            }
        }

        if(category==Constant.CATEGORY_SIMILAR) {
            lifecycleScope.launch {
                viewModel.similarListMovie(id,lang).collect { pagingData ->
                    mPosterPagerAdapter.submitData(pagingData)
                }
            }
        }
    }
}