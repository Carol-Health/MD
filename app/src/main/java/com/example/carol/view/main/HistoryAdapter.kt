import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.carol.R
import com.example.carol.network.HistoryResponse
import com.bumptech.glide.Glide;

class HistoryAdapter(private val onItemClicked: (HistoryResponse) -> Unit) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private var historyList = listOf<HistoryResponse>()

    fun submitList(newList: List<HistoryResponse>) {
        historyList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = historyList[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onItemClicked(item)
        }
    }

    override fun getItemCount(): Int = historyList.size

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        private val diseaseImageView: ImageView = itemView.findViewById(R.id.diseaseImageView)
        private val diseaseNameTextView: TextView = itemView.findViewById(R.id.diseaseNameTextView)

        fun bind(item: HistoryResponse) {
            dateTextView.text = item.date
            diseaseNameTextView.text = item.diseaseName

            val correctedUrl = item.imageUrl.replace("carol-image-predict/images/carol-image-predict/images", "carol-image-predict/images")

            Glide.with(itemView.context)
                .load(correctedUrl)
                .into(diseaseImageView)

        }
    }
}
