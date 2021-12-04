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
import br.edu.ifsp.scl.sdm.pa1.listpad.adapter.ListasRvAdapter
import br.edu.ifsp.scl.sdm.pa1.listpad.controller.ListaController
import br.edu.ifsp.scl.sdm.pa1.listpad.databinding.ActivityListaBinding
import br.edu.ifsp.scl.sdm.pa1.listpad.model.Lista
import com.google.android.material.snackbar.Snackbar

class ListaActivity : AppCompatActivity(), OnListaClickListener {

    private val POSICAO_INVALIDA = -1

    companion object Extras {
        const val EXTRA_LISTA = "EXTRA_LISTA"
        const val EXTRA_POSICAO_LISTA = "EXTRA_POSICAO_LISTA"
    }

    private val activityListaBinding: ActivityListaBinding by lazy {
        ActivityListaBinding.inflate(layoutInflater)
    }

    private lateinit var adicionarlistaActivityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var visualizarListaActivityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var editarCategoriaActivityResultLauncher: ActivityResultLauncher<Intent>


    //Controller
    private val listaController: ListaController by lazy {
        ListaController(this)
    }

    //Data source
    private val listaList: MutableList<Lista> by lazy {
        listaController.recuperarListas()
    }

    //Adapter
    private val listaAdapter: ListasRvAdapter by lazy {
        ListasRvAdapter(this, listaList)
    }

    //Layout Manager
    private val listaLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityListaBinding.root)
        supportActionBar?.subtitle = "Listas"

        //Associar Adapter e Layout Manager ao Recycler View
        activityListaBinding.listasRv.adapter = listaAdapter
        activityListaBinding.listasRv.layoutManager = listaLayoutManager

        //Adicionar
        adicionarlistaActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
            if (resultado.resultCode == RESULT_OK) {
                resultado.data?.getParcelableExtra<Lista>(EXTRA_LISTA)?.apply {
                    listaController.criarLista(this)
                    listaList.add(this)
                    listaAdapter.notifyDataSetChanged()
                }
            }
        }

        visualizarListaActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
            if (resultado.resultCode == RESULT_OK) {
                resultado.data?.getIntExtra(EXTRA_POSICAO_LISTA, POSICAO_INVALIDA)
                resultado.data?.getParcelableExtra<Lista>(EXTRA_LISTA)?.apply {
                }
            }
        }

        //Editar um episódio
        editarCategoriaActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
            if (resultado.resultCode == RESULT_OK) {
                val posicao = resultado.data?.getIntExtra(EXTRA_POSICAO_LISTA, POSICAO_INVALIDA)
                resultado.data?.getParcelableExtra<Lista>(EXTRA_LISTA)?.apply {
                    if (posicao != null && posicao != -1) {
                        listaController.atualizarLista(this)
                        listaList[posicao] = this
                        listaAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

        activityListaBinding.adicionarListaFb.setOnClickListener {
            val addListaIntent = Intent(this, ListaFormActivity::class.java)
            //addListaIntent.putExtra()
            adicionarlistaActivityResultLauncher.launch(addListaIntent)
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val posicao = listaAdapter.posicao
        val lista = listaList[posicao]

        return when(item.itemId) {
            R.id.EditarListaMi -> {
                //Editar
                val editarListaIntent = Intent(this, ListaFormActivity::class.java)
                editarListaIntent.putExtra(EXTRA_LISTA, lista)
                editarListaIntent.putExtra(EXTRA_POSICAO_LISTA, posicao)
                editarCategoriaActivityResultLauncher.launch(editarListaIntent)
                true
            } R.id.removerListaMi -> {
                //Remover
                with(AlertDialog.Builder(this)) {
                    setMessage("Confirma a remoção?")
                    setPositiveButton("Sim") { _, _ ->
                        listaController.removerLista(lista.nome)
                        listaList.removeAt(posicao)
                        listaAdapter.notifyDataSetChanged()
                        Snackbar.make(activityListaBinding.root, "Lista removida", Snackbar.LENGTH_SHORT).show()
                    }
                    setNegativeButton("Não") { _, _ ->
                        Snackbar.make(activityListaBinding.root, "Remoção cancelada", Snackbar.LENGTH_SHORT).show()
                    }
                    create()
                }.show()

                true
            } else -> { false }
        }
    }

    override fun onListaClick(posicao: Int) {
        val lista = listaList[posicao]
        val consultarItensIntent = Intent(this, ItemActivity::class.java)
        consultarItensIntent.putExtra(EXTRA_LISTA, lista)
        startActivity(consultarItensIntent)
    }

    override fun onStart() {
        super.onStart()
    }

}