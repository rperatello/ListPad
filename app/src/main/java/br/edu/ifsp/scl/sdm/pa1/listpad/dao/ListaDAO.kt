package br.edu.ifsp.scl.sdm.pa1.listpad.dao

import br.edu.ifsp.scl.sdm.pa1.listpad.model.Lista

interface ListaDAO {
    fun criarLista(lista: Lista): Long
    fun atualizarLista(lista: Lista)
    fun recuperarLista(nome: String): Lista
    fun recuperarListas(): MutableList<Lista>
    fun removerLista(nome: String): Int
}