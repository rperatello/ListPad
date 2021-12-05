package br.edu.ifsp.scl.sdm.pa1.listpad.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


// Tabela Listas
private const val CRIAR_TABELA_LISTAS =
        """
        CREATE TABLE IF NOT EXISTS LISTAS (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        nome TEXT NOT NULL UNIQUE, 
        descricao TEXT NOT NULL, 
        categoria TEXT NOT NULL, 
        urgente INTEGER NOT NULL, 
        FOREIGN KEY(categoria) REFERENCES CATEGORIAS(nome) ON UPDATE CASCADE ON DELETE CASCADE); 
    """

// Tabela Itens
private const val CRIAR_TABELA_ITENS =
        """
        CREATE TABLE IF NOT EXISTS ITENS (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        lista_id INTEGER NOT NULL,
        descricao String NOT NULL, 
        realizado INTEGER NOT NULL, 
        UNIQUE(descricao, lista_id),
        FOREIGN KEY(lista_id) REFERENCES LISTAS(id) ON UPDATE CASCADE ON DELETE CASCADE);
    """

// Tabela Categorias
private const val CRIAR_TABELA_CATEGORIAS =
        "CREATE TABLE IF NOT EXISTS CATEGORIAS (nome TEXT NOT NULL PRIMARY KEY);"

// Valores default
private const val INSERTS_TABELA_CATEGORIAS =
        "INSERT OR IGNORE INTO CATEGORIAS VALUES('Compras'), ('Compromisso'), ('Geral'), ('Tarefas');"

object DatabaseManager {
    private var database: SQLiteDatabase? = null

    @Synchronized
    fun getInstance(context: Context): SQLiteDatabase {
        val localDatabase = database

        if (localDatabase == null) {
            val newDatabase = DatabaseHelper(context).writableDatabase
            database = newDatabase
            return newDatabase
        } else {
            return localDatabase
        }
    }
}

private class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, LISTPAD_DB, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.apply {
            execSQL("PRAGMA foreign_keys = ON;")
            execSQL(CRIAR_TABELA_CATEGORIAS)
            execSQL(CRIAR_TABELA_LISTAS)
            execSQL(CRIAR_TABELA_ITENS)

            execSQL(INSERTS_TABELA_CATEGORIAS)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val LISTPAD_DB = "listpad.db"
    }
}