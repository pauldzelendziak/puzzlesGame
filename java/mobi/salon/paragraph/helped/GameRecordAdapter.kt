package mobi.salon.paragraph.helped

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mobi.salon.paragraph.helped.databinding.ItemGameRecordBinding

class GameRecordAdapter(private val records: List<GameRecord>) :
    RecyclerView.Adapter<GameRecordAdapter.GameRecordViewHolder>() {

    inner class GameRecordViewHolder(private val binding: ItemGameRecordBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(record: GameRecord) {
            binding.tvPlayedTime.text = record.playedTime
            binding.tvLevel.text = record.level
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameRecordViewHolder {
        val binding = ItemGameRecordBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GameRecordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GameRecordViewHolder, position: Int) {
        holder.bind(records[position])
    }

    override fun getItemCount(): Int = records.size
}

