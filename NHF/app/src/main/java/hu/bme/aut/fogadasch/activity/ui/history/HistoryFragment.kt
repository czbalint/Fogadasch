package hu.bme.aut.fogadasch.activity.ui.history

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.fogadasch.R
import hu.bme.aut.fogadasch.activity.ui.dialog.ActiveBetListDialog
import hu.bme.aut.fogadasch.activity.ui.gallery.NewBetFragment
import hu.bme.aut.fogadasch.adapter.HistoryAdapter
import hu.bme.aut.fogadasch.data.BetItemDatabase
import hu.bme.aut.fogadasch.data.item.ActiveBetListItem
import hu.bme.aut.fogadasch.databinding.FragmentHistoryBinding
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

class HistoryFragment : Fragment(), HistoryAdapter.HistoryListener {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var database: BetItemDatabase
    private lateinit var adapter: HistoryAdapter

    private val systemDate: String = SimpleDateFormat("yyyy.MM.dd").format(Date())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        database = BetItemDatabase.getDatabase(applicationContext = activity?.applicationContext!!)
        initRecycleView()


        return root
    }

    private fun initRecycleView(){
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
                    if (systemDate.compareTo(closestDate.containBetItem.date) > 0)
                        tmpList.add(item)
                }
            }
            activity?.runOnUiThread {
                adapter = HistoryAdapter(this, itemList)
                binding.rvMain.layoutManager = LinearLayoutManager(activity?.applicationContext)
                binding.rvMain.adapter = adapter
                adapter.update(tmpList)
                //adapter.getStat()
            }
        }
    }

    override fun onItemSelected(item: ActiveBetListItem) {
        ActiveBetListDialog(item.rowItem).show(childFragmentManager, ActiveBetListDialog.TAG)
    }

    override fun onItemLoaded(win: Int, lose: Int) {
        val builder = NotificationCompat.Builder(activity?.applicationContext!!, "asd")
            .setSmallIcon(R.drawable.baseline_sports_soccer_black_48)
            .setContentTitle("Fogadasch")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Nyert meccsek: $win\nVesztett meccsek: $lose"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this.requireContext())){
            notify(2, builder.build())
        }
    }


}