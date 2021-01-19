package tn.LostAndFound.mini_projet_android_laf.UI;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.itgate.uptofind.utils.TinyDB;
import com.squareup.picasso.Picasso;

import retrofit2.Retrofit;
import tn.LostAndFound.mini_projet_android_laf.R;
import tn.LostAndFound.mini_projet_android_laf.Retrofit.RetrofitClient;
import tn.LostAndFound.mini_projet_android_laf.Retrofit.Service;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {
    Service service;
    EditText txtEmail;
    EditText txtTel;
    EditText txtFirstName;
    EditText txtLastName;
    ImageView imgUser,logout;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        SignInActivity s = new SignInActivity();
        txtEmail = view.findViewById(R.id.txtEmail);
        txtTel = view.findViewById(R.id.txtTel);
        txtFirstName = view.findViewById(R.id.txtFirstName);
        txtLastName = view.findViewById(R.id.txtLastName);
        logout = view.findViewById(R.id.iv_logout);
        ProfileFragment frg1 = new ProfileFragment();


        Retrofit retrofitClient = RetrofitClient.getInstance();
        service = retrofitClient.create(Service.class);
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("myKey", MODE_PRIVATE);
        String photo = new TinyDB(getActivity()).getString("photo");
        String email = new TinyDB(getActivity()).getString("email");
        String phone = new TinyDB(getActivity()).getString("phone");
        String firstName = new TinyDB(getActivity()).getString("firstName");
        String lastName = new TinyDB(getActivity()).getString("lastName");





        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),SignInActivity.class);
                startActivity(intent);
            }
        });

        imgUser = view.findViewById(R.id.imgUser);
        txtEmail.setText(email);
        txtTel.setText(phone);
        txtFirstName.setText(firstName);
        txtLastName.setText(lastName);

        Picasso.get().load(RetrofitClient.server + photo).placeholder(R.drawable.profil)
                .error(R.drawable.profil)
                .into(imgUser);
        view.findViewById(R.id.btnedit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), UpdateProfileActivity.class);

                String LastName = txtLastName.getText().toString();
                String FirstName = txtFirstName.getText().toString();
                String Email = txtEmail.getText().toString();
                String Phone = txtTel.getText().toString();


// pour envoyer  les champs dans update profil
                intent.putExtra("eLastName", LastName);
                intent.putExtra("eFirstName", FirstName);
                intent.putExtra("eEmail", Email);
                intent.putExtra("ePhone", Phone);
                startActivity(intent);
            }
        });


        return view;


    }

}