package com.example.emilence.recreationcenter


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment__join_teams.*
import org.json.JSONArray


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class Fragment_JoinTeams : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__join_teams, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val b =arguments
        var  joinList = b?.getStringArrayList("TeamIdList")
        Log.d("LIST_PLAYER",joinList.toString())
        listJoinTeams.layoutManager= StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        val adp1=CustomAdapter_JoinTeam(context,joinList!!)
        listJoinTeams.adapter=adp1
        Back_HomeJoinTeam.setOnClickListener()
        {

            val fm = fragmentManager
            val count = fm.backStackEntryCount

                fm.popBackStack()

        }
    }
}
