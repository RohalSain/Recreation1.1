package com.example.emilence.recreationcenter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.playing_list.view.*
import kotlinx.android.synthetic.main.waiting_list.view.*

/**
 * Created by emilence on 27/3/18.
 */
class CustomAdapter_PlayingList: RecyclerView.Adapter<CustomAdapter_PlayingList.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder
    {
        val v= LayoutInflater.from(parent?.context).inflate(R.layout.playing_list,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int
    {
        return 5
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int)
    { var c = "Captain"
        if(position>0)
        {
            c = ""
        }
        var num = position+1

        holder?.t1?.text="Player "+ num
        holder?.t2?.text=c

    }
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    {
        val t1=itemView.playerNameText
        val t2 = itemView.playerCaptainText
        val v1 = itemView.view


    }
}