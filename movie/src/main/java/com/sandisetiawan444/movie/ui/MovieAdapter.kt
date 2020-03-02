package com.sandisetiawan444.movie.ui

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.sandisetiawan444.core.util.GlideApp
import com.sandisetiawan444.movie.R
import com.sandisetiawan444.movie.data.remote.model.Movie
import com.sandisetiawan444.movie.util.UrlHelper.getPosterUrl
import kotlinx.android.synthetic.main.item_movie.view.*


/**
 * Created by Sandi on 31/01/2020.
 */

class MovieAdapter(private val listener: MovieActionListener) :
    RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    private val movies = ArrayList<Movie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false))

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.onBind(position)

    fun setMovies(movies: List<Movie>?) {
        movies?.let {
            this.movies.apply {
                clear()
                addAll(it)
            }
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun onBind(position: Int) {
            inflateData(movies[position])
            setPoster(movies[position].posterPath, position)
        }

        private fun inflateData(movie: Movie?) {
            itemView.tv_title.text = movie?.title
            itemView.tv_rating.text = movie?.voteAverage.toString()
            itemView.setOnClickListener { listener.onItemMovieClicked(movie, itemView.img_poster) }
        }

        private fun setPoster(posterPath: String?, position: Int) {
            GlideApp
                .with(itemView.img_poster.context)
                .asBitmap()
                .load(getPosterUrl(posterPath))
                .into(object : CustomTarget<Bitmap>() {
                    override fun onLoadCleared(placeholder: Drawable?) {
                        // nothing to do
                    }

                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        itemView.img_poster.setImageBitmap(resource)

                        Palette.from(resource).generate { palette ->
                            val vibrantSwatchColor = palette?.vibrantSwatch?.rgb ?: Color.GRAY

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                itemView.card_movie.outlineSpotShadowColor = vibrantSwatchColor
                            }
                        }
                    }
                })
        }
    }
}