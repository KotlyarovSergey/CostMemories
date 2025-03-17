package com.ksv.costmemories.ui.settings.model

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksv.costmemories.data.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repository: Repository
): ViewModel() {
    private val _state = MutableStateFlow<SettingsState>(SettingsState.Normal)
    val state = _state.asStateFlow()


    fun onExportClick(){
        _state.value = SettingsState.SaveFileDialog
    }

    fun onImportClick(){
        _state.value = SettingsState.OpenFileDialog(arrayOf(CSV_MIME_TYPE))
    }

    fun onFileForExportSelect(uri: Uri?){
        _state.value = SettingsState.Normal
        uri?.let {
            _state.value = SettingsState.Loading
            viewModelScope.launch {
                val success = repository.exportPurchasesToCsv(uri)
                if(success)
                    _state.value = SettingsState.Success("Успешно!")
                else
                    _state.value = SettingsState.Error("export fail")
            }
        }
    }

    fun onFileForImportSelect(uri: Uri?){
        _state.value = SettingsState.Normal
        uri?.let {
            _state.value = SettingsState.Loading
            viewModelScope.launch {
                val purchases = repository.getPurchasesListFromFile(uri)
                if (purchases.isNotEmpty()){
                    repository.purchasesToDb(purchases)
                    _state.value = SettingsState.Success("загружено ${purchases.size} записей")


                } else {
                    _state.value = SettingsState.Error("import fail")
                }
            }
        }
    }

    fun msgShowed(){
        _state.value = SettingsState.Normal
    }

    companion object {
        const val DEFAULT_FILE_NAME = "purchases.csv"
        const val CSV_MIME_TYPE = "text/comma-separated-values"
    }
}