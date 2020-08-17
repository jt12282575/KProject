package dada.com.kproject.ui.homepage

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dada.com.kproject.R
import dada.com.kproject.model.Album

class CategoryListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private lateinit var rcvCategories:RecyclerView

    fun bind(albums:List<Album>, onCategoryClick:(album:Album)->Unit){
        rcvCategories = itemView.findViewById(R.id.vh_rcv_category_list)
        rcvCategories.apply {
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false)
            adapter = CategoryAdapter(itemView.context,albums,onCategoryClick)
        }
    }
}