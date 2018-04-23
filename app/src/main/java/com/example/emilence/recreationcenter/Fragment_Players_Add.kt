package com.example.emilence.recreationcenter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment__notification.*
import kotlinx.android.synthetic.main.fragment_fragment__players__add.*
import org.json.JSONArray
import org.json.JSONException
import android.content.Intent.getIntent
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class Fragment_Players_Add : Fragment() {
    var plsize:Int? = null
    var idarray:String=""
    var ids:String=""
    var c:Bundle?=null
    var ctx:Context?=null
    val fpa = Fragment_CreateTeam()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_fragment__players__add, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val b =arguments
      var  listplayer = b?.getString("playerlist")
      Log.d("LIST_PLAYER",listplayer.toString())
        var pl = JSONArray(listplayer)
        Log.d("LIST_OBJECT",pl.toString())
        plsize = pl.length()
        Log.d("LIST_SIZE",plsize.toString())
        ctx=activity.applicationContext
        listPlayersAdd.layoutManager= StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        val adp1=CustomAdapter_AddPlayer(context,pl,idarray,ctx!!,fpa)
        listPlayersAdd.adapter=adp1
    Back_CreatePlayer.setOnClickListener()
    {

        val fm = fragmentManager
        val count = fm.backStackEntryCount
        var c=count-1

            fm.popBackStack()


    }
    }
}
