package dada.com.kproject.ui.homepage

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import dada.com.kproject.R
import dada.com.kproject.model.PlayList
import kotlinx.android.synthetic.main.item_play_list.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class PlayListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val sdfOriginal = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZ", Locale.TAIWAN)
    private val sdfConvert = SimpleDateFormat("yyyy/MM/dd")

    fun bind(playList:PlayList){
        itemView.ipl_tv_play_list_name.text = playList.title

        val updateDate= sdfOriginal.parse(playList.updatedAt)
        val dateStr = sdfConvert.format(updateDate)
        itemView.ipl_tv_play_list_owner_and_date.text = "${playList.owner.name}@${dateStr}"
        val imageSize:Int = itemView.context.resources.getDimension(R.dimen.thumb_image_size).roundToInt()
        Picasso.get()
            .load(playList.images[0].url)
            .resize(imageSize,imageSize)
            .centerCrop()
            .into(itemView.ipl_iv_image)
    }

}