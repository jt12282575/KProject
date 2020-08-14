package dada.com.kproject.ui.homepage

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import dada.com.kproject.R
import dada.com.kproject.model.PlayList
import dada.com.kproject.util.DateUtil.Companion.getApiDateFormat
import dada.com.kproject.util.DateUtil.Companion.getShowDateFormat
import kotlinx.android.synthetic.main.item_play_list.view.*
import kotlin.math.roundToInt

class PlayListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val sdfApiFormat = getApiDateFormat()
    private val sdfShowFormat = getShowDateFormat()

    fun bind(playList:PlayList){
        itemView.ipl_tv_play_list_name.text = playList.title

        val updateDate= sdfApiFormat.parse(playList.updatedAt)
        val dateStr = sdfShowFormat.format(updateDate)
        itemView.ipl_tv_play_list_owner_and_date.text = "${playList.owner.name}@${dateStr}"
        val imageSize:Int = itemView.context.resources.getDimension(R.dimen.thumb_image_size).roundToInt()
        Picasso.get()
            .load(playList.images[0].url)
            .resize(imageSize,imageSize)
            .centerCrop()
            .into(itemView.ipl_iv_image)
    }

}