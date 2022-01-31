package hu.bme.aut.fogadasch.activity.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.fogadasch.R
import hu.bme.aut.fogadasch.adapter.AddNewMatchAdapter
import hu.bme.aut.fogadasch.data.BetItemDatabase
import hu.bme.aut.fogadasch.data.item.BetItem
import hu.bme.aut.fogadasch.data.item.NewBetItem
import hu.bme.aut.fogadasch.databinding.FragmentSelectMatchDialogBinding
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

class AddNewMatchDialog(private val listener: AddNewMatchDialogListener) : DialogFragment(), AddNewMatchAdapter.AddNewMatchClickListener {
    interface AddNewMatchDialogListener{
        fun onSelectedMatchCreated(newBetItem: NewBetItem?)
    }

    private val systemDate: String = SimpleDateFormat("yyyy.MM.dd").format(Date())

    private lateinit var binding: FragmentSelectMatchDialogBinding
    private lateinit var adapter: AddNewMatchAdapter
    private lateinit var database: BetItemDatabase
    private var returnBetItem: BetItem? = null
    private var betCategory: NewBetItem.BetCategory = NewBetItem.BetCategory.X

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentSelectMatchDialogBinding.inflate(LayoutInflater.from(context))
        database = BetItemDatabase.getDatabase(applicationContext = activity?.applicationContext!!)
        initRecycleView()

        binding.mbtSelect.setOnValueChangedListener { position ->
            when(position){
                0 -> betCategory = NewBetItem.BetCategory.HOME
                1 -> betCategory = NewBetItem.BetCategory.X
                2 -> betCategory = NewBetItem.BetCategory.AWAY
            }
        }

        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.addMatchDialogTitle))
            .setView(binding.root)
            .setPositiveButton(R.string.button_ok) { dialogInterface, i ->
                    if (returnBetItem == null) {
                        listener.onSelectedMatchCreated(null)
                    } else {
                        listener.onSelectedMatchCreated(when(betCategory){
                            NewBetItem.BetCategory.HOME -> {
                                returnBetItem?.odds1
                            }
                            NewBetItem.BetCategory.X -> {
                                returnBetItem?.oddsX
                            }
                            NewBetItem.BetCategory.AWAY -> {
                                returnBetItem?.odds2
                            }
                        }?.let {
                            NewBetItem(returnBetItem?.name1!!, returnBetItem?.name2!!, betCategory,
                                it, returnBetItem!!
                            )
                        })
                    }
            }
            .setNegativeButton(R.string.button_cancel) { dialogInterface, i ->
                listener.onSelectedMatchCreated(null)
            }
            .create()
    }

    private fun initRecycleView(){
        adapter = AddNewMatchAdapter(this)
        binding.rvMatches.layoutManager = LinearLayoutManager(activity?.applicationContext)
        binding.rvMatches.adapter = adapter
        loadItemsInBackground()
    }

    private fun loadItemsInBackground(){
        thread {
            val items = database.BetItemDao().getAll()
            val selected = mutableListOf<BetItem>()

            for (item in items){
                if (systemDate.compareTo(item.date) < 0)
                    selected.add(item)
            }

            activity?.runOnUiThread {
                adapter.update(selected)
            }
        }
    }

    override fun onItemSelected(item: BetItem) {
            returnBetItem = item
    }

    override fun onDestroyView() {
        super.onDestroyView()
        database.close()
    }

    companion object {
        const val TAG = "AddNewDialog"
    }

}