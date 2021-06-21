package com.devfk.ratedmovie.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.devfk.ratedmovie.R
import com.devfk.ratedmovie.data.api.APIService
import com.devfk.ratedmovie.data.models.Category
import com.devfk.ratedmovie.feature.banner.BannerPagingSource
import com.devfk.ratedmovie.feature.poster.PosterPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject
constructor (private val apiService: APIService):ViewModel(){

    val bannerListData = Pager(PagingConfig(pageSize = 1)){
        BannerPagingSource(apiService)
    }.flow.cachedIn(viewModelScope)

    val categoryList: MutableList<Category> = arrayListOf( Category(1, R.drawable.ic_series, "Series", R.drawable.card_view_series),
        Category(2, R.drawable.ic_discover, "Discover", R.drawable.card_view_discover),
        Category(3, R.drawable.ic_bookmark, "My List", R.drawable.card_view_list))

    val popularListMovie = Pager(PagingConfig(pageSize = 1)){
        PosterPagingSource(apiService, PosterPagingSource.TYPE.POPULAR)
    }.flow.cachedIn(viewModelScope)

    val nowPlayingListMovie = Pager(PagingConfig(pageSize = 1)){
        PosterPagingSource(apiService, PosterPagingSource.TYPE.NOW_PLAY)
    }.flow.cachedIn(viewModelScope)

    val topRatedListMovie = Pager(PagingConfig(pageSize = 1)){
        PosterPagingSource(apiService, PosterPagingSource.TYPE.TOP_RATED)
    }.flow.cachedIn(viewModelScope)

    val newListMovie = Pager(PagingConfig(pageSize = 1)){
        PosterPagingSource(apiService, PosterPagingSource.TYPE.NEW)
    }.flow.cachedIn(viewModelScope)

    fun similarListMovie(id:Int, language: String) = Pager(PagingConfig(pageSize = 1)){
        PosterPagingSource(apiService, PosterPagingSource.TYPE.SIMILAR, id, language)
    }.flow.cachedIn(viewModelScope)

}