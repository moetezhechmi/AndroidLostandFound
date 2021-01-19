package tn.LostAndFound.mini_projet_android_laf.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import tn.LostAndFound.mini_projet_android_laf.R;
import tn.LostAndFound.mini_projet_android_laf.Retrofit.RetrofitClient;
import tn.LostAndFound.mini_projet_android_laf.Retrofit.Service;
import tn.LostAndFound.mini_projet_android_laf.models.Like;
import tn.LostAndFound.mini_projet_android_laf.models.Owner;
import tn.LostAndFound.mini_projet_android_laf.models.Publication;

public class PublicationAdapter extends RecyclerView.Adapter<PublicationViewHolder> {
    List<Publication> publications = new ArrayList<>();
    Retrofit retrofitClient = RetrofitClient.getInstance();
    Service service = retrofitClient.create(Service.class);
    Context context;

    public PublicationAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public PublicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_publication, parent, false);
        return new PublicationViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull PublicationViewHolder holder, int position) {
        holder.bind(publications.get(position), context);
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(holder.ivDelete.getContext());
                dlgAlert.setIcon(R.drawable.error);

                dlgAlert.setMessage("VOUS ETES SUR DE SUPPRIMER CETTE PUBLICATION?");
                dlgAlert.setTitle("");
                dlgAlert.setPositiveButton("OUI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deletePub(publications.get(position).get_id(), v.getContext());
                        publications.remove(position);
                        notifyDataSetChanged();

                    }
                });
                dlgAlert.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dlgAlert.setCancelable(true);
                dlgAlert.show();

            }
        });

    }

    public void setPub(List<Publication> publications) {
        this.publications = publications;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return publications.size();
    }


    private void deletePub(String idPub, Context context) {
        service.deletePublication(idPub).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        System.out.println("hech " + s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(context, "verifier votre internet", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
