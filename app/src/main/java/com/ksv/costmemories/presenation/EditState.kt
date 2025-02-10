package com.ksv.costmemories.presenation

sealed class EditState {
    data object Normal: EditState()
    data object Waiting: EditState()
    data object Finish: EditState()
    data class Error(val message: String): EditState()
}

