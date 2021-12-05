package br.edu.ifsp.scl.sdm.pa1.listpad.adapter

import android.view.*
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.scl.sdm.pa1.listpad.R
import br.edu.ifsp.scl.sdm.pa1.listpad.databinding.LayoutItemBinding
import br.edu.ifsp.scl.sdm.pa1.listpad.databinding.LayoutListaBinding
import br.edu.ifsp.scl.sdm.pa1.listpad.model.Item
import br.edu.ifsp.scl.sdm.pa1.listpad.view.OnItemClickListener

class ItensRvAdapter (
    private val OnItemClickListener: OnItemClickListener,
    private val itemList: MutableList<Item>
): RecyclerView.Adapter<ItensRvAdapter.ItemLayoutHolder>() {

    // Posição será setada pelo onBindViewHolder para chamar as funções de tratamento de clique
    private val POSICAO_INVALIDA = -1
    var posicao: Int = POSICAO_INVALIDA

    //View Holder
    inner class ItemLayoutHolder(layoutItemBinding: LayoutItemBinding): RecyclerView.ViewHolder(layoutItemBinding.root),
        View.OnCreateContextMenuListener {
        val listaItemLayoutTv: TextView = layoutItemBinding.listaItemLayoutTv
        val descItemLayoutTv: TextView = layoutItemBinding.descItemLayoutTv
        val concluidoItemLayoutCb: CheckBox = layoutItemBinding.concluidoItemLayoutCb

        init {
            itemView.setOnCreateContextMenuListener(this)
        }


        override fun onCreateContextMenu(
            menu: ContextMenu?,
            view: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            MenuInflater(view?.context).inflate(R.menu.menu_lista, menu)
        }
    }

    //Quando uma nova célula precisa ser criada
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemLayoutHolder {
        //Criar uma nova célula
        val layoutItemBinding = LayoutItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        //Criar um holder associado a nova célula
        return ItemLayoutHolder(layoutItemBinding)
    }

    // Chamado pelo ViewHolder para alterar o conteúdo de uma View
    override fun onBindViewHolder(holder: ItemLayoutHolder, position: Int) {
        // Busca o contato para pegar os valores
        val item = itemList[position]

        with(holder){
            listaItemLayoutTv.text = item.lista
            descItemLayoutTv.text = item.descricao
            concluidoItemLayoutCb.isChecked = item.realizado

            itemView.setOnClickListener {
                OnItemClickListener.onItemClick(position)
            }
            itemView.setOnLongClickListener{
                posicao = position
                false
            }
        }

    }

    // Chamado pelo LayoutManager para buscar a quantidade de dados e preparar a quanti
    // dade de Views e ViewHolders
    override fun getItemCount(): Int = itemList.size

}