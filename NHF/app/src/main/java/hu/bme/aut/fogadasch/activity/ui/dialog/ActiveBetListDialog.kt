package hu.bme.aut.fogadasch.activity.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.fogadasch.R
import hu.bme.aut.fogadasch.activity.ui.gallery.NewBetFragment
import hu.bme.aut.fogadasch.adapter.ActiveBetListDialogAdapter
import hu.bme.aut.fogadasch.data.BetItemDatabase
import hu.bme.aut.fogadasch.data.item.BetItem
import hu.bme.aut.fogadasch.databinding.FragmentActivbetDialogBinding
import kotlinx.serialization.json.Json
import kotlin.concurrent.thread

class ActiveBetListDialog(private val matchList: String) : DialogFragment(){

    private lateinit var binding: FragmentActivbetDialogBinding
    private lateinit var adapter: ActiveBetListDialogAdapter
    private lateinit var database: BetItemDatabase

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentActivbetDialogBinding.inflate(LayoutInflater.from(context))
        database = BetItemDatabase.getDatabase(applicationContext = activity?.applicationContext!!)
        initRecycleView()


        return AlertDialog.Builder(requireContext())
            .setTitle("Mérkőzés lista")
            .setView(binding.root)
            .setPositiveButton(getString(R.string.back)){ dialog, i ->
                val nothing = null
            }
            .create()
    }

    private fun initRecycleView(){
        thread {
            adapter = ActiveBetListDialogAdapter(database.BetItemDao().getAll())
            binding.rvMain.layoutManager = LinearLayoutManager(activity?.applicationContext)
            binding.rvMain.adapter = adapter

            val items = Json.decodeFromString(NewBetFragment.MatchList.serializer(), matchList)
            adapter.update(items.matchList)
        }
    }

    companion object{
        const val TAG = "ActiveDialogList"
    }
}