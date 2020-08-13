package dada.com.kproject.ui.homepage

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_header.view.*

class HeaderViewHolder( itemView: View): RecyclerView.ViewHolder(itemView) {
    fun bind(headerName:String){
        itemView.ih_title.text = headerName
    }
}