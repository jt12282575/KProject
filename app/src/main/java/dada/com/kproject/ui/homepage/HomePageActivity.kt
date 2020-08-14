package dada.com.kproject.ui.homepage


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import dada.com.kproject.R
import dada.com.kproject.model.Category
import dada.com.kproject.util.logi
import kotlinx.android.synthetic.main.activity_homepage.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

class HomePageActivity : AppCompatActivity() {

    val model:HomePageViewModel by viewModels { LiveDataVMFactory }
    private val categories = mutableListOf<Category>()
    private val homePageAdapter by lazy {
        HomePageAdapter(applicationContext,categories){

        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)
        ah_rcv_list.apply {
            layoutManager = LinearLayoutManager(applicationContext)

            adapter = homePageAdapter.withLoadStateHeaderAndFooter(
                header = PlayListStateAdapter{
                    homePageAdapter.retry()
                },
                footer = PlayListStateAdapter{
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


        model.tenNewReleaseCategories.observe(this, Observer {
            if (it.data != null){
                categories.clear()
                categories.addAll(it.data!!)
                homePageAdapter.notifyDataSetChanged()
            }
            logi("size : ${it.data?.size}")
            if(it?.message != null){
                logi("error : ${it?.message}")
            }
        })


        model.playList.observe(this,
            Observer{
                logi("play list size : ${it.data?.body()?.data?.size}")
                if(it?.message != null){
                    logi("error : ${it?.message}")
                }
            }
        )

        model.fetchToken.observe(this, Observer {
            logi("finish fetch")
        })

        loadPlayList()

    }

    private fun loadPlayList(){
        lifecycleScope.launch {
            model.loadPlayList().collectLatest{
                homePageAdapter.submitData(it)
            }
        }
    }
}