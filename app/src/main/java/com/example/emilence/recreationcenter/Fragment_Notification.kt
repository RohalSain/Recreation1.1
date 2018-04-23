package com.example.emilence.recreationcenter


import android.app.AlertDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment__notification.*
import kotlinx.android.synthetic.main.fragment__team_1.*
import kotlinx.android.synthetic.main.notification_list.*


/**
 * A simple [Fragment] subclass.
 */
class Fragment_Notification : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment__notification, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
         listNotification.layoutManager= StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        val adp1=CustomAdapter_NotificationList(context)
       listNotification.adapter=adp1
        Back_HomeNotification.setOnClickListener()
        {
            val fm = fragmentManager // or 'getSupportFragmentManager();'
            val count = fm.backStackEntryCount
            for (i in 0 until count) {
                fm.popBackStack()
            }
        }

    }
}// Required empty public constructor
