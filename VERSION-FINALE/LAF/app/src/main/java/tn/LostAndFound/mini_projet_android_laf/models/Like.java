package tn.LostAndFound.mini_projet_android_laf.models;

public class Like {
    String date, _id, user;
    UserItemModel userItemModel;

    public UserItemModel getUserItemModel() {
        return userItemModel;
    }

    public void setUserItemModel(UserItemModel userItemModel) {
        this.userItemModel = userItemModel;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Like{" +
                "date='" + date + '\'' +
                ", _id='" + _id + '\'' +
                ", user='" + user + '\'' +
                ", userItemModel=" + userItemModel +
                '}';
    }
}
