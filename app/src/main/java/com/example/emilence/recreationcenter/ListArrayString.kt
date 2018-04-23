package com.example.emilence.recreationcenter

import android.content.Context

class ListArrayString private constructor() {
    init {
        println("This ($this) is a singleton")
    }

    private object Holder {
        val INSTANCE = ListArrayString()
    }

    companion object {
        val instance:ListArrayString by lazy { Holder.INSTANCE }
    }


    var context: Context?=null
    var LAS:ArrayList<String> = ArrayList()
}


