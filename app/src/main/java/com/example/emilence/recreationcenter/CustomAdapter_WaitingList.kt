package com.example.emilence.recreationcenter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.waiting_list.view.*
import org.json.JSONArray

/**
 * Created by emilence on 27/3/18.
 */
class CustomAdapter_WaitingList(var n:Int,var wl:String):RecyclerView.Adapter<CustomAdapter_WaitingList.ViewHolder>() {
    var s:Int?=null
    var waitList:String?=null
    var waitListArray:JSONArray?=null
    init {
        this.s=n
        this.waitList=wl
        this.waitListArray= JSONArray(waitList!!)
    }
    override fun getItemCount(): Int {
        return s!!
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {

        holder?.t1?.text=waitListArray!!.getJSONObject(position)!!.getString("teamName")

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {

        val v= LayoutInflater.from(parent?.context).inflate(R.layout.waiting_list,parent,false)
        return ViewHolder(v)
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    {
        val t1=itemView.waitingTeam_Text
        val v1 = itemView.view


    }


}