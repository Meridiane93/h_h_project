package com.meridiane.lection3.presentation.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.meridiane.lection3.R

class RcViewPager(private val list: MutableList<String?>) :
    RecyclerView.Adapter<RcViewPager.ImageHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_page, parent, false)
        return ImageHolder(view)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) =
        holder.setData(list[position])

    override fun getItemCount(): Int = list.size

    inner class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var imageView: ImageView = itemView.findViewById(R.id.imagePage)

        fun setData(image: String?) {
            if (image == null) imageView.load(R.drawable.default_image_view_pager)
            else imageView.load(image)
        }


    }
}