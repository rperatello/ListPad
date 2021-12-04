package br.edu.ifsp.scl.sdm.pa1.listpad.controller

import br.edu.ifsp.scl.sdm.pa1.listpad.dao.CategoriaDAO
import br.edu.ifsp.scl.sdm.pa1.listpad.dao.CategoriaSqlite
import br.edu.ifsp.scl.sdm.pa1.listpad.model.Categoria
import br.edu.ifsp.scl.sdm.pa1.listpad.view.CategoriaActivity

class CategoriaController (categoriaActivity: CategoriaActivity) {
    private val categoriaDAO: CategoriaDAO = CategoriaSqlite(categoriaActivity)

    fun criarCategoria(categoria: Categoria) = categoriaDAO.criarCategoria(categoria)
    fun atualizarCategoria(categoria: Categoria) = categoriaDAO.atualizarCategoria(categoria)
    fun recuperarCategoria(nome: String) = categoriaDAO.recuperarCategoria(nome)
    fun recuperarCategorias() = categoriaDAO.recuperarCategorias()
    fun removerCategoria(nome: String) = categoriaDAO.removerCategoria(nome)

}