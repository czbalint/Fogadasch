package hu.bme.aut.fogadasch.activity.ui.home

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import hu.bme.aut.fogadasch.R
import hu.bme.aut.fogadasch.activity.NavActivity
import hu.bme.aut.fogadasch.activity.ui.dialog.BetItemDetailsDialog
import hu.bme.aut.fogadasch.adapter.BetAdapter
import hu.bme.aut.fogadasch.data.BetItemDatabase
import hu.bme.aut.fogadasch.data.item.BetItem
import hu.bme.aut.fogadasch.databinding.AppBarNavBinding
import hu.bme.aut.fogadasch.databinding.FragmentHomeBinding
import java.time.LocalDate
import kotlin.concurrent.thread
import kotlin.math.log

class HomeFragment : Fragment(), BetAdapter.BetItemClickListener {


    private var _binding: FragmentHomeBinding? = null
    private lateinit var database: BetItemDatabase
    private lateinit var adapter: BetAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        homeViewModel =
//            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        database = BetItemDatabase.getDatabase(applicationContext = activity?.applicationContext!!)
        binding.fab.setOnClickListener { view ->
            loadItemsInBackground()
        }

        val root: View = binding.root
        setHasOptionsMenu(true)
        initRecyclerView()

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.nav, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.football -> {
                Log.d("football", "foci")
                sortBetRv(Category.FOOTBALL)
                true
            }
            R.id.basketball -> {
                Log.d("basketball", "kosar")
                sortBetRv(Category.BASKETBALL)
                true
            }
            R.id.fav -> {
                Log.d("fav","fav")
                sortBetRv(Category.FAVOURITE)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initRecyclerView(){
        adapter = BetAdapter(this)
        binding.rvBetList.layoutManager = LinearLayoutManager(activity?.applicationContext)
        binding.rvBetList.adapter = adapter
        loadItemsInBackground()
    }

    private fun loadItemsInBackground(){
        thread {
            val items = database.BetItemDao().getAll()
            activity?.runOnUiThread {
                adapter.update(items)
            }
        }
    }

    private fun sortBetRv(sortCategory: Category){
        when (sortCategory){
            Category.FOOTBALL -> {
                thread {
                    val items = database.BetItemDao().getFootball()
                    activity?.runOnUiThread {
                        adapter.update(items)
                    }
                }
            }
            Category.BASKETBALL -> {
                thread {
                    val items = database.BetItemDao().getBasketball()
                    activity?.runOnUiThread {
                        adapter.update(items)
                    }
                }
            }
            Category.FAVOURITE -> {
                thread {
                    val items = database.BetItemDao().getFavourite()
                    activity?.runOnUiThread {
                        adapter.update(items)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        database.close()
    }

    override fun onItemChange(item: BetItem) {
        thread {
            database.BetItemDao().update(item)
        }
    }

    override fun onItemSelected(item: BetItem) {
        activity?.supportFragmentManager?.let {
            BetItemDetailsDialog.newInstance(item).show(
                it,
                BetItemDetailsDialog.TAG
            )
        }
    }
}

enum class Category{
    FOOTBALL, BASKETBALL, FAVOURITE
}