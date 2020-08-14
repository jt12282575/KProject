package dada.com.kproject.ui.homepage


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dada.com.kproject.R
import dada.com.kproject.const.ViewStateConst.Companion.CATEGORY_LIST_ITEM_INDEX
import dada.com.kproject.const.ViewStateConst.Companion.HOMEPAGE_CATEGORY_LIST_VERTICAL_SIZE
import dada.com.kproject.model.Category
import dada.com.kproject.util.logi
import kotlinx.android.synthetic.main.activity_homepage.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomePageActivity : AppCompatActivity() {

    val model:HomePageViewModel by viewModels() { LiveDataVMFactory }
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
            adapter = homePageAdapter
        }


        model.tenNewReleaseCategories.observe(this, Observer {
            if (it.data != null){
                categories.clear()
                categories.addAll(it.data!!)
                homePageAdapter.notifyItemRangeChanged(CATEGORY_LIST_ITEM_INDEX,HOMEPAGE_CATEGORY_LIST_VERTICAL_SIZE)
            }
            logi("size : ${it.data?.size}")
            if(it?.message != null){
                logi("error : ${it?.message}")
            }
        })

        model.showError.observe(this, Observer {
            //TODO 畫面是否 error message 蓋住
        })

        model.songList.observe(this,
            Observer{
                logi("song list size : ${it.data?.body()?.data?.size}")
                if(it?.message != null){
                    logi("error : ${it?.message}")
                }
            }
        )

        model.fetchToken.observe(this, Observer {
            logi("finish fetch")
        })

        loadSongList()

    }

    private fun loadSongList(){
        lifecycleScope.launch {
            model.loadSongList().collectLatest{
                homePageAdapter.submitData(it)
            }
        }
    }
}