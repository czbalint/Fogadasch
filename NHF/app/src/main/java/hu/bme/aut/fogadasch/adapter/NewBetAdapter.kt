package hu.bme.aut.fogadasch.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.fogadasch.data.item.NewBetItem
import hu.bme.aut.fogadasch.databinding.ItemNewBetBinding

class NewBetAdapter(private val listener: NewBetClickListener) : RecyclerView.Adapter<NewBetAdapter.NewBetViewHolder>(){

    val items = mutableListOf<NewBetItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = NewBetViewHolder (
        ItemNewBetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: NewBetViewHolder, position: Int) {
        val betItem = items[position]

        holder.binding.idItem.text = (position + 1).toString()
        holder.binding.txTeam1.text = betItem.teamName1
        holder.binding.txTeam2.text = betItem.teamName2
        holder.binding.txSelectedOdds.text = NewBetItem.BetCategory.getNameByCat(betItem.selectedOdds)
        holder.binding.txOdds.text = betItem.selectedOddsValue.toString()

        holder.binding.btRemoveItem.setOnClickListener {
            deleteItem(position)
            listener.onItemDeleted(betItem)
        }

    }

    fun addItem(item: NewBetItem) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(newItems: List<NewBetItem>){
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    private fun deleteItem(position: Int){
        items.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, items.size)
    }

    override fun getItemCount() : Int = items.size

    interface NewBetClickListener{
        fun onItemDeleted(item: NewBetItem)
    }

    inner class NewBetViewHolder(val binding: ItemNewBetBinding) : RecyclerView.ViewHolder(binding.root)
}