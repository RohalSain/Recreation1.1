package com.example.emilence.recreationcenter
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.app.FragmentManager
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.location.*
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import com.example.emilence.recreationcenter.R.id.map
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.fragment__home.*
import android.widget.TextView
import com.facebook.drawee.view.SimpleDraweeView
import android.widget.Toast
import android.view.KeyEvent.KEYCODE_BACK
import android.support.v4.widget.DrawerLayout
import android.util.Log
import com.example.emilence.petapp2.Interface.RedditAPI
import kotlinx.android.synthetic.main.activity__container.*
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import java.io.IOException

import java.util.*
import kotlin.collections.ArrayList

class  Fragment_Home : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    var mGoogleMap: GoogleMap? = null
    lateinit var mMapView: MapView
    lateinit var mView: View
    lateinit var myMarker: Marker
    private var session: Session? = null
    lateinit var c: Context
    lateinit var lt: String
    lateinit var ln: String
    var size:Int? = null
    var canGetLocation: Boolean = false
    var MIN_TIME_BW_UPDATES: Long = 111
    var MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 112
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater!!.inflate(R.layout.fragment__home, container, false)
        return mView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mMapView = mView.findViewById(R.id.map)
        if (mMapView != null) {
            mMapView.onCreate(null)
            mMapView.onResume()
            mMapView.getMapAsync(this)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mGoogleMap?.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this.activity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
        session = Session(context);

        var nameUser = session?.nameUser()
        var pic = session?.ProfilePicUrl()
        Log.d("NAME", nameUser)
        Log.d("USERPIC", pic)
        val mDrawerLayout = activity.findViewById(R.id.Container) as DrawerLayout
        /*view!!.isFocusableInTouchMode = true
        view!!.requestFocus()
        view!!.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
                if (event.getAction() === KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                            //drawer is open
                            mDrawerLayout.closeDrawers();
                            return false
                        } else {

                            onBack()

                            return true
                        }

                    }

                }
                return false
            }
        })*/
        currentMatch.setOnClickListener()
        {
            var token = session?.gettoken()
            var call = ApiCall().retrofitClient()!!.create(RedditAPI::class.java).GetCurrentMatch(token!!)
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
                        var data=jsonobj.getJSONObject("data")
                        Log.d("DATA",data.toString())
                        var waitingTeams:JSONArray = data.getJSONArray("waitingTeams")
                        if(waitingTeams!=null) {

                          size=waitingTeams.length()
                            Log.d("NUMBER OF WAITING TEAMS", size.toString())
                        }
                        var currentMatch:JSONObject=data.getJSONObject("currentMatch")
                        Log.d("CURRENT MATCH",currentMatch.toString())
                        var teamOne:JSONObject=currentMatch.getJSONObject("teamOne")
                        Log.d("TEAM_ONE",teamOne.toString())
                        var teamTwo:JSONObject=currentMatch.getJSONObject("teamTwo")
                        Log.d("TEAM_TWO",teamTwo.toString())
                        if (s == "0") {
                           Toast.makeText(context,"No current Matches",Toast.LENGTH_SHORT).show()
                        } else {
                            val fpa = Fragment_CurrentMatch()
                            val b = Bundle()
                            b.putInt("WaitingNumber",size!!)
                            b.putString("WaitingTeams",waitingTeams.toString())
                            b.putString("TeamOne",teamOne.toString())
                            b.putString("TeamTwo",teamTwo.toString())
                            fpa.setArguments(b)
                            val fm = fragmentManager // or 'getSupportFragmentManager();'
                            val count = fm.backStackEntryCount
                            for (i in 0 until count) {
                                fm.popBackStack()
                            }
                            val transaction = fm?.beginTransaction();
                            transaction?.setCustomAnimations(R.anim.left_enter, R.anim.right_exit, R.anim.right_enter, R.anim.left_exit);
                            transaction?.replace(R.id.Container,fpa);
                            transaction?.addToBackStack(tag)
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

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (permissions.size == 1 &&
                permissions[0] == android.Manifest.permission.ACCESS_FINE_LOCATION &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                return;
            }
            mGoogleMap?.setMyLocationEnabled(true);
        } else {
            Toast.makeText(context,"permission denied",Toast.LENGTH_SHORT).show()
        }
    }
    @SuppressLint("MissingPermission")
    override fun onMapReady(p0: GoogleMap?) {
        MapsInitializer.initialize(context)
        mGoogleMap = p0!!
        mGoogleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
        //var homecourtlat=session?.getlat()?.toDouble()
        //var homecourtlng=session?.getlng()?.toDouble()
        var homecourtlat="30.7113186".toDouble()
        var homecourtlng="76.7071965".toDouble()
        Log.d("HOMECOURT LAT",homecourtlat.toString())
        Log.d("HOMECOURT LNG",homecourtlng.toString())
        var location: Location? = null
        if((homecourtlat==null || homecourtlat==30.7113186) && (homecourtlng==null || homecourtlng==76.7071965))
        {
            try {
                var locationManager: LocationManager? = null
                locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager
                // Creating a criteria object to retrieve provider
                var criteria: Criteria = Criteria();
                // Getting the name of the best provider
                var provider: String? = null
                provider = locationManager.getBestProvider(criteria, true);
                // Getting Current Location
                location = locationManager.getLastKnownLocation(provider);
            }catch (e:Exception){
                Log.d("ERROR LOcation",e.toString())
            }
            if (location != null) {
                // Getting latitude of the current location
                var latitude: Double = location.getLatitude();
                // Getting longitude of the current location
                var longitude: Double = location.getLongitude();
                // Creating a LatLng object for the current location
                var latLng: LatLng = LatLng(latitude, longitude);
                var myPosition = LatLng(latitude, longitude);
                val position = LatLng(latitude, longitude)
                myMarker = mGoogleMap!!.addMarker(MarkerOptions().position(LatLng(latitude, longitude)).title("Mohali").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_pin)))
                val mapSettings = mGoogleMap?.uiSettings
                mapSettings?.isRotateGesturesEnabled = true
                mGoogleMap?.animateCamera(CameraUpdateFactory.zoomIn())
                mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 10f))
                val cameraPosition = CameraPosition.Builder()
                        .target(position)
                        .zoom(50f)
                        .bearing(70f)
                        .tilt(25f)
                        .build()
                mGoogleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(
                        cameraPosition))
                mGoogleMap?.setOnMarkerClickListener(this);
                Log.d("latitude", latitude.toString())
                Log.d("longitude", longitude.toString())
                var geocoder: Geocoder = Geocoder(context, Locale.getDefault());


                var _homeAddress: StringBuilder? = null;
                try {

                    _homeAddress = StringBuilder();
                    var address: Address? = null;
                    var addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1);


                    address = addresses.get(0);
                    _homeAddress.append("Name: " + address.getLocality() + "\n");
                    _homeAddress.append("Sub-Admin Ares: " + address.getSubAdminArea() + "\n");
                    _homeAddress.append("Admin Area: " + address.getAdminArea() + "\n");
                    _homeAddress.append("Country: " + address.getCountryName() + "\n");
                    _homeAddress.append("Country Code: " + address.getCountryCode() + "\n");
                    _homeAddress.append("Latitude: " + address.getLatitude() + "\n");
                    _homeAddress.append("Longitude: " + address.getLongitude() + "\n\n");

                } catch (e: Exception) {

                }
                Log.d("Adress", _homeAddress.toString())
            }else
            {
                val position = LatLng(homecourtlat!!, homecourtlng!!)
                myMarker = mGoogleMap!!.addMarker(MarkerOptions().position(LatLng(homecourtlat!!, homecourtlng!!)).title("Mohali").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_pin)))
                val mapSettings = mGoogleMap?.uiSettings
                mapSettings?.isRotateGesturesEnabled = true
                mGoogleMap?.animateCamera(CameraUpdateFactory.zoomIn())
                mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 10f))
                val cameraPosition = CameraPosition.Builder()
                        .target(position)
                        .zoom(50f)
                        .bearing(70f)
                        .tilt(25f)
                        .build()
                mGoogleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(
                        cameraPosition))
                mGoogleMap?.setOnMarkerClickListener(this);
                Log.d("lat", homecourtlat.toString())
                Log.d("long", homecourtlng!!.toString())
                var geocoder: Geocoder = Geocoder(context, Locale.getDefault());
                var _homeAddress: StringBuilder? = null;
                try {
                    Counter.instance.context=context
                    Counter.instance.count=true
                    _homeAddress = StringBuilder();
                    var address: Address? = null;
                    var addresses: List<Address> = geocoder.getFromLocation(homecourtlat, homecourtlng, 1);
                    address = addresses.get(0);
                    _homeAddress.append("Name: " + address.getLocality() + "\n");
                    _homeAddress.append("Sub-Admin Ares: " + address.getSubAdminArea() + "\n");
                    _homeAddress.append("Admin Area: " + address.getAdminArea() + "\n");
                    _homeAddress.append("Country: " + address.getCountryName() + "\n");
                    _homeAddress.append("Country Code: " + address.getCountryCode() + "\n");
                    _homeAddress.append("Latitude: " + address.getLatitude() + "\n");
                    _homeAddress.append("Longitude: " + address.getLongitude() + "\n\n");
                } catch (e: Exception) {

                }
                Log.d("Adress", _homeAddress.toString())
            }}}
    override fun onMarkerClick(p0: Marker?): Boolean
    {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_layout)
        dialog.setTitle("")
        dialog.getWindow().setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT))
        dialog.window.attributes.windowAnimations = R.style.customAnimationsgrow
        val text = dialog.findViewById(R.id.t1) as TextView
        val text1 = dialog.findViewById(R.id.t2) as TextView
        val text2 = dialog.findViewById(R.id.t3) as TextView
        val join_btn = dialog.findViewById(R.id.joinButton) as Button
        val create_btn = dialog.findViewById(R.id.createButton) as Button
        join_btn.setTypeface(Typeface.createFromAsset(activity?.assets ,"fonts/SF-Pro-Text-Medium.otf"))
        create_btn.setTypeface(Typeface.createFromAsset(activity?.assets ,"fonts/SF-Pro-Text-Medium.otf"))
        text.setTypeface(Typeface.createFromAsset(activity?.assets ,"fonts/SF-Pro-Text-Regular.otf"))
        text1.setTypeface(Typeface.createFromAsset(activity?.assets ,"fonts/SF-Pro-Text-Regular.otf"))
        text2.setTypeface(Typeface.createFromAsset(activity?.assets ,"fonts/SF-Pro-Text-Regular.otf"))
        dialog.show()
        create_btn.setOnClickListener()
        {
            val count = fragmentManager.backStackEntryCount
            for (i in 0 until count) {
                fragmentManager.popBackStack()
            }
            val transaction = fragmentManager.beginTransaction()
            transaction?.setCustomAnimations(R.anim.left_enter, R.anim.right_exit, R.anim.right_enter, R.anim.left_exit);
            transaction?.replace(R.id.Container,Fragment_CreateTeam());
            transaction?.addToBackStack(tag)
            transaction?.commit();
            dialog.dismiss()
        }
        join_btn.setOnClickListener()
        {
            var client = ApiCall()
            var retrofit = client.retrofitClient()
            val retrofitAp = retrofit!!.create(RedditAPI::class.java)
            var token = session?.gettoken()
            var call = retrofitAp.GetAllTeams(token!!)
            call.enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>?) {
                    Log.d("server", "onResponse: Server Response: " + response.toString());
                    try {
                        var json: String? = null
                        json = response?.body()!!.string()
                        Log.d("JSON", json)
                        var obj = JSONObject(json)
                        var data = obj.getJSONArray("data")
                        Log.d("DATA", data.toString())
                        var sizeOfPlayers = data.length()
                        Log.d("NUMBER OF TEAMS", sizeOfPlayers.toString())
                        var id:ArrayList<String> = ArrayList()
                        var TeamName:ArrayList<String> = ArrayList()
                        for(i in 0..sizeOfPlayers-1) {
                          id.add(data.getJSONObject(i).getString("_id"))
                          /*if (data.getJSONObject(i).getString("teamName") == null) {
                              TeamName.add("TeamName")
                          } else {
                              TeamName.add(data.getJSONObject(i).getString("teamName"))
                          }*/
                      }
                        Log.d("IDS",id.toString())
                      // Log.d("NAMES",TeamName.toString())
                        //var teamlist=data.toString()
                       // Log.d("Array",teamlist.toString())
                        dialog.dismiss()
                        var b=Bundle()
                        b.putStringArrayList("TeamIdList",id)
                       // b.putStringArrayList("TeamNameList",TeamName)
                        var fpa=Fragment_JoinTeams()
                        fpa.setArguments(b)
                                val count = fragmentManager.backStackEntryCount
                        for (i in 0 until count) {
                            fragmentManager.popBackStack()
                        }
                        val transaction = fragmentManager.beginTransaction()
                        transaction?.setCustomAnimations(R.anim.left_enter, R.anim.right_exit, R.anim.right_enter, R.anim.left_exit);
                        transaction?.replace(R.id.Container,fpa);
                        transaction?.addToBackStack(tag)
                        transaction?.commit();
                        dialog.dismiss()
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
        return true
    }
    fun onBack()
    {
        val alertDilog = AlertDialog.Builder(context).create()
        alertDilog.setTitle("Alert")
        alertDilog.setMessage("Are you sure You want to Exit?")
        alertDilog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", {
            dialogInterface, i ->
            val fragmentManager =fragmentManager
            val count = fragmentManager.backStackEntryCount
            for (i in 0 until count) {
                fragmentManager.popBackStack()
            }
            android.os.Process.killProcess(android.os.Process.myPid());
            alertDilog.cancel();
        })
        alertDilog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", {
            dialogInterface, i ->
            alertDilog.cancel();
        })
        alertDilog.show()
        return
    }
}