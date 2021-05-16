package com.devfk.ratedmovie.feature.bookmark

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.devfk.ratedmovie.R
import com.devfk.ratedmovie.data.models.Movie
import com.devfk.ratedmovie.databinding.FragmentMyListBinding
import com.devfk.ratedmovie.feature.bookmark.dummy.DummyContent
import com.devfk.ratedmovie.feature.movie.MovieAdapter
import com.devfk.ratedmovie.feature.movie.MovieViewModel
import com.devfk.ratedmovie.feature.poster.PosterPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyListFragment : Fragment() {

    private lateinit var binding: FragmentMyListBinding
    private val viewModel:MovieViewModel by viewModels()
    private lateinit var mMovieAdapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRV()

    }

    private fun setupRV() {
        mMovieAdapter = MovieAdapter(requireContext())

        binding.rvMyList.apply {
            adapter = mMovieAdapter
            layoutManager = GridLayoutManager(
                requireContext(),
                2,
                LinearLayoutManager.VERTICAL,
                false)
        }
        viewModel.getAllMovies.observe(viewLifecycleOwner,{
            mMovieAdapter.differ.submitList(it)
        })
    }

}