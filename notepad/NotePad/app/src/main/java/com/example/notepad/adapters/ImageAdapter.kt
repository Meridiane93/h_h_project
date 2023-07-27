package com.example.notepad.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notepad.ListItem
import com.example.notepad.ListOpenAndClosed
import com.example.notepad.R
import com.example.notepad.SharedPrefTheme

// адаптер который отображает фотографии в vpImages из лайоута select_item
class ImageAdapter(
    private var listArray: MutableList<ListItem>,
    private var listOpenAnd : MutableList<ListOpenAndClosed>,
    private val listener:PositionListener): RecyclerView.Adapter<ImageAdapter.ImageHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.select_item,parent,false)
        return ImageHolder(view,parent.context)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) =
        holder.setData(listOpenAnd[position],listArray[position],listener)

    override fun getItemCount(): Int = listArray.size

    class ImageHolder(itemView: View,val context: Context) :RecyclerView.ViewHolder(itemView) {

        private val image = itemView.findViewById<ImageView>(R.id.imageContent)
        private val titles = itemView.findViewById<TextView>(R.id.tvTittle)
        private val constrain = itemView.findViewById<ViewGroup>(R.id.constrainL)
        private  val textClosed = itemView.findViewById<TextView>(R.id.textClosed)

        private lateinit var sharedPrefTheme: SharedPrefTheme

        fun setData(boolean: ListOpenAndClosed,listItem: ListItem,listener: PositionListener){
            titles.text = listItem.text_id
            image.setImageResource(listItem.image_id)

            // смена темы
            sharedPrefTheme = SharedPrefTheme(context)

            if (!boolean.boolean){
                when (sharedPrefTheme.intTheme) { // закрытая тема
                    0 -> constrain.setBackgroundResource(R.color.grey_calendar_close)
                    1 -> constrain.setBackgroundResource(R.color.theme_color_pink_closed_image)
                    2 -> constrain.setBackgroundResource(R.color.grey_calendar_close)
                    3 -> constrain.setBackgroundResource(R.color.color_gold_buttonCloseImageTheme)
                    4 -> constrain.setBackgroundResource(R.color.color_violet_image_close)
                    5 -> constrain.setBackgroundResource(R.color.color_blue_image_close)
                    6 -> constrain.setBackgroundResource(R.color.color_orange_image_close)
                    7 -> constrain.setBackgroundResource(R.color.color_green_image_close)
                    8 -> constrain.setBackgroundResource(R.color.color_brown_image_close)
                    9 -> constrain.setBackgroundResource(R.color.color_greyDark_image_close)
                    10 -> constrain.setBackgroundResource(R.color.color_mint_image_close)
                }
                textClosed.visibility = View.VISIBLE
            }
            else{
                constrain.setBackgroundResource(R.color.grey_calendar)
                when (sharedPrefTheme.intTheme) {
                    0 -> constrain.setBackgroundResource(R.color.grey_calendar)
                    1 -> constrain.setBackgroundResource(R.color.theme_color_pink)
                    2 -> constrain.setBackgroundResource(R.color.grey_calendar)
                    3 -> constrain.setBackgroundResource(R.color.color_gold_button)
                    4 -> constrain.setBackgroundResource(R.color.color_violet_button)
                    5 -> constrain.setBackgroundResource(R.color.color_blue_button)
                    6 -> constrain.setBackgroundResource(R.color.color_orange_button)
                    7 -> constrain.setBackgroundResource(R.color.color_green_button)
                    8 -> constrain.setBackgroundResource(R.color.color_brown_button)
                    9 -> constrain.setBackgroundResource(R.color.color_greyDark_button)
                    10 -> constrain.setBackgroundResource(R.color.color_mint_button)
                }
                textClosed.visibility = View.GONE
            }

            itemView.setOnClickListener {
                // при выборе темы
                val position: Int = adapterPosition
                listener.onClick(position)
            }

        }
    }

    fun update() = notifyDataSetChanged()

    interface PositionListener{
        fun onClick(int: Int)
    }
}