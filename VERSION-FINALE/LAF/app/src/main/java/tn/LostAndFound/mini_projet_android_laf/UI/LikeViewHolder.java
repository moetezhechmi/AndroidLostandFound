package tn.LostAndFound.mini_projet_android_laf.UI;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itgate.uptofind.utils.TinyDB;
import com.squareup.picasso.Picasso;

import tn.LostAndFound.mini_projet_android_laf.R;
import tn.LostAndFound.mini_projet_android_laf.Retrofit.RetrofitClient;
import tn.LostAndFound.mini_projet_android_laf.models.Like;



public class LikeViewHolder extends RecyclerView.ViewHolder{

    ImageView ivUserLike;
    TextView tvUserNamelike;


    public LikeViewHolder(@NonNull View itemView) {
        super(itemView);

        ivUserLike = itemView.findViewById(R.id.iv_publication);
        tvUserNamelike = itemView.findViewById(R.id.tv_usernamelike);




    }

    public void bind(Like like){


        Picasso.get().load(RetrofitClient.server + like.getUserItemModel().getPhoto()).placeholder(R.drawable.profil)
                .error(R.drawable.profil)
                .into(ivUserLike);

        tvUserNamelike.setText(like.getUserItemModel().getFirstName()+" "+like.getUserItemModel().getLastName());


    }

}
