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
import br.edu.ifsp.scl.sdm.pa1.listpad.adapter.ListasRvAdapter
import br.edu.ifsp.scl.sdm.pa1.listpad.dao.ListaDAO
import br.edu.ifsp.scl.sdm.pa1.listpad.dao.ListaDAOImpl
import br.edu.ifsp.scl.sdm.pa1.listpad.databinding.ActivityListaBinding
import br.edu.ifsp.scl.sdm.pa1.listpad.model.Lista
import com.google.android.material.snackbar.Snackbar

class ListaActivity : AppCompatActivity(), OnListaClickListener {

    companion object Extras {
        const val EXTRA_LISTA = "EXTRA_LISTA"
    }

    private val activityListaBinding: ActivityListaBinding by lazy {
        ActivityListaBinding.inflate(layoutInflater)
    }

    private val activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
                if (resultado.resultCode == RESULT_OK) {
                    refreshList()
                }
            }

    private val listaDao: ListaDAO by lazy {
        ListaDAOImpl(this)
    }

    //Data source
    private lateinit var listaList: MutableList<Lista>

    //Adapter
    private lateinit var listaAdapter: ListasRvAdapter

    //Layout Manager
    private val listaLayoutManager: LinearLayoutManager = LinearLayoutManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityListaBinding.root)
        supportActionBar?.subtitle = "Listas"

        activityListaBinding.listasRv.layoutManager = listaLayoutManager
        refreshList()

        activityListaBinding.adicionarListaFb.setOnClickListener {
            activityResultLauncher.launch(Intent(this, ListaFormActivity::class.java))
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val posicao = listaAdapter.posicao
        val lista = listaList[posicao]

        return when (item.itemId) {
            R.id.editarListaMi -> {
                //Editar
                val editarListaIntent = Intent(this, ListaFormActivity::class.java)
                editarListaIntent.putExtra(EXTRA_LISTA, lista)
                activityResultLauncher.launch(editarListaIntent)
                true
            }
            R.id.removerListaMi -> {
                //Remover
                with(AlertDialog.Builder(this)) {
                    setMessage("Confirma a remoção?")
                    setPositiveButton("Sim") { _, _ ->
                        lista.id?.let {
                            listaDao.removerLista(it)
                            refreshList()
                            Snackbar.make(activityListaBinding.root, "Lista removida", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                    setNegativeButton("Não") { _, _ ->
                        Snackbar.make(activityListaBinding.root, "Remoção cancelada", Snackbar.LENGTH_SHORT).show()
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

    override fun onListaClick(posicao: Int) {
        val consultarItensIntent = Intent(this, ItemActivity::class.java).apply {
            putExtra(EXTRA_LISTA, listaList[posicao])
        }
        startActivity(consultarItensIntent)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshList() {
        listaList = listaDao.recuperarListas()
        listaAdapter = ListasRvAdapter(this, listaList)
        activityListaBinding.listasRv.adapter = listaAdapter
    }

}