package com.example.notepad

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.notepad.db.MyDbManager
import java.util.*

// сохранение запущенной темы
class SharedPrefTheme(val context: Context){

    var sharedPreferences: SharedPreferences = context.getSharedPreferences("TABLES", Context.MODE_PRIVATE)
    var intTheme = sharedPreferences.getInt("counter",0)  // тема которая сейчас

    // перезаписывание темы
    fun savePref(theme: Int){
        val editor = sharedPreferences.edit()
        editor?.putInt("counter",theme)
        editor?.apply()
    }

    // применение темы
    fun theme(){
        intTheme = sharedPreferences.getInt("counter",0)
        context.apply {
            when(intTheme){
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
    }

    val myDbManager = MyDbManager(context)

    val textSend = getText("0")
    val tag = "myLogs"

    fun getText(str:String):String {
        Log.d(tag,"вызван")
        myDbManager.openDb()
        val list = myDbManager.readDateAndText(str)
        Log.d(tag,"$list")
        val newDate: Calendar = Calendar.getInstance()
        if (list.isNotEmpty()) {
            for (i in list) {
                if (i.date + i.time > newDate.time.time) return i.text
            }
        }
        myDbManager.closeDb()
        return "Нет заметок"
    }
}