package br.edu.ifsp.scl.sdm.pa1.listpad.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.ifsp.scl.sdm.pa1.listpad.R
import br.edu.ifsp.scl.sdm.pa1.listpad.adapter.ItensRvAdapter
import br.edu.ifsp.scl.sdm.pa1.listpad.dao.ItemDAO
import br.edu.ifsp.scl.sdm.pa1.listpad.dao.ItemDAOImpl
import br.edu.ifsp.scl.sdm.pa1.listpad.databinding.ActivityItemBinding
import br.edu.ifsp.scl.sdm.pa1.listpad.model.Item
import br.edu.ifsp.scl.sdm.pa1.listpad.model.Lista
import com.google.android.material.snackbar.Snackbar

class ItemActivity : AppCompatActivity() {

    private var listId = -1

    companion object Extras {
        const val EXTRA_ITEM = "EXTRA_ITEM"
        const val EXTRA_LIST_ID = "EXTRA_LIST_ID"
    }

    private val activityItemBinding: ActivityItemBinding by lazy {
        ActivityItemBinding.inflate(layoutInflater)
    }

    private val activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
                if (resultado.resultCode == RESULT_OK) {
                    refreshItemList()
                }
            }

    private val itemDao: ItemDAO by lazy {
        ItemDAOImpl(this)
    }

    //Data source
    private lateinit var itemList: MutableList<Item>

    //Adapter
    private lateinit var itemAdapter: ItensRvAdapter

    //Layout Manager
    private val itemLayoutManager: LinearLayoutManager = LinearLayoutManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityItemBinding.root)
        supportActionBar?.subtitle = "Itens da Lista"


        intent.getParcelableExtra<Lista>(ListaActivity.EXTRA_LISTA)?.apply {
            listId = this.id ?: -1
        }

        activityItemBinding.itensRv.layoutManager = itemLayoutManager
        refreshItemList()

        activityItemBinding.adicionarItemFb.setOnClickListener {
            val addItemIntent = Intent(this, ItemFormActivity::class.java).apply {
                putExtra(EXTRA_LIST_ID, listId)
            }
            activityResultLauncher.launch(addItemIntent)
        }
    }

    override fun onContextItemSelected(itemMenu: MenuItem): Boolean {
        val posicao = itemAdapter.posicao
        val item = itemList[posicao]

        return when (itemMenu.itemId) {
            R.id.editarItemMi -> {
                //Editar
                val editarItemIntent = Intent(this, ItemFormActivity::class.java).apply {
                    putExtra(EXTRA_ITEM, item)
                    putExtra(EXTRA_LIST_ID, item.listaId)
                }

                activityResultLauncher.launch(editarItemIntent)
                true
            }
            R.id.removerItemMi -> {
                //Remover
                with(AlertDialog.Builder(this)) {
                    setMessage("Confirma a remoção?")
                    setPositiveButton("Sim") { _, _ ->
                        item.id?.let {
                            itemDao.removerItem(it)
                            refreshItemList()
                            Snackbar.make(activityItemBinding.root, "Item removido", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                    setNegativeButton("Não") { _, _ ->
                        Snackbar.make(activityItemBinding.root, "Remoção cancelada", Snackbar.LENGTH_SHORT).show()
                    }
                    create()
                }.show()

                true
            }
            else -> {
                false
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshItemList() {
        itemList = itemDao.recuperarItens(this.listId)
        itemAdapter = ItensRvAdapter(itemList)
        activityItemBinding.itensRv.adapter = itemAdapter
    }

}