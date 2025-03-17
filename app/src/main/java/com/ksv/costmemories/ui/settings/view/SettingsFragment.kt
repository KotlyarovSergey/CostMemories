package com.ksv.costmemories.ui.settings.view

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ksv.costmemories.Dependencies
import com.ksv.costmemories.data.Repository
import com.ksv.costmemories.databinding.FragmentSettingsBinding
import com.ksv.costmemories.ui.settings.model.SettingsState
import com.ksv.costmemories.ui.settings.model.SettingsViewModel
import com.ksv.costmemories.ui.settings.model.SettingsViewModelFactory
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null

    private val binding get() = _binding!!
    private val viewModel: SettingsViewModel by viewModels {
        SettingsViewModelFactory(
            Repository(requireContext(), Dependencies.getPurchasesDao())
        )
    }

    private val saveFileLauncher = registerForActivityResult(
        ActivityResultContracts.CreateDocument(SettingsViewModel.CSV_MIME_TYPE)
    ) { savedUri ->
        viewModel.onFileForExportSelect(savedUri)
    }

    private val openFileLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { openedUri ->
        viewModel.onFileForImportSelect(openedUri)
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

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.state.onEach { state ->
            when (state) {
                is SettingsState.Success -> showMessage(state.message)
                is SettingsState.Error -> showError(state.message)
                is SettingsState.OpenFileDialog -> openFileLauncher.launch(state.arrayOfTypes)
                is SettingsState.SaveFileDialog ->
                    saveFileLauncher.launch(SettingsViewModel.DEFAULT_FILE_NAME)

                else -> {}
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)


//        binding.exportDb.setOnClickListener {
//            viewModel.onExportClick()
//        }
//
//        binding.importDb.setOnClickListener {
//            viewModel.onImportClick()
//        }

    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        viewModel.msgShowed()
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        viewModel.msgShowed()
    }




}