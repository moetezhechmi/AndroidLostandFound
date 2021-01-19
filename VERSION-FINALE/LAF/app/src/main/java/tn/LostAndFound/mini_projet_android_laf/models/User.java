package tn.LostAndFound.mini_projet_android_laf.models;

import java.util.List;

public class User {
    int status;
    String token;
    private List<UserItemModel> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<UserItemModel> getData() {
        return data;
    }

    public void setData(List<UserItemModel> data) {
        this.data = data;
    }
    @Override
    public String toString() {
        return "User{" +
                "status=" + status +
                ", token='" + token + '\'' +
                '}';
    }
}
