package dada.com.kproject.ui.songlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dada.com.kproject.R
import dada.com.kproject.const.ViewStateConst.Companion.SONGLIST_HEADER
import dada.com.kproject.const.ViewStateConst.Companion.SONGLIST_HEADER_SIZE
import dada.com.kproject.const.ViewStateConst.Companion.SONGLIST_ITEM
import dada.com.kproject.model.Song
import dada.com.kproject.ui.HeaderViewHolder

class SonglistAdapter(
    private val songlist: List<Song>,
    private val albumReleaseDate:String,
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
            else -> {
                (holder as SonglistViewHolder).bind(songlist[mapSonglistPosition(position)],albumReleaseDate)
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