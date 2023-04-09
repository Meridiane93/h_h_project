package com.meridiane.lection3.presentation.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.meridiane.lection3.R

class RcPreView(
    private val list: MutableList<Int?>,
    //private var onItemClicked: ((image: Int?) -> Unit)
) :
    RecyclerView.Adapter<RcPreView.ImageHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pre_view, parent, false)
        return ImageHolder(view)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) =
        holder.setData(list[position])

    override fun getItemCount(): Int = list.size

    inner class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var imageView: ImageView = itemView.findViewById(R.id.imageRcView)

        fun setData(image: Int?) {
            if (image == null) imageView.load(R.drawable.default_image_pre_view)
            else imageView.load(image)

//            imageView.setOnClickListener {
//                onItemClicked(image)
//            }
        }
    }

}