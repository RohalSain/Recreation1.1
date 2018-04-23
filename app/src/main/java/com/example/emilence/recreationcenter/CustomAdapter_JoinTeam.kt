package com.example.emilence.recreationcenter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.emilence.petapp2.Interface.RedditAPI
import kotlinx.android.synthetic.main.join_team_list.view.*
import kotlinx.android.synthetic.main.players_add.view.*
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import java.io.IOException
import java.util.HashMap

class CustomAdapter_JoinTeam(context: Context, joinList: ArrayList<String>): RecyclerView.Adapter<CustomAdapter_JoinTeam.ViewHolder>()
{
    val intent: Intent = Intent()
    var context: Context?=null
    var joinList: ArrayList<String>
    var i:Int=0
    var session:Session?=null
    var teamId:String?=null
    init {
        this.context=context
        this.joinList=joinList
        this.teamId=""
        this.session= Session(context)
    }
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int):ViewHolder {
        var v = LayoutInflater.from(parent?.context).inflate(R.layout.join_team_list,parent,false)

        return CustomAdapter_JoinTeam.ViewHolder(v)
    }
    override fun getItemCount(): Int {

        return joinList!!.size
    }
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        var i = position+1
        holder?.t1?.text="Team "+i
        holder?.b1?.setOnClickListener()
        {
            teamId = joinList!!.get(position)
            var token:String=session!!.gettoken()
            Log.d("ID",teamId)
            var client=ApiCall()
            var retrofit=client.retrofitClient()
            val retrofitAp = retrofit!!.create(RedditAPI::class.java)
            val headerMap = HashMap<String, String>()
            headerMap.put("teamId", teamId!!)
            var call = retrofitAp.getJoinTeam(token!!,teamId!!)
            call.enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>?) {
                    Log.d("server", "onResponse: Server Response: " + response.toString());

                    try {

                        var json:String?=null
                        json= response?.body()!!.string()
                        Log.d("JSON",json)
                        var jsonobj:JSONObject= JSONObject(json)
                        var success=jsonobj.getString("success")
                        if(success=="1") {
                            holder?.b1?.setText(R.string.requested)
                            holder?.b1?.setBackgroundColor(Color.TRANSPARENT)
                            holder?.b1?.setTextColor(Color.BLACK)
                            holder?.b1?.isEnabled = false
                        }else
                        {
                            Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show()
                        }
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
    }
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val t1 = itemView.JoinTeamName as TextView
        val b1 = itemView.JoinRequest
        val v1 = itemView.viewJoinTeam as View
    }

}