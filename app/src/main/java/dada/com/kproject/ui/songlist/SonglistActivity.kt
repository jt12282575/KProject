package dada.com.kproject.ui.songlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import dada.com.kproject.R
import dada.com.kproject.const.BundleKey.Companion.ARG_PLAYLIST_ID

class SonglistActivity : AppCompatActivity() {
    private var playlistId = ""
    private val model:SonglistViewModel by viewModels { SonglistVMFactory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_songlist)
        init()
    }

    private fun init() {
        playlistId = intent.getStringExtra(ARG_PLAYLIST_ID)
    }
}