package hu.bme.aut.fogadasch.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.fogadasch.data.item.BetItem
import hu.bme.aut.fogadasch.databinding.ItemBetListBinding

class BetAdapter(private val listener: BetItemClickListener) : RecyclerView.Adapter<BetAdapter.BetViewHolder>(){

    private val items = mutableListOf<BetItem>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BetViewHolder(
        ItemBetListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: BetAdapter.BetViewHolder, position: Int) {
        val betItem = items[position]

        holder.binding.cbFavourite.setOnCheckedChangeListener(null)
        holder.binding.cbFavourite.isChecked = betItem.suspect
        holder.binding.date.text = betItem.date
        holder.binding.score1.text = betItem.score1.toString()
        holder.binding.score2.text = betItem.score2.toString()
        holder.binding.team1.text = betItem.name1
        holder.binding.team2.text = betItem.name2

        holder.binding.cbFavourite.setOnCheckedChangeListener {
            buttonView, isChecked ->
            updateItem(position)
        }

        holder.binding.llMain.setOnClickListener {
            Log.d("test_onclick_holder","mukodik: ${position}")
            listener.onItemSelected(items[position])
        }
    }

    private fun updateItem(position: Int) {
        items[position].suspect = !items[position].suspect
        listener.onItemChange(items[position])
    }

    fun addItem(item: BetItem){
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(betItem: List<BetItem>){
        items.clear()
        items.addAll(betItem)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    interface BetItemClickListener{
        fun onItemChange(item: BetItem)
        fun onItemSelected(item: BetItem)
    }

    inner class BetViewHolder(val binding: ItemBetListBinding) : RecyclerView.ViewHolder(binding.root)
}