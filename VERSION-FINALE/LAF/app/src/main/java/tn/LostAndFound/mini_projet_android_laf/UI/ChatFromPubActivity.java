package tn.LostAndFound.mini_projet_android_laf.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.itgate.uptofind.utils.TinyDB;

import tn.LostAndFound.mini_projet_android_laf.R;
import tn.LostAndFound.mini_projet_android_laf.Retrofit.Service;

public class ChatFromPubActivity extends AppCompatActivity {

    tn.LostAndFound.mini_projet_android_laf.Retrofit.Service Service;
    String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_from_pub);
        currentUserId = new TinyDB(ChatFromPubActivity.this).getString("_id");
        System.out.println("Aloulou  "+currentUserId);
    }
}