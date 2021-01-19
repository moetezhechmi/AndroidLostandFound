package tn.LostAndFound.mini_projet_android_laf.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;

import tn.LostAndFound.mini_projet_android_laf.R;

public class SplashScreenActivity extends AppCompatActivity {
ImageView backImg ,ivLogo;
LottieAnimationView lottieAnimationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
getSupportActionBar().hide();
        backImg = findViewById(R.id.back_image);
        ivLogo= findViewById(R.id.iv_logo);



        backImg.animate().translationX(-2000).setDuration(3000).setStartDelay(1000);
        ivLogo.animate().translationX(-2000).setDuration(3000).setStartDelay(1000);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this,SignInActivity.class);
                startActivity(intent);
                finish();
            }
        },3000);

    }
}