package br.edu.ifsp.scl.sdm.pa1.listpad.adapter

import android.view.*
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.scl.sdm.pa1.listpad.R
import br.edu.ifsp.scl.sdm.pa1.listpad.databinding.LayoutListaBinding
import br.edu.ifsp.scl.sdm.pa1.listpad.model.Lista
import br.edu.ifsp.scl.sdm.pa1.listpad.view.OnListaClickListener

class ListasRvAdapter (
    private val OnListaClickListener: OnListaClickListener,
    private val listaList: MutableList<Lista>
): RecyclerView.Adapter<ListasRvAdapter.ListaLayoutHolder>() {

    // Posição será setada pelo onBindViewHolder para chamar as funções de tratamento de clique
    private val POSICAO_INVALIDA = -1
    var posicao: Int = POSICAO_INVALIDA

    //View Holder
    inner class ListaLayoutHolder(layoutListaBinding: LayoutListaBinding): RecyclerView.ViewHolder(layoutListaBinding.root),
        View.OnCreateContextMenuListener {
        val nomeListaLayout: TextView = layoutListaBinding.nomeListaLayoutTv
        val descListaLayoutTv: TextView = layoutListaBinding.descListaLayoutTv
        val categoriaListaLayoutTv: TextView = layoutListaBinding.categoriaListaLayoutTv
        val urgenteListaLayoutCb: CheckBox = layoutListaBinding.urgenteListaLayoutCb

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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListaLayoutHolder {
        //Criar uma nova célula
        val layoutListaBinding = LayoutListaBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        //Criar um holder associado a nova célula
        return ListaLayoutHolder(layoutListaBinding)
    }

    // Chamado pelo ViewHolder para alterar o conteúdo de uma View
    override fun onBindViewHolder(holder: ListaLayoutHolder, position: Int) {
        // Busca o contato para pegar os valores
        val lista = listaList[position]

        with(holder){
            nomeListaLayout.text = lista.nome
            descListaLayoutTv.text = lista.descricao
            categoriaListaLayoutTv.text = lista.categoria
            urgenteListaLayoutCb.isChecked = lista.urgente

            itemView.setOnClickListener {
                OnListaClickListener.onListaClick(position)
            }
            itemView.setOnLongClickListener{
                posicao = position
                false
            }
        }

    }

    // Chamado pelo LayoutManager para buscar a quantidade de dados e preparar a quanti
    // dade de Views e ViewHolders
    override fun getItemCount(): Int = listaList.size

}