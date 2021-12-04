package br.edu.ifsp.scl.sdm.pa1.listpad.controller

import br.edu.ifsp.scl.sdm.pa1.listpad.dao.ListaDAO
import br.edu.ifsp.scl.sdm.pa1.listpad.dao.ListaSqlite
import br.edu.ifsp.scl.sdm.pa1.listpad.model.Lista
import br.edu.ifsp.scl.sdm.pa1.listpad.view.ListaActivity

class ListaController (listaActivity: ListaActivity) {
    private val listaDAO: ListaDAO = ListaSqlite(listaActivity)

    fun criarLista(lista: Lista) = listaDAO.criarLista(lista)
    fun atualizarLista(lista: Lista) = listaDAO.atualizarLista(lista)
    fun recuperarLista(nome: String) = listaDAO.recuperarLista(nome)
    fun recuperarListas() = listaDAO.recuperarListas()
    fun removerLista(nome: String) = listaDAO.removerLista(nome)
}