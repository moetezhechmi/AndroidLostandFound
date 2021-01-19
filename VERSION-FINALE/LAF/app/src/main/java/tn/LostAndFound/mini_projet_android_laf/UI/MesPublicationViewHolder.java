package tn.LostAndFound.mini_projet_android_laf.UI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itgate.uptofind.utils.TinyDB;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.sql.Timestamp;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import tn.LostAndFound.mini_projet_android_laf.R;
import tn.LostAndFound.mini_projet_android_laf.Retrofit.RetrofitClient;
import tn.LostAndFound.mini_projet_android_laf.Retrofit.Service;
import tn.LostAndFound.mini_projet_android_laf.models.Like;
import tn.LostAndFound.mini_projet_android_laf.models.Publication;

public class MesPublicationViewHolder extends RecyclerView.ViewHolder  {
    ImageView ivPublication, ivLike, ivCommentaire,ivDelete;
    CircleImageView ivUser;
    TextView tvUserName, tvTitle, tvDescription, tvTime, tvnbcomment, tvnblike,tvAdresse;
    Button btnTrouve;
    Retrofit retrofitClient = RetrofitClient.getInstance();
    Service service = retrofitClient.create(Service.class);
    HomeFragment hf = new HomeFragment();
    String currentUserId ;
    Boolean liked=false;

    public MesPublicationViewHolder(@NonNull View itemView) {
        super(itemView);
        ivPublication = itemView.findViewById(R.id.iv_publication);
        ivLike = itemView.findViewById(R.id.iv_like);
        ivCommentaire = itemView.findViewById(R.id.iv_commentaire);
        ivUser = itemView.findViewById(R.id.iv_user);
        tvUserName = itemView.findViewById(R.id.tv_username);
        btnTrouve = itemView.findViewById(R.id.btn_trouve);
        tvTitle = itemView.findViewById(R.id.tv_title);
        tvDescription = itemView.findViewById(R.id.tv_description);
        tvTime = itemView.findViewById(R.id.tv_time);
        tvnbcomment = itemView.findViewById(R.id.tv_nbcomment);
        tvnblike= itemView.findViewById(R.id.tv_nblike);
        ivDelete= itemView.findViewById(R.id.iv_delete);
        tvAdresse= itemView.findViewById(R.id.tv_adresse);

        currentUserId= new TinyDB(ivPublication.getContext()).getString("_id");

    }

    public void bind(Publication publication, Context context) {
        int nb = 0;
        int nbLike;

        liked = false;
        Picasso.get().load(RetrofitClient.server + "/uploads/images/publications/" + publication.getName_file()).placeholder(R.drawable.profil)
                .error(R.drawable.profil)
                .into(ivPublication);

        Picasso.get().load(RetrofitClient.server + publication.getOwner().getPhoto()).placeholder(R.drawable.profil)
                .error(R.drawable.profil)
                .into(ivUser);



        for (Like like : publication.getLikes()) {
            if (like.getUser().equals(currentUserId)) {
                liked = true;
                break;
            }
        }
        if (liked) {
            ivLike.setImageResource(R.drawable.coeurrouge);
        } else {
            ivLike.setImageResource(R.drawable.coeur);
        }
        //

        if (publication.getOwner().get_id().equals(currentUserId)){
            ivDelete.setVisibility(View.VISIBLE);

        }
        else {
            ivDelete.setVisibility(View.INVISIBLE);

        }

        tvnblike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                context.startActivity(new Intent(context, ListLikeActivity.class));
            }
        });

        ivLike.setOnClickListener(l -> {
                    int nbLikes=Integer.parseInt(tvnblike.getText().toString());

                    String idPub =
                            publication.get_id();
                    if (liked){
                        dislikeCall(idPub);
                        nbLikes--;
                        liked=false;
                    }else{
                        likeCall(idPub);
                        nbLikes++;
                        liked=true;
                    }
                    tvnblike.setText(String.valueOf(nbLikes));


                }
        );
        tvnbcomment.setText(""+publication.getComments().size());
        tvnblike.setText(""+publication.getLikes().size());



        ivCommentaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idPub =
                        publication.get_id();
                Context context = v.getContext();
                Intent intent = new Intent(context, CommentaireActivity.class);
                intent.putExtra("idPub", idPub);
                ( (Activity)context).startActivityForResult(intent,0);
                new TinyDB(v.getContext()).putString("idPub",idPub);

            }
        });

        String format2 = publication.getCreatedAt();

        String string = publication.getCreatedAt().substring(0, publication.getCreatedAt().length() - 1);


        PrettyTime pr = new PrettyTime();
        format2 = pr.format(new Date(Timestamp.from(Timestamp.valueOf(string.replace('T', ' ')).toInstant().plus(1, ChronoUnit.HOURS)).getTime()));
        System.out.println("myformat " +format2);

        tvAdresse.setText(publication.getRue()+" "+publication.getVille()+" "+publication.getGouvernaurat());
        tvTime.setText(format2);
        btnTrouve.setText("J'ai " + publication.getNom());
        tvTitle.setText(publication.getTitle());
        tvDescription.setText(publication.getTextPub());
        tvUserName.setText(publication.getOwner().getFirstName() + " " + publication.getOwner().getLastName());


    }

    private void likeCall(String idPub) {
        service.AddLikeToPub(idPub, currentUserId).subscribeOn(Schedulers.io())
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
                        Toast.makeText(ivPublication.getContext(), "verifier votre internet", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        ivLike.setImageResource(R.drawable.coeurrouge);
    }

    private void dislikeCall(String idPub) {
        service.dislikeLikeToPub(idPub, currentUserId).subscribeOn(Schedulers.io())
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
                        Toast.makeText(ivPublication.getContext(), "verifier votre internet", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        ivLike.setImageResource(R.drawable.coeur);
    }


    private void deletePub(String idPub) {
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
                        Toast.makeText(ivPublication.getContext(), "verifier votre internet", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }



}
