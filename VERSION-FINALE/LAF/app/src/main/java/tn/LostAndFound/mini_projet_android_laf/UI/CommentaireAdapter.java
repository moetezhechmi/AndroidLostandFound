package tn.LostAndFound.mini_projet_android_laf.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.itgate.uptofind.utils.TinyDB;

import java.text.ParseException;
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
import tn.LostAndFound.mini_projet_android_laf.models.Comment;

public class CommentaireAdapter extends RecyclerView.Adapter<CommentaireViewHolder>{
        List<Comment> comments= new ArrayList<>();
        Retrofit retrofitClient = RetrofitClient.getInstance();
        Service service = retrofitClient.create(Service.class);


@NonNull
@Override
public CommentaireViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_commentaire, parent, false);
        return new CommentaireViewHolder(view);
        }

@Override
public void onBindViewHolder(@NonNull CommentaireViewHolder holder, int position) {

    try {
        holder.bind(comments.get(position));
    } catch (ParseException e) {
        e.printStackTrace();
    }

    holder.ivdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(holder.ivdelete.getContext());
                        dlgAlert.setIcon(R.drawable.error);

                        dlgAlert.setMessage("VOUS ETES SUR DE SUPPRIMER CE COMMENTAIRE?");
                        dlgAlert.setTitle("");
                        dlgAlert.setPositiveButton("OUI", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                        deleteCom(comments.get(position).get_id(),v.getContext());
                                        comments.remove(position);
                                    CommentaireActivity.nbCommentaire--;
                                        notifyDataSetChanged();

                                }
                        });
                        dlgAlert.setNegativeButton("NON", new DialogInterface.OnClickListener() {
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
public void setComments(List<Comment> comments){
        this.comments=comments;
        notifyDataSetChanged();
        }
@Override
public int getItemCount() {
        return comments.size();
        }

        private void deleteCom(String idCom, Context context) {
                String idPubli =    new TinyDB(context).getString("idPub");
                System.out.println("lotfi " +idCom);
                service.deleteComment(idPubli,idCom).subscribeOn(Schedulers.io())
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
