package com.example.emilence.recreationcenter


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment__create_profile.*


/**
 * A simple [Fragment] subclass.
 */
 class Fragment_CreateProfile:Fragment() {


public override fun onCreateView(inflater:LayoutInflater?, container:ViewGroup?,
savedInstanceState:Bundle?):View? {
 // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment__create_profile, container, false)
}

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        done.setOnClickListener()
        {
            val transaction = fragmentManager?.beginTransaction();
            transaction?.setCustomAnimations(R.anim.right_enter, R.anim.left_exit, R.anim.left_enter, R.anim.right_exit);
            transaction?.replace(R.id.Container,Fragment_Profile());
            transaction?.addToBackStack(tag);
            transaction?.commit();
        }
    }

}// Required empty public constructor
