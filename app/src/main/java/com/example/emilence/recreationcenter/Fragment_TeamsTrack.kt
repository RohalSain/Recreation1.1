package com.example.emilence.recreationcenter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment__my_teams.*
import kotlinx.android.synthetic.main.fragment__notification.*
import kotlinx.android.synthetic.main.fragment__teams_track.*
// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
/**
 * A simple [Fragment] subclass.
 *
 */
class Fragment_TeamsTrack : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__teams_track, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val b =arguments
        var  teamTracksList = b?.getString("TeamTracksList")
        var teamTracksSize = b?.getInt("TeamTracksLength")
        listTeamsTracks.layoutManager= StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        val adp1=CustomAdapter_TeamsTracks(teamTracksSize!!,teamTracksList!!)
        listTeamsTracks.adapter=adp1
    }


}
