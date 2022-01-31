package hu.bme.aut.fogadasch.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.fogadasch.activity.ui.gallery.NewBetFragment
import hu.bme.aut.fogadasch.data.item.ActiveBetListItem
import hu.bme.aut.fogadasch.databinding.ItemActivBetBinding
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class ActiveBetAdapter(private val listener: ActiveBetListener) : RecyclerView.Adapter<ActiveBetAdapter.ActiveBetViewHolder>(){

    private val items = mutableListOf<ActiveBetListItem>()
    private val systemDate: String = SimpleDateFormat("yyyy.MM.dd").format(Date())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ActiveBetViewHolder(
        ItemActivBetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ActiveBetViewHolder, position: Int) {
        val activeItem = items[position]

        holder.binding.txBasicBet.text = activeItem.basicBet.toString()
        holder.binding.txBasicOdds.text = activeItem.oddsSum.toString()
        holder.binding.txKotes.text = if (activeItem.kombiMin != 0) (activeItem.kombiMin.toString() + "/" + activeItem.kombiMax.toString()) else "Nincs"
        holder.binding.txPrise.text = activeItem.maxPrize.toString()

        holder.binding.llMain.setOnClickListener {
            listener.onItemSelected(activeItem)
        }

        val list = Json.decodeFromString(NewBetFragment.MatchList.serializer(), activeItem.rowItem).matchList

        val closestDate = list.maxByOrNull {
            it.containBetItem.date
        }

        if (closestDate != null) {
            if (systemDate.compareTo(closestDate.containBetItem.date) > 0){
                Log.d("date",closestDate.teamName1)
            }
        }


       // closestDate?.containBetItem?.let { Log.d("date", it.name1) }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(activeItems: List<ActiveBetListItem>){
        items.clear()
        items.addAll(activeItems)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    interface ActiveBetListener {
        fun onItemSelected(item: ActiveBetListItem)
    }

    inner class ActiveBetViewHolder(val binding: ItemActivBetBinding) : RecyclerView.ViewHolder(binding.root)

}