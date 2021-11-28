package br.edu.ifsp.scl.sdm.pa1.listpad.dao

import br.edu.ifsp.scl.sdm.pa1.listpad.model.Item

interface ItemDAO {
    fun criarItem(item: Item): Long
    fun atualizarItem(item: Item)
    fun recuperarItem(nome: String): Item
    fun recuperarItens(): MutableList<Item>
    fun removerLista(nome: String): Int
}