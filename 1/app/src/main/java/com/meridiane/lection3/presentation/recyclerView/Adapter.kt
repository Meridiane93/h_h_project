package com.meridiane.lection3.presentation.recyclerView

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.meridiane.lection3.R
import com.meridiane.lection3.databinding.BookLayoutBinding
import com.meridiane.lection4.data.getBookData.BookListUiState

class Adapter : RecyclerView.Adapter<Adapter.Holder>() {

    private val list = mutableListOf<BookListUiState>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = BookLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(list[position])
    }

    fun updateAdapter(item: MutableList<BookListUiState>){
        list.addAll(item)
        notifyDataSetChanged()
    }

    fun clear() {
        list.clear()
        notifyItemRangeRemoved(0, list.size)
    }

    override fun getItemCount(): Int = list.size

    class Holder(private val binding: BookLayoutBinding): RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("StringFormatMatches")
        fun bind(bookListUiState: BookListUiState){
            binding.textAuthor.text = bookListUiState.name
            var listTxt = ""

           bookListUiState.books?.forEach {
                listTxt += if (it.inStock) (binding.root.context.getString(R.string.inStock_true,it.title) )
                else (binding.root.context.getString(R.string.inStock_false,it.title ))
            }

            binding.textBook.text = listTxt
        }
    }

}