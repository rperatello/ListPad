package br.edu.ifsp.scl.sdm.pa1.listpad.dao

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import br.edu.ifsp.scl.sdm.pa1.listpad.model.Lista

interface ListaDAO {

    fun criarLista(lista: Lista): Boolean

    fun atualizarLista(lista: Lista): Boolean

    fun recuperarLista(nome: String): Lista?

    fun recuperarListas(): MutableList<Lista>

    fun removerLista(id: Int): Int
}

class ListaDAOImpl(context: Context) : ListaDAO {
    private val listPadBD: SQLiteDatabase = DatabaseManager.getInstance(context)

    private val TABLE_LISTAS = "LISTAS"
    private val COLUMN_ID = "id"
    private val COLUMN_NOME = "nome"
    private val COLUMN_DESCRICAO = "descricao"
    private val COLUMN_CATEGORIA = "categoria"
    private val COLUMN_URGENTE = "urgente"


    override fun criarLista(lista: Lista): Boolean {
        return ContentValues().apply {
            put(COLUMN_NOME, lista.nome)
            put(COLUMN_DESCRICAO, lista.descricao)
            put(COLUMN_CATEGORIA, lista.categoria)
            put(COLUMN_URGENTE, lista.urgente)
        }.let {
            listPadBD.insert(TABLE_LISTAS, null, it) > 0
        }
    }

    override fun atualizarLista(lista: Lista): Boolean {
        return try {
            ContentValues().apply {
                put(COLUMN_NOME, lista.nome)
                put(COLUMN_DESCRICAO, lista.descricao)
                put(COLUMN_CATEGORIA, lista.categoria)
                put(COLUMN_URGENTE, lista.urgente)
            }.let {
                listPadBD.update(TABLE_LISTAS, it, "$COLUMN_ID = ?", arrayOf(lista.id?.toString())) > 0
            }
        } catch (exception: Exception) {
            false
        }
    }

    override fun recuperarLista(nome: String): Lista? {
        val listasCursor = listPadBD.query(
                true,
                TABLE_LISTAS,
                null,
                "$COLUMN_NOME = ?",
                arrayOf(nome),
                null,
                null,
                null,
                null
        )

        val lista = if (listasCursor.moveToFirst())
            Lista(
                    listasCursor.getInt(listasCursor.getColumnIndexOrThrow(COLUMN_ID)),
                    listasCursor.getString(listasCursor.getColumnIndexOrThrow(COLUMN_NOME)),
                    listasCursor.getString(listasCursor.getColumnIndexOrThrow(COLUMN_DESCRICAO)),
                    listasCursor.getString(listasCursor.getColumnIndexOrThrow(COLUMN_CATEGORIA)),
                    intToBoolean(listasCursor.getInt(listasCursor.getColumnIndexOrThrow(COLUMN_URGENTE)))
            )
        else
            null

        listasCursor.close()
        return lista
    }

    override fun recuperarListas(): MutableList<Lista> {
        val listas: MutableList<Lista> = ArrayList()
        val listasCursor = listPadBD.rawQuery("select * from $TABLE_LISTAS order by $COLUMN_NOME", null)

        if (listasCursor.moveToFirst()) {
            while (!listasCursor.isAfterLast) {
                val lista = Lista(
                        listasCursor.getInt(listasCursor.getColumnIndexOrThrow(COLUMN_ID)),
                        listasCursor.getString(listasCursor.getColumnIndexOrThrow(COLUMN_NOME)),
                        listasCursor.getString(listasCursor.getColumnIndexOrThrow(COLUMN_DESCRICAO)),
                        listasCursor.getString(listasCursor.getColumnIndexOrThrow(COLUMN_CATEGORIA)),
                        intToBoolean(listasCursor.getInt(listasCursor.getColumnIndexOrThrow(COLUMN_URGENTE)))
                )
                listas.add(lista)
                listasCursor.moveToNext()
            }
        }
        listasCursor.close()
        return listas
    }

    override fun removerLista(id: Int): Int {
        return listPadBD.delete(TABLE_LISTAS, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    private fun intToBoolean(int: Int): Boolean {
        return int != 0
    }
}