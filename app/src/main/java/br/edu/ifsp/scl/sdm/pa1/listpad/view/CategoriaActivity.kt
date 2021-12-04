package br.edu.ifsp.scl.sdm.pa1.listpad.view

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.ifsp.scl.sdm.pa1.listpad.R
import br.edu.ifsp.scl.sdm.pa1.listpad.adapter.CategoriasRvAdapter
import br.edu.ifsp.scl.sdm.pa1.listpad.controller.CategoriaController
import br.edu.ifsp.scl.sdm.pa1.listpad.databinding.ActivityCategoriaBinding
import br.edu.ifsp.scl.sdm.pa1.listpad.model.Categoria
import com.google.android.material.snackbar.Snackbar

class CategoriaActivity : AppCompatActivity(), OnCategoriaClickListener {

    companion object Extras {
        const val EXTRA_CATEGORIA = "EXTRA_CATEGORIA"
        const val EXTRA_POSICAO_CATEGORIA = "EXTRA_POSICAO_CATEGORIA"
    }

       private val activityCategoriaBinding: ActivityCategoriaBinding by lazy {
        ActivityCategoriaBinding.inflate(layoutInflater)
    }

    private lateinit var categoriaActivityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var editarCategoriaActivityResultLauncher: ActivityResultLauncher<Intent>


    //Controller
    private val categoriaController: CategoriaController by lazy {
        CategoriaController(this)
    }

    //Data source
    private val categoriaList: MutableList<Categoria> by lazy {
        categoriaController.recuperarCategorias()
    }

    //Adapter
    private val categoriaAdapter: CategoriasRvAdapter by lazy {
        CategoriasRvAdapter(this, categoriaList)
    }

    //Layout Manager
    private val categoriaLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityCategoriaBinding.root)
        supportActionBar?.subtitle = "Categorias"

        //Associar Adapter e Layout Manager ao Recycler View
        activityCategoriaBinding.categoriasRv.adapter = categoriaAdapter
        activityCategoriaBinding.categoriasRv.layoutManager = categoriaLayoutManager

        //Adicionar
        categoriaActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
            if (resultado.resultCode == RESULT_OK) {
                resultado.data?.getParcelableExtra<Categoria>(EXTRA_CATEGORIA)?.apply {
                    categoriaController.criarCategoria(this)
                    categoriaList.add(this)
                    categoriaAdapter.notifyDataSetChanged()
                }
            }
        }

        //Editar um episódio
        editarCategoriaActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
            if (resultado.resultCode == RESULT_OK) {
                val posicao = resultado.data?.getIntExtra(EXTRA_POSICAO_CATEGORIA, -1)
                resultado.data?.getParcelableExtra<Categoria>(EXTRA_CATEGORIA)?.apply {
                    if (posicao != null && posicao != -1) {
                        categoriaController.atualizarCategoria(this)
                        categoriaList[posicao] = this
                        categoriaAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

        activityCategoriaBinding.adicionarCategoriaFb.setOnClickListener {
            val addCategoriaIntent = Intent(this, CategoriaFormActivity::class.java)
            //addCategoriaIntent.putExtra()
            categoriaActivityResultLauncher.launch(addCategoriaIntent)
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val posicao = categoriaAdapter.posicao
        val categoria = categoriaList[posicao]

        return when(item.itemId) {
            R.id.EditarCategoriaMi -> {
                //Editar episódio
                val editarCategoriaIntent = Intent(this, CategoriaFormActivity::class.java)
                editarCategoriaIntent.putExtra(EXTRA_CATEGORIA, categoria)
                editarCategoriaIntent.putExtra(EXTRA_POSICAO_CATEGORIA, posicao)
                editarCategoriaActivityResultLauncher.launch(editarCategoriaIntent)
                true
            } else -> { false }
        }
    }

    override fun onCategoriaClick(posicao: Int) {
    }

    override fun onStart() {
        super.onStart()
    }
}