package dada.com.kproject.ui.songlist

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dada.com.kproject.R
import dada.com.kproject.const.ApiConst.Companion.SOMETHING_WRONG
import dada.com.kproject.const.BundleKey.Companion.ARG_ALBUM
import dada.com.kproject.const.BundleKey.Companion.ARG_ID
import dada.com.kproject.const.BundleKey.Companion.ARG_IMAGE
import dada.com.kproject.const.BundleKey.Companion.ARG_TYPE
import dada.com.kproject.const.BundleKey.Companion.ARG_URL
import dada.com.kproject.model.Album
import dada.com.kproject.model.Song
import dada.com.kproject.ui.song.SongPageActivity
import dada.com.kproject.util.logi
import dada.com.kproject.util.wrapper.Status
import kotlinx.android.synthetic.main.activity_songlist.*
import java.lang.Exception


class SonglistActivity : AppCompatActivity() {
    private var id = ""
    private var type = SOMETHING_WRONG
    private var imageUrl = ""
    private val songlist = mutableListOf<Song>()
    private var album: Album? = null
    private val songlistAdapter  by lazy {
        SonglistAdapter(songlist,album){
            val intent = Intent(this,SongPageActivity::class.java)
            intent.putExtra(ARG_URL,it.url)
            startActivity(intent)
        }
    }

    private val model:SonglistViewModel by viewModels { SonglistVMFactory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_songlist)
        init()
        model.loadSonglist(id,type)

        as_rcv_songlist.apply {
            layoutManager = LinearLayoutManager(this@SonglistActivity)
            adapter = songlistAdapter
        }

        val dm = DisplayMetrics()
        as_retry_button.setOnClickListener {
            as_retry_button.isEnabled = false
            model.loadSonglist(id,type)
        }

        this.windowManager.defaultDisplay.getMetrics(dm)
        if (imageUrl.isNotEmpty()){
            Picasso.get()
                .load(imageUrl)
                .noFade()
                .resize(dm.widthPixels,dm.widthPixels)
                .centerCrop()
                .placeholder(R.color.white)
                .into(as_iv_cover_image)
        }

        model.spinner.observe(this, Observer {
            as_progress_bar.isVisible = it
        })

        model.fetchTracks().observe(this, Observer {
            as_retry_button.isEnabled = true
            if (it.data != null){
                songlist.clear()
                songlist.addAll(it.data.data)
                songlistAdapter.notifyDataSetChanged()
            }
            as_retry_button.isVisible = it.status == Status.ERROR
            as_appbar.isVisible = it.status != Status.ERROR
        })

        model.fetchToken().observe(this, Observer {

        })


    }

    private fun init() {
        id = intent.getStringExtra(ARG_ID)
        type = intent.getIntExtra(ARG_TYPE,SOMETHING_WRONG)
        imageUrl = intent.getStringExtra(ARG_IMAGE)
        val albumObj  = intent.getSerializableExtra(ARG_ALBUM)
        albumObj?.let {
            album = albumObj as Album
        }
    }
}