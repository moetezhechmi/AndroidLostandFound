package tn.LostAndFound.mini_projet_android_laf.UI;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.itgate.uptofind.utils.TinyDB;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import tn.LostAndFound.mini_projet_android_laf.R;
import tn.LostAndFound.mini_projet_android_laf.Retrofit.RetrofitClient;
import tn.LostAndFound.mini_projet_android_laf.Retrofit.Service;
import tn.LostAndFound.mini_projet_android_laf.models.Comment;
import tn.LostAndFound.mini_projet_android_laf.models.GetPubResponse;
import tn.LostAndFound.mini_projet_android_laf.models.Publication;

public class CommentaireViewHolder extends RecyclerView.ViewHolder {
    TextView textCom, textDate;
    TextView tvUsername;
    ImageView ivUsername, ivUser, ivdelete;
    Retrofit retrofitClient = RetrofitClient.getInstance();
    Service service = retrofitClient.create(Service.class);
    String currentUserId;
    CardView cardView;
    Date date;

    public CommentaireViewHolder(@NonNull View itemView) {
        super(itemView);
        textCom = itemView.findViewById(R.id.tv_textCom);
        tvUsername = itemView.findViewById(R.id.tv_usernameCom);
        ivUsername = itemView.findViewById(R.id.iv_userCom);
        ivUser = itemView.findViewById(R.id.iv_userCom);
        textDate = itemView.findViewById(R.id.tv_dateCom);
        ivdelete = itemView.findViewById(R.id.iv_delete);
        cardView = itemView.findViewById(R.id.cardView);

        currentUserId = new TinyDB(textCom.getContext()).getString("_id");


    }


    public void bind(Comment comment) throws ParseException {
        String format2 = comment.getDate();

        String string = comment.getDate().substring(0, comment.getDate().length() - 1);


        PrettyTime pr = new PrettyTime();
        format2 = pr.format(new Date(Timestamp.from(Timestamp.valueOf(string.replace('T', ' ')).toInstant().plus(1, ChronoUnit.HOURS)).getTime()));


        String idCom = comment.get_id();
        System.out.println("aya " + idCom);

        textCom.setText(comment.getTextPub());
        tvUsername.setText(comment.getAuthor().getFirstName() + " " + comment.getAuthor().getLastName());
        Picasso.get().load(RetrofitClient.server + comment.getAuthor().getPhoto()).placeholder(R.drawable.profil)
                .error(R.drawable.profil)
                .into(ivUser);
        textDate.setText(format2);


        if (currentUserId.equals(comment.getAuthor().get_id())) {
            ivdelete.setVisibility(View.VISIBLE);
        } else {
            ivdelete.setVisibility(View.INVISIBLE);
        }


    }


}
