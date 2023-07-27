package com.example.notepad.dateAndTime

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.appodeal.ads.Appodeal
import com.appodeal.ads.RewardedVideoCallbacks
import com.example.notepad.*
import com.example.notepad.adapters.ImageAdapter
import com.example.notepad.databinding.ActivityCalendarMainBinding
import com.example.notepad.databinding.ActivityCalendarMainBinding.inflate
import java.util.*


class CalendarMainActivity : AppCompatActivity(),ImageAdapter.PositionListener {

    private lateinit var bindingActivityCalendarMain: ActivityCalendarMainBinding
    private val nowDate = Calendar.getInstance() // дата сейчас
    private val converterDate =  ConverterDate() // конвертируем дату
    lateinit var imageAdapter: ImageAdapter
    lateinit var sharedPreferencesBalls: SharedPreferences  // сохранение количества баллов
    lateinit var sharedPreferencesBoolean: SharedPreferences // список открытых и закрытых тем
    var balls = 0 // баллы за рекламу

    lateinit var sharedPrefTheme: SharedPrefTheme // сохранённая тема

    val listTheme = mutableListOf( // список тем
        ListItem(R.drawable.lo, "Стандартная тема", false),
        ListItem(R.drawable.color10,"Барби тема",false),
        ListItem(R.drawable.color2,"Светлая тема",false),
        ListItem(R.drawable.color3,"Золотая тема",false),
        ListItem(R.drawable.color4,"Фиолетовая тема",false),
        ListItem(R.drawable.color5,"Синяя тема",false),
        ListItem(R.drawable.color6,"Оранжевая тема",false),
        ListItem(R.drawable.color7,"Зелёная тема",false),
        ListItem(R.drawable.color8,"Коричневая тема",false),
        ListItem(R.drawable.color9,"Серая тема",false),
        ListItem(R.drawable.color11,"Мятная тема",false)
    )
    var listOpenAndClose = mutableListOf(
        ListOpenAndClosed(false),ListOpenAndClosed(false),
        ListOpenAndClosed(false),ListOpenAndClosed(false),
        ListOpenAndClosed(false),ListOpenAndClosed(false),
        ListOpenAndClosed(false),ListOpenAndClosed(false),
        ListOpenAndClosed(false),ListOpenAndClosed(false),
        ListOpenAndClosed(false)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // смена темы
        sharedPrefTheme = SharedPrefTheme(this)
        sharedPrefTheme.theme()

        bindingActivityCalendarMain = inflate(layoutInflater)

        setContentView(bindingActivityCalendarMain.root)
        Appodeal.initialize(this, "d32bb1905398d2ca6a3def64a03461359e21598fd25b485c", Appodeal.REWARDED_VIDEO,true)

        sharedPreferencesBalls = getSharedPreferences("TABLE", Context.MODE_PRIVATE)
        sharedPreferencesBoolean = getSharedPreferences("BOOLEAN", Context.MODE_PRIVATE) // открыто или

        balls = sharedPreferencesBalls.getInt("counter",0)

        if (balls >= 20){
            listOpenAndClose.forEach { it.boolean = true }
            imageAdapter = ImageAdapter(listTheme,listOpenAndClose,this)
        } // все темы открыты
        else {
            booleanChange() // изменяем список сразу если в префенс есть значения true
            imageAdapter = ImageAdapter(listTheme, listOpenAndClose, this)
        }

        bindingActivityCalendarMain.apply {
            calendarMain.minDate = nowDate.time.time
            calendarMain.maxDate = nowDate.time.time + 8640000000 // range to 100 days

            val message = intent.getStringExtra(Constants.DATE_STRING)
            calendarMain.date = converterDate.convertString(message.toString())

            calendarMain.setOnDateChangeListener { _, year, month, dayOfMonth ->
                val dateText = "$dayOfMonth/${month + 1}/$year"
                val intentDate = Intent()
                intentDate.putExtra(Constants.DATE_LONG, converterDate.convertString(dateText))
                intentDate.putExtra(Constants.DATE_STRING_CALENDAR, dateText)
                setResult(RESULT_OK, intentDate)
                finish()
            }
            // запускаем рекламу при нажатии на кнопку
            buttonAds.setOnClickListener {
                Appodeal.show(this@CalendarMainActivity, Appodeal.REWARDED_VIDEO)
            }

            rcSpinner.adapter = imageAdapter
            rcSpinner.layoutManager =
                LinearLayoutManager(this@CalendarMainActivity, LinearLayoutManager.HORIZONTAL, false)

            tvBalls.text = if (balls == 0) getString(R.string.text_selected_theme)
                           else getString(R.string.text_item_balls,balls)
        }
    }

    override fun onResume() {
        super.onResume()

        // жизненный цикл видео - рекламы
        Appodeal.setRewardedVideoCallbacks(object : RewardedVideoCallbacks {
            override fun onRewardedVideoLoaded(isPrecache: Boolean) {
                Toast.makeText(this@CalendarMainActivity,"загрузка видео",Toast.LENGTH_LONG).show()
                // Вызывается при загрузке видео с вознаграждением
            }

            override fun onRewardedVideoFailedToLoad() {
                Toast.makeText(this@CalendarMainActivity,"Не удалось загрузить",Toast.LENGTH_LONG).show()
                // Вызывается, когда видео с вознаграждением не удалось загрузить
            }

            override fun onRewardedVideoShown() {
                Toast.makeText(this@CalendarMainActivity,"показ видео с вознаграждением",Toast.LENGTH_LONG).show()
                // Вызывается при показе видео с вознаграждением
            }

            override fun onRewardedVideoShowFailed() {
                Toast.makeText(this@CalendarMainActivity,"сбой видеопоказа",Toast.LENGTH_LONG).show()
                // Вызывается при сбое видеопоказа с вознаграждением
            }

            override fun onRewardedVideoClicked() {
                Toast.makeText(this@CalendarMainActivity,"clicked",Toast.LENGTH_LONG).show()
                // Called when rewarded video is clicked
            }

            override fun onRewardedVideoFinished(amount: Double, name: String?) {
                Toast.makeText(this@CalendarMainActivity,"просматривается до конца",Toast.LENGTH_LONG).show()
                balls+=10
                bindingActivityCalendarMain.tvBalls.text = getString(R.string.text_item_balls,balls)
                savePref(balls) // сохраним значение в SharedPreferences
                if (balls >= 20){
                    listOpenAndClose.forEach { it.boolean = true } // изменяем все значения прямо в исходных элементах
                    imageAdapter.update()
                }
            }

            override fun onRewardedVideoClosed(finished: Boolean) {
                Toast.makeText(this@CalendarMainActivity,"Закрыто",Toast.LENGTH_LONG).show()
                // Закрыто
            }

            override fun onRewardedVideoExpired() {
                Toast.makeText(this@CalendarMainActivity,"срок действия",Toast.LENGTH_LONG).show()
                // срок действия вознагражденного видео истек
            }
        })
    }

    // функция для показа всех заметок
    fun allNote(@Suppress("UNUSED_PARAMETER")view: View) {
        val intentDate = Intent()
        intentDate.putExtra(Constants.DATE_LONG, 0L)
        intentDate.putExtra(Constants.DATE_STRING_CALENDAR, getString(R.string.all_note))
        setResult(RESULT_OK, intentDate)
        finish()
    }

    // сохраняем значение баллов в SharedPreferences
    fun savePref(res: Int){
        val editor = sharedPreferencesBalls.edit()
        editor?.putInt("counter",res)
        editor?.apply()
    }

    // сохраняем boolean значение баллов в SharedPreferencesBoolean
    fun savePrefBoolean(){
        val serialized = listOpenAndClose.map { if (it.boolean) '1' else '0' }.joinToString("") // преобразует каждое значение в определенный символ (создаем строку)
        val editor = sharedPreferencesBoolean.edit()
        editor?.putString("string",serialized )
        editor?.apply()
    }


    // изменение тем при клике
    override fun onClick(int: Int) {
        when {
            int == sharedPrefTheme.intTheme && listOpenAndClose[int].boolean
            -> Toast.makeText(this, "Тема выбрана", Toast.LENGTH_LONG).show()
            balls >= 20 -> {
                when(int){
                    0 -> sharedPrefTheme.savePref(0)
                    1 -> sharedPrefTheme.savePref(1)
                    2 -> sharedPrefTheme.savePref(2)
                    3 -> sharedPrefTheme.savePref(3)
                    4 -> sharedPrefTheme.savePref(4)
                    5 -> sharedPrefTheme.savePref(5)
                    6 -> sharedPrefTheme.savePref(6)
                    7 -> sharedPrefTheme.savePref(7)
                    8 -> sharedPrefTheme.savePref(8)
                    9 -> sharedPrefTheme.savePref(9)
                    10 -> sharedPrefTheme.savePref(10)
                }
                recreate()
                updateTheme(int)
            }
            listOpenAndClose[int].boolean -> {
                sharedPrefTheme.savePref(int)
                recreate()
                imageAdapter.update()
            }
            else -> Toast.makeText(this, "Нужно 20 баллов, у вас: $balls", Toast.LENGTH_LONG).show()
        }
    }

    // обновляем UI после выбора
    fun updateTheme(int: Int){
        balls -= 20
        savePref(balls)
        bindingActivityCalendarMain.tvBalls.text = getString(R.string.text_item_balls,balls)
        booleanChange()
        listOpenAndClose[int].boolean = true
        savePrefBoolean()
        booleanChange()
        imageAdapter.update()
    }

    //  вытаскиваем из префернс боолеан значения если оно не пустое
    private fun booleanChange() {
        val  check = sharedPreferencesBoolean.getString("string", "1") // проверяем есть ли открытые темы в sharedPreferens
        if(check != "1") {
            val deserialized = sharedPreferencesBoolean.getString("string", "1")?.map { it == '1' }
            listOpenAndClose.clear()
            deserialized?.forEach {
                listOpenAndClose.add(ListOpenAndClosed(it))
            }
        }else listOpenAndClose.forEach { it.boolean = false }
    }
}