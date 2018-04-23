package com.example.emilence.recreationcenter
import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.emilence.petapp2.Interface.RedditAPI
import kotlinx.android.synthetic.main.fragment__sign_up.*
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import android.util.Base64
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlacePicker
import kotlinx.android.synthetic.main.fragment__login.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
class Fragment_SignUp : Fragment()
{
    lateinit var mCapturedImageURI: Uri
    val PICK_FROM_FILE = 1
    val REQUEST_IMAGE_CAPTURE = 2
    val PLACE_PICKER_REQUEST = 5
    private var session: Session? = null
    lateinit var bmp: Bitmap
    lateinit var bmpuri: Bitmap
    private var currentImageUri: Uri? = null
    var currentImagePath: String? = null
    var imagePath: File? = null
    var basic=""
    var placeName=""
     var lat:String=""
    var lng:String=""
    lateinit var place: Place
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment__sign_up, container, false)
    }
    @TargetApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var load = Loader()
        Name_Text.setTypeface(Typeface.createFromAsset(activity?.assets, "fonts/arial.ttf"))
        Email_Text.setTypeface(Typeface.createFromAsset(activity?.assets, "fonts/arial.ttf"))
        SignUp_Password_Text.setTypeface(Typeface.createFromAsset(activity?.assets, "fonts/arial.ttf"))
        SignUp_Confirm_Password_Text.setTypeface(Typeface.createFromAsset(activity?.assets, "fonts/arial.ttf"))
        PhoneNumber.setTypeface(Typeface.createFromAsset(activity?.assets, "fonts/arial.ttf"))
        signUp_Submit.setTypeface(Typeface.createFromAsset(activity?.assets, "fonts/arialbd.ttf"))
        BackLoggin_Button.setOnClickListener()
        {
            val fm = fragmentManager // or 'getSupportFragmentManager();'
            val count = fm.backStackEntryCount
            for (i in 0 until count) {
                fm.popBackStack()
            }

            val transaction = fm?.beginTransaction();
            //transaction?.setCustomAnimations(R.anim.right_enter, R.anim.left_exit, R.anim.left_enter, R.anim.right_exit);
            transaction?.replace(R.id.Container, Fragment_Login());
            transaction?.commit();
        }
        userPic.setOnClickListener()
        {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle("Select Picture")
            val country_Name: Array<String> = arrayOf("CAMERA", "GALLERY")
            builder.setItems(country_Name, DialogInterface.OnClickListener { dialog, which ->
                if (which == 0)
                {
                    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    if (savedInstanceState != null)
                    {
                        currentImagePath = savedInstanceState.getString("currentImagePath")
                    } else {
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentImageUri)
                        if (takePictureIntent.resolveActivity(context?.packageManager) != null) {
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                        }
                        dialog.cancel()
                    }
                } else {
                    val intent = Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                    startActivityForResult(intent, PICK_FROM_FILE)
                    dialog.cancel()
                }
            }
            )
                    .create()
                    .show()
        }
        Location_Button.setOnClickListener()
        {
            Log.d("LOCATION","clicked")
            val builder = PlacePicker.IntentBuilder()
            try {
            startActivityForResult(builder.build(activity), PLACE_PICKER_REQUEST)



            }catch (e:GooglePlayServicesRepairableException)
            {
                Log.d("ERROR",e.toString())
            }
            catch (e:GooglePlayServicesNotAvailableException)
            {
                Log.d("ERROR2",e.toString())
            }
        }
        signUp_Submit.setOnClickListener()
        {
            val name1 = Name_Text.text.toString().trim()
            val email1 = Email_Text.text.toString().trim()
            val password1 = SignUp_Password_Text.text.toString().trim()
            val cpassword = SignUp_Confirm_Password_Text.text.toString().trim()
            val phoneNumber = PhoneNumber.text.toString().trim()
            val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z]+\\.+[a-z]"
            fun isValidEmail(e: String): Boolean {
                return !TextUtils.isEmpty(e) && android.util.Patterns.EMAIL_ADDRESS.matcher(e).matches();
            }
            if (name1.length < 1) {
                Toast.makeText(context, "Enter Name", Toast.LENGTH_SHORT).show()
            } else if (email1.length < 1) {
                Toast.makeText(context, "Enter Email", Toast.LENGTH_SHORT).show()
            } else if (email1.matches(emailPattern.toRegex())) {
                Toast.makeText(context, "Invalid Email", Toast.LENGTH_SHORT).show()
            } else if (password1.length < 6) {
                Toast.makeText(context, "Password should be of minimum 6 characters", Toast.LENGTH_SHORT).show()
            } else if (password1 != cpassword) {
                Toast.makeText(context, "Password and Confirm Password should be same", Toast.LENGTH_SHORT).show()
            } else if (phoneNumber.length < 10 || phoneNumber.length > 10) {
                Toast.makeText(context, "Phone number should be 10 digits only", Toast.LENGTH_SHORT).show()
            } else {
                load.ShowLoader(context)
                var client=ApiCall()
                var retrofit=client.retrofitClient()
                val retrofitAp = retrofit!!.create(RedditAPI::class.java)
                val headerMap = HashMap<String, RequestBody>()
                headerMap.put("email", RequestBody.create(MediaType.parse("text/plain"), email1))
                headerMap.put("password", RequestBody.create(MediaType.parse("text/plain"), password1))
                headerMap.put("confirmPassword", RequestBody.create(MediaType.parse("text/plain"), cpassword))
                headerMap.put("name", RequestBody.create(MediaType.parse("text/plain"), name1))
                headerMap.put("mobileNumber", RequestBody.create(MediaType.parse("text/plain"), phoneNumber))
                headerMap.put("lat", RequestBody.create(MediaType.parse("text/plain"),lat.toString()))
                headerMap.put("lng", RequestBody.create(MediaType.parse("text/plain"),lng.toString()))
                var query: String = "profilePic\";filename=\"${FilePicture()?.absolutePath}"
                headerMap.put(query, RequestBody.create(MediaType.parse("/Images"), FilePicture()))
                headerMap.put("homeCourt", RequestBody.create(MediaType.parse("text/plain"),"abc"))
                val call = retrofitAp.registerUser(headerMap)
                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>?) {
                        Log.d("server", "onResponse: Server Response: " + response.toString());
                        Log.d("Response", "Successfull")
                        try {
                            val json = response?.body()!!.string()
                            Log.d("JSON", "onResponse: json: " + json);
                            var str_response = response?.body()!!.string()
                            Log.d("Data", str_response)

                            var data: JSONObject = JSONObject(json)
                            if (data.getString("success") == "1") {
                                var acc =data.getJSONObject("account")
                                val name: String = acc.getString("name")
                                val username:String=acc.getString("username")
                                val location = acc.getJSONObject("location")
                                Log.d("LOCATION",location.toString())
                                var lat = location.getString("lat")
                                var lng = location.getString("lng")
                                var profile = acc.get("profilePic")
                                var profileUrl = "http://139.59.18.239:6009/basketball/" + profile
                                var token = data.getString("token")
                                var teamId = "0"
                                session =  Session(context);
                                session!!.setLoggedin(true, name, profileUrl, token, lat, lng, teamId);
                                load.HideLoader()
                                load.HideLoader()
                                Toast.makeText(context, "Registerd Successfully", Toast.LENGTH_SHORT).show();
                                val fm = fragmentManager // or 'getSupportFragmentManager();'
                                val count = fm.backStackEntryCount

                                    fm.popBackStack()

                                val intent = Intent(activity, Activity_Container::class.java)
                                startActivity(intent)

                            } else {
                                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                            }
                        }
                            catch (e:JSONException)
                            {

                            Log.e("JSONException", "onResponse: JSONException: " + e );
                        } catch (e: IOException) {
                            load.HideLoader()
                            Log.e("IOexception", "onResponse: JSONException: ");
                        }

                    }
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.e("OnFailure", "onFailure: Something went wrong: "+ t.toString());

                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                        load.HideLoader()
                    }
                })
            }
        }
    }
    fun getImageFileUri(): Uri?
    {
        imagePath = File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Tuxuri");
        if (!imagePath!!.exists())
        {
            if (!imagePath!!.mkdirs())
            {

                return null;
            } else
            {
                // Log.d("create new Tux folder");
            }
        }
        //Creating an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date());
        val image: File = File(imagePath, "TUX_" + timeStamp + ".jpg");
        if (!image.exists())
        {
            try
            {
                image.createNewFile();
            } catch (e: IOException)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return Uri.fromFile(image);
    }
    fun FilePicture(): File
    {
        var f: File = File(context?.cacheDir, "Saii")
        f.createNewFile()
        //Convert bitmap to byte array
        var bitmap: Bitmap? = bmpuri
        var bos: ByteArrayOutputStream = ByteArrayOutputStream();
        bitmap?.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        var bitmapdata = bos.toByteArray();
        //write the bytes in file
        var fos: FileOutputStream = FileOutputStream(f);
        fos.write(bitmapdata);
        fos.flush();
        fos.close();
        return f
    }
    override fun onSaveInstanceState(outState: Bundle)
    {
        super.onSaveInstanceState(outState)
        outState?.putString("currentImagePath", currentImageUri?.getPath());
    }
    //Overriding Function to Handling Dialog Box Result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK)
        {
            val bundle: Bundle = data!!.extras
            bmp = bundle.get("data") as Bitmap
            bmpuri = bmp
            userPic.setImageURI(Uri.fromFile(FilePicture()))
        } else if (requestCode == PICK_FROM_FILE && resultCode == Activity.RESULT_OK)
        {
            try
            {
                mCapturedImageURI = data!!.data
                userPic.setImageURI(mCapturedImageURI)
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, mCapturedImageURI)
                bmpuri = bitmap
            } catch (e: Exception)
            {
                e.printStackTrace()
                Toast.makeText(context, "Image_notfound", Toast.LENGTH_LONG).show()
            }
        }
        if(requestCode==PLACE_PICKER_REQUEST && resultCode == Activity.RESULT_OK)
        {
           place = PlacePicker.getPlace(context,data)
            placeName=place.name.toString()
            lat = place.latLng.latitude.toString()
            lng = place.latLng.longitude.toString()
            Log.d("LATITUDE",lat)
            Log.d("LONGITUDE",lng)
            Location_Button.text=placeName

        }
    }
}
