package br.edu.ifsp.scl.sdm.pa1.listpad.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Item(
        val id: Int?,
        val listaId: Int,
        val descricao: String = "",
        val realizado: Boolean = false
) : Parcelable