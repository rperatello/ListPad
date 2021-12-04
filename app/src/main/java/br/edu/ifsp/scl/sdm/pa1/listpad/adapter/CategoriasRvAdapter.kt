package br.edu.ifsp.scl.sdm.pa1.listpad.adapter

import android.view.*
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.scl.sdm.pa1.listpad.R
import br.edu.ifsp.scl.sdm.pa1.listpad.databinding.ActivityCategoriaBinding
import br.edu.ifsp.scl.sdm.pa1.listpad.databinding.LayoutCategoriaBinding
import br.edu.ifsp.scl.sdm.pa1.listpad.model.Categoria
import br.edu.ifsp.scl.sdm.pa1.listpad.view.OnCategoriaClickListener

class CategoriasRvAdapter (
    private val OnCategoriaClickListener: OnCategoriaClickListener,
    private val categoriaList: MutableList<Categoria>
): RecyclerView.Adapter<CategoriasRvAdapter.CategoriaLayoutHolder>() {

    // Posição será setada pelo onBindViewHolder para chamar as funções de tratamento de clique para a categoria correta
    private val POSICAO_INVALIDA = -1
    var posicao: Int = POSICAO_INVALIDA

    //View Holder
    inner class CategoriaLayoutHolder(layoutCategoriaBinding: LayoutCategoriaBinding): RecyclerView.ViewHolder(layoutCategoriaBinding.root),
        View.OnCreateContextMenuListener {
        val nomeCategoriaLayout: TextView = layoutCategoriaBinding.nomeCategoriaLayoutTv
        init {
            itemView.setOnCreateContextMenuListener(this)
        }


        override fun onCreateContextMenu(
            menu: ContextMenu?,
            view: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            MenuInflater(view?.context).inflate(R.menu.menu_categoria, menu)
        }
    }

    //Quando uma nova célula precisa ser criada
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaLayoutHolder {
        //Criar uma nova célula
        val layoutCategoriaBinding = LayoutCategoriaBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        //Criar um holder associado a nova célula
        return CategoriaLayoutHolder(layoutCategoriaBinding)
    }

    // Chamado pelo ViewHolder para alterar o conteúdo de uma View
    override fun onBindViewHolder(holder: CategoriaLayoutHolder, position: Int) {
        // Busca o contato para pegar os valores
        val categoria = categoriaList[position]

        with(holder){
            nomeCategoriaLayout.text = categoria.nome
            itemView.setOnClickListener {
                OnCategoriaClickListener.onCategoriaClick(position)
            }
            itemView.setOnLongClickListener{
                posicao = position
                false
            }
        }

    }

    // Chamado pelo LayoutManager para buscar a quantidade de dados e preparar a quanti
    // dade de Views e ViewHolders
    override fun getItemCount(): Int = categoriaList.size

}