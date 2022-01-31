package hu.bme.aut.fogadasch.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.fogadasch.data.item.BetItem
import hu.bme.aut.fogadasch.data.item.NewBetItem
import hu.bme.aut.fogadasch.databinding.ItemAddMatchBinding
import hu.bme.aut.fogadasch.databinding.ItemNewBetBinding

class AddNewMatchAdapter(private val listener: AddNewMatchClickListener) : RecyclerView.Adapter<AddNewMatchAdapter.AddNewMatchViewHolder>(){

    private val items = mutableListOf<BetItem>()
    private var itemIdx: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = AddNewMatchViewHolder (
        ItemAddMatchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: AddNewMatchViewHolder, position: Int) {
        val betItem = items[position]

        holder.binding.llMainNewBet.setOnClickListener(null)
        holder.binding.team1AddDialog.text = betItem.name1
        holder.binding.tem2AddDialog.text = betItem.name2
        holder.binding.sportCategory.text = BetItem.Category.getCategoryName(betItem.category)
        holder.binding.oddsHome.text = betItem.odds1.toString()
        holder.binding.oddsDraw.text = betItem.oddsX.toString()
        holder.binding.oddsAway.text = betItem.odds2.toString()

        holder.binding.llMainNewBet.setOnClickListener {
            itemIdx = holder.adapterPosition
            notifyDataSetChanged()
            listener.onItemSelected(betItem)
        }

        if (itemIdx == position){
            holder.binding.llMainNewBet.setBackgroundColor(Color.GRAY)
        } else {
            holder.binding.llMainNewBet.setBackgroundColor(Color.WHITE)
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(newItems: List<BetItem>){
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun getItemCount() : Int = items.size

    interface AddNewMatchClickListener{
        fun onItemSelected(item: BetItem)
    }

    inner class AddNewMatchViewHolder(val binding: ItemAddMatchBinding) : RecyclerView.ViewHolder(binding.root)
}