package br.edu.ifsp.scl.sdm.pa1.listpad.view

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.scl.sdm.pa1.listpad.R
import br.edu.ifsp.scl.sdm.pa1.listpad.dao.CategoriaDAO
import br.edu.ifsp.scl.sdm.pa1.listpad.dao.CategoriaDAOImpl
import br.edu.ifsp.scl.sdm.pa1.listpad.dao.ListaDAO
import br.edu.ifsp.scl.sdm.pa1.listpad.dao.ListaDAOImpl
import br.edu.ifsp.scl.sdm.pa1.listpad.databinding.ActivityListaFormBinding
import br.edu.ifsp.scl.sdm.pa1.listpad.model.Categoria
import br.edu.ifsp.scl.sdm.pa1.listpad.model.Lista
import br.edu.ifsp.scl.sdm.pa1.listpad.view.ListaActivity.Extras.EXTRA_LISTA
import com.google.android.material.snackbar.Snackbar

class ListaFormActivity : AppCompatActivity() {

    // Classe de ViewBinding
    private lateinit var activityListaFormBinding: ActivityListaFormBinding
    private var listId = -1

    private val categoriaDao: CategoriaDAO by lazy {
        CategoriaDAOImpl(this)
    }

    private val listaDao: ListaDAO by lazy {
        ListaDAOImpl(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityListaFormBinding = ActivityListaFormBinding.inflate(layoutInflater)
        setContentView(activityListaFormBinding.root)

        val categoriasObjLista: MutableList<Categoria> = categoriaDao.recuperarCategorias()
        val categoriasArray = categoriasObjLista
                .map { it.nome }
                .toTypedArray()

        val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter(this, R.layout.layout_spinner_item, categoriasArray)
        activityListaFormBinding.categoriaListaSp.adapter = spinnerAdapter


        intent.getParcelableExtra<Lista>(EXTRA_LISTA)?.apply {
            listId = this.id ?: -1
            activityListaFormBinding.nomeListaEt.setText(this.nome)
            activityListaFormBinding.descricaoListaEt.setText(this.descricao)
            activityListaFormBinding.categoriaListaSp.setSelection(spinnerAdapter.getPosition(this.categoria))
            activityListaFormBinding.urgenteListaCb.isChecked = this.urgente

        }

        activityListaFormBinding.salvarBtLista.setOnClickListener {
            val nome = activityListaFormBinding.nomeListaEt.text.toString()
            if (nome.isEmpty()) {
                Snackbar.make(activityListaFormBinding.root,
                        "Nome não pode ser em branco",
                        Snackbar.LENGTH_SHORT)
                        .show()
                return@setOnClickListener
            }

            val lista = Lista(
                    if (listId != -1) listId else null,
                    nome,
                    activityListaFormBinding.descricaoListaEt.text.toString(),
                    activityListaFormBinding.categoriaListaSp.selectedItem.toString(),
                    activityListaFormBinding.urgenteListaCb.isChecked
            )


            val (success, create) = if (lista.id == null) {
                listaDao.criarLista(lista) to true
            } else {
                listaDao.atualizarLista(lista) to false
            }

            if (!success) {
                Snackbar.make(activityListaFormBinding.root,
                        "Erro ao ${if (create) "inserir" else "editar"} lista",
                        Snackbar.LENGTH_SHORT)
                        .show()

                return@setOnClickListener
            }

            setResult(RESULT_OK)
            finish()
        }
    }
}