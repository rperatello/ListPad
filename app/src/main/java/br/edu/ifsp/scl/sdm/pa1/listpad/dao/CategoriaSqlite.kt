package br.edu.ifsp.scl.sdm.pa1.listpad.dao

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import br.edu.ifsp.scl.sdm.pa1.listpad.model.Categoria

class CategoriaSqlite (context: Context): CategoriaDAO {
    private val listPadBD: SQLiteDatabase = Manager(context).getListPadBD()

    private val TABLE_CATEGORIAS = "CATEGORIAS"
    private val COLUMN_NOME = "nome"

    override fun criarCategoria(categoria: Categoria): Long {
        val categoriaCv = ContentValues()
        categoriaCv.put(COLUMN_NOME, categoria.nome)

        return listPadBD.insert(TABLE_CATEGORIAS, null, categoriaCv)
    }

    override fun atualizarCategoria(categoria: Categoria) {
        val categoriaCv = ContentValues()
        categoriaCv.put(COLUMN_NOME, categoria.nome)

       listPadBD.update(TABLE_CATEGORIAS, categoriaCv, "${COLUMN_NOME} = ?", arrayOf(categoria.nome))
    }

    override fun recuperarCategoria(nome: String): Categoria {
        val categoriasCursor = listPadBD.query(
                true,
                TABLE_CATEGORIAS,
                null,
                "${COLUMN_NOME} = ?",
                arrayOf("${nome}"),
                null,
                null,
                null,
                null
        )

        return if(categoriasCursor.moveToFirst())
            Categoria(
                    categoriasCursor.getString(categoriasCursor.getColumnIndexOrThrow(COLUMN_NOME))
            )
        else
            Categoria()
    }

    override fun recuperarCategorias(): MutableList<Categoria> {
        val categorias: MutableList<Categoria> = ArrayList()
        val categoriasCursor = listPadBD.rawQuery("select * from ${TABLE_CATEGORIAS} order by ${COLUMN_NOME}", null)

        if(categoriasCursor.moveToFirst()){
            while (!categoriasCursor.isAfterLast){
                val categoria = Categoria (
                    categoriasCursor.getString(categoriasCursor.getColumnIndexOrThrow(COLUMN_NOME))
                )
                categorias.add(categoria)
                categoriasCursor.moveToNext()
            }
        }

        return categorias
    }

    override fun removerCategoria(nome: String): Int {
        return listPadBD.delete(TABLE_CATEGORIAS, "${COLUMN_NOME} = ?", arrayOf(nome))
    }
}