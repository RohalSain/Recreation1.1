package com.example.emilence.recreationcenter

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.support.constraint.R.id.parent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import kotlinx.android.synthetic.main.notification_list.view.*

import kotlinx.android.synthetic.main.playing_list.view.*
import kotlinx.android.synthetic.main.waiting_list.view.*

/**
 * Created by emilence on 30/3/18.
 */
class CustomAdapter_NotificationList(context: Context): RecyclerView.Adapter<CustomAdapter_NotificationList.ViewHolder>()
{
    var context: Context?=null
    init {
        this.context=context
    }
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder
    {
        var v = LayoutInflater.from(parent?.context).inflate(R.layout.notification_list,parent,false)

        return CustomAdapter_NotificationList.ViewHolder(v)
    }
    override fun getItemCount(): Int {
        return 8
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int)
    {
        var num = position+1

        holder?.t1?.text="User "+ num
        holder?.b1?.setOnClickListener()
        {
            val alertDilog = AlertDialog.Builder(context).create()
            alertDilog.setTitle("Alert")
            alertDilog.setMessage("Are you sure You want to Confirm?")
            alertDilog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", {
                dialogInterface, i ->


                alertDilog.cancel();
            })
            alertDilog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", {
                dialogInterface, i ->
                alertDilog.cancel();
            })
            alertDilog.show()
        }
        holder?.b2?.setOnClickListener()
        {
            val alertDilog = AlertDialog.Builder(context).create()
            alertDilog.setTitle("Alert")
            alertDilog.setMessage("Are you sure You want to Delete?")
            alertDilog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", {
                dialogInterface, i ->


                alertDilog.cancel();
            })
            alertDilog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", {
                dialogInterface, i ->
                alertDilog.cancel();
            })
            alertDilog.show()
        }

    }

  public  class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val t1 = itemView.notificationUserNAme as TextView
        val b1 = itemView.notificationConfirmButton
        val b2 = itemView.notificationDeleteButton as Button
        val img = itemView.notificationUserPic as com.facebook.drawee.view.SimpleDraweeView
        val v1 = itemView.viewNotification as View
}


}