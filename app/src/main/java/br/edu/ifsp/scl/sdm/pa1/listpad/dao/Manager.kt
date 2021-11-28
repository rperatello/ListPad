package br.edu.ifsp.scl.sdm.pa1.listpad.dao

import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import br.edu.ifsp.scl.sdm.pa1.listpad.R

class Manager(context: Context){

    companion object {
        private val LISTPAD_DB = "listPad"

        //<><><><><><><><><> CRIANDO TABELA LISTAS <><><><><><><><><><><><><><><>
        private val CRIAR_TABELA_LISTAS = "CREATE TABLE IF NOT EXISTS LISTAS (" +
                "nome TEXT NOT NULL PRIMARY KEY, " +
                "descricao TEXT NOT NULL, " +
                "categoria TEXT NOT NULL, " +
                "urgente INTEGER NOT NULL, " +
                "FOREIGN KEY(categoria) REFERENCES CATEGORIAS(nome));"

        //<><><><><><><><><> CRIANDO TABELA ITENS <><><><><><><><><><><><><><>
        private val CRIAR_TABELA_ITENS = "CREATE TABLE IF NOT EXISTS ITENS (" +
                "descricao String NOT NULL PRIMARY KEY, " +
                "lista String NOT NULL, " +
                "realizado INTEGER NOT NULL, " +
                "FOREIGN KEY(lista) REFERENCES LISTAS(nome));"

        //<><><><><><><><><> CRIANDO TABELA CATEGORIAS <><><><><><><><><><><><><><>
        private val CRIAR_TABELA_CATEGORIAS = "CREATE TABLE IF NOT EXISTS CATEGORIAS (" +
                "nome TEXT NOT NULL PRIMARY KEY);"


        //INSERINDO VALORES DEFAULT
        private val INSERTS_TABELA_CATEGORIAS = "INSERT OR IGNORE INTO CATEGORIAS VALUES('Compras'); " +
                                                "INSERT OR IGNORE INTO CATEGORIAS VALUES('Compromisso'); " +
                                                "INSERT OR IGNORE INTO CATEGORIAS VALUES('Geral'); " +
                                                "INSERT OR IGNORE INTO CATEGORIAS VALUES('Tarefas'); "

    }

    private val listPadBD: SQLiteDatabase = context.openOrCreateDatabase(LISTPAD_DB, Context.MODE_PRIVATE, null)

    init {
        try {
            listPadBD.execSQL(CRIAR_TABELA_CATEGORIAS)
            listPadBD.execSQL(CRIAR_TABELA_LISTAS)
            listPadBD.execSQL(CRIAR_TABELA_ITENS)
            listPadBD.execSQL(INSERTS_TABELA_CATEGORIAS)
        } catch (se: SQLException) {
            Log.e(context.getString(R.string.app_name), se.toString())
        }
    }

    fun getListPadBD(): SQLiteDatabase {
        return listPadBD;
    }
}