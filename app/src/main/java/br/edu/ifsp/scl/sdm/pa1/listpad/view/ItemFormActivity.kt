package br.edu.ifsp.scl.sdm.pa1.listpad.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.scl.sdm.pa1.listpad.databinding.ActivityItemFormBinding
import br.edu.ifsp.scl.sdm.pa1.listpad.model.Item

class ItemFormActivity : AppCompatActivity() {

    // Classe de ViewBinding
    private lateinit var activityItemFormBinding: ActivityItemFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityItemFormBinding = ActivityItemFormBinding.inflate(layoutInflater)
        setContentView(activityItemFormBinding.root)

        // Novo contato ou editar contato
        val item: Item? = intent.getParcelableExtra(CategoriaActivity.Extras.EXTRA_ITEM)
        if (item != null) {
            // Editar contato
            activityItemFormBinding.listaItemTv.setText(item.lista)
            activityItemFormBinding.listaItemTv.isEnabled = false
            activityItemFormBinding.descricaoItemEt.setText(item.descricao)
            activityItemFormBinding.realizadoItemCb.isChecked = item.realizado

            if (intent.action == ItemActivity.Extras.VISUALIZAR_ITEM_ACTION) {
                // Visualizar contato
                activityItemFormBinding.listaItemTv.isEnabled = false
                activityItemFormBinding.descricaoItemEt.isEnabled = false
                activityItemFormBinding.realizadoItemCb.isEnabled = false
                activityItemFormBinding.salvarBtItem.visibility = View.GONE
            }
        }

        activityItemFormBinding.salvarBtItem.setOnClickListener {
            val novoItem = Item(
                activityItemFormBinding.listaItemTv.text.toString(),
                activityItemFormBinding.descricaoItemEt.text.toString(),
                activityItemFormBinding.realizadoItemCb.isChecked
                )

            val retornoIntent = Intent()
            retornoIntent.putExtra(ItemActivity.Extras.EXTRA_ITEM, novoItem)
            setResult(RESULT_OK, retornoIntent)
            finish()
        }
    }
}