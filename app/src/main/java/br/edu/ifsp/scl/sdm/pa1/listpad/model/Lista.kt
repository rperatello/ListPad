package br.edu.ifsp.scl.sdm.pa1.listpad.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Lista (
    val nome: String = "",
    val descricao: String = "",
    val categoria: String = "",
    val urgente: Boolean = false
): Parcelable