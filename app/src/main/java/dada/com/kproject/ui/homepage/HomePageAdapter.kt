package dada.com.kproject.ui.homepage

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dada.com.kproject.R
import dada.com.kproject.const.ViewStateConst.Companion.CATEGORY_LIST_HEADER
import dada.com.kproject.const.ViewStateConst.Companion.CATEGORY_LIST_ITEM
import dada.com.kproject.const.ViewStateConst.Companion.HOMEPAGE_CATEGORY_HEADER_SIZE
import dada.com.kproject.const.ViewStateConst.Companion.HOMEPAGE_CATEGORY_LIST_VERTICAL_SIZE
import dada.com.kproject.const.ViewStateConst.Companion.HOMEPAGE_PLAY_LIST_HEADER_SIZE
import dada.com.kproject.const.ViewStateConst.Companion.PLAY_LIST_HEADER
import dada.com.kproject.const.ViewStateConst.Companion.PLAY_LIST_ITEM
import dada.com.kproject.model.Category
import dada.com.kproject.model.PlayList
import dada.com.kproject.ui.HeaderViewHolder

class HomePageAdapter(
    private val context: Context,
    private val categories:List<Category>,
    private val onCategoryClick:(category:Category)->Unit,
    private val onPlaylistClick:(playlist:PlayList)->Unit
): PagingDataAdapter<PlayList, RecyclerView.ViewHolder>(PLAY_LIST_COMPARATOR) {


    override fun getItemCount(): Int {
        return if (categories.isNotEmpty()){
            super.getItemCount() +
                    HOMEPAGE_CATEGORY_HEADER_SIZE +
                    HOMEPAGE_CATEGORY_LIST_VERTICAL_SIZE +
                    HOMEPAGE_PLAY_LIST_HEADER_SIZE
        }else{
            super.getItemCount() +
                    HOMEPAGE_PLAY_LIST_HEADER_SIZE
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            isCategoryHeader(position) -> {
                CATEGORY_LIST_HEADER
            }
            isCategoryItem(position) -> {
                CATEGORY_LIST_ITEM
            }
            isPlayListHeader(position) -> {
                PLAY_LIST_HEADER
            }
            else -> {
                PLAY_LIST_ITEM
            }
        }

    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is HeaderViewHolder){
            if (isCategoryHeader(position)){
                holder.bind(context.getString(R.string.category_header))
            }else if(isPlayListHeader(position)){
                holder.bind(context.getString(R.string.play_list_header))
            }
        }else if(holder is CategoryListViewHolder){
            holder.bind(categories,onCategoryClick)
        }else if(holder is PlayListViewHolder){
            val playList =getItem(mapPlayListPosition(position))
            playList?.let {playlist ->
                holder.itemView.setOnClickListener {
                    onPlaylistClick.invoke(playlist)
                }
                holder.bind(playlist)
            }
        } else{

        }
    }

    private fun mapPlayListPosition(position:Int):Int{
        return if (categories.isNotEmpty()){
            position - (
                    HOMEPAGE_CATEGORY_HEADER_SIZE +
                            HOMEPAGE_CATEGORY_LIST_VERTICAL_SIZE +
                            HOMEPAGE_PLAY_LIST_HEADER_SIZE
                    )
        }else{
            position - (
                            HOMEPAGE_PLAY_LIST_HEADER_SIZE
                    )
        }
    }

    private fun isCategoryHeader(position:Int):Boolean{
        return (position == 0 && categories.isNotEmpty())
    }

    private fun isCategoryItem(position:Int):Boolean{
        return (position == 1 && categories.isNotEmpty())
    }

    private fun isPlayListHeader(position:Int):Boolean{
        return (position == 2 && categories.isNotEmpty()) || (position == 0 && categories.isEmpty())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            CATEGORY_LIST_HEADER ->{
                HeaderViewHolder(
                    LayoutInflater.from(context).inflate(
                        R.layout.item_header, parent, false
                    )
                )
            }
            CATEGORY_LIST_ITEM ->{
                CategoryListViewHolder(
                    LayoutInflater.from(context).inflate(
                        R.layout.item_horizontal_category, parent, false
                    )
                )
            }
            PLAY_LIST_HEADER ->{
                HeaderViewHolder(
                    LayoutInflater.from(context).inflate(
                        R.layout.item_header, parent, false
                    )
                )
            }
            else->{
                PlayListViewHolder(
                    LayoutInflater.from(context).inflate(
                        R.layout.item_play_list, parent, false
                    )
                )
            }
        }
    }

    companion object {
        private val PLAY_LIST_COMPARATOR = object : DiffUtil.ItemCallback<PlayList>() {
            override fun areItemsTheSame(oldItem: PlayList, newItem: PlayList): Boolean {
                return (TextUtils.equals(oldItem.id , newItem.id))
            }

            override fun areContentsTheSame(oldItem: PlayList, newItem: PlayList): Boolean =
                ( (TextUtils.equals(oldItem.id , newItem.id)) && (TextUtils.equals(oldItem.updatedAt , newItem.updatedAt)))
        }
    }
}