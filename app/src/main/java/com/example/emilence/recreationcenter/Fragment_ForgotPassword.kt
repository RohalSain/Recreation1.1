package com.example.emilence.recreationcenter


import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.emilence.petapp2.Interface.RedditAPI
import kotlinx.android.synthetic.main.fragment__forgot_password.*
import kotlinx.android.synthetic.main.fragment__login.*
import kotlinx.android.synthetic.main.fragment__sign_up.*
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit


/**
 * A simple [Fragment] subclass.
 */
class Fragment_ForgotPassword : Fragment() {

    val u = "basketball@emilence.com"
    val p = "Emilence@1"
    private val BASE_URL = "http://139.59.18.239:6009/"
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment__forgot_password, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        logoText_Forgot.setTypeface(Typeface.createFromAsset(activity?.assets ,"fonts/New Athletic M54.ttf"))

        Back_Forgot.setOnClickListener()
        {
            var f = Fragment_SignUp()

            val transaction = fragmentManager?.beginTransaction();
            transaction?.setCustomAnimations(R.anim.right_enter, R.anim.left_exit, R.anim.left_enter, R.anim.right_exit);
            transaction?.replace(R.id.Container,Fragment_Login());
            transaction?.commit();

        }
        Send_Forgot.setOnClickListener()
        {
            val email=Forgot_Password_Text.text.trim().toString()
            val obj:poho=poho(email)
            val  okHttpClient: OkHttpClient =  OkHttpClient.Builder()
                    .addInterceptor( BasicAuthInterceptor(u,p))
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .build()

            val retrofit: Retrofit =  Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            val redditAPI = retrofit.create(RedditAPI::class.java)
            val call = redditAPI.forgotPassword(obj)
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>?) {
                    Log.d("server", "onResponse: Server Response: " + response.toString());
                    if (response?.message() == "Unauthorized") {
                        Toast.makeText(context, "Username or Password incorrect", Toast.LENGTH_SHORT).show()
                    } else {

                        try {
                            val json = response!!.body()
                            //val jsonObj:JSONObject = JSONObject(json.toString())
                            var str_response = response?.body()!!.string()
                            Log.d("Data", str_response)


                            Log.d("JSON", "onResponse: json: " + json);

                            // Log.d("result",resul.toString())
                            //Log.d("data",data.toString())
                            // val username:String=resul.getString("username")
                            // val name:String=resul.getString("name")
                            //val country:String=resul.getString("country")
                            // val phone:String=resul.getString("phoneNumber")

                            //val id:String=resul.getString("_id")
                            // Log.d("JSON_Value", "" + username+" "+name+" "+country+" "+phone+" "+pic);
                            //val p = User_Info()
                            // p.name=name
                            // p.email=username
                            //p.phone=phone
                            // p.country=country
                            // p.url="http://139.59.18.239:6009/basketball/"+pic
                            //  p.id=id
                            val transaction = fragmentManager?.beginTransaction();
                            transaction?.setCustomAnimations(R.anim.left_enter, R.anim.right_exit, R.anim.left_enter, R.anim.right_exit);
                            transaction?.replace(R.id.Container,Fragment_Home());
                            transaction?.commit();
                        } catch (e: JSONException) {
                            Log.e("JSONException", "onResponse: JSONException: " + e);
                        } catch (e: IOException) {
                            Log.e("IOexception", "onResponse: JSONException: " + e);
                        }
                    }
                }
                override  fun  onFailure(call: Call<ResponseBody>, t:Throwable)
                {
                    Log.e("OnFailure", "onFailure: Something went wrong: "  );
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                } });

        }
        }


}// Required empty public constructor
