package tn.LostAndFound.mini_projet_android_laf.UI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tn.LostAndFound.mini_projet_android_laf.R;
import tn.LostAndFound.mini_projet_android_laf.models.Like;
import tn.LostAndFound.mini_projet_android_laf.models.Publication;

public class LikeAdapter extends RecyclerView.Adapter<LikeViewHolder> {
    List<Like> likes= new ArrayList<>();



    @NonNull
    @Override
    public LikeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_list_like, parent, false);
        return new LikeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LikeViewHolder holder, int position) {
        holder.bind(likes.get(position));
    }

   public void setLike(List<Like> likes){
        this.likes=likes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return likes.size();
    }
}
