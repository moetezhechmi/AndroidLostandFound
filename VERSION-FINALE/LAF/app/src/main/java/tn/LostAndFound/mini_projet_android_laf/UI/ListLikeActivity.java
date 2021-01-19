package tn.LostAndFound.mini_projet_android_laf.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import tn.LostAndFound.mini_projet_android_laf.R;
import tn.LostAndFound.mini_projet_android_laf.Retrofit.RetrofitClient;
import tn.LostAndFound.mini_projet_android_laf.Retrofit.Service;
import tn.LostAndFound.mini_projet_android_laf.models.GetLikeResponse;
import tn.LostAndFound.mini_projet_android_laf.models.Publication;

public class ListLikeActivity extends AppCompatActivity {

    LikeAdapter likeAdapter;
    Service service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_like);
        likeAdapter = new LikeAdapter();
        Retrofit retrofitClient=RetrofitClient.getInstance();
        service= retrofitClient.create(Service.class);
        Publication publication=new Publication();
        GetLikeResponse getLikeResponse=new GetLikeResponse();
        String idPub=publication.get_id();
        service.getListLikesByPublication(idPub).observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {

                        likeAdapter.setLike(getLikeResponse.getLikes());

                        System.out.println("hech "+ s);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

}