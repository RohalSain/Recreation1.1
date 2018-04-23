package com.example.emilence.recreationcenter


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment__current_match.*
import kotlinx.android.synthetic.main.fragment__team_1.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


/**
 * A simple [Fragment] subclass.
 */
class Fragment_Team_1 : Fragment() {

    var WaitingNumber:Int? = null
    var WaitingList: JSONArray?=null
    var Team1: JSONObject?= null
    var Team2: JSONObject?= null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment__team_1, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val b =arguments
        WaitingNumber = b?.getInt("WaitingNumber")
        var wl=b?.getString("WaitingTeams")
        var t1=b?.getString("TeamOne")
        var t2=b?.getString("TeamTwo")
        try {
            WaitingList= JSONArray(wl)
            Team1= JSONObject(t1)
            Team2=JSONObject(t2)
        }catch (e: JSONException)
        {
            Log.d("JSONEXCEPTION",e.toString())
        }

        playingPlayersList.layoutManager= StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        val adp=CustomAdapter_PlayingList()
        playingPlayersList.adapter=adp

        BenchPlayersList.layoutManager= StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)

        val adp1=CustomAdapter_BenchPlayers()
       BenchPlayersList.adapter=adp1

        Back_CurrentTeam1.setOnClickListener()
        {
            val fpa = Fragment_CurrentMatch()
            val b = Bundle()
            b.putInt("WaitingNumber",WaitingNumber!!)
            b.putString("WaitingTeams",wl)
            b.putString("TeamOne",Team1.toString())
            b.putString("TeamTwo",Team2.toString())
            fpa.setArguments(b)
            val fm = fragmentManager
            val count = fm.backStackEntryCount
            fm.popBackStack()

        }
    }

}// Required empty public constructor
