package tn.LostAndFound.mini_projet_android_laf.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetCommentsResponse {
    int status;
    String message;
    @SerializedName("comments")
    List<Comment> comments;


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

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "GetCommentResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", comments=" + comments +
                '}';
    }
}


