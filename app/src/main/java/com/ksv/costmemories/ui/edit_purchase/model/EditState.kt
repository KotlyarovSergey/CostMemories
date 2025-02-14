package com.ksv.costmemories.ui.edit_purchase.model

sealed class EditState {
    data object Normal: EditState()
    class Error(val msg: String): EditState()
    data object Finish: EditState()
    data object SetDate: EditState()
}