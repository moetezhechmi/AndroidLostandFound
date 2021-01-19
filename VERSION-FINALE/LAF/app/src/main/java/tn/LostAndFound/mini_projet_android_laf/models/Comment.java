package tn.LostAndFound.mini_projet_android_laf.models;

public class Comment {
    public Comment(String date, String _id, String textPub, Owner author) {
        this.date = date;
        this._id = _id;
        this.textPub = textPub;
        this.author = author;
    }

    String date,_id,textPub;
    Owner author;

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

    public String getTextPub() {
        return textPub;
    }

    public void setTextPub(String textPub) {
        this.textPub = textPub;
    }

    public Owner getAuthor() {
        return author;
    }

    public void setAuthor(Owner author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "date='" + date + '\'' +
                ", _id='" + _id + '\'' +
                ", textPub='" + textPub + '\'' +
                ", author=" + author +
                '}';
    }
}
