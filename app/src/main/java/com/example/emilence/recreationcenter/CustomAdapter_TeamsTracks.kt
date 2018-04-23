package com.example.emilence.recreationcenter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.list_teams_tracks.view.*
import kotlinx.android.synthetic.main.playing_list.view.*
import kotlinx.android.synthetic.main.waiting_list.view.*
import org.json.JSONArray

class CustomAdapter_TeamsTracks(var teamTracksSize:Int,var teamTracksList:String): RecyclerView.Adapter<CustomAdapter_TeamsTracks.ViewHolder>() {
    var s:Int?=null
    var tracksList:String?=null
    var trackListArray: JSONArray?=null
    init {
        this.s=teamTracksSize
        this.tracksList=teamTracksList
        this.trackListArray= JSONArray(teamTracksList)
    }
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {

        val v= LayoutInflater.from(parent?.context).inflate(R.layout.list_teams_tracks,parent,false)
        return CustomAdapter_TeamsTracks.ViewHolder(v)
          }

    override fun getItemCount(): Int {
        return s!!
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.t1?.text="Team 1 wins"
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    {
        val t1=itemView.wins
        val t2 = itemView.vs
        val img = itemView.imgTeamsTrack
        val v1 = itemView.viewTeamsTracks
    }
}