package dada.com.kproject.ui.songlist

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import dada.com.kproject.R
import dada.com.kproject.model.Album
import dada.com.kproject.model.Song
import dada.com.kproject.util.DateUtil
import kotlinx.android.synthetic.main.item_song.view.*
import kotlin.math.roundToInt

class SonglistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val sdfAlbumFormat = DateUtil.getAlbumDateFormat()
    private val sdfShowFormat = DateUtil.getShowDateFormat()

    fun bind(song: Song, album: Album?) {
        itemView.is_tv_song_name.text = song.name

        val updateDate = sdfAlbumFormat.parse(
            if (song.album != null) {
                song.album.releaseDate
            } else {
                album?.releaseDate
            }
        )
        var dateStr = ""
        updateDate?.let {
            dateStr = sdfShowFormat.format(updateDate)
        }

        var artist = ""
        var imageUrl = ""
        if (song.album != null) {
            artist = song.album!!.artist.name
            imageUrl = song.album!!.images[0].url
        } else if (
            album != null
        ) {
            artist = album.artist.name
            imageUrl = album!!.images[0].url
        }
        itemView.is_tv_artist_and_date.text = "$artist@${dateStr}"
        val imageSize: Int =
            itemView.context.resources.getDimension(R.dimen.thumb_image_size).roundToInt()

        Picasso.get()
            .load(imageUrl)
            .resize(imageSize, imageSize)
            .centerCrop()
            .into(itemView.is_iv_image)
    }
}