package tn.LostAndFound.mini_projet_android_laf.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itgate.uptofind.utils.TinyDB;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;
import tn.LostAndFound.mini_projet_android_laf.R;
import tn.LostAndFound.mini_projet_android_laf.Retrofit.RetrofitClient;
import tn.LostAndFound.mini_projet_android_laf.Retrofit.Service;

public class SignInActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity";
TextView tvCreerCompte;
EditText email;
EditText password;
Button btnConnecter;

    Service Service;
    CompositeDisposable compositeDisposable= new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        tvCreerCompte= findViewById(R.id.tvCreerCompte);
        email= findViewById(R.id.email);
        password= findViewById(R.id.password);
        btnConnecter= findViewById(R.id.btnConnecter);
        //init Service

        Retrofit retrofitClient = RetrofitClient.getInstance();
        Service = retrofitClient.create(Service.class);
        btnConnecter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(email.getText().toString(),password.getText().toString());
            }


        });

        //text view pour aller page inscription
        tvCreerCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser(String email, String password) {
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Email cannot be null or empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Password cannot be null or empty", Toast.LENGTH_SHORT).show();
            return;
        }

         Service.loginUser(email,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(String response) {
                        int obj = 0;
                        try {
                            obj = new JSONObject(response).getInt("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.v(TAG, "status =" + obj);

                        if (obj==8) {
                            Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                            startActivity(intent);
                            JSONObject us = null;
                            String id= null;
                            String firstName = null;
                            String email = null;
                            String phone = null;
                            String photo = null;
                            String lastName = null;
                            try {
                                us = new JSONObject(response).getJSONObject("user");
                                id = us.getString("_id");
                                firstName = us.getString("firstName");
                                email = us.getString("email");
                                phone = us.getString("phone");
                                photo = us.getString("photo");
                                lastName = us.getString("lastName");

                                new TinyDB(SignInActivity.this).putString("phone",phone);
                                new TinyDB(SignInActivity.this).putString("firstName",firstName);
                                new TinyDB(SignInActivity.this).putString("email", email);
                                new TinyDB(SignInActivity.this).putString("lastName", lastName);
                                new TinyDB(SignInActivity.this).putString("photo", photo);
                                new TinyDB(SignInActivity.this).putString("_id", id);
                            } catch (JSONException e) {
                                System.out.println("Jason error");
                                e.printStackTrace();
                            }
                            System.out.println("a1 "+us);



                        }
                        else if (obj==0)
                            openDialogPass();
                        else
                            openDialogUser();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(SignInActivity.this, "verifier votre connection", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });





    }
    /*
    public int json(Response response){
        Response response;
        int obj =new JSONObject(response).getInt("status");


    }
    */

    public  void openDialogPass(){
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setIcon(R.drawable.error);

        dlgAlert.setMessage("Vérifiez votre mot de passe !!!");
        dlgAlert.setTitle("MOT DE PASSE INCORRECT");
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.show();
    }
    public  void openDialogUser(){
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setIcon(R.drawable.error);

        dlgAlert.setMessage("Vérifiez votre Email !!!");
        dlgAlert.setTitle("UTILISATEUR NON TROUVÉ");
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.show();
    }

}