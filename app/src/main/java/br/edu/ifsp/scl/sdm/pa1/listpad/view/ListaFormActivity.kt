package br.edu.ifsp.scl.sdm.pa1.listpad.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.scl.sdm.pa1.listpad.R
import br.edu.ifsp.scl.sdm.pa1.listpad.controller.CategoriaController
import br.edu.ifsp.scl.sdm.pa1.listpad.databinding.ActivityListaFormBinding
import br.edu.ifsp.scl.sdm.pa1.listpad.model.Categoria
import br.edu.ifsp.scl.sdm.pa1.listpad.model.Lista
import br.edu.ifsp.scl.sdm.pa1.listpad.view.ListaActivity.Extras.EXTRA_LISTA
import br.edu.ifsp.scl.sdm.pa1.listpad.view.ListaActivity.Extras.EXTRA_POSICAO_LISTA

class ListaFormActivity : AppCompatActivity() {

    // Classe de ViewBinding
    private lateinit var activityListaFormBinding: ActivityListaFormBinding
    private val POSICAO_INVALIDA = -1
    private var posicao = POSICAO_INVALIDA
    //private lateinit var lista: Lista

    /*//Controller
    private val categoriaController: CategoriaController by lazy {
        CategoriaController(this)
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityListaFormBinding = ActivityListaFormBinding.inflate(layoutInflater)
        setContentView(activityListaFormBinding.root)

        /*var categoriasObjLista: MutableList<Categoria> = categoriaController.recuperarCategorias()
        var categorias: MutableList<String> = arrayListOf()
        for (c in  categoriasObjLista){
            categorias.add(c.nome)
        }*/

        val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter(
            this, R.layout.activity_lista_form, categorias)
        activityListaFormBinding.categoriaListaSp.adapter = spinnerAdapter

        posicao = intent.getIntExtra(EXTRA_POSICAO_LISTA, POSICAO_INVALIDA)

        intent.getParcelableExtra<Lista>(EXTRA_LISTA)?.apply {
            activityListaFormBinding.nomeListaEt.setText(this.nome)
            activityListaFormBinding.descricaoListaEt.setText(this.descricao)
            activityListaFormBinding.categoriaListaSp.setSelection(spinnerAdapter.getPosition(this.categoria))
            activityListaFormBinding.urgenteListaCb.isChecked = this.urgente

            if (posicao != -1) {
                activityListaFormBinding.nomeListaEt.isEnabled = false
                activityListaFormBinding.descricaoListaEt.isEnabled = false
                activityListaFormBinding.categoriaListaSp.isEnabled = false
                activityListaFormBinding.urgenteListaCb.isEnabled = false
                activityListaFormBinding.salvarBtLista.visibility = View.GONE
            }
        }


        activityListaFormBinding.salvarBtLista.setOnClickListener {
            val lista = Lista(
                activityListaFormBinding.nomeListaEt.text.toString(),
                activityListaFormBinding.descricaoListaEt.text.toString(),
                activityListaFormBinding.categoriaListaSp.selectedItem.toString(),
                activityListaFormBinding.urgenteListaCb.isChecked
            )

            val resultadoIntent = Intent()

            if (posicao != POSICAO_INVALIDA) {
                resultadoIntent.putExtra(EXTRA_POSICAO_LISTA, posicao)
            }

            resultadoIntent.putExtra(EXTRA_LISTA, lista)
            setResult(RESULT_OK, resultadoIntent)
            finish()
        }
    }
}