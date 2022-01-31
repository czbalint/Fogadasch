package hu.bme.aut.fogadasch.activity.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import hu.bme.aut.fogadasch.R
import hu.bme.aut.fogadasch.data.item.BetItem
import hu.bme.aut.fogadasch.databinding.FragmentDetailsBinding

class BetItemDetailsDialog: DialogFragment() {

    private lateinit var binding: FragmentDetailsBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentDetailsBinding.inflate(LayoutInflater.from(context))
        binding.team1Text.text = arguments?.getString("name1") ?: "csapat1"
        binding.team2Text.text = arguments?.getString("name2") ?: "csapat2"
        binding.dateDet.text = arguments?.getString("date") ?: "date"
        binding.oddsTeam1.text = arguments?.getDouble("odds1")?.toString() ?: "--"
        binding.oddsTeam2.text = arguments?.getDouble("odds2")?.toString() ?: "--"
        binding.oddsX.text = arguments?.getDouble("oddsX")?.toString() ?: "--"

        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.details)
            .setView(binding.root)
            .setPositiveButton(R.string.button_ok) { dialogInterface, i -> }
            .create()
    }

    companion object {
        const val TAG = "FragmentDetails"
        fun newInstance(betItem: BetItem?): BetItemDetailsDialog{
            val dialog = BetItemDetailsDialog()
            val arg = Bundle().apply {
                betItem?.let { putString("name1", it.name1) }
                betItem?.let { putString("name2", it.name2) }
                betItem?.let { putString("date", it.date) }
                betItem?.let { putDouble("odds1", it.odds1) }
                betItem?.let { putDouble("odds2", it.odds2) }
                betItem?.let { putDouble("oddsX", it.oddsX) }
            }

            dialog.arguments = arg
            return dialog
        }
    }
}