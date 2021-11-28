package br.edu.ifsp.scl.sdm.pa1.listpad.dao

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import br.edu.ifsp.scl.sdm.pa1.listpad.model.Categoria
import br.edu.ifsp.scl.sdm.pa1.listpad.model.Lista

class ListaSqlite(context: Context): ListaDAO {
    private val listPadBD: SQLiteDatabase = Manager(context).getListPadBD()

    private val TABLE_LISTAS = "LISTAS"
    private val COLUMN_NOME = "nome"
    private val COLUMN_DESCRICAO = "descricao"
    private val COLUMN_CATEGORIA = "categoria"
    private val COLUMN_URGENTE = "urgente"


    override fun criarLista(lista: Lista): Long {
        val listaCv = ContentValues()
        listaCv.put(COLUMN_NOME, lista.nome)
        listaCv.put(COLUMN_DESCRICAO, lista.descricao)
        listaCv.put(COLUMN_CATEGORIA, lista.categoria)
        listaCv.put(COLUMN_URGENTE, lista.urgente)

        return  listPadBD.insert(TABLE_LISTAS, null, listaCv)
    }

    override fun atualizarLista(lista: Lista) {
        val listaCv = ContentValues()
        listaCv.put(COLUMN_NOME, lista.nome)
        listaCv.put(COLUMN_DESCRICAO, lista.descricao)
        listaCv.put(COLUMN_CATEGORIA, lista.categoria)
        listaCv.put(COLUMN_URGENTE, lista.urgente)

        listPadBD.update(TABLE_LISTAS, listaCv, "${COLUMN_NOME} = ?", arrayOf(lista.nome))
    }

    override fun recuperarLista(nome: String): Lista {
        val listasCursor = listPadBD.query(
                true,
                TABLE_LISTAS,
                null,
                "${COLUMN_NOME} = ?",
                arrayOf("${nome}"),
                null,
                null,
                null,
                null
        )

        return if(listasCursor.moveToFirst())
            Lista(
                    listasCursor.getString(listasCursor.getColumnIndexOrThrow(COLUMN_NOME)),
                    listasCursor.getString(listasCursor.getColumnIndexOrThrow(COLUMN_DESCRICAO)),
                    listasCursor.getString(listasCursor.getColumnIndexOrThrow(COLUMN_CATEGORIA)),
                    intToBoolean(listasCursor.getInt(listasCursor.getColumnIndexOrThrow(COLUMN_URGENTE)))
            )
        else
            Lista()
    }

    override fun recuperarListas(): MutableList<Lista> {
        val listas: MutableList<Lista> = ArrayList()
        val listasCursor = listPadBD.rawQuery("select * from ${TABLE_LISTAS} order by ${COLUMN_NOME}", null)

        if(listasCursor.moveToFirst()){
            while (!listasCursor.isAfterLast){
                val lista = Lista(
                        listasCursor.getString(listasCursor.getColumnIndexOrThrow(COLUMN_NOME)),
                        listasCursor.getString(listasCursor.getColumnIndexOrThrow(COLUMN_DESCRICAO)),
                        listasCursor.getString(listasCursor.getColumnIndexOrThrow(COLUMN_CATEGORIA)),
                        intToBoolean(listasCursor.getInt(listasCursor.getColumnIndexOrThrow(COLUMN_URGENTE)))
                )
                listas.add(lista)
                listasCursor.moveToNext()
            }
        }
        return listas
    }

    override fun removerLista(nome: String): Int {
        return listPadBD.delete(TABLE_LISTAS, "${COLUMN_NOME} = ?", arrayOf(nome))
    }

    private fun intToBoolean(int: Int): Boolean {
        return int != 0
    }
}