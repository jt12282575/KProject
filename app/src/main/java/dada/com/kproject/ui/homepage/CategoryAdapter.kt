package dada.com.kproject.ui.homepage

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import dada.com.kproject.R
import dada.com.kproject.model.Album
import kotlinx.android.synthetic.main.activity_songlist.view.*
import kotlinx.android.synthetic.main.item_category.view.*

class CategoryAdapter(
    private val context: Context,
    private val albums:List<Album>,
    private val onCategoryClick:((album: Album)->Unit)
): RecyclerView.Adapter<CategoryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_category,parent, false)
        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return albums.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(albums[position])
        holder.itemView.setOnClickListener {
            onCategoryClick.invoke(albums[position])
        }
    }

}