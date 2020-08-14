package dada.com.kproject.ui.homepage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import dada.com.kproject.R

class SongListStateAdapter(
    private val retry:()->Unit
): LoadStateAdapter<SongListStateViewHolder>() {
    override fun onBindViewHolder(holder: SongListStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): SongListStateViewHolder {
        return SongListStateViewHolder(
            retry,
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_load_state_footer, parent, false)
        )
    }

}