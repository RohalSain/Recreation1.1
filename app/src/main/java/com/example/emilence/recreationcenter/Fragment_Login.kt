package com.example.emilence.recreationcenter
import android.app.Activity
import android.app.FragmentManager
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.emilence.petapp2.Interface.RedditAPI
import kotlinx.android.synthetic.main.fragment__login.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager

import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
class Fragment_Login : Fragment() {
    private var session: Session? = null
    var team="0"
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment__login, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        session =  Session(context);
        var load = Loader()
        logoText.setTypeface(Typeface.createFromAsset(activity?.assets ,"fonts/New Athletic M54.ttf"))
        UserName_Text.setTypeface(Typeface.createFromAsset(activity?.assets ,"fonts/arial.ttf"))
        Password_Text.setTypeface(Typeface.createFromAsset(activity?.assets ,"fonts/arial.ttf"))
        Login_Button.setTypeface(Typeface.createFromAsset(activity?.assets ,"fonts/arialbd.ttf"))
        SignUp_Button.setTypeface(Typeface.createFromAsset(activity?.assets ,"fonts/arialbd.ttf"))
        forgotPassword.setTypeface(Typeface.createFromAsset(activity?.assets ,"fonts/ariali.ttf"))
        SignUp_Button.setOnClickListener()
        {
            val fm = fragmentManager // or 'getSupportFragmentManager();'
            val count = fm.backStackEntryCount
            for (i in 0 until count) {
                fm.popBackStack()
            }
            val transaction = fm?.beginTransaction();
            transaction?.setCustomAnimations(R.anim.left_enter, R.anim.right_exit, R.anim.right_enter, R.anim.left_exit);
            transaction?.replace(R.id.Container,Fragment_SignUp());
            transaction?.addToBackStack(tag)
            transaction?.commit();
        }
        Login_Button.setOnClickListener()
        {
            var client=ApiCall()
            var retrofit=client.retrofitClient()
            val username = UserName_Text.text.toString()
            val password = Password_Text.text.toString()
            val headerMap = HashMap<String, String>()
            headerMap.put("Content-Type", "application/json")
            val parameters = PojoData(username,password)
            if(username.length<1) {
                Toast.makeText(context, "Enter Username", Toast.LENGTH_SHORT).show()
            }else if(password.length<1)
                {
                    Toast.makeText(context,"Enter Password",Toast.LENGTH_SHORT).show()
                }
                else
                {
                    load.ShowLoader(context)
                    val redditAPI = retrofit!!.create(RedditAPI::class.java)
                    val call = redditAPI.login(headerMap,parameters)
                    call.enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                            Log.d("server", "onResponse: Server Response: " + response.toString());
                            if (response?.message() == "Unauthorized") {
                               load.HideLoader()
                                Toast.makeText(context, "Username or Password incorrect", Toast.LENGTH_SHORT).show()
                            } else {
                                try {
                                    var json = response?.body()!!.string()
                                  var data:JSONObject = JSONObject(json)
                                    Log.d("JSON", "onResponse: json: " + json);
                                    Log.d("Data", data.toString())
                                    var success=data.getString("success")
                                    if(success=="1") {
                                        var acc = data.getJSONObject("account")
                                        Log.d("ACCOUNT", acc.toString())
                                        val username: String = acc.getString("username")
                                        val name: String = acc.getString("name")
                                        val location = acc.getJSONObject("location")
                                        Log.d("NaMELOGIN", name)
                                        var lat = location.getString("lat")
                                        var lng = location.getString("lng")
                                        Log.d("LATLNG", lat + " " + lng)
                                        var profile = acc.get("profilePic")
                                        Log.d("pic", "${profile}")
                                        var profileUrl = "http://139.59.18.239:6009/basketball/" + profile

                                        var token = data.getString("token")
                                        team = acc.getString("team")
                                        Log.d("TOKEN", token)
                                        Log.d("HOMECOURT", location.toString())
                                        if (team == null) {
                                            session!!.setLoggedin(true, name, profileUrl, token, lat, lng, "0");
                                        }else{

                                            session!!.setLoggedin(true, name, profileUrl, token, lat, lng, team);
                                        }


                                        val fm = fragmentManager // or 'getSupportFragmentManager();'
                                        val count = fm.backStackEntryCount

                                            fm.popBackStack()

                                        val intent = Intent(activity, Activity_Container::class.java)
                                        startActivity(intent)
                                        Log.d("Team", team)
                                        load.HideLoader()
                                    }
                                    else
                                    {
                                        load.HideLoader()
                                        Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show()
                                    }
                                } catch (e: JSONException) {
                                    load.HideLoader()
                                    Log.e("JSONException", "onResponse: JSONException: " + e);
                                } catch (e: IOException) {
                                    load.HideLoader()
                                    Log.e("IOexception", "onResponse: JSONException: " + e);
                                }
                            }
                        }
                        override  fun  onFailure( call:Call<ResponseBody>,  t:Throwable)
                        {
                            load.HideLoader()
                            Log.e("OnFailure", "onFailure: Something went wrong: "  );
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                        } })
                }
            }
        forgotPassword.setOnClickListener()
        {
            val transaction = fragmentManager?.beginTransaction();
            transaction?.setCustomAnimations(R.anim.left_enter, R.anim.right_exit, R.anim.left_enter, R.anim.right_exit);
            transaction?.replace(R.id.Container,Fragment_ForgotPassword());
            transaction?.commit();
        }
    }
}
