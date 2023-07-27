package com.example.notepad.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notepad.MyOnClickListener
import com.example.notepad.R
import com.example.notepad.SharedPrefTheme
import com.example.notepad.databinding.ModelRecyclerviewTimeBinding
import com.example.notepad.dateAndTime.SelectedTime

class CalendarAdapter(private val onClickListener: MyOnClickListener) :
    RecyclerView.Adapter<CalendarAdapter.CalendarHolder>() {

    private val listTime = ArrayList<SelectedTime>()

    lateinit var sharedPrefTheme: SharedPrefTheme

    inner class CalendarHolder(item: View) : RecyclerView.ViewHolder(item) {

        val bindingModelRc = ModelRecyclerviewTimeBinding.bind(item)

        fun bind(selectedTime: SelectedTime) {
            bindingModelRc.txTime.text = selectedTime.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarHolder {

        sharedPrefTheme = SharedPrefTheme(parent.context)

        parent.context.apply {
            when (sharedPrefTheme.intTheme) {
                0 -> setTheme(R.style.Theme_NotePad)
                1 -> setTheme(R.style.Theme_NotePadPink)
                2 -> setTheme(R.style.Theme_NotePadWhite)
                3 -> setTheme(R.style.Theme_NotePadGold)
                4 -> setTheme(R.style.Theme_NotePadViolet)
                5 -> setTheme(R.style.Theme_NotePadBlue)
                6 -> setTheme(R.style.Theme_NotePadOrange)
                7 -> setTheme(R.style.Theme_NotePadGreen)
                8 -> setTheme(R.style.Theme_NotePadBrown)
                9 -> setTheme(R.style.Theme_NotePadGreyDark)
                10 -> setTheme(R.style.Theme_NotePadMint)
            }
        }

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.model_recyclerview_time, parent, false)
        return CalendarHolder(view)
    }

    override fun onBindViewHolder(holder: CalendarHolder, position: Int) {

        holder.bind(listTime[position])
        holder.bindingModelRc.txTime.setOnClickListener {
            onClickListener.onClicked(position)
        }
    }

    override fun getItemCount(): Int = listTime.size

    fun addTimeCalendar(selectedTime: SelectedTime) =  listTime.add(selectedTime)
}