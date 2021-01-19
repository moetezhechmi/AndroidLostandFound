package tn.LostAndFound.mini_projet_android_laf.models;

public class CommentPublication {
    String date,_id,textPub,author;

    public CommentPublication(String date, String _id, String textPub, String author) {
        this.date = date;
        this._id = _id;
        this.textPub = textPub;
        this.author = author;
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

public String getTextPub() {
        return textPub;
        }

public void setTextPub(String textPub) {
        this.textPub = textPub;
        }


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "CommentPublication{" +
                "date='" + date + '\'' +
                ", _id='" + _id + '\'' +
                ", textPub='" + textPub + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
