package com.example.notepad.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notepad.*
import com.example.notepad.activity.EditActivity
import com.example.notepad.dateAndTime.ConverterDate
import com.example.notepad.dateAndTime.ListDate
import com.example.notepad.db.MyDbManager
import java.util.*
import kotlin.collections.ArrayList

class MainAdapter(listMain:ArrayList<RcMainModel>, contextMain:Context): RecyclerView.Adapter<MainAdapter.MainHolder>(){

    private var listArray = listMain
    private val contextM = contextMain

    class MainHolder(itemView: View, contextV:Context) : RecyclerView.ViewHolder(itemView) {

        private val converterDate =  ConverterDate()

        private val titleItem:TextView = itemView.findViewById(R.id.textView)
        private val descItem:TextView = itemView.findViewById(R.id.textDesc)
        private val dateItem:TextView = itemView.findViewById(R.id.textDate)
        private val timeItem:TextView = itemView.findViewById(R.id.textTime)
        private val constRc:ViewGroup = itemView.findViewById(R.id.constrainModelRcMain)
        private val textOld:TextView = itemView.findViewById(R.id.textOld)

        val context = contextV
        private val listDate = ListDate()
        private val newDate: Calendar = Calendar.getInstance()

        lateinit var sharedPrefTheme: SharedPrefTheme

        val list = mutableListOf<String>()

        @SuppressLint("SetTextI18n")
        fun setData(item: RcMainModel){

            titleItem.text = if (item.title.length > 15) item.title.substring(0,15) else item.title
            descItem.text = if (item.desc.length > 20) item.desc.substring(0,20) else item.desc

            sharedPrefTheme = SharedPrefTheme(context)

            if (item.date.plus(item.time) < newDate.time.time){

                textOld.visibility = View.VISIBLE
                textOld.text = "Заметка устарела"

                // смена темы
                when(sharedPrefTheme.intTheme){ // старая заметка
                    0 -> constRc.setBackgroundResource(R.color.red_dateOld)
                    1 -> constRc.setBackgroundResource(R.color.red_closed)
                    2 -> constRc.setBackgroundResource(R.color.red_dateOld)
                    3 -> constRc.setBackgroundResource(R.color.color_gold_buttonCloseImageTheme)
                    4 -> constRc.setBackgroundResource(R.color.color_violet_image_close)
                    5 -> constRc.setBackgroundResource(R.color.color_blue_image_close)
                    6 -> constRc.setBackgroundResource(R.color.color_orange_image_close)
                    7 -> constRc.setBackgroundResource(R.color.color_green_image_close)
                    8 -> constRc.setBackgroundResource(R.color.color_brown_image_close)
                    9 -> constRc.setBackgroundResource(R.color.color_greyDark_image_close)
                    10 -> constRc.setBackgroundResource(R.color.color_mint_image_close)
                }
            }
            else{
                constRc.setBackgroundResource(R.color.grey_darker)

                // смена темы
                when(sharedPrefTheme.intTheme){
                    0 -> constRc.setBackgroundResource(R.color.grey_darker)
                    1 -> constRc.setBackgroundResource(R.color.theme_color_pink)
                    2 -> constRc.setBackgroundResource(R.color.grey_dark)
                    3 -> constRc.setBackgroundResource(R.color.color_gold_button)
                    4 -> constRc.setBackgroundResource(R.color.color_violet_button)
                    5 -> constRc.setBackgroundResource(R.color.color_blue_button)
                    6 -> constRc.setBackgroundResource(R.color.color_orange_button)
                    7 -> constRc.setBackgroundResource(R.color.color_green_button)
                    8 -> constRc.setBackgroundResource(R.color.color_brown_button)
                    9 -> constRc.setBackgroundResource(R.color.color_greyDark_button)
                    10 -> constRc.setBackgroundResource(R.color.color_mint_button)
                }

                textOld.visibility = View.GONE
            }

            listDate.listDate.add(item.date)  // list date the note

            val sDate = converterDate.convertMillis(item.date)
            dateItem.text = sDate

            val sTime = converterDate.converterTimeInt(item.time)
            timeItem.text = "$sTime:00 - ${sTime+1}:00"

            itemView.setOnClickListener {
                val intent = Intent(context, EditActivity::class.java).apply {
                    putExtra(Constants.TITLE_KEY, item.title)
                    putExtra(Constants.DESC_KEY, item.desc)
                    putExtra(Constants.DATE_KEY, sDate)
                    putExtra(Constants.TIME_KEY, sTime)
                    putExtra(Constants.ID_KEY, item.id)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MainHolder(inflater.inflate(R.layout.model_recyclerview,parent,false), contextM)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) = holder.setData(listArray[position])

    override fun getItemCount(): Int = listArray.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(listItems:List<RcMainModel>){
        listArray.clear()
        listArray.addAll(listItems)
        notifyDataSetChanged()
    }

    fun removeItem(pos:Int, dbManager: MyDbManager){
        dbManager.removeItemFromDb(listArray[pos].id.toString())
        listArray.removeAt(pos)
        notifyItemRangeChanged(0,listArray.size)
        notifyItemRemoved(pos)
    }
    fun removeItemNoDelete(pos:Int){
        notifyItemRangeChanged(0,listArray.size)
        notifyItemRemoved(pos)
    }
}