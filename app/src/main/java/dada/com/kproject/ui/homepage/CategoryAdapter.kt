package dada.com.kproject.ui.homepage

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dada.com.kproject.R
import dada.com.kproject.model.Category

class CategoryAdapter(
    private val context: Context,
    private val categories:List<Category>,
    private val onCategoryClick:((category: Category)->Unit)
): RecyclerView.Adapter<CategoryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_category,parent, false)
        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position])
        holder.itemView.setOnClickListener {
            onCategoryClick.invoke(categories[position])
        }
    }

}