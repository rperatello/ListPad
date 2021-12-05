package br.edu.ifsp.scl.sdm.pa1.listpad.controller

import br.edu.ifsp.scl.sdm.pa1.listpad.dao.CategoriaDAO
import br.edu.ifsp.scl.sdm.pa1.listpad.dao.CategoriaSqlite
import br.edu.ifsp.scl.sdm.pa1.listpad.model.Categoria
import br.edu.ifsp.scl.sdm.pa1.listpad.view.CategoriaActivity
import br.edu.ifsp.scl.sdm.pa1.listpad.view.ListaActivity
import br.edu.ifsp.scl.sdm.pa1.listpad.view.ListaFormActivity

class CategoriaController (listaActivity: ListaActivity) {
    private val categoriaDAO: CategoriaDAO = CategoriaSqlite(listaActivity!!)

    fun criarCategoria(categoria: Categoria) = categoriaDAO.criarCategoria(categoria)
    fun atualizarCategoria(categoria: Categoria) = categoriaDAO.atualizarCategoria(categoria)
    fun recuperarCategoria(nome: String) = categoriaDAO.recuperarCategoria(nome)
    fun recuperarCategorias() = categoriaDAO.recuperarCategorias()
    fun removerCategoria(nome: String) = categoriaDAO.removerCategoria(nome)

}