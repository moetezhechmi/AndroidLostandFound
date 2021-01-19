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

import com.itgate.uptofind.utils.TinyDB;

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
import tn.LostAndFound.mini_projet_android_laf.models.GetMesPubResponse;
import tn.LostAndFound.mini_projet_android_laf.models.Publication;


public class MesPublicationsFragment extends Fragment {
    private ArrayList<String> permissionsToRequest;

    RecyclerView recyclerView;
     static MesPublicationsAdapter mesPublicationsAdapter;
    static ArrayList<Publication> publications ;
    tn.LostAndFound.mini_projet_android_laf.Retrofit.Service Service;
    String idU;
    String idP;
    String id;

    SwipeRefreshLayout swipe;
Publication pub;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_mes_publications, container, false);

        Retrofit retrofitClient = RetrofitClient.getInstance();
        Service = retrofitClient.create(Service.class);
        String currentUserId= new TinyDB(getActivity()).getString("_id");
        mesPublicationsAdapter = new MesPublicationsAdapter(getContext());
        Service.getAllPublicationsByOwner(currentUserId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetMesPubResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(GetMesPubResponse response) {
                        System.out.println( "moetez "+response);
                        System.out.println( "user "+currentUserId);
                        publications= new ArrayList(response.getPublications());

                        mesPublicationsAdapter.setPub(publications);


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




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = getView().findViewById(R.id.recyclerViewMESPUB);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mesPublicationsAdapter);


    }

    private void getUserListData() {
        // display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        Log.d("yassin",""+requestCode);

        if(requestCode==0){
            String idPub = data.getStringExtra("idPublication") ;
            int nbCom = data.getIntExtra("nbCommentaire",0);
            Log.d("yassin",""+nbCom);
            if(publications!=null){
                Publication pub = null;
                for(Publication publication:publications){
                    if(publication.get_id().equals(idPub)){
                        pub=publication;
                        break;
                    }
                }
                if (pub.getComments().size()>nbCom){
                    while (pub.getComments().size()>nbCom){
                        pub.getComments().remove( 0);

                    }
                }
                else if(pub.getComments().size()<nbCom)
                {
                    while (pub.getComments().size()<nbCom) {
                        pub.getComments().add(new CommentPublication("","","",""));

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
        if (publications!=null)
        {
            refreshnbComment();
            System.out.println("hello "+publications.get(0).getComments().size());
        }
    }

    static public  void refreshnbComment(){
        mesPublicationsAdapter.setPub(publications);
    }






}