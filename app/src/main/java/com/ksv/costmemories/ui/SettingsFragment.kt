package com.ksv.costmemories.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.ksv.costmemories.data.Repository
import com.ksv.costmemories.databinding.FragmentSettingsBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.exportDb.setOnClickListener {
            val repository = Repository()
            lifecycleScope.launch {
                repository.exportPurchasesTuplesToCsv(requireContext(), FILE_NAME)
            }
        }

        binding.importDb.setOnClickListener {
            val repository = Repository()
            lifecycleScope.launch {
                binding.progress.visibility = View.VISIBLE
                val purchases = repository.importPurchasesTuplesFromCsv(requireContext(), FILE_NAME)
                val newText = binding.tvResult.text.toString() + "\n" + purchases.toString()
                binding.tvResult.text = newText
                binding.progress.visibility = View.GONE
            }
        }
    }

    companion object{
        private const val FILE_NAME = "purchases.csv"
    }
}