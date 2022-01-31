package hu.bme.aut.fogadasch.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.fogadasch.R
import hu.bme.aut.fogadasch.data.BetItemDatabase
import hu.bme.aut.fogadasch.data.item.ActiveBetListItem
import hu.bme.aut.fogadasch.data.item.BetItem
import hu.bme.aut.fogadasch.data.item.NewBetItem
import hu.bme.aut.fogadasch.databinding.ItemActivebetDialogBinding

class ActiveBetListDialogAdapter(private var betList: List<BetItem>) : RecyclerView.Adapter<ActiveBetListDialogAdapter.ActiveBetListViewHolder>(){

    private val items = mutableListOf<NewBetItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ActiveBetListViewHolder(
        ItemActivebetDialogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ActiveBetListViewHolder, position: Int) {
        val listItem = items[position]
        var betItem: BetItem = listItem.containBetItem

        if (listItem.containBetItem.category == BetItem.Category.FOOTBALL) holder.binding.imCategory.setImageResource(R.drawable.baseline_sports_soccer_black_48)
            else holder.binding.imCategory.setImageResource(R.drawable.baseline_sports_basketball_black_48)

        betList.forEach { item ->
            if (item.id == listItem.containBetItem.id) betItem = item
        }

        holder.binding.txTeam1Dialog.text = listItem.teamName1
        holder.binding.txTeam2Dialog.text = listItem.teamName2
        holder.binding.txScore1.text = betItem.score1.toString()
        holder.binding.txScore2.text = betItem.score2.toString()
        holder.binding.txSelectedCat.text = NewBetItem.BetCategory.getNameByCat(listItem.selectedOdds)
        holder.binding.txOddsListDialog.text = listItem.selectedOddsValue.toString()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(activeItems: List<NewBetItem>){
        items.clear()
        items.addAll(activeItems)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    inner class ActiveBetListViewHolder(val binding: ItemActivebetDialogBinding) : RecyclerView.ViewHolder(binding.root)

}