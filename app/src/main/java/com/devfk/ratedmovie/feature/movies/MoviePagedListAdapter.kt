package com.devfk.ratedmovie.feature.movies

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devfk.ratedmovie.data.repository.NetworkState
import com.devfk.ratedmovie.data.util.Constant
import com.devfk.ratedmovie.data.models.Movie
import com.devfk.ratedmovie.databinding.MovieListItemBinding
import com.devfk.ratedmovie.databinding.NetworkStateItemBinding
import com.devfk.ratedmovie.feature.movies.detail.MovieDetail

class MoviePagedListAdapter(val context: Context):PagedListAdapter<Movie, RecyclerView.ViewHolder>(MovieDiffCallback()) {

    val MOVIE_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return if(viewType == MOVIE_VIEW_TYPE){
            val view = MovieListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            MovieItemViewHolder(view)
        }else{
            val view = NetworkStateItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            NetworkItemViewHolder(view)
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position) == MOVIE_VIEW_TYPE){
            (holder as MovieItemViewHolder).bind(getItem(position),context)
        }else{
            (holder as NetworkItemViewHolder).bind(networkState)
        }
    }

    private fun hasExtraRow():Boolean{
        return networkState !=null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if(hasExtraRow()){1}else{0}
    }

    override fun getItemViewType(position: Int): Int {
        return if(hasExtraRow() && position == itemCount - 1){
            NETWORK_VIEW_TYPE
        }else{
            MOVIE_VIEW_TYPE
        }
    }

    class MovieDiffCallback : DiffUtil.ItemCallback<Movie>(){
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }

    }

    class MovieItemViewHolder(var binding:MovieListItemBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(data: Movie?,context: Context){
          data?.let {
              binding.cvMovieTitle.text = it.title
              binding.cvMovieReleaseDate.text = it.releaseDate

              val moviePosterURL = Constant.POSTER_BASE_URL+it.posterPath
              Glide.with(itemView.context)
                      .load(moviePosterURL)
                      .into(binding.cvIvMoviePoster)

              binding.cardView.setOnClickListener {
                  val intent = Intent(context, MovieDetail::class.java)
                  intent.putExtra("id",data.id)
                  context.startActivity(intent)
              }
          }
        }
    }


    class NetworkItemViewHolder(var binding: NetworkStateItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(networkState: NetworkState?){
            if(networkState !=null && networkState == NetworkState.LOADING){
                binding.progressBarItem.visibility = View.VISIBLE
            }else{
                binding.progressBarItem.visibility = View.GONE
            }

            if(networkState !=null && networkState == NetworkState.ERROR){
                binding.errorMsgItem.visibility = View.VISIBLE
                binding.errorMsgItem.text = networkState.msg
            }else if(networkState !=null && networkState == NetworkState.ENDOFLIST){
                binding.errorMsgItem.visibility = View.VISIBLE
                binding.errorMsgItem.text = networkState.msg
            }else{
                binding.errorMsgItem.visibility = View.GONE
            }
        }
    }

    fun setNetWorkState(newNetworkState: NetworkState){
        val previousState:NetworkState? = this.networkState
        val hadExtraRow:Boolean = hasExtraRow()

        this.networkState = newNetworkState
        val hasExtraRow:Boolean = hasExtraRow()
        if(hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState){
            notifyItemChanged(itemCount-1)
        }
    }
}