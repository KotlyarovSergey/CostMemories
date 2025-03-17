package com.ksv.costmemories.ui.settings.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ksv.costmemories.data.Repository

class SettingsViewModelFactory(
    private val repository: Repository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SettingsViewModel::class.java))
            return SettingsViewModel(repository) as T
        throw IllegalArgumentException("Unknown SettingsViewModel class")
    }
}