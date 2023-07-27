package com.example.notepad.activity

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appodeal.ads.Appodeal
import com.example.notepad.*
import com.example.notepad.adapters.MainAdapter
import com.example.notepad.databinding.ActivityMainBinding
import com.example.notepad.dateAndTime.ActivityCalendar
import com.example.notepad.dateAndTime.CalendarMainActivity
import com.example.notepad.db.MyDbManager
import com.example.notepad.viewModel.ActivityMainVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*


class MainActivity : AppCompatActivity(){

    private var launcher: ActivityResultLauncher<Intent>? = null
    private lateinit var bindingMain: ActivityMainBinding
    lateinit var sharedPrefTheme: SharedPrefTheme
    private var job: Job ?= null

    val myDbManager = MyDbManager(this)
    val mainAdapter = MainAdapter(ArrayList(),this)

    var count = 0  // значение начальной темы ( для смены темы при нажатии кнопки назад в "Все заметки"

    private var listDateTime = mutableListOf<Long>()

    private val activityMainVM: ActivityMainVM by lazy {
        ViewModelProvider(this)[ActivityMainVM::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // смена темы
        sharedPrefTheme = SharedPrefTheme(this)
        sharedPrefTheme.theme()
        count = SharedPrefTheme(this).sharedPreferences.getInt("counter",0)

        bindingMain = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingMain.root)
        Appodeal.initialize(this, "d32bb1905398d2ca6a3def64a03461359e21598fd25b485c",
            Appodeal.BANNER,true)
        Appodeal.setBannerViewId(R.id.appodealBannerView)

        bindingMain.addDateMain.text = activityMainVM.btDateText

        launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == RESULT_OK) {
                    activityMainVM.dateLongSendManagerBd = result.data?.getLongExtra(Constants.DATE_LONG, 0)!!

                    activityMainVM.btDateText = result.data?.getStringExtra(Constants.DATE_STRING_CALENDAR)!!
                    bindingMain.addDateMain.text = activityMainVM.btDateText
                    activityMainVM.dateTextInLongSendCalendarMain =  if(bindingMain.addDateMain.text != "все заметки") activityMainVM.btDateText else activityMainVM.text
                    initSearchView()
                }
            }
        init()
    }

    override fun onResume() {
        super.onResume()
        myDbManager.openDb()
        fillAdapter("${activityMainVM.dateLongSendManagerBd}")
        sharedPrefTheme.theme()
        if (count != sharedPrefTheme.intTheme) recreate()

        Appodeal .show (this, Appodeal .BANNER_BOTTOM)
    }

    override fun onDestroy() {
        super.onDestroy()
        Appodeal.destroy(Appodeal.BANNER)
        myDbManager.closeDb()
    }

    fun calendar(@Suppress("UNUSED_PARAMETER")view: View) {
        val intent = Intent(this, CalendarMainActivity::class.java)
        intent.putExtra(Constants.DATE_STRING,activityMainVM.dateTextInLongSendCalendarMain)
        launcher?.launch(intent)
    }

    fun onClickCalendar(@Suppress("UNUSED_PARAMETER")view2: View) {
        val i = Intent(this, ActivityCalendar::class.java)
        startActivity(i)
    }

    private fun init(){
        val swapHelper = getSwapMg()
        swapHelper.attachToRecyclerView(bindingMain.rcView)

        bindingMain.rcView.layoutManager = LinearLayoutManager(this)
        bindingMain.rcView.adapter = mainAdapter
    }

    private fun fillAdapter(text:String){
        job?.cancel()
        job = CoroutineScope(Dispatchers.Main).launch {
            val list = myDbManager.readDbData(text)
            mainAdapter.updateAdapter(list)

            bindingMain.tvNoElements.visibility = if (list.size > 0) View.GONE
            else View.VISIBLE
        }
        listDateTime = myDbManager.readDbDataTime(text)

        arrayListDate()

        val intent = Intent(this, TesstAppWidget::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE

        val ids: IntArray = AppWidgetManager.getInstance(application)
            .getAppWidgetIds(ComponentName(application, TesstAppWidget::class.java))
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        sendBroadcast(intent)
    }

    private fun getSwapMg(): ItemTouchHelper{
        return ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) { // Dialog show
                DeleteItem.showDialog(this@MainActivity, object : DeleteItem.Listener {
                    override fun onClick(boolean: Boolean) {
                        if (boolean) mainAdapter.removeItem(viewHolder.adapterPosition, myDbManager)
                        else mainAdapter.removeItemNoDelete(viewHolder.adapterPosition)
                    }
                })
            }
        })
    }
    private fun initSearchView(){
        fillAdapter("${activityMainVM.dateLongSendManagerBd}")
    }

    private fun arrayListDate(){ // check time for notification
        val calendar: Calendar = Calendar.getInstance()
        val list = listDateTime.sorted()
        val listNewDate = mutableListOf<Long>()
        if(list.isNotEmpty()) {
            list.forEach {
                if (it >= calendar.time.time) listNewDate.add(it)
            }
            if (listNewDate.isNotEmpty()) initOnAlarm(listNewDate[0])
        }
    }

    companion object {
        const val ALARM_REQUEST_CODE = 1000
    }

    @SuppressLint("InlinedApi")
    private fun initOnAlarm(long: Long) { // show the notification at the specified time

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, ALARM_REQUEST_CODE,
            intent, PendingIntent.FLAG_IMMUTABLE)

        alarmManager.setExact(AlarmManager.RTC_WAKEUP,long,pendingIntent)
    }
}