package br.edu.ifsp.scl.sdm.pa1.listpad.controller

import br.edu.ifsp.scl.sdm.pa1.listpad.dao.ItemDAO
import br.edu.ifsp.scl.sdm.pa1.listpad.dao.ItemSqlite
import br.edu.ifsp.scl.sdm.pa1.listpad.model.Item
import br.edu.ifsp.scl.sdm.pa1.listpad.view.ItemActivity
import br.edu.ifsp.scl.sdm.pa1.listpad.view.ListaFormActivity

class ItemController (ItemActivity: ItemActivity?) {
    private val itemDAO: ItemDAO = ItemSqlite(ItemActivity!!)

    fun criarItem(item: Item) = itemDAO.criarItem(item)
    fun atualizarItem(item: Item) = itemDAO.atualizarItem(item)
    fun recuperarItem(nome: String) = itemDAO.recuperarItem(nome)
    fun recuperarItens() = itemDAO.recuperarItens()
    fun removerLista(descricao: String) = itemDAO.recuperarItem(descricao)
}