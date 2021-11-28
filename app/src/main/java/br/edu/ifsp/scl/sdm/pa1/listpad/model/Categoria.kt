package br.edu.ifsp.scl.sdm.pa1.listpad.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Categoria (
    val nome: String = ""
): Parcelable