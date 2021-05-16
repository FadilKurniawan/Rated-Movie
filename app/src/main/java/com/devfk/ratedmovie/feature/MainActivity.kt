package com.devfk.ratedmovie.feature

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.GridLayoutAnimationController
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.viewpager.widget.ViewPager
import com.devfk.ratedmovie.R
import com.devfk.ratedmovie.databinding.ActivityMainBinding
import com.devfk.ratedmovie.feature.home.HomeFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.hypot
import kotlin.math.max


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


//    private var mPagerAdapter: MainPagerAdapter? = null
    private lateinit var binding:ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setUpPagerListener()
        setupNavigation()
        actionClick()

    }

    private fun setupNavigation() {
        val navHostFrag = supportFragmentManager.findFragmentById(R.id.fragmentHost) as NavHostFragment
        navController = navHostFrag.navController

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            setSelectedNavigation(destination.id)
        }
    }

//        private fun setUpPagerListener() {
//        mPagerAdapter = MainPagerAdapter(supportFragmentManager)
////        binding.pager.clipToPadding = false
////        binding.pager.offscreenPageLimit = 1
//
//        val gap = TypedValue.applyDimension(
//            TypedValue.COMPLEX_UNIT_DIP,
//            4f,
//            resources.displayMetrics
//        ).toInt()
//
//        binding.pager.pageMargin = gap
//        binding.pager.adapter = mPagerAdapter
//        binding.pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
//            override fun onPageScrolled(
//                position: Int,
//                positionOffset: Float,
//                positionOffsetPixels: Int
//            ) {
//
//            }
//
//            override fun onPageSelected(position: Int) {
//                setSelectedNavigation(position)
//            }
//
//            override fun onPageScrollStateChanged(state: Int) {
//
//            }
//        })
//
//        binding.pager.setPageTransformer(false) { view, _ ->
//            view.alpha = 0f
//            view.visibility = View.VISIBLE
//
//            // Start Animation for a short period of time
//            view.animate().alpha(1f).duration = view.resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
//        }
//
//    }

    private fun setSelectedNavigation(navId: Int) {
        when (navId) {
            R.id.navIdHome -> {
                setSelectorBackgroundNav(binding.relHome, binding.imgHome, R.drawable.home_active, true)
                setSelectorBackgroundNav(binding.relList, binding.imgList, R.drawable.bookmarks_deactive, false)
            }
            R.id.navIdMylist -> {
                setSelectorBackgroundNav(binding.relHome, binding.imgHome, R.drawable.home_deactive, false)
                setSelectorBackgroundNav(binding.relList, binding.imgList, R.drawable.bookmarks_active, true)
            }
        }
    }

    private fun setSelectorBackgroundNav(
        relBtn: RelativeLayout,
        imgMenu: ImageView,
        selectedDrawableRes: Int,
        active: Boolean
    ) {
        if(active){
//            imgMenu.visibility = View.GONE
//            imgMenu.setBackgroundResource(selectedDrawableRes)

            // change parent size
            var params = relBtn.layoutParams
            params.height = resources.getDimensionPixelSize(R.dimen._46sdp)
            params.width = resources.getDimensionPixelSize(R.dimen._36sdp)
            relBtn.layoutParams = params

            // change child size
            params = imgMenu.layoutParams
            params.height = resources.getDimensionPixelSize(R.dimen._46sdp)
            params.width = resources.getDimensionPixelSize(R.dimen._36sdp)
            imgMenu.layoutParams = params
            imgMenu.setBackgroundResource(selectedDrawableRes)
            makeAnimationFlip(imgMenu, selectedDrawableRes)

//            makeAnimationCircleShow(imgMenu)
        }else{
//            makeAnimationCircleHide(imgMenu)

            // change parent size
            var params = relBtn.layoutParams
            params.height = resources.getDimensionPixelSize(R.dimen._30sdp)
            params.width = resources.getDimensionPixelSize(R.dimen._30sdp)
            relBtn.layoutParams = params

            // change child size
            params = imgMenu.layoutParams
            params.height = resources.getDimensionPixelSize(R.dimen._20sdp)
            params.width = resources.getDimensionPixelSize(R.dimen._20sdp)
            imgMenu.layoutParams = params
            imgMenu.setBackgroundResource(selectedDrawableRes)
            makeAnimationFlip(imgMenu, selectedDrawableRes)
//            Handler(Looper.getMainLooper()).postDelayed({
//                makeAnimationCircleShow(imgMenu)},200)

        }
    }

    private fun makeAnimationFlip(imgMenu: ImageView, selectedDrawableRes: Int) {
        val oa1 = ObjectAnimator.ofFloat(imgMenu, "scaleY", 1f, 0f)
        val oa2 = ObjectAnimator.ofFloat(imgMenu, "scaleY", 0f, 1f)
        oa1.interpolator = DecelerateInterpolator()
        oa2.interpolator = AccelerateDecelerateInterpolator()
        oa1.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                imgMenu.setBackgroundResource(selectedDrawableRes)
                oa2.start()
            }
        })
        oa1.start()
    }

    private fun makeAnimationCircleHide(imgMenu: ImageView) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
            // get the center for the clipping circle
            val cx = imgMenu.width / 2
            val cy = 0
            // get the initial radius for the clipping circle
            val initialRadius = hypot(cx.toDouble(), cy.toDouble()).toFloat()
            // create the animation (the final radius is zero)
            val anim = ViewAnimationUtils.createCircularReveal(imgMenu, cx, cy, initialRadius, 0f)
            // make the view invisible when the animation is done
            anim.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    imgMenu.visibility = View.INVISIBLE
                }
            })
            // start the animation
            anim.start()
        } else {
            // set the view to visible without a circular reveal animation below Lollipop
            imgMenu.visibility = View.INVISIBLE
        }
    }

    private fun makeAnimationCircleShow(imgMenu: ImageView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // get the center for the clipping circle
            val cx = imgMenu.width / 2
            val cy = imgMenu.height + imgMenu.height

            // get the final radius for the clipping circle
            val finalRadius = max(cx.toDouble(), cy.toDouble()).toFloat()

            // create the animator for this view (the start radius is zero)
            val anim = ViewAnimationUtils.createCircularReveal(imgMenu, cx, cy, 0f, finalRadius)
            // make the view visible and start the animation
            imgMenu.visibility = View.VISIBLE
            anim.start()
        } else {
            // set the view to invisible without a circular reveal animation below Lollipop
            imgMenu.visibility = View.VISIBLE
        }
    }

    private fun actionClick() {
        binding.relHome.setOnClickListener {
//            binding.pager.currentItem = 0
            if(navController.currentDestination?.id != R.id.navIdHome) {
                navController.navigate(R.id.action_bookmarkItemFragment_to_homeFragment)
            }
        }

        binding.cvSearch.setOnClickListener {

        }

        binding.relList.setOnClickListener {
//            binding.pager.currentItem = 1
            if(navController.currentDestination?.id != R.id.navIdMylist) {
                navController.navigate(HomeFragmentDirections.actionHomeFragmentToBookmarkItemFragment())
            }
        }

    }
}