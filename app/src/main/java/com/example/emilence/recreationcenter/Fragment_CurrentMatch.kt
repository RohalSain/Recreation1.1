package com.example.emilence.recreationcenter


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment__current_match.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


/**
 * A simple [Fragment] subclass.
 */
class Fragment_CurrentMatch : Fragment() {
    var WaitingNumber:Int? = null
    var WaitingList:JSONArray?=null
    var Team1:JSONObject?= null
    var Team2:JSONObject?= null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment__current_match, container, false)
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
            var t1Name:String?=Team1?.getString("teamName")
            var t2Name:String?=Team2?.getString("teamName")
            team1_CurrentMatch.text=t1Name
            team2_CurrentMatch.text=t2Name
        }catch (e:JSONException)
        {
            Log.d("JSONEXCEPTION",e.toString())
        }

        Log.d("WAITING LIST NUMBER",WaitingNumber.toString())
        teamsWaiting_Number.text=WaitingNumber.toString()
       teamsWaitingList.layoutManager=StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL)
        val adp=CustomAdapter_WaitingList(WaitingNumber!!,wl!!)
        teamsWaitingList.adapter=adp
        Back_HomeCurrent.setOnClickListener()
        {
            val fm = fragmentManager
            val count = fm.backStackEntryCount
                fm.popBackStack()
        }
        team1_CurrentMatch.setOnClickListener()
        {
            val fpa = Fragment_Team_1()
            val b = Bundle()
            b.putInt("WaitingNumber",WaitingNumber!!)
            b.putString("WaitingTeams",wl)
            b.putString("TeamOne",Team1.toString())
            b.putString("TeamTwo",Team2.toString())
            fpa.setArguments(b)
            val fm = fragmentManager // or 'getSupportFragmentManager();'
            val transaction = fm?.beginTransaction();
            transaction?.setCustomAnimations(R.anim.left_enter, R.anim.right_exit, R.anim.right_enter, R.anim.left_exit);
            transaction?.replace(R.id.Container,fpa);
            transaction?.addToBackStack(tag)
            transaction?.commit();
        }
        team2_CurrentMatch.setOnClickListener()
        {

            val fpa = Fragment_Team2()
            val b = Bundle()
            b.putInt("WaitingNumber",WaitingNumber!!)
            b.putString("WaitingTeams",wl)
            b.putString("TeamOne",Team1.toString())
            b.putString("TeamTwo",Team2.toString())
            fpa.setArguments(b)
            val fm = fragmentManager // or 'getSupportFragmentManager();'
            val transaction = fm?.beginTransaction();
            transaction?.setCustomAnimations(R.anim.left_enter, R.anim.right_exit, R.anim.right_enter, R.anim.left_exit);
            transaction?.replace(R.id.Container,fpa);
            transaction?.addToBackStack(tag)
            transaction?.commit();
        }
    }

}// Required empty public constructor
