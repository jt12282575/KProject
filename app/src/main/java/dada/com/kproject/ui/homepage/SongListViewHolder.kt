package dada.com.kproject.ui.homepage

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import dada.com.kproject.R
import dada.com.kproject.model.SongList
import kotlinx.android.synthetic.main.item_song_list.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class SongListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val sdfOriginal = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZ", Locale.TAIWAN)
    private val sdfConvert = SimpleDateFormat("yyyy/MM/dd")

    fun bind(songList:SongList){
        itemView.isl_tv_song_list_name.text = songList.title

        val updateDate= sdfOriginal.parse(songList.updatedAt)
        val dateStr = sdfConvert.format(updateDate)
        itemView.isl_tv_song_list_owner_and_date.text = "${songList.owner.name}@${dateStr}"
        val imageSize:Int = itemView.context.resources.getDimension(R.dimen.category_image_size).roundToInt()
        Picasso.get()
            .load(songList.images[0].url)
            .resize(imageSize,imageSize)
            .centerCrop()
            .into(itemView.isl_iv_image)
    }

}