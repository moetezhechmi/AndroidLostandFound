package tn.LostAndFound.mini_projet_android_laf.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetPubResponse {
    int status;
    String message;
    @SerializedName("publication")
    List<Publication> publications;


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

    public List<Publication> getPublications() {
        return publications;
    }

    public void setPublications(List<Publication> publications) {
        this.publications = publications;
    }

    @Override
    public String toString() {
        return "GetPubResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", publications=" + publications +
                '}';
    }
}

