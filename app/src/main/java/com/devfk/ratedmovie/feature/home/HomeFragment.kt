package com.devfk.ratedmovie.feature.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.devfk.ratedmovie.R
import com.devfk.ratedmovie.data.util.Constant
import com.devfk.ratedmovie.databinding.FragmentHomeBinding
import com.devfk.ratedmovie.feature.banner.BannerPagerAdapter
import com.devfk.ratedmovie.feature.home.category.CategoryAdapter
import com.devfk.ratedmovie.feature.movie.MovieDetailActivity
import com.devfk.ratedmovie.feature.poster.PosterActivity
import com.devfk.ratedmovie.feature.poster.PosterPagerAdapter
import com.devfk.ratedmovie.helper.HorizontalMarginItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.math.abs


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var mBannerAdapter: BannerPagerAdapter
    private lateinit var mPopulerAdapter: PosterPagerAdapter
    private lateinit var mTopRatedAdapter: PosterPagerAdapter
    private lateinit var mCategoryAdapter: CategoryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRv()
        setupBanner()
        loadingData()
        actionClick()
    }

    private fun actionClick() {
        binding.linkPopular.setOnClickListener {
            val intent = Intent(context, PosterActivity::class.java)
            intent.putExtra(Constant.CATEGORY_PARAMS, Constant.CATEGORY_POPULAR)
            startActivity(intent)
        }
        binding.linkTopRate.setOnClickListener {
            val intent = Intent(context, PosterActivity::class.java)
            intent.putExtra(Constant.CATEGORY_PARAMS, Constant.CATEGORY_TOP_RATED)
            startActivity(intent)
        }
    }

    private fun setupBanner() {
        mBannerAdapter = BannerPagerAdapter(requireContext())
        binding.vpUpcomingMovie.apply {
            adapter = mBannerAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
        }
        binding.vpUpcomingMovie.offscreenPageLimit = 1

        viewPagerSlideEffect()
//        viewPagerAnimation()

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
                requireContext(),
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
        mPopulerAdapter = PosterPagerAdapter(requireContext())
        mTopRatedAdapter = PosterPagerAdapter(requireContext())
        mCategoryAdapter = CategoryAdapter(requireContext())
        mCategoryAdapter.listItem = viewModel.categoryList

        binding.rvPopularMovie.apply {
            adapter = mPopulerAdapter
            layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
            )
            setHasFixedSize(true)
        }

        binding.rvTopRated.apply {
            adapter = mTopRatedAdapter
            layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
            )
            setHasFixedSize(true)
        }

        binding.rvCategory.apply {
            adapter = mCategoryAdapter
            layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
            )
            setHasFixedSize(true)
        }
    }
}