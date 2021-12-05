package br.edu.ifsp.scl.sdm.pa1.listpad.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.scl.sdm.pa1.listpad.databinding.ActivityItemFormBinding
import br.edu.ifsp.scl.sdm.pa1.listpad.model.Item
import br.edu.ifsp.scl.sdm.pa1.listpad.model.Lista
import br.edu.ifsp.scl.sdm.pa1.listpad.view.ItemActivity.Extras.EXTRA_ITEM

class ItemFormActivity : AppCompatActivity() {

    // Classe de ViewBinding
    private lateinit var activityItemFormBinding: ActivityItemFormBinding
    private val POSICAO_INVALIDA = -1
    private var posicao = POSICAO_INVALIDA
    private lateinit var item: Item

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityItemFormBinding = ActivityItemFormBinding.inflate(layoutInflater)
        setContentView(activityItemFormBinding.root)

        posicao = intent.getIntExtra(ListaActivity.EXTRA_POSICAO_LISTA, POSICAO_INVALIDA)

        intent.getParcelableExtra<Lista>(EXTRA_ITEM)?.apply {
            activityItemFormBinding.listaItemTv.text = item.lista
            activityItemFormBinding.descricaoItemEt.setText(item.descricao)
            activityItemFormBinding.realizadoItemCb.isChecked = item.realizado

            if (posicao != -1) {
                //activityItemFormBinding.listaItemTv.isEnabled = false
                activityItemFormBinding.descricaoItemEt.isEnabled = false
                activityItemFormBinding.realizadoItemCb.isEnabled = false
                activityItemFormBinding.salvarBtItem.visibility = View.GONE
            }
        }

        activityItemFormBinding.salvarBtItem.setOnClickListener {
            val item = Item(
                activityItemFormBinding.listaItemTv.text.toString(),
                activityItemFormBinding.descricaoItemEt.text.toString(),
                activityItemFormBinding.realizadoItemCb.isChecked
                )

            val retornoIntent = Intent()
            retornoIntent.putExtra(EXTRA_ITEM, item)
            setResult(RESULT_OK, retornoIntent)
            finish()
        }
    }
}