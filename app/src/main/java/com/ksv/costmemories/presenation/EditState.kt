package com.ksv.costmemories.presenation

sealed class EditState {
    object Normal: EditState()
    object Waiting: EditState()
    object Finish: EditState()
    data class Error(val message: String): EditState()
}

