package com.ksv.costmemories.ui.database.model

import com.ksv.costmemories.ui.database.entity.DbItem

sealed class DbFragmentState {
    data object Normal: DbFragmentState()
    class DeleteConfirmRequest(val id: Long, val request: String): DbFragmentState()
    class ReplaceConfirmRequest(val item: DbItem, val text: String, val msg: String): DbFragmentState()
    class ShowSuccessMessage(val msg: String): DbFragmentState()
}