package dada.com.kproject.ui.homepage

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import dada.com.kproject.R
import dada.com.kproject.model.Category
import kotlinx.android.synthetic.main.item_category.view.*
import kotlin.math.roundToInt

class CategoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    fun bind(category:Category){
        itemView.ic_tv_category_name.text = category.name
        val imageSize:Int = itemView.context.resources.getDimension(R.dimen.thumb_image_size).roundToInt()
        Picasso.get()
            .load(category.images[0].url)
            .resize(imageSize,imageSize)
            .centerCrop()
            .into(itemView.ic_iv_cover_image)
    }
}