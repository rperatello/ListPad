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
import br.edu.ifsp.scl.sdm.pa1.listpad.controller.CategoriaController
import br.edu.ifsp.scl.sdm.pa1.listpad.controller.ListaController
import br.edu.ifsp.scl.sdm.pa1.listpad.databinding.ActivityListaBinding
import br.edu.ifsp.scl.sdm.pa1.listpad.model.Categoria
import br.edu.ifsp.scl.sdm.pa1.listpad.model.Lista
import com.google.android.material.snackbar.Snackbar

class ListaActivity : AppCompatActivity(), OnListaClickListener {

    private val POSICAO_INVALIDA = -1
    private var categorias: MutableList<String> = arrayListOf()

    companion object Extras {
        const val EXTRA_LISTA = "EXTRA_LISTA"
        const val EXTRA_CATEGORIAS = "EXTRA_CATEGORIAS"
        const val EXTRA_POSICAO_LISTA = "EXTRA_POSICAO_LISTA"
    }

    private val activityListaBinding: ActivityListaBinding by lazy {
        ActivityListaBinding.inflate(layoutInflater)
    }

    private lateinit var adicionarListaActivityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var visualizarListaActivityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var editarListaActivityResultLauncher: ActivityResultLauncher<Intent>

    //Controller
    private val listaController: ListaController by lazy {
        ListaController(this)
    }

    //Controller
    private val categoriaController: CategoriaController by lazy {
        CategoriaController(this)
    }

    private val categoriasObjLista: MutableList<Categoria> by lazy{
        categoriaController.recuperarCategorias()
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

        for (c in  categoriasObjLista){
            categorias.add(c.nome)
        }

        //Adicionar
        adicionarListaActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
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

        //Editar
        editarListaActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
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
            addListaIntent.putExtra(EXTRA_CATEGORIAS, categorias)
            adicionarListaActivityResultLauncher.launch(addListaIntent)
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
                //editarListaIntent.putExtra(EXTRA_CATEGORIAS, categorias)
                editarListaIntent.putExtra(EXTRA_POSICAO_LISTA, posicao)
                editarListaActivityResultLauncher.launch(editarListaIntent)
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