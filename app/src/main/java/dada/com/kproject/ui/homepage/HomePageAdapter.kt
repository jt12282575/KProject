package dada.com.kproject.ui.homepage

import android.content.Context
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
import dada.com.kproject.const.ViewStateConst.Companion.HOMEPAGE_SONG_LIST_HEADER_SIZE
import dada.com.kproject.const.ViewStateConst.Companion.SONG_LIST_HEADER
import dada.com.kproject.const.ViewStateConst.Companion.SONG_LIST_ITEM
import dada.com.kproject.model.Category
import dada.com.kproject.model.SongList

class HomePageAdapter(
    private val context: Context,
    private val categories:List<Category>,
    private val onCategoryClick:(category:Category)->Unit
): PagingDataAdapter<SongList, RecyclerView.ViewHolder>(SONG_LIST_COMPARATOR) {


    override fun getItemCount(): Int {
        return super.getItemCount() +
                HOMEPAGE_CATEGORY_HEADER_SIZE +
                HOMEPAGE_CATEGORY_LIST_VERTICAL_SIZE +
                HOMEPAGE_SONG_LIST_HEADER_SIZE
    }

    override fun getItemViewType(position: Int): Int {
        return when(position){
            0 ->{
                CATEGORY_LIST_HEADER
            }
            1 ->{
                CATEGORY_LIST_ITEM
            }
            2 ->{
                SONG_LIST_HEADER
            }
            else->{
                SONG_LIST_ITEM
            }

        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is HeaderViewHolder){
            if (position == 0){
                holder.bind(context.getString(R.string.category_header))
            }else if(position == 2){
                holder.bind(context.getString(R.string.song_list_header))
            }
        }else if(holder is CategoryListViewHolder){
            holder.bind(categories,onCategoryClick)
        }else if(holder is SongListViewHolder){
            val songList =getItem(mapSongListPosition(position))
            songList?.let {
                holder.bind(it)
            }
        } else{

        }
    }

    private fun mapSongListPosition(position:Int):Int{
        return position - (
                HOMEPAGE_CATEGORY_HEADER_SIZE +
                        HOMEPAGE_CATEGORY_LIST_VERTICAL_SIZE +
                        HOMEPAGE_SONG_LIST_HEADER_SIZE
                )
    }

    fun isCategoryHeader(position:Int):Boolean{
        return position == 0
    }

    fun isCategoryItem(position:Int):Boolean{
        return position == 1
    }

    fun isSongListHeader(position:Int):Boolean{
        return position == 2
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
            SONG_LIST_HEADER ->{
                HeaderViewHolder(
                    LayoutInflater.from(context).inflate(
                        R.layout.item_header, parent, false
                    )
                )
            }
            else->{
                SongListViewHolder(
                    LayoutInflater.from(context).inflate(
                        R.layout.item_song_list, parent, false
                    )
                )
            }
        }
    }

    companion object {
        private val SONG_LIST_COMPARATOR = object : DiffUtil.ItemCallback<SongList>() {
            override fun areItemsTheSame(oldItem: SongList, newItem: SongList): Boolean {
                return (oldItem.id == newItem.id)
            }

            override fun areContentsTheSame(oldItem: SongList, newItem: SongList): Boolean =
                ( (oldItem.id == newItem.id) && (oldItem.updatedAt == newItem.updatedAt))
        }
    }
}