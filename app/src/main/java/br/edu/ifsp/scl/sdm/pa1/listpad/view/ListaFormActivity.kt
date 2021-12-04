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

class ListaFormActivity : AppCompatActivity() {

    // Classe de ViewBinding
    private lateinit var activityListaFormBinding: ActivityListaFormBinding

    //Controller
    private val categoriaController: CategoriaController by lazy {
        CategoriaController(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityListaFormBinding = ActivityListaFormBinding.inflate(layoutInflater)
        setContentView(activityListaFormBinding.root)

        val categoriasObjLista: MutableList<Categoria> = categoriaController.recuperarCategorias()

        val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter(
            this, R.layout.activity_lista_form, categorias)
        activityListaFormBinding.categoriaListaSp.adapter = spinnerAdapter

        // Novo contato ou editar contato
        val lista: Lista? = intent.getParcelableExtra(CategoriaActivity.Extras.EXTRA_LISTA)
        if (lista != null) {
            // Editar
            activityListaFormBinding.nomeListaEt.setText(lista.nome)
            activityListaFormBinding.descricaoListaEt.setText(lista.nome)
            activityListaFormBinding.categoriaListaSp.setSelection(spinnerAdapter.getPosition(lista.categoria))
            activityListaFormBinding.urgenteListaCb.isChecked = lista.urgente

            if (intent.action == ItemActivity.Extras.VISUALIZAR_LISTA_ACTION) {
                // Visualizar
                activityListaFormBinding.nomeListaEt.isEnabled = false
                activityListaFormBinding.descricaoListaEt.isEnabled = false
                activityListaFormBinding.categoriaListaSp.isEnabled = false
                activityListaFormBinding.urgenteListaCb.isEnabled = false
                activityListaFormBinding.salvarBtItem.visibility = View.GONE
            }
        }

        activityListaFormBinding.salvarBtItem.setOnClickListener {
            val novaLista = Lista(
                activityListaFormBinding.nomeListaEt.text.toString(),
                activityListaFormBinding.descricaoListaEt.text.toString(),
                activityListaFormBinding.categoriaListaSp.selectedItem.toString(),
                activityListaFormBinding.urgenteListaCb.isChecked
            )

            val retornoIntent = Intent()
            retornoIntent.putExtra(ItemActivity.Extras.EXTRA_LISTA, novaLista)
            setResult(RESULT_OK, retornoIntent)
            finish()
        }
    }
}