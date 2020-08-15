package dada.com.kproject.ui.songlist

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import dada.com.kproject.R
import dada.com.kproject.const.ApiConst.Companion.SOMETHING_WRONG
import dada.com.kproject.const.BundleKey.Companion.ARG_ID
import dada.com.kproject.const.BundleKey.Companion.ARG_IMAGE
import dada.com.kproject.const.BundleKey.Companion.ARG_RELEASE_DATE
import dada.com.kproject.const.BundleKey.Companion.ARG_TYPE
import dada.com.kproject.model.Song
import dada.com.kproject.util.logi
import kotlinx.android.synthetic.main.activity_songlist.*


class SonglistActivity : AppCompatActivity() {
    private var id = ""
    private var type = SOMETHING_WRONG
    private var imageUrl = ""
    private var releaseDate = ""
    private val songlist = mutableListOf<Song>()
    private val songlistAdapter  by lazy {
        SonglistAdapter(songlist,releaseDate){

        }
    }

    private val model:SonglistViewModel by viewModels { SonglistVMFactory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_songlist)
        init()
        logi("id: $id")
        model.loadSonglist(id,type)

        as_rcv_songlist.apply {
            layoutManager = LinearLayoutManager(this@SonglistActivity)
            adapter = songlistAdapter
        }
        val dm = DisplayMetrics()
        this.windowManager.defaultDisplay.getMetrics(dm)


        if (imageUrl.isNotEmpty()){
            Picasso.get()
                .load(imageUrl)
                .resize(dm.widthPixels,dm.widthPixels)
                .centerCrop()
                .into(as_iv_cover_image)
        }



        model.fetchTracks().observe(this, Observer {
            if (it.data != null){
                songlist.clear()
                songlist.addAll(it.data.data)
                songlistAdapter.notifyDataSetChanged()
            }
        })


    }

    private fun init() {
        id = intent.getStringExtra(ARG_ID)
        type = intent.getIntExtra(ARG_TYPE,SOMETHING_WRONG)
        imageUrl = intent.getStringExtra(ARG_IMAGE)
//        releaseDate = intent.getStringExtra(ARG_RELEASE_DATE)
    }
}