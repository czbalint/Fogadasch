package hu.bme.aut.fogadasch.activity.ui.gallery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.fogadasch.R
import hu.bme.aut.fogadasch.activity.ui.dialog.AddNewMatchDialog
import hu.bme.aut.fogadasch.adapter.NewBetAdapter
import hu.bme.aut.fogadasch.data.BetItemDatabase
import hu.bme.aut.fogadasch.data.item.ActiveBetListItem
import hu.bme.aut.fogadasch.data.item.NewBetItem
import hu.bme.aut.fogadasch.databinding.FragmentNewBetBinding
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.math.RoundingMode
import kotlin.concurrent.thread

class NewBetFragment : Fragment(), NewBetAdapter.NewBetClickListener,
    AddNewMatchDialog.AddNewMatchDialogListener {

    private var _binding: FragmentNewBetBinding? = null
    private lateinit var adapter: NewBetAdapter
    private lateinit var database: BetItemDatabase

    private var oddsSum: Double = 0.0
                set(value) {
                    field = value
                    field = field.toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
                    binding.txOddsSum.text = field.toString()
                }
    private var maxPrise: Double = 0.0

    private var kombiMin: Int = 0
    private var kombiMax: Int = 0

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentNewBetBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initRecycleView()
        database = BetItemDatabase.getDatabase(applicationContext = activity?.applicationContext!!)
        binding.txOddsSum.text = oddsSum.toString()

        binding.spKotes.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.spinner_kombi)
        )

        binding.spKotes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                Log.d("sp", binding.spKotes.selectedItemPosition.toString())
                when(binding.spKotes.selectedItemPosition){
                    0 -> {
                        kombiMin = 0
                        kombiMax = 0
                    }
                    1 -> {
                        if (adapter.itemCount >= 4) {
                            kombiMin = 3
                            kombiMax = 4
                        } else NotEnoughMatchToast()
                    }
                    2 -> {
                        if (adapter.itemCount >= 5) {
                            kombiMin = 3
                            kombiMax = 5
                        } else NotEnoughMatchToast()
                    }
                    3 -> {
                        if (adapter.itemCount >= 6) {
                            kombiMin = 3
                            kombiMax = 6
                        } else NotEnoughMatchToast()
                    }
                    4 -> {
                        if (adapter.itemCount >= 5) {
                            kombiMin = 4
                            kombiMax = 5
                        } else NotEnoughMatchToast()
                    }
                    5 -> {
                        if (adapter.itemCount >= 6) {
                            kombiMin = 4
                            kombiMax = 6
                        } else NotEnoughMatchToast()
                    }
                    6 -> {
                        if (adapter.itemCount >= 6) {
                            kombiMin = 5
                            kombiMax = 6
                        } else NotEnoughMatchToast()
                    }
                    7 -> {
                        if (adapter.itemCount >= 7) {
                            kombiMin = 5
                            kombiMax = 7
                        } else NotEnoughMatchToast()
                    }
                    8 -> {
                        if (adapter.itemCount >= 6) {
                            kombiMin = 6
                            kombiMax = 7
                        } else NotEnoughMatchToast()
                    }
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        binding.floatingActionButton.setOnClickListener {
            AddNewMatchDialog(this).show(childFragmentManager, AddNewMatchDialog.TAG)
        }

        binding.btSubbmit.setOnClickListener {
            reloadMaxValue()
        }

        binding.btReload.setOnClickListener {
            reloadFragment()
        }

        binding.btDone.setOnClickListener {
            if (maxPrise > 1000) {
                Log.d("d", "OK")
                val tmp = MatchList(adapter.items)
                val matchListJson = Json.encodeToString(MatchList.serializer(), tmp)
                val insertItem = ActiveBetListItem(null, matchListJson, oddsSum, binding.betValue.text.toString().toDouble(),  maxPrise, kombiMin, kombiMax)
                insertActiveBet(insertItem)
                reloadFragment()
                Log.d("d",matchListJson)
            } else {
                Toast.makeText(activity?.applicationContext, "Túl alacsony végösszeg", Toast.LENGTH_SHORT).show()
            }
        }
        return root
    }

    private fun reloadFragment(){
        val id = findNavController().currentDestination?.id
        findNavController().popBackStack(id!!, true)
        findNavController().navigate(id)
    }

    private fun insertActiveBet(activeBetListItem: ActiveBetListItem){
        thread {
            database.ActiveBetDao().insert(activeBetListItem)
        }
    }

    private fun NotEnoughMatchToast() {
        binding.spKotes.setSelection(0)
        Toast.makeText(activity?.applicationContext, "Nincs elég meccs felvéve", Toast.LENGTH_SHORT)
            .show()
    }

    private fun reloadMaxValue() {
        if (binding.betValue.text.toString() != "") {
            maxPrise = (binding.betValue.text.toString().toDouble() * oddsSum).toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
            binding.maxPrise.text = maxPrise.toString()
        }
    }


    private fun initRecycleView(){
        adapter = NewBetAdapter(this)
        binding.rvSelectedItems.layoutManager = LinearLayoutManager(activity?.applicationContext)
        binding.rvSelectedItems.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        database.close()
    }

    override fun onItemDeleted(item: NewBetItem) {
        oddsSum -= item.selectedOddsValue
        if (adapter.itemCount == 0){
            oddsSum = 0.0
        }
        reloadMaxValue()
    }

    override fun onSelectedMatchCreated(newBetItem: NewBetItem?) {
        if (newBetItem != null) {
            adapter.addItem(newBetItem)
            oddsSum += newBetItem.selectedOddsValue
            reloadMaxValue()
        }
    }

    @Serializable
    data class MatchList(
        var matchList: List<NewBetItem>
    )
}