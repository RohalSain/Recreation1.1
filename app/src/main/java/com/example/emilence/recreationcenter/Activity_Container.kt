package com.example.emilence.recreationcenter
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.app.Notification
import android.content.Context
import android.net.Uri
import android.opengl.Visibility
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import com.example.emilence.petapp2.Interface.RedditAPI
import com.facebook.drawee.backends.pipeline.Fresco
import kotlinx.android.synthetic.main.activity__container.*
import kotlinx.android.synthetic.main.fragment__home.*
import kotlinx.android.synthetic.main.waiting_list.*
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import java.io.IOException
import java.util.HashMap

class Activity_Container : AppCompatActivity() {
    private var session: Session? = null
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        Fresco.initialize(applicationContext)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__container)
if(Counter.instance.count==true)
{
    HomeText.text="Home"
    notificationHome1.visibility=View.VISIBLE
}
        session =  Session(this);
        var nameUser = session?.nameUser()
        var pic = session?.ProfilePicUrl()
        Log.d("NAME", nameUser)
        Log.d("USERPIC", pic)
        userPic_Side.setImageURI(Uri.parse(pic))
        name_Side.text = nameUser
       val mDrawerLayout = this.findViewById(R.id.Container) as DrawerLayout
       // on Back
        if(session!!.loggedin())
        {
            val fragment = Fragment_Home()
            val fragmentManager = supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.dashboard, fragment)
            transaction.commit()
        }else
        {

            val fragment = Fragment_Login()
            val fragmentManager = supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.Container, fragment)
            transaction.commit()
        }
        side_button1.setOnClickListener()
        {
            mDrawerLayout.openDrawer(Gravity.START)

        }
        notificationHome1.setOnClickListener()
        {

                var client = ApiCall()
                var retrofit = client.retrofitClient()
                val retrofitAp = retrofit!!.create(RedditAPI::class.java)
                var token = session?.gettoken()
                var call = retrofitAp.GetNotifications(token!!)
                call.enqueue(object : retrofit2.Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>?) {
                        Log.d("server", "onResponse: Server Response: " + response.toString());
                        try {
                            var json: String? = null
                            json = response?.body()!!.string()
                            Log.d("JSON", json)
                            var jsonobj: JSONObject = JSONObject(json)
                            var s = jsonobj.getString("success")
                            Log.d("SUCCESS", s)
                            if (s == "0") {
                                val fm = supportFragmentManager // or 'getSupportFragmentManager();'
                                val count = fm.backStackEntryCount
                                for (i in 0 until count) {
                                    fm.popBackStack()
                                }
                                val transaction = fm?.beginTransaction();
                                transaction?.setCustomAnimations(R.anim.left_enter, R.anim.right_exit, R.anim.right_enter, R.anim.left_exit);
                                transaction?.replace(R.id.dashboard, Fragment_NotificationNull());
                                transaction?.addToBackStack("abc")
                                transaction?.commit();
                            } else {
                                val fm = supportFragmentManager // or 'getSupportFragmentManager();'
                                val count = fm.backStackEntryCount
                                for (i in 0 until count) {
                                    fm.popBackStack()
                                }
                                val transaction = fm?.beginTransaction();
                                transaction?.setCustomAnimations(R.anim.left_enter, R.anim.right_exit, R.anim.right_enter, R.anim.left_exit);
                                transaction?.replace(R.id.Container, Fragment_Notification());
                                transaction?.addToBackStack("abc")
                                transaction?.commit();
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

        Home_Side.setOnClickListener()
        {
            if(HomeText.text=="Home") {
                Container.closeDrawer(Gravity.START);
            }else {
                val fm = supportFragmentManager
                HomeText.text = "Home"
                notificationHome1.visibility=View.VISIBLE
                val transaction = fm?.beginTransaction();
                transaction?.setCustomAnimations(R.anim.left_enter, R.anim.right_exit, R.anim.right_enter, R.anim.left_exit);
                transaction?.replace(R.id.dashboard, Fragment_Home());
                transaction?.commit();
                Container.closeDrawer(Gravity.START);
            }
        }

        myTeam_Side.setOnClickListener()
        {

            Counter.instance.count=false
            if(HomeText.text=="My Teams")
            {
                Container.closeDrawer(Gravity.START);
            }else {
                HomeText.text="My Teams"
                notificationHome1.visibility=View.GONE
                var team = session?.getteamId()
                if (team == "0") {
                    val fragment = Fragment_CreateTeam()
                    val transaction = supportFragmentManager?.beginTransaction();
                    transaction?.setCustomAnimations(R.anim.left_enter, R.anim.right_exit, R.anim.right_enter, R.anim.left_exit);
                    transaction?.replace(R.id.dashboard, fragment);
                    transaction?.commit();
                    Container.closeDrawer(Gravity.START);
                } else {
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
                                val fpa = Fragment_MyTeams()
                                val b = Bundle()
                                b.putString("captain", captainName)
                                b.putString("teamName", teamName)
                                b.putString("teamId", teamId)
                                fpa.setArguments(b)
                                val fm = supportFragmentManager
                                val transaction = fm?.beginTransaction();
                                transaction?.setCustomAnimations(R.anim.left_enter, R.anim.right_exit, R.anim.right_enter, R.anim.left_exit);
                                transaction?.replace(R.id.dashboard, fpa);
                                transaction?.addToBackStack("abc");
                                transaction?.commit();
                                Container.closeDrawer(Gravity.START);
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
        }
        teamTracks_Side.setOnClickListener()
        {
            Counter.instance.count=false
            if(HomeText.text=="Teams Tracks")
            {
                Container.closeDrawer(Gravity.START);
            }else {
                HomeText.text = "Teams Tracks"
                notificationHome1.visibility = View.GONE
                var client = ApiCall()
                var retrofit = client.retrofitClient()
                val retrofitAp = retrofit!!.create(RedditAPI::class.java)
                var token = session?.gettoken()
                var call = retrofitAp.GetTeamTracks(token!!)
                call.enqueue(object : retrofit2.Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>?) {
                        Log.d("server", "onResponse: Server Response: " + response.toString());

                        try {
                            var json: String? = null
                            json = response?.body()!!.string()
                            Log.d("JSON", json)
                            var obj = JSONObject(json)
                            var success = obj.getString("success")
                            if (success == "1") {
                                var data = obj.getJSONArray("data")
                                var sizeOfTracks = data.length()
                                Log.d("NUMBER OF TEAMS", sizeOfTracks.toString())
                                if (data.length() == 0) {
                                    Toast.makeText(applicationContext, "No Teams To Track", Toast.LENGTH_SHORT).show()
                                } else {
                                    HomeText.text = ""

                                    var b = Bundle()
                                    b.putInt("TeamTracksLength", sizeOfTracks)
                                    b.putString("TeamTracksList", data.toString())
                                    var fpa = Fragment_TeamsTrack()
                                    val transaction = supportFragmentManager?.beginTransaction();
                                    transaction?.setCustomAnimations(R.anim.left_enter, R.anim.right_exit, R.anim.right_enter, R.anim.left_exit);
                                    transaction?.replace(R.id.dashboard, fpa);
                                    transaction?.addToBackStack("abc");
                                    transaction?.commit();
                                    Container.closeDrawer(Gravity.START);
                                }
                            } else {
                                Toast.makeText(applicationContext, "Something went Wrong", Toast.LENGTH_SHORT).show()
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
        settings_Side.setOnClickListener()
        {
            Counter.instance.count=false
            if(HomeText.text=="Settings")
            {
                Container.closeDrawer(Gravity.START);
            }else {
                HomeText.text = "Settings"
                notificationHome1.visibility = View.GONE
                val fm = supportFragmentManager // or 'getSupportFragmentManager();'
                val count = fm.backStackEntryCount
                for (i in 0 until count) {
                    fm.popBackStack()
                }
                val transaction = fm?.beginTransaction();
                transaction?.setCustomAnimations(R.anim.left_enter, R.anim.right_exit, R.anim.right_enter, R.anim.left_exit);
                transaction?.replace(R.id.dashboard, Fragment_Settings());
                transaction?.addToBackStack("abc")
                transaction?.commit();
                Container.closeDrawer(Gravity.START);
            }
        }
        terms_Side.setOnClickListener()
        {
            Counter.instance.count=false
            if (HomeText.text == "Terms & Conditions") {
                Container.closeDrawer(Gravity.START);
            } else {
                HomeText.text = "Terms & Conditions"
                notificationHome1.visibility = View.GONE
                val fm = supportFragmentManager // or 'getSupportFragmentManager();'
                val count = fm.backStackEntryCount
                for (i in 0 until count) {
                    fm.popBackStack()
                }
                val transaction = fm?.beginTransaction();
                transaction?.setCustomAnimations(R.anim.left_enter, R.anim.right_exit, R.anim.right_enter, R.anim.left_exit);
                transaction?.replace(R.id.dashboard, Fragment_Terms_And_Condition());
                transaction?.addToBackStack("abc")
                transaction?.commit();
                Container.closeDrawer(Gravity.START);
            }
        }
        logout_Side.setOnClickListener()
        {

            val alertDilog = AlertDialog.Builder(this).create()
            alertDilog.setTitle("Alert")
            alertDilog.setMessage("Are you sure You want to LogOut?")
            alertDilog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", {
                dialogInterface, i ->
                session!!.setLoggedin(false,"","","wrong","","","0");
                Counter.instance.count==false
                val transaction = supportFragmentManager?.beginTransaction();
                transaction?.setCustomAnimations(R.anim.right_enter, R.anim.left_exit, R.anim.left_enter, R.anim.right_exit);
                transaction?.replace(R.id.Container,Fragment_Login());
                transaction?.commit();
                Container.closeDrawer(Gravity.START);
                alertDilog.cancel();

            })
            alertDilog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", {
                dialogInterface, i ->
                alertDilog.cancel();
            })
            alertDilog.show()
        }

    }





    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean
    {
        if (getCurrentFocus() != null) {
            var imm:InputMethodManager =  getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev)
    }
}
