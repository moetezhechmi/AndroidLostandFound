package tn.LostAndFound.mini_projet_android_laf.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.itgate.uptofind.utils.TinyDB;

import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import tn.LostAndFound.mini_projet_android_laf.R;
import tn.LostAndFound.mini_projet_android_laf.Retrofit.RetrofitClient;
import tn.LostAndFound.mini_projet_android_laf.Retrofit.Service;
import tn.LostAndFound.mini_projet_android_laf.models.AddCommentResponse;
import tn.LostAndFound.mini_projet_android_laf.models.Comment;
import tn.LostAndFound.mini_projet_android_laf.models.GetCommentsResponse;
import tn.LostAndFound.mini_projet_android_laf.models.Owner;

public class CommentaireActivity extends AppCompatActivity {
    Service Service;
    String currentUserId;
    ImageView Send;
    EditText E_Add_Comment;
    RecyclerView recyclerView;
    CommentaireAdapter commentaireAdapter;
    String idPublication;
    ArrayList<Comment> commentaires;
    static int nbCommentaire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commentaire);
        idPublication = getIntent().getStringExtra("idPub");

        Retrofit retrofitClient = RetrofitClient.getInstance();
        Service = retrofitClient.create(Service.class);

        Send = findViewById(R.id.send);

        E_Add_Comment = findViewById(R.id.EditAddComment);


        Send.setOnClickListener(l -> {

            addComment();
            E_Add_Comment.setText("");
            CommentaireActivity.nbCommentaire++;


        });

        commentaireAdapter = new CommentaireAdapter();
        recyclerView = findViewById(R.id.recycleViewComment);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(commentaireAdapter);
        // recup id
        String idPub = getIntent().getStringExtra("idPub");
        Service.getCommentsByPublication(idPub)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetCommentsResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("zuuuu ");

                    }

                    @Override
                    public void onNext(GetCommentsResponse resp) {
                        System.out.println("hechmiiii " + resp.getComments());
                        commentaires = new ArrayList<>(resp.getComments());
                        commentaireAdapter.setComments(resp.getComments());
                        nbCommentaire = commentaires.size();


                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO:handle network error
                        System.out.println("hiiii " + e.toString());

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    public void addComment() {
        String idPub = getIntent().getStringExtra("idPub");

        Retrofit retrofitClient = RetrofitClient.getInstance();
        Service = retrofitClient.create(Service.class);
        Service = retrofitClient.create(Service.class);

        currentUserId = new TinyDB(CommentaireActivity.this).getString("_id");
        Date date = new Date();
        PrettyTime p = new PrettyTime();
        p.format(new Date(System.currentTimeMillis() + 1000 * 60 * 10));
        String photo = new TinyDB(CommentaireActivity.this).getString("photo");
        String firstName = new TinyDB(CommentaireActivity.this).getString("firstName");
        String lastName = new TinyDB(CommentaireActivity.this).getString("lastName");


        String commentText = E_Add_Comment.getText().toString();
        Service.addComment(idPub, currentUserId, E_Add_Comment.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AddCommentResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(AddCommentResponse response) {

                        commentaires.add(new Comment(response.getDate(), response.getCommentId(), commentText, new Owner(firstName, lastName, photo, currentUserId)));
                        commentaireAdapter.setComments(commentaires);
                        Toast.makeText(CommentaireActivity.this, "commentaire ajoute", Toast.LENGTH_SHORT).show();
                        System.out.println("idPublication" + idPub);
                        System.out.println("userconnected" + currentUserId);
                        System.out.println("textee" + E_Add_Comment.getText().toString());


                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO:handle network error
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("idPublication", idPublication);
        returnIntent.putExtra("nbCommentaire", nbCommentaire);

        setResult(0, returnIntent);
        finish();
    }
}