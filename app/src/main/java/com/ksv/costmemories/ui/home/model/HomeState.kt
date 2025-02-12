package com.ksv.costmemories.ui.home.model

sealed class HomeState {
    data object Loading: HomeState()
    data object Normal: HomeState()
    data object AddPurchase: HomeState()
    class EditPurchase(id: Long): HomeState()
    data object Empty: HomeState()
}