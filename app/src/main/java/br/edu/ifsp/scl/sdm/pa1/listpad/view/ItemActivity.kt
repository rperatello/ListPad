package br.edu.ifsp.scl.sdm.pa1.listpad.view

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.ifsp.scl.sdm.pa1.listpad.R
import br.edu.ifsp.scl.sdm.pa1.listpad.adapter.ItensRvAdapter
import br.edu.ifsp.scl.sdm.pa1.listpad.adapter.ListasRvAdapter
import br.edu.ifsp.scl.sdm.pa1.listpad.controller.ItemController
import br.edu.ifsp.scl.sdm.pa1.listpad.controller.ListaController
import br.edu.ifsp.scl.sdm.pa1.listpad.databinding.ActivityItemBinding
import br.edu.ifsp.scl.sdm.pa1.listpad.databinding.ActivityListaBinding
import br.edu.ifsp.scl.sdm.pa1.listpad.model.Item
import br.edu.ifsp.scl.sdm.pa1.listpad.model.Lista
import com.google.android.material.snackbar.Snackbar

class ItemActivity : AppCompatActivity(), OnItemClickListener {

    private val POSICAO_INVALIDA = -1

    companion object Extras {
        const val EXTRA_ITEM = "EXTRA_ITEM"
        const val EXTRA_POSICAO_ITEM = "EXTRA_POSICAO_ITEM"
    }

    private val activityItemBinding: ActivityItemBinding by lazy {
        ActivityItemBinding.inflate(layoutInflater)
    }

    private lateinit var adicionarItemActivityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var visualizarItemActivityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var editarItemActivityResultLauncher: ActivityResultLauncher<Intent>


    //Controller
    private val itemController: ItemController by lazy {
        ItemController(this)
    }

    //Data source
    private val itemList: MutableList<Item> by lazy {
        itemController.recuperarItens()
    }

    //Adapter
    private val itemAdapter: ItensRvAdapter by lazy {
        ItensRvAdapter(this, itemList)
    }

    //Layout Manager
    private val itemLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityItemBinding.root)
        supportActionBar?.subtitle = "Itens da Lista"

        //Associar Adapter e Layout Manager ao Recycler View
        activityItemBinding.itensRv.adapter = itemAdapter
        activityItemBinding.itensRv.layoutManager = itemLayoutManager

        //Adicionar
        adicionarItemActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
            if (resultado.resultCode == RESULT_OK) {
                resultado.data?.getParcelableExtra<Item>(EXTRA_ITEM)?.apply {
                    itemController.criarItem(this)
                    itemList.add(this)
                    itemAdapter.notifyDataSetChanged()
                }
            }
        }

        visualizarItemActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
            if (resultado.resultCode == RESULT_OK) {
                resultado.data?.getIntExtra(EXTRA_POSICAO_ITEM, POSICAO_INVALIDA)
                resultado.data?.getParcelableExtra<Item>(EXTRA_ITEM)?.apply {
                }
            }
        }

        //Editar
        editarItemActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
            if (resultado.resultCode == RESULT_OK) {
                val posicao = resultado.data?.getIntExtra(EXTRA_POSICAO_ITEM, POSICAO_INVALIDA)
                resultado.data?.getParcelableExtra<Item>(EXTRA_ITEM)?.apply {
                    if (posicao != null && posicao != -1) {
                        itemController.atualizarItem(this)
                        itemList[posicao] = this
                        itemAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

        activityItemBinding.adicionarItemFb.setOnClickListener {
            val addItemIntent = Intent(this, ItemFormActivity::class.java)
            //addListaIntent.putExtra()
            adicionarItemActivityResultLauncher.launch(addItemIntent)
        }
    }

    override fun onContextItemSelected(itemMenu: MenuItem): Boolean {
        val posicao = itemAdapter.posicao
        val item = itemList[posicao]

        return when(itemMenu.itemId) {
            R.id.EditarItemMi -> {
                //Editar
                val editarListaIntent = Intent(this, ListaFormActivity::class.java)
                editarListaIntent.putExtra(EXTRA_ITEM, item)
                editarListaIntent.putExtra(EXTRA_POSICAO_ITEM, posicao)
                editarItemActivityResultLauncher.launch(editarListaIntent)
                true
            } R.id.removerItemMi -> {
                //Remover
                with(AlertDialog.Builder(this)) {
                    setMessage("Confirma a remoção?")
                    setPositiveButton("Sim") { _, _ ->
                        itemController.removerLista(item.descricao)
                        itemList.removeAt(posicao)
                        itemAdapter.notifyDataSetChanged()
                        Snackbar.make(activityItemBinding.root, "Item removido", Snackbar.LENGTH_SHORT).show()
                    }
                    setNegativeButton("Não") { _, _ ->
                        Snackbar.make(activityItemBinding.root, "Remoção cancelada", Snackbar.LENGTH_SHORT).show()
                    }
                    create()
                }.show()

                true
            } else -> { false }
        }
    }

    override fun onItemClick(posicao: Int) {
        val item = itemList[posicao]
        val consultarItensIntent = Intent(this, ItemActivity::class.java)
        consultarItensIntent.putExtra(EXTRA_ITEM, item)
        startActivity(consultarItensIntent)
    }

    override fun onStart() {
        super.onStart()
    }

}