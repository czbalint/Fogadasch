package hu.bme.aut.fogadasch.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.fogadasch.activity.ui.gallery.NewBetFragment
import hu.bme.aut.fogadasch.data.item.ActiveBetListItem
import hu.bme.aut.fogadasch.data.item.BetItem
import hu.bme.aut.fogadasch.data.item.NewBetItem
import hu.bme.aut.fogadasch.databinding.ItemActivBetBinding
import kotlinx.serialization.json.Json

class HistoryAdapter(private val listener: HistoryListener, private val betList: List<BetItem>) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private val items = mutableListOf<ActiveBetListItem>()
    private var itemIdx: Int = -1
    private var wonItemsCount: Int = 0
    private var loseItemsCount: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = HistoryViewHolder(
        ItemActivBetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = items[position]

        holder.binding.txBasicBet.text = item.basicBet.toString()
        holder.binding.txBasicOdds.text = item.oddsSum.toString()
        holder.binding.txKotes.text = if (item.kombiMin != 0) (item.kombiMin.toString() + "/" + item.kombiMax.toString()) else "Nincs"
        holder.binding.txPrise.text = item.maxPrize.toString()

        holder.binding.llMain.setOnClickListener {
            listener.onItemSelected(item)
        }

        val matchList = Json.decodeFromString(NewBetFragment.MatchList.serializer(), item.rowItem).matchList
        var winCount = 0
        var multiCount = 0.0

        for (match in matchList){
            var betItem = match.containBetItem
            betList.forEach {
                if (it.id == match.containBetItem.id) betItem = it
            }
            when(match.selectedOdds){
                NewBetItem.BetCategory.HOME -> {
                    if (betItem.score1 > betItem.score2) {
                        winCount++
                        multiCount += betItem.odds1
                    }
                }

                NewBetItem.BetCategory.X -> {
                    if (betItem.score1 == betItem.score2) {
                        winCount++
                        multiCount += betItem.oddsX
                    }
                }

                NewBetItem.BetCategory.AWAY -> {
                    if (betItem.score1 < betItem.score2) {
                        winCount++
                        multiCount += betItem.odds2
                    }
                }
            }
        }

        if (winCount == matchList.size) {
            Log.d("win", item.id.toString())
            itemIdx = holder.adapterPosition
        } else {
            val ret = combiWin(winCount, multiCount, item, matchList.size)
            if (ret != 0.0){
                itemIdx = holder.adapterPosition
                holder.binding.txPrise.text = ret.toString()
            }
        }

        if (itemIdx == position){
            holder.binding.llMain.setBackgroundColor(Color.parseColor("#b5fc8b"))
            wonItemsCount++
        } else {
            holder.binding.llMain.setBackgroundColor(Color.parseColor("#ff5c5c"))
            loseItemsCount++
        }

        if (position + 1 == items.size)
            listener.onItemLoaded(wonItemsCount,loseItemsCount)
    }

    private fun combiWin(winCount: Int, multiCount: Double, item: ActiveBetListItem, sum: Int): Double{
        val haveToWin = (sum - item.kombiMax) + item.kombiMin
        if (winCount < haveToWin){
            return 0.0
        }
        return item.basicBet * multiCount
    }

    fun update(historyItems: List<ActiveBetListItem>){
        items.clear()
        items.addAll(historyItems)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    interface HistoryListener {
        fun onItemSelected(item: ActiveBetListItem)
        fun onItemLoaded(win: Int, lose: Int)
    }

    inner class HistoryViewHolder(val binding: ItemActivBetBinding) : RecyclerView.ViewHolder(binding.root)

}