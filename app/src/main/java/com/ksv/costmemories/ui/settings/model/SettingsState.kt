package com.ksv.costmemories.ui.settings.model


sealed class SettingsState {
    data object Normal: SettingsState()
    class Error(val message: String): SettingsState()
    class Success(val message: String): SettingsState()
    data object Loading: SettingsState()
    data object SaveFileDialog: SettingsState()
    class OpenFileDialog(val arrayOfTypes: Array<String>): SettingsState()
}