package com.example.emilence.recreationcenter


import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import java.util.*
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import android.content.Intent
import kotlinx.android.synthetic.main.fragment__location__sign_up.*


/**
 * A simple [Fragment] subclass.
 */
class Fragment_Location_SignUp : Fragment(), OnMapReadyCallback
{
    var PLACE_AUTOCOMPLETE_REQUEST_CODE = 1
    var mGoogleMap1:GoogleMap?=null
    lateinit var mMapView1:MapView
    lateinit var mView1: View
     var myMarker1:Marker?=null
    lateinit var lat:String
    lateinit var lng:String
    var autocompleteFragment:PlaceAutocompleteFragment? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
      mView1 =   inflater!!.inflate(R.layout.fragment__location__sign_up, container, false)
    return mView1
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mGoogleMap1?.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this.activity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION ),1)
        }
    }
    @SuppressLint("MissingPermission")
    override fun onMapReady(p0: GoogleMap?) {
        MapsInitializer.initialize(context)
        mGoogleMap1 = p0!!
        mGoogleMap1?.mapType = GoogleMap.MAP_TYPE_NORMAL
        var locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // Creating a criteria object to retrieve provider
        var criteria: Criteria = Criteria();
        // Getting the name of the best provider
        var provider: String = locationManager.getBestProvider(criteria, true);
        // Getting Current Location
        var location: Location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            var latitude: Double = location.getLatitude();
            var longitude: Double = location.getLongitude();
            lat = latitude.toString()
            lng = longitude.toString()
            var latLng: LatLng = LatLng(latitude, longitude);
            var myPosition = LatLng(latitude, longitude);
            val position = LatLng(latitude, longitude)
            myMarker1 = mGoogleMap1!!.addMarker(MarkerOptions().position(LatLng(latitude, longitude)).title("Mohali").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_pin)))
            val mapSettings = mGoogleMap1?.uiSettings
            mapSettings?.isRotateGesturesEnabled = true
            mGoogleMap1?.animateCamera(CameraUpdateFactory.zoomIn())
            mGoogleMap1?.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 10f))
            val cameraPosition = CameraPosition.Builder()
                    .target(position)
                    .zoom(50f)
                    .bearing(70f)
                    .tilt(25f)
                    .build()
            mGoogleMap1?.animateCamera(CameraUpdateFactory.newCameraPosition(
                    cameraPosition))
            autocompleteFragment = fragmentManager.findFragmentById(R.id.place) as? PlaceAutocompleteFragment
            autocompleteFragment?.setOnPlaceSelectedListener(object : PlaceSelectionListener
            {
                override fun onPlaceSelected(p0: Place?) {
                    Log.d("PlaceSelect","FunctionCalled")
                    var place: Place?=null
                    place=p0!!
                    Log.d("PLACE",place.toString())
                    val position = p0?.latLng
                    lat=p0?.latLng?.latitude.toString()
                    lng=p0?.latLng?.longitude.toString()
                    Log.d("LAT",lat)
                    Log.d("LNG",lng)
                    myMarker1 = mGoogleMap1?.addMarker(MarkerOptions().position(p0!!.latLng).title(p0!!.name.toString()).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_pin)))
                    val mapSettings = mGoogleMap1?.uiSettings
                    mapSettings?.isRotateGesturesEnabled = true
                    mGoogleMap1?.animateCamera(CameraUpdateFactory.zoomIn())
                    mGoogleMap1?.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 10f))
                    val cameraPosition = CameraPosition.Builder()
                            .target(position)
                            .zoom(50f)
                            .bearing(70f)
                            .tilt(25f)
                            .build()
                    mGoogleMap1?.animateCamera(CameraUpdateFactory.newCameraPosition(
                            cameraPosition))
                }
                override fun onError(p0: Status?)
                {
                    Log.d("ERROR","autocomplete Fragment " +
                            "Listner")
                }
            });
            Log.d("latitude",latitude.toString())
            Log.d("longitude",longitude.toString())
            var geocoder: Geocoder =  Geocoder(context, Locale.getDefault());
            var _homeAddress:StringBuilder? = null;
            try{
                _homeAddress =  StringBuilder();
                var address: Address? = null;
                var addresses:List<Address> = geocoder.getFromLocation(latitude,longitude,1);
                address = addresses.get(0);
                _homeAddress.append("Name: " + address.getLocality() + "\n");
                _homeAddress.append("Sub-Admin Ares: " + address.getSubAdminArea() + "\n");
                _homeAddress.append("Admin Area: " + address.getAdminArea() + "\n");
                _homeAddress.append("Country: " + address.getCountryName() + "\n");
                _homeAddress.append("Country Code: " + address.getCountryCode() + "\n");
                _homeAddress.append("Latitude: " + address.getLatitude() + "\n");
                _homeAddress.append("Longitude: " + address.getLongitude() + "\n\n");
            }
            catch( e:Exception){
            }
            Log.d("Adress",_homeAddress.toString())
        } }
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mMapView1 = mView1.findViewById(R.id.mapSignUp)
        if(mMapView1 != null)
        {
            mMapView1.onCreate(null)
            mMapView1.onResume()
            mMapView1.getMapAsync(this)
        }
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
            mGoogleMap1?.setMyLocationEnabled(true);
        } else {
            Toast.makeText(context,"permission denied", Toast.LENGTH_SHORT).show()
        }
    }
}
