package dada.com.kproject.ui.songlist

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import dada.com.kproject.R
import dada.com.kproject.model.Song
import dada.com.kproject.util.DateUtil
import kotlinx.android.synthetic.main.item_play_list.view.*
import kotlinx.android.synthetic.main.item_song.view.*
import kotlin.math.roundToInt

class SonglistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val sdfAlbumFormat = DateUtil.getAlbumDateFormat()
    private val sdfShowFormat = DateUtil.getShowDateFormat()

    fun bind(song: Song,releaseData:String){
        itemView.is_tv_song_name.text = song.name

        val updateDate= sdfAlbumFormat.parse(
            if (song.album!= null){
                song.album.releaseData
            }else{
                releaseData
            }
        )
        val dateStr = sdfShowFormat.format(updateDate)
        val artist = ""
        itemView.is_tv_artist_and_date.text = "${song.album.artist.name}@${dateStr}"
        val imageSize:Int = itemView.context.resources.getDimension(R.dimen.thumb_image_size).roundToInt()
        Picasso.get()
            .load(song.album.images[0].url)
            .resize(imageSize,imageSize)
            .centerCrop()
            .into(itemView.is_iv_image)
    }
}