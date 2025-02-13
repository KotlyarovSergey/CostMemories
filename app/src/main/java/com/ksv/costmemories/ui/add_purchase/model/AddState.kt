package com.ksv.costmemories.ui.add_purchase.model

sealed class AddState {
    data object Normal: AddState()
    class Error(val msg: String): AddState()
    data object Finish: AddState()
}