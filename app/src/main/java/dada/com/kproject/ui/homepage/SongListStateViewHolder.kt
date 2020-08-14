package dada.com.kproject.ui.homepage

import android.view.View
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_load_state_footer.view.*

class SongListStateViewHolder(retry:(()->Unit),itemView: View): RecyclerView.ViewHolder(itemView) {

    init {
        itemView.ilsf_retry_button.setOnClickListener {
            retry.invoke()
        }
    }

    fun bind(loadState: LoadState){
        if (loadState is LoadState.Error) {
            itemView.ilsf_error_msg.text = loadState.error.localizedMessage
        }
        itemView.ilsf_progress_bar.isVisible = loadState is LoadState.Loading
        itemView.ilsf_retry_button.isVisible = loadState !is LoadState.Loading
        itemView.ilsf_error_msg.isVisible = loadState !is LoadState.Loading
    }

}