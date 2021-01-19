package tn.LostAndFound.mini_projet_android_laf.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetLikeResponse {
    int status;
    String message;

    List<Like> likes;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    @Override
    public String toString() {
        return "GetLikeResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", likes=" + likes +
                '}';
    }
}
