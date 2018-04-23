package com.example.emilence.recreationcenter;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by emilence on 29/3/18.
 */

public class Session {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context ctx;
    public Session(Context ctx){
        this.ctx = ctx;
        prefs = ctx.getSharedPreferences("app", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }
    public void setLoggedin(boolean logggedin,String name,String url, String token,String lat,String lng,String teamId){
        editor.putBoolean("loggedInmode",logggedin);
        editor.putString("name",name);
        editor.putString("url",url);
        editor.putString("token",token);
        editor.putString("lat,",lat);
        editor.putString("lng,",lng);
        editor.putString("teamId",teamId);
        editor.commit();
    }
    public String ProfilePicUrl()
    {
        return prefs.getString("url","profileplaceholder.jpg");
    }
    public String nameUser()
{
    return prefs.getString("name","user");
}
    public String gettoken()
    {
        return prefs.getString("token","");
    }
    public String getlat()
    {
        return prefs.getString("lat","empty");
    }
    public String getlng() {
        return prefs.getString("lng", "empty");
    }
    public String getteamId() {
        return prefs.getString("teamId", "0");
    }
    public boolean loggedin(){
        return prefs.getBoolean("loggedInmode", false);
    }

}
