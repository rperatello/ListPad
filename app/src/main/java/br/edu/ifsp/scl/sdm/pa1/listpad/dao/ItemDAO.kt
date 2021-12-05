package br.edu.ifsp.scl.sdm.pa1.listpad.dao

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import br.edu.ifsp.scl.sdm.pa1.listpad.model.Item

interface ItemDAO {

    fun criarItem(item: Item): Boolean

    fun atualizarItem(item: Item): Boolean

    fun recuperarItem(descricao: String): Item?

    fun recuperarItens(idLista: Int): MutableList<Item>

    fun removerItem(id: Int): Int
}

class ItemDAOImpl(context: Context) : ItemDAO {

    private val listPadBD: SQLiteDatabase = DatabaseManager.getInstance(context)

    private val TABLE_ITENS = "ITENS"
    private val COLUMN_ID = "id"
    private val COLUMN_DESCRICAO = "descricao"
    private val COLUMN_LISTA_ID = "lista_id"
    private val COLUMN_REALIZADO = "realizado"

    override fun criarItem(item: Item): Boolean {
        return ContentValues().apply {
            put(COLUMN_DESCRICAO, item.descricao)
            put(COLUMN_LISTA_ID, item.listaId)
            put(COLUMN_REALIZADO, item.realizado)
        }.let {
            listPadBD.insert(TABLE_ITENS, null, it) > 0
        }
    }

    override fun atualizarItem(item: Item): Boolean {
        return try {
            ContentValues().apply {
                put(COLUMN_DESCRICAO, item.descricao)
                put(COLUMN_LISTA_ID, item.listaId)
                put(COLUMN_REALIZADO, item.realizado)
            }.let {
                listPadBD.update(TABLE_ITENS, it, "$COLUMN_ID = ?", arrayOf(item.id.toString())) > 0
            }
        } catch (exception: Exception) {
            false
        }
    }

    override fun recuperarItem(descricao: String): Item? {
        val itensCursor = listPadBD.query(
                true,
                TABLE_ITENS,
                null,
                "$COLUMN_DESCRICAO = ?",
                arrayOf(descricao),
                null,
                null,
                null,
                null
        )

        val item = if (itensCursor.moveToFirst())
            Item(
                    itensCursor.getInt(itensCursor.getColumnIndexOrThrow(COLUMN_ID)),
                    itensCursor.getInt(itensCursor.getColumnIndexOrThrow(COLUMN_LISTA_ID)),
                    itensCursor.getString(itensCursor.getColumnIndexOrThrow(COLUMN_DESCRICAO)),
                    intToBoolean(itensCursor.getInt(itensCursor.getColumnIndexOrThrow(COLUMN_REALIZADO)))
            )
        else
            null

        itensCursor.close()
        return item
    }

    override fun recuperarItens(idLista: Int): MutableList<Item> {
        val itens: MutableList<Item> = ArrayList()
        val sql = "select * from $TABLE_ITENS where lista_id = $idLista order by $COLUMN_DESCRICAO"
        val itensCursor = listPadBD.rawQuery(sql, null)

        if (itensCursor.moveToFirst()) {
            while (!itensCursor.isAfterLast) {
                val item = Item(
                        itensCursor.getInt(itensCursor.getColumnIndexOrThrow(COLUMN_ID)),
                        itensCursor.getInt(itensCursor.getColumnIndexOrThrow(COLUMN_LISTA_ID)),
                        itensCursor.getString(itensCursor.getColumnIndexOrThrow(COLUMN_DESCRICAO)),
                        intToBoolean(itensCursor.getInt(itensCursor.getColumnIndexOrThrow(COLUMN_REALIZADO)))
                )
                itens.add(item)
                itensCursor.moveToNext()
            }
        }
        itensCursor.close()
        return itens
    }

    override fun removerItem(id: Int): Int {
        return listPadBD.delete(TABLE_ITENS, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    private fun intToBoolean(int: Int): Boolean {
        return int != 0
    }
}