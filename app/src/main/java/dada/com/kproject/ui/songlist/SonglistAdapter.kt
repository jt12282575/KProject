package dada.com.kproject.ui.songlist

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dada.com.kproject.R
import dada.com.kproject.const.ViewStateConst.Companion.SONGLIST_HEADER
import dada.com.kproject.const.ViewStateConst.Companion.SONGLIST_HEADER_SIZE
import dada.com.kproject.const.ViewStateConst.Companion.SONGLIST_ITEM
import dada.com.kproject.model.Album
import dada.com.kproject.model.Song
import dada.com.kproject.ui.HeaderViewHolder
import kotlinx.android.synthetic.main.item_song.view.*

class SonglistAdapter(
    private val songlist: List<Song>,
    private val album: Album?,
    private val songOnClick:((song:Song)->Unit)
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            SONGLIST_HEADER -> {
                HeaderViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.item_header, parent, false)
                )
            }
            else -> {
                SonglistViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
                )
            }


        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isSongListHeader(position)) {
            SONGLIST_HEADER
        } else {
            SONGLIST_ITEM
        }
    }

    override fun getItemCount(): Int {
        return if (songlist.isNotEmpty()) {
            songlist.size + SONGLIST_HEADER_SIZE
        } else {
            0
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                holder.bind(holder.itemView.context.getString(R.string.songlist_header))
            }
            is SonglistViewHolder-> {

                holder.bind(songlist[mapSonglistPosition(position)],album)
                holder.itemView.is_tv_artist_and_date.setOnClickListener {
                    holder.itemView.is_tv_artist_and_date.isSelected = true
                    holder.itemView.is_tv_artist_and_date.marqueeRepeatLimit = 1
                    holder.itemView.is_tv_artist_and_date.ellipsize = TextUtils.TruncateAt.MARQUEE
                }
            }

        }
    }

    private fun mapSonglistPosition(position: Int):Int{
        return position - SONGLIST_HEADER_SIZE
    }

    private fun isSongListHeader(position: Int): Boolean {
        return (songlist.isNotEmpty() && position == 0)
    }
}