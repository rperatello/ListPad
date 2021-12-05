package br.edu.ifsp.scl.sdm.pa1.listpad.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.scl.sdm.pa1.listpad.dao.ItemDAO
import br.edu.ifsp.scl.sdm.pa1.listpad.dao.ItemDAOImpl
import br.edu.ifsp.scl.sdm.pa1.listpad.databinding.ActivityItemFormBinding
import br.edu.ifsp.scl.sdm.pa1.listpad.model.Item
import br.edu.ifsp.scl.sdm.pa1.listpad.view.ItemActivity.Extras.EXTRA_ITEM
import br.edu.ifsp.scl.sdm.pa1.listpad.view.ItemActivity.Extras.EXTRA_LIST_ID
import com.google.android.material.snackbar.Snackbar

class ItemFormActivity : AppCompatActivity() {

    // Classe de ViewBinding
    private lateinit var activityItemFormBinding: ActivityItemFormBinding

    private var listaId = -1
    private var itemId = -1

    private val itemDao: ItemDAO by lazy {
        ItemDAOImpl(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityItemFormBinding = ActivityItemFormBinding.inflate(layoutInflater)
        setContentView(activityItemFormBinding.root)

        listaId = intent.getIntExtra(EXTRA_LIST_ID, -1)
        intent.getParcelableExtra<Item>(EXTRA_ITEM)?.apply {
            itemId = this.id ?: -1
            activityItemFormBinding.descricaoItemEt.setText(descricao)
            activityItemFormBinding.realizadoItemCb.isChecked = realizado
        }

        activityItemFormBinding.salvarBtItem.setOnClickListener {
            val descricao = activityItemFormBinding.descricaoItemEt.text.toString()
            if (descricao.isEmpty()) {
                Snackbar.make(activityItemFormBinding.root,
                        "Descrição não pode ser em branco",
                        Snackbar.LENGTH_SHORT)
                        .show()
                return@setOnClickListener
            }

            val item = Item(
                    if (itemId != -1) itemId else null,
                    listaId,
                    descricao,
                    activityItemFormBinding.realizadoItemCb.isChecked
            )

            val (success, create) = if (item.id == null) {
                itemDao.criarItem(item) to true
            } else {
                itemDao.atualizarItem(item) to false
            }

            if (!success) {
                Snackbar.make(activityItemFormBinding.root,
                        "Erro ao ${if (create) "inserir" else "editar"} item",
                        Snackbar.LENGTH_SHORT)
                        .show()

                return@setOnClickListener
            }

            setResult(RESULT_OK)
            finish()
        }
    }
}