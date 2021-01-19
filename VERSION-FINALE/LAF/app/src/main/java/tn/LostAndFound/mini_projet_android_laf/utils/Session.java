package tn.LostAndFound.mini_projet_android_laf.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {

    private static Session instance;

    private SharedPreferences prefs;

    private Session(Context cntx) { prefs = PreferenceManager.getDefaultSharedPreferences(cntx); }

    public static Session getInstance(Context context) {
        if (instance == null) {
            instance = new Session(context);
        }
        return instance;
    }

    public void clearSession() {
        prefs.edit().clear().commit();
    }

    public void setId(int id) {
        prefs.edit().putInt("id", id).commit();
    }

    public int getId() {
        int id = prefs.getInt("id", 0);
        return id;
    }

    public void setFirstName(String firstName) {
        prefs.edit().putString("firstName", firstName).commit();
    }

    public String getFirstName() {
        String firstName = prefs.getString("firstName", "");
        return firstName;
    }

    public void setLastName(String lastName) {
        prefs.edit().putString("lastName", lastName).commit();
    }

    public String getLastName() {
        String lastName = prefs.getString("lastName", "");
        return lastName;
    }

    public void setEmail(String email) {

        prefs.edit().putString("email", email).commit();
    }

    public String getEmail() {
        String email = prefs.getString("email", "");
        return email;
    }

    public void setPassword(String password) {
        prefs.edit().putString("password", password).commit();
    }

    public String getPassword() {
        String password = prefs.getString("password", "");
        return password;
    }

    public void setPhone(String phone) {
        prefs.edit().putString("phone", phone).commit();
    }

    public String getPhone() {
        String phone = prefs.getString("phone", "");
        return phone;
    }

    public void setPhoto(String photo) {
        prefs.edit().putString("photo", photo).commit();
    }

    public String getPhoto() {
        String photo = prefs.getString("photo", "null");
        return photo;
    }
    public void  setToken(String token){
        prefs.edit().putString("token", token).commit();
    }
    public String getToken() {
        String token = prefs.getString("token", "null");
        return token;
    }

    public void  setEtat(int etat){
        prefs.edit().putInt("etat", etat).commit();
    }
    public int getEtat() {
        int etat = prefs.getInt("etat", 0);
        return etat;
    }

}
