package br.edu.ifsp.scl.sdm.pa1.listpad.dao

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import br.edu.ifsp.scl.sdm.pa1.listpad.model.Item
import br.edu.ifsp.scl.sdm.pa1.listpad.model.Lista

class ItemSqlite(context: Context): ItemDAO {

    private val listPadBD: SQLiteDatabase = Manager(context).getListPadBD()

    private val TABLE_ITENS = "ITENS"
    private val COLUMN_DESCRICAO = "descricao"
    private val COLUMN_LISTA = "lista"
    private val COLUMN_REALIZADO = "realizado"


    override fun criarItem(item: Item): Long {
        val itemCv = ContentValues()
        itemCv.put(COLUMN_DESCRICAO, item.descricao)
        itemCv.put(COLUMN_LISTA, item.lista)
        itemCv.put(COLUMN_REALIZADO, item.realizado)

        return  listPadBD.insert(TABLE_ITENS, null, itemCv)
    }

    override fun atualizarItem(item: Item) {
        val itemCv = ContentValues()
        itemCv.put(COLUMN_DESCRICAO, item.descricao)
        itemCv.put(COLUMN_LISTA, item.lista)
        itemCv.put(COLUMN_REALIZADO, item.realizado)

        listPadBD.update(TABLE_ITENS, itemCv, "${COLUMN_DESCRICAO} = ?", arrayOf(item.descricao))
    }

    override fun recuperarItem(descricao: String): Item {
        val itensCursor = listPadBD.query(
                true,
                TABLE_ITENS,
                null,
                "${COLUMN_DESCRICAO} = ?",
                arrayOf("${descricao}"),
                null,
                null,
                null,
                null
        )

        return if(itensCursor.moveToFirst())
            Item(
                    itensCursor.getString(itensCursor.getColumnIndexOrThrow(COLUMN_DESCRICAO)),
                    itensCursor.getString(itensCursor.getColumnIndexOrThrow(COLUMN_LISTA)),
                    intToBoolean(itensCursor.getInt(itensCursor.getColumnIndexOrThrow(COLUMN_REALIZADO)))
            )
        else
            Item()
    }

    override fun recuperarItens(): MutableList<Item> {
        val itens: MutableList<Item> = ArrayList()
        val itensCursor = listPadBD.rawQuery("select * from ${TABLE_ITENS} order by ${COLUMN_DESCRICAO}", null)

        if(itensCursor.moveToFirst()){
            while (!itensCursor.isAfterLast){
                val item = Item(
                        itensCursor.getString(itensCursor.getColumnIndexOrThrow(COLUMN_DESCRICAO)),
                        itensCursor.getString(itensCursor.getColumnIndexOrThrow(COLUMN_LISTA)),
                        intToBoolean(itensCursor.getInt(itensCursor.getColumnIndexOrThrow(COLUMN_REALIZADO)))
                )
                itens.add(item)
                itensCursor.moveToNext()
            }
        }
        return itens
    }

    override fun removerLista(descricao: String): Int {
        return listPadBD.delete(TABLE_ITENS, "${COLUMN_DESCRICAO} = ?", arrayOf(descricao))
    }

    private fun intToBoolean(int: Int): Boolean {
        return int != 0
    }
}