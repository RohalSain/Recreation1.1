package com.example.emilence.recreationcenter

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
//import javax.swing.UIManager.put
import android.content.ContentValues
import android.util.Log


/**
 * Created by emilence on 29/3/18.
 */
class DBHelper(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {
    val TAG = "DbHelper"
    val DB_NAME = "myapp.db"
    val DB_VERSION = 1

    val USER_TABLE = "users"
    val COLUMN_ID = "_id"
    val COLUMN_EMAIL = "email"
    val COLUMN_PASS = "password"

    val CREATE_TABLE_USERS = ("CREATE TABLE " + USER_TABLE + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_EMAIL + " TEXT,"
            + COLUMN_PASS + " TEXT);")


    override fun onCreate(db: SQLiteDatabase?)
    {
        db!!.execSQL(CREATE_TABLE_USERS);
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        onCreate(db);
        }

    fun addUser(email: String, password: String) {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(COLUMN_EMAIL, email)
        values.put(COLUMN_PASS, password)

        val id = db.insert(USER_TABLE, null, values)
        db.close()

        Log.d(TAG, "user inserted$id")
    }

    fun getUser(email: String, pass: String): Boolean {
        //HashMap<String, String> user = new HashMap<String, String>();
        val selectQuery = "select * from  " + USER_TABLE + " where " +
                COLUMN_EMAIL + " = " + "'" + email + "'" + " and " + COLUMN_PASS + " = " + "'" + pass + "'"

        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        // Move to first row
        cursor.moveToFirst()
        if (cursor.count > 0) {

            return true
        }
        cursor.close()
        db.close()

        return false
    }
}