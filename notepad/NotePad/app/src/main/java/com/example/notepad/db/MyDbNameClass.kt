package com.example.notepad.db

import android.provider.BaseColumns

object MyDbNameClass{
    const val TABLE_NAME = "my_table"
    const val COLUMN_NAME_TITLE = "title"
    const val COLUMN_NAME_CONTENT = "content"
    const val COLUMN_NAME_DATE = "long"
    const val COLUMN_NAME_TIME = "int"
    const val COLUMN_NAME_DATE_AND_TIME = "dateAndTime"

    const val DATABASE_VERSION = 1
    const val DATABASE_NAME = "MyLessonDb.db"

    const val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY, " +
            "$COLUMN_NAME_TITLE TEXT, " +
            "$COLUMN_NAME_CONTENT TEXT, " +
            "$COLUMN_NAME_DATE INTEGER, " +
            "$COLUMN_NAME_TIME INTEGER, " +
            "$COLUMN_NAME_DATE_AND_TIME INTEGER)"

    const val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
}