package br.edu.ifsp.scl.sdm.pa1.listpad.view

interface OnItemClickListener {
    fun onItemClick(position: Int)

    // Funções adicionadas para ContextMenu
    fun onEditarMenuItemClick(position: Int)
    fun onRemoverMenuItemClick(position: Int)
}