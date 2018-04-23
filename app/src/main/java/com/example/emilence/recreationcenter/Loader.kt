package com.example.emilence.recreationcenter

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View

/**
 * Created by emilence on 29/3/18.
 */
class Loader {
    var Dialog: Dialog?=null
    var loaderContext: Context?=null
    fun ShowLoader(context: Context)
    {
        loaderContext=context
        var builder: AlertDialog.Builder = AlertDialog.Builder(context)
        var inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var view: View = inflater.inflate(R.layout.fragment__loader, null)
        builder.setView(view)
        Dialog=builder.create()
        Dialog?.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        (Dialog as AlertDialog?)?.show()
    }
    fun HideLoader()
    {
        Dialog?.dismiss()
    }
}