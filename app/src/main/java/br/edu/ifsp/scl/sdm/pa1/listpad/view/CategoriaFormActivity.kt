package br.edu.ifsp.scl.sdm.pa1.listpad.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.scl.sdm.pa1.listpad.databinding.ActivityCategoriaBinding
import br.edu.ifsp.scl.sdm.pa1.listpad.databinding.ActivityCategoriaFormBinding
import br.edu.ifsp.scl.sdm.pa1.listpad.model.Categoria
import br.edu.ifsp.scl.sdm.pa1.listpad.view.CategoriaActivity.Extras.EXTRA_CATEGORIA
import br.edu.ifsp.scl.sdm.pa1.listpad.view.CategoriaActivity.Extras.EXTRA_POSICAO_CATEGORIA

class CategoriaFormActivity : AppCompatActivity() {
    // Classe de ViewBinding
    private lateinit var activityCategoriaFormBinding: ActivityCategoriaFormBinding
    private val POSICAO_INVALIDA = -1
    private var posicao: Int = POSICAO_INVALIDA
    private lateinit var categoria: Categoria

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityCategoriaFormBinding = ActivityCategoriaFormBinding.inflate(layoutInflater)
        setContentView(activityCategoriaFormBinding.root)

        posicao = intent.getIntExtra(EXTRA_POSICAO_CATEGORIA, POSICAO_INVALIDA)

        intent.getParcelableExtra<Categoria>(EXTRA_CATEGORIA)?.run {
            activityCategoriaFormBinding.nomeCategoriaEt.setText(this.nome)

            if (posicao == POSICAO_INVALIDA) {
                activityCategoriaFormBinding.nomeCategoriaEt.isEnabled = false
                activityCategoriaFormBinding.salvarBtCategoria.visibility = View.GONE
            }
        }

        activityCategoriaFormBinding.salvarBtCategoria .setOnClickListener {
            val categoria = Categoria(
                activityCategoriaFormBinding.nomeCategoriaEt.text.toString()
            )

            val resultadoIntent = intent.putExtra(EXTRA_CATEGORIA, categoria)

            if (posicao != POSICAO_INVALIDA) {
                resultadoIntent.putExtra(EXTRA_POSICAO_CATEGORIA, posicao)
            }
            setResult(RESULT_OK, resultadoIntent)
            finish()
        }
    }
}