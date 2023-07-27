package com.example.notepad

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.Toast
import com.example.notepad.activity.MainActivity

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in [TesstAppWidgetConfigureActivity]
 */
class TesstAppWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // обновление виджетов ( все что есть )
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        // When the user deletes the widget, delete the preference associated with it.
        for (appWidgetId in appWidgetIds) {
            deleteTitlePref(context, appWidgetId)
        }
    }

    // при создании виджета
    override fun onEnabled(context: Context) {
        Toast.makeText(context,"Проверка связи 2",Toast.LENGTH_LONG).show()
    }

    // при удалении виджета
    override fun onDisabled(context: Context) {
        Toast.makeText(context,"Проверка связи 1",Toast.LENGTH_LONG).show()
    }
}


internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {

    val sharedPref = SharedPrefTheme(context)

    val widgetText = loadTitlePref(context, appWidgetId)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.tesst_app_widget)
    val intent = Intent(context,MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(context,0,intent,0)
    views.setTextViewText(R.id.appwidget_text, widgetText)
    views.setTextViewText(R.id.appwidget_textr, sharedPref.textSend)
    Toast.makeText(context,"Виджет обновлён",Toast.LENGTH_LONG).show()
    views.setOnClickPendingIntent(R.id.appwidget_textr,pendingIntent)
    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}
