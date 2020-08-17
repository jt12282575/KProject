package dada.com.kproject.ui.homepage


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import dada.com.kproject.R
import dada.com.kproject.const.ApiConst.Companion.NEW_RELEASE_CATEGORIES_TRACKS
import dada.com.kproject.const.ApiConst.Companion.PLAY_LIST_TRACKS
import dada.com.kproject.const.BundleKey.Companion.ARG_ALBUM
import dada.com.kproject.const.BundleKey.Companion.ARG_ID
import dada.com.kproject.const.BundleKey.Companion.ARG_IMAGE
import dada.com.kproject.const.BundleKey.Companion.ARG_TYPE
import dada.com.kproject.model.Album
import dada.com.kproject.ui.songlist.SonglistActivity
import dada.com.kproject.util.logi
import dada.com.kproject.util.needToRefreshToken
import kotlinx.android.synthetic.main.activity_homepage.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

class HomePageActivity : AppCompatActivity() {

    private var errorState: LoadState.Error? = null
    val model: HomePageViewModel by viewModels { HomePageVMFactory }
    private val categories = mutableListOf<Album>()
    private val homePageAdapter by lazy {
        val intent = Intent(this, SonglistActivity::class.java)
        HomePageAdapter(applicationContext, categories,
            onCategoryClick = {album:Album ->
                intent.apply {
                    putExtra(ARG_ID, album.id)
                    putExtra(ARG_TYPE, NEW_RELEASE_CATEGORIES_TRACKS)
                    putExtra(ARG_IMAGE, album.images[1].url)
                    putExtra(ARG_ALBUM,album)
                }
                startActivity(intent)
            },
            onPlaylistClick = {
                intent.apply {
                    putExtra(ARG_ID, it.id)
                    putExtra(ARG_TYPE, PLAY_LIST_TRACKS)
                    putExtra(ARG_IMAGE, it.images[1].url)
                }
                startActivity(intent)
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)
        ah_rcv_list.apply {
            layoutManager = LinearLayoutManager(applicationContext)

            adapter = homePageAdapter.withLoadStateHeaderAndFooter(
                header = PlayListStateAdapter {
                    homePageAdapter.retry()
                },
                footer = PlayListStateAdapter {
                    homePageAdapter.retry()
                }
            )
        }

        ah_retry_button.setOnClickListener {
            homePageAdapter.retry()
            model.loadTenCategories()
        }

        homePageAdapter.addLoadStateListener { loadState ->
            ah_rcv_list.isVisible = loadState.source.refresh is LoadState.NotLoading
            ah_progress_bar.isVisible = loadState.source.refresh is LoadState.Loading
            ah_retry_button.isVisible = loadState.source.refresh is LoadState.Error
            errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                        ?: loadState.append as? LoadState.Error
                        ?: loadState.prepend as? LoadState.Error
                        ?: loadState.refresh as? LoadState.Error
            errorState?.let {
                if (needToRefreshToken(it.error)) {
                    model.refreshToken()
                }
            }

        }

        lifecycleScope.launch {
            homePageAdapter.loadStateFlow
                .distinctUntilChangedBy {
                    it.refresh
                }.filter {
                    it.refresh is LoadState.NotLoading
                }.collect {
                    ah_rcv_list.scrollToPosition(0)
                }
        }

        model.fetchToken().observe(this, Observer {

        })


        model.fetchTenNewReleaseCategories().observe(this, Observer {
            if (it.data != null && it.data.data.isNotEmpty()) {
                categories.clear()
                categories.addAll(it.data.data)
                homePageAdapter.notifyDataSetChanged()
            }
        })




        model.isTokenRefreshed().observe(this, Observer {
            errorState?.let {
                if (needToRefreshToken(it.error)) {
                    homePageAdapter.retry()
                }
            }
        })


        loadCategories()
        loadPlayList()

    }

    private fun loadCategories() {
        model.loadTenCategories()
    }

    private fun loadPlayList() {
        lifecycleScope.launch {
            model.loadPlayList().collectLatest {
                homePageAdapter.submitData(it)
            }
        }
    }
}