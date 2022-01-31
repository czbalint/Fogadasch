package hu.bme.aut.fogadasch.activity.ui.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.fogadasch.activity.ui.dialog.ActiveBetListDialog
import hu.bme.aut.fogadasch.activity.ui.gallery.NewBetFragment
import hu.bme.aut.fogadasch.adapter.ActiveBetAdapter
import hu.bme.aut.fogadasch.data.BetItemDatabase
import hu.bme.aut.fogadasch.data.item.ActiveBetListItem
import hu.bme.aut.fogadasch.databinding.FragmentActiveBetBinding
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread


class ActiveBetListFragment : Fragment(), ActiveBetAdapter.ActiveBetListener {

    private lateinit var database: BetItemDatabase
    private lateinit var adapter: ActiveBetAdapter
    private var _binding: FragmentActiveBetBinding? = null
    private val systemDate: String = SimpleDateFormat("yyyy.MM.dd").format(Date())

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentActiveBetBinding.inflate(inflater, container, false)
        val root: View = binding.root
        database = BetItemDatabase.getDatabase(applicationContext = activity?.applicationContext!!)
        initRecyclerView()


        return root
    }

    private fun initRecyclerView(){
        adapter = ActiveBetAdapter(this)
        binding.rvMain.layoutManager = LinearLayoutManager(activity?.applicationContext)
        binding.rvMain.adapter = adapter
        loadItemsInBackground()
    }

    private fun loadItemsInBackground(){
        thread {
            val itemList = database.BetItemDao().getAll()
            val items = database.ActiveBetDao().getAll()
            val tmpList = mutableListOf<ActiveBetListItem>()
            for (item in items){
                val matchList = Json.decodeFromString(NewBetFragment.MatchList.serializer(), item.rowItem).matchList
                for (m in matchList){
                    for (i in itemList){
                        if (m.containBetItem.id == i.id) {
                            m.containBetItem.date = i.date
                            break
                        }
                    }
                }
                val closestDate = matchList.maxByOrNull {
                    it.containBetItem.date
                }

                if (closestDate != null){
                    if (systemDate.compareTo(closestDate.containBetItem.date) < 0)
                        tmpList.add(item)
                }
            }
            activity?.runOnUiThread {
                adapter.update(tmpList)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemSelected(item: ActiveBetListItem) {
        ActiveBetListDialog(item.rowItem).show(childFragmentManager, ActiveBetListDialog.TAG)
        //Toast.makeText(activity?.applicationContext,"katt",Toast.LENGTH_SHORT).show()

    }
}