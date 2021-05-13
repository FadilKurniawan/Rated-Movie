package com.devfk.ratedmovie.feature.home

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.devfk.ratedmovie.R
import com.devfk.ratedmovie.databinding.ActivityHomeBinding
import com.devfk.ratedmovie.feature.banner.BannerPagerAdapter
import com.devfk.ratedmovie.feature.home.category.CategoryAdapter
import com.devfk.ratedmovie.feature.poster.PosterPagerAdapter
import com.devfk.ratedmovie.helper.HorizontalMarginItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.abs

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var mBannerAdapter: BannerPagerAdapter
    private lateinit var mPopulerAdapter: PosterPagerAdapter
    private lateinit var mTopRatedAdapter: PosterPagerAdapter
    private lateinit var mCategoryAdapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRv()
        setupBanner()
        loadingData()
    }

    private fun setupBanner() {
        mBannerAdapter = BannerPagerAdapter(this)
        binding.vpUpcomingMovie.apply {
            adapter = mBannerAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
        }
        binding.vpUpcomingMovie.offscreenPageLimit = 1

        viewPagerSlideEffect()
//        viewPagerAnimation()

    }

    private fun viewPagerAnimation() {
        //Auto Animation
        var currentPage = 0
        val delay: Long = 1500 //delay in milliseconds before task is to be executed
        val period: Long = 10000 // time in milliseconds between successive task executions.

        val timer = Timer() // This will create a new Thread
        timer.schedule(object : TimerTask() {
            // task to be scheduled
            override fun run() {
                if (currentPage == binding.vpUpcomingMovie.childCount - 1) {
                    currentPage = 0
                }
                binding.vpUpcomingMovie.setCurrentItem(currentPage++, true)
            }
        }, delay, period)
    }

    private fun viewPagerSlideEffect() {
        // SLIDE EFFECT
        // Add a PageTransformer that translates the next and previous items horizontally
        // towards the center of the screen, which makes them visible
        val nextItemVisiblePx = resources.getDimension(R.dimen.viewpager_next_item_visible)
        val currentItemHorizontalMarginPx = resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
        val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
            page.translationX = -pageTranslationX * position
            // Next line scales the item's height. You can remove it if you don't want this effect
            page.scaleY = 1 - (0.25f * abs(position))
            // If you want a fading effect uncomment the next line:
            // page.alpha = 0.25f + (1 - abs(position))
        }

        binding.vpUpcomingMovie.setPageTransformer(pageTransformer)

        // The ItemDecoration gives the current (centered) item horizontal margin so that
        // it doesn't occupy the whole screen width. Without it the items overlap
        val itemDecoration = HorizontalMarginItemDecoration(
            this,
            R.dimen.viewpager_current_item_horizontal_margin
        )
        binding.vpUpcomingMovie.addItemDecoration(itemDecoration)
    }

    private fun loadingData() {
        lifecycleScope.launch {
            viewModel.bannerListData.collect { pagingData->
                mBannerAdapter.submitData(pagingData)
            }
        }
        lifecycleScope.launch {
            viewModel.popularListMovie.collect { pagingData->
                mPopulerAdapter.submitData(pagingData)
            }
        }

        lifecycleScope.launch {
            viewModel.topRatedListMovie.collect { pagingData->
                mTopRatedAdapter.submitData(pagingData)
            }
        }
    }

    private fun setupRv(){
        mPopulerAdapter = PosterPagerAdapter(this)
        mTopRatedAdapter = PosterPagerAdapter(this)
        mCategoryAdapter = CategoryAdapter(this)
        mCategoryAdapter.listItem = viewModel.categoryList

        binding.rvPopularMovie.apply {
            adapter = mPopulerAdapter
            layoutManager = LinearLayoutManager(
                this@HomeActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            setHasFixedSize(true)
        }

        binding.rvTopRated.apply {
            adapter = mTopRatedAdapter
            layoutManager = LinearLayoutManager(
                this@HomeActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            setHasFixedSize(true)
        }

        binding.rvCategory.apply {
            adapter = mCategoryAdapter
            layoutManager = LinearLayoutManager(
                    this@HomeActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
            )
            setHasFixedSize(true)
        }
    }
}