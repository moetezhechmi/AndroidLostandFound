package tn.LostAndFound.mini_projet_android_laf.UI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import tn.LostAndFound.mini_projet_android_laf.R;
import tn.LostAndFound.mini_projet_android_laf.Retrofit.RetrofitClient;
import tn.LostAndFound.mini_projet_android_laf.Retrofit.Service;
import tn.LostAndFound.mini_projet_android_laf.models.CommentPublication;
import tn.LostAndFound.mini_projet_android_laf.models.GetPubResponse;
import tn.LostAndFound.mini_projet_android_laf.models.Publication;


public class HomeFragment extends Fragment {
    private ArrayList<String> permissionsToRequest;

    RecyclerView recyclerView;
    static PublicationAdapter publicationAdapter;
    static ArrayList<Publication> publications;
    Service Service;
    String idU;
    String idP;
    SwipeRefreshLayout swipe;
    ImageView ivAllLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        publicationAdapter = new PublicationAdapter(getActivity());
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Retrofit retrofitClient = RetrofitClient.getInstance();
        Service = retrofitClient.create(Service.class);
        Service.getPub()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetPubResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(GetPubResponse response) {
                        publications = new ArrayList<>(response.getPublications());
                        publicationAdapter.setPub(publications);

                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO:handle network error
                    }

                    @Override
                    public void onComplete() {

                    }
                });


        return view;
    }


/*public void  get(){
   String idU = "5fcc012f52c1c80883e8392a";
    String idP = "5fcd6a1b86502c26f9bc6c77";

    RequestBody idUser =
            RequestBody.create(MediaType.parse("multipart/form-data"), idU);

    RequestBody idPub =
            RequestBody.create(MediaType.parse("multipart/form-data"), idP);

    System.out.println("done");

    Service.AddLikeToPub(idUser,idPub)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<String>() {
                @Override
                public void accept(String response) throws Exception {
                    System.out.println("mcheet");
//traitement


                }
            });
}*/

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = getView().findViewById(R.id.recyclerViewMESPUB);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(publicationAdapter);

        ivAllLocation= view.findViewById(R.id.iv_all_location);






        ivAllLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), LocationPerduActivity.class);
                startActivity(intent);

            }
        });
    }

    private void getUserListData() {
        // display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog

        // Api is a class in which we define a method getClient() that returns the API Interface class object
        // getUsersList() is a method in API Interface class, in this method we define our API sub url

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        Log.d("yassin", "" + requestCode);

        if (requestCode == 0) {
            String idPub = data.getStringExtra("idPublication");
            int nbCom = data.getIntExtra("nbCommentaire", 0);
            Log.d("yassin", "" + nbCom);
            if (publications != null) {
                Publication pub = null;
                for (Publication publication : publications) {
                    if (publication.get_id().equals(idPub)) {
                        pub = publication;
                        break;
                    }
                }
                if (pub.getComments().size() > nbCom) {
                    while (pub.getComments().size() > nbCom) {
                        pub.getComments().remove(0);

                    }
                } else if (pub.getComments().size() < nbCom) {
                    while (pub.getComments().size() < nbCom) {
                        pub.getComments().add(new CommentPublication("", "", "", ""));

                    }
                }
                publications.get(publications.indexOf(pub)).setComments(pub.getComments());
                refreshnbComment();

            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (publications != null) {
            refreshnbComment();
        }
    }

    static public void refreshnbComment() {
        publicationAdapter.setPub(publications);
    }

}