package br.edu.ifsp.scl.sdm.pa1.listpad.dao

import br.edu.ifsp.scl.sdm.pa1.listpad.model.Categoria

interface CategoriaDAO {
    fun criarCategoria(categoria: Categoria): Long
    fun atualizarCategoria(categoria: Categoria)
    fun recuperarCategoria(nome: String): Categoria
    fun recuperarCategorias(): MutableList<Categoria>
    fun removerCategoria(nome: String): Int
}