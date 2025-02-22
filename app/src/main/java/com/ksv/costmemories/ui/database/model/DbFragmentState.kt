package com.ksv.costmemories.ui.database.model

sealed class DbFragmentState {
    data object Normal: DbFragmentState()
    class ConfirmRequest(val id: Long, val request: String): DbFragmentState()
}