package com.sandisetiawan444.movie.ui

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearSnapHelper
import com.sandisetiawan444.core.ext.hide
import com.sandisetiawan444.core.ext.show
import com.sandisetiawan444.core.ui.BaseFragment
import com.sandisetiawan444.movie.R
import com.sandisetiawan444.movie.data.remote.model.Movie
import com.sandisetiawan444.movie.di.DaggerMovieComponent
import com.sandisetiawan444.movie.di.MovieDataModule
import com.sandisetiawan444.movie.ui.detail.DetailActivity
import com.sandisetiawan444.movie.util.MovieEnvironment.Extras.EXTRA_MOVIE
import com.sandisetiawan444.movie.util.MovieEnvironment.ViewAnimations.VIEW_POSTER
import kotlinx.android.synthetic.main.fragment_movie.*
import javax.inject.Inject


class MovieFragment : BaseFragment<MovieViewModel>(), MovieActionListener {

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    private var mViewModel: MovieViewModel? = null

    private val mAdapter: MovieAdapter by lazy { MovieAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipe_refresh.setOnRefreshListener { mViewModel?.getMovies() }
        setRecyclerView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel =
            ViewModelProvider(this, mViewModelFactory).get(MovieViewModel::class.java).apply {
                showProgress.observe(viewLifecycleOwner, Observer {
                    if (it) rv_movie.hide()
                    else rv_movie.show()

                    swipe_refresh.isRefreshing = it
                })

                movies.observe(viewLifecycleOwner, Observer {
                    mAdapter.setMovies(it)
                })
            }
    }

    override fun onCreateInjector() {
        DaggerMovieComponent.builder()
            .movieDataModule(MovieDataModule())
            .build()
            .inject(this)
    }

    override fun getViewModel(): MovieViewModel? = mViewModel

    override fun onItemMovieClicked(movie: Movie?, imageView: ImageView?) {
        val intent = Intent(context, DetailActivity::class.java)
            .putExtra(EXTRA_MOVIE, movie)
        val options = ActivityOptions.makeSceneTransitionAnimation(
            activity,
            Pair.create(imageView, VIEW_POSTER)
        )

        startActivity(intent, options.toBundle())
    }

    private fun setRecyclerView() {
        rv_movie.adapter = mAdapter
        LinearSnapHelper().attachToRecyclerView(rv_movie)
    }
}