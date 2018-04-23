package com.example.emilence.recreationcenter


import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.emilence.petapp2.Interface.RedditAPI
import kotlinx.android.synthetic.main.activity__container.*
import kotlinx.android.synthetic.main.fragment__home.*
import kotlinx.android.synthetic.main.fragment__my_team1.view.*
import kotlinx.android.synthetic.main.fragment__my_teams.*
import kotlinx.android.synthetic.main.fragment__notification.*
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import java.io.IOException
import java.util.HashMap


/**
 * A simple [Fragment] subclass.
 */
class Fragment_MyTeams : Fragment() {
    var session:Session?=null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment__my_teams, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        session = Session(context)
        val b = arguments
        var cName = b?.getString("captain")
        var tName = b?.getString("teamName")
        var tId = b?.getString("teamId")
        Log.d("TEAM", tName)
        var f = Fragment_MyTeam1()
        listMyTeams.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        val adp1 = CustomAdapter_MyTeam(context, tName, tId,
                object : CustomAdapter_MyTeam.OnItemClickListener {
                    override fun onItemClick(position: Int) {
                        Log.d("item", "item clicked")
                        var client = ApiCall()
                        var retrofit = client.retrofitClient()
                        val retrofitAp = retrofit!!.create(RedditAPI::class.java)
                        var token = session?.gettoken()
                        var teamId = session?.getteamId()
                        val headerMap = HashMap<String, String>()
                        headerMap.put("teamId", teamId!!)
                        Log.d("TEAMID", teamId)
                        var call = retrofitAp.getSingleTeam(token!!, teamId)
                        call.enqueue(object : retrofit2.Callback<ResponseBody> {
                            override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>?) {
                                Log.d("server", "onResponse: Server Response: " + response.toString());

                                try {
                                    var json: String? = null
                                    json = response?.body()!!.string()
                                    Log.d("JSON", json)
                                    var obj = JSONObject(json)
                                    var data = obj.getJSONObject("data")
                                    Log.d("DATA", data.toString())
                                    var captainName = data.getString("captainName")
                                    var teamName = data.getString("teamName")
                                    var teamId = data.getString("_id")
                                    Log.d("CAPTAIN", captainName)
                                    Log.d("TeamName", teamName)
                                    Log.d("TeamId", teamId)
                                    val fpa = Fragment_MyTeam1()
                                    val b = Bundle()
                                    b.putString("captain", captainName)
                                    b.putString("teamName", teamName)
                                    b.putString("teamId", teamId)
                                    fpa.setArguments(b)
                                    val fm = fragmentManager
                                    val transaction = fm?.beginTransaction();
                                    transaction?.setCustomAnimations(R.anim.left_enter, R.anim.right_exit, R.anim.right_enter, R.anim.left_exit);
                                    transaction?.replace(R.id.Container, fpa);
                                    transaction?.addToBackStack(tag);
                                    transaction?.commit();

                                } catch (e: JSONException) {
                                    Log.e("JSONException", "onResponse: JSONException: " + e);
                                } catch (e: IOException) {
                                    Log.e("IOexception", "onResponse: JSONException: ");
                                }
                            }

                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                Log.e("OnFailure", "onFailure: Something went wrong: ")
                                //Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        })
                    }
                })
        listMyTeams.adapter = adp1
    }
}
