package com.ksv.costmemories.ui

import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.ksv.costmemories.Dependencies
import com.ksv.costmemories.data.Repository
import com.ksv.costmemories.databinding.FragmentSettingsBinding
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val saveFileLauncher =
        registerForActivityResult(
            ActivityResultContracts.CreateDocument(CSV_MIME_TYPE)
        ) { savedUri ->
            savedUri?.let{
                savePurchases(savedUri)
            }
        }

    private val openFileLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { openedUri ->
//        try {
            openedUri?.let{
                loadPurchases(openedUri)
            }
//        } catch (e: Exception) {
//            Log.d("ksvlog", "SettingsFragment.selectFileLauncher exception: ${e.message}")
//        }
    }


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
            saveFileLauncher.launch(DEFAULT_FILE_NAME)
        }

        binding.importDb.setOnClickListener {
            openFileLauncher.launch(arrayOf(CSV_MIME_TYPE))
        }

    }

    private fun savePurchases(uri: Uri) {
        val repository = Repository(Dependencies.getPurchasesDao())
        lifecycleScope.launch {
            repository.exportPurchasesTuplesToCsv(requireContext(), uri)
        }
    }

    private fun loadPurchases(uri: Uri){
        lifecycleScope.launch {
            binding.progress.visibility = View.VISIBLE

            val repository = Repository(Dependencies.getPurchasesDao())
            repository.importPurchasesTuplesFromCsv(requireContext(), uri)

            binding.progress.visibility = View.GONE
        }
    }

    companion object{
        private const val DEFAULT_FILE_NAME = "purchases.csv"
        private const val CSV_MIME_TYPE = "text/comma-separated-values"
    }
}