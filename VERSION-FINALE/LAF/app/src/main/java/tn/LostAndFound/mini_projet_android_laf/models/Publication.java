package tn.LostAndFound.mini_projet_android_laf.models;

import java.util.List;

public class Publication {
   String _id;
   List<CommentPublication> comments;
   List<Like> likes;
   Owner owner;

    String name_file,title,textPub,nom,createdAt,rue,ville,gouvernaurat,latitude,longitude;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public List<CommentPublication> getComments() {
        return comments;
    }

    public void setComments(List<CommentPublication> comments) {
        this.comments = comments;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public String getName_file() {
        return name_file;
    }

    public void setName_file(String name_file) {
        this.name_file = name_file;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTextPub() {
        return textPub;
    }

    public void setTextPub(String textPub) {
        this.textPub = textPub;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public String getRue() {
        return rue;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getGouvernaurat() {
        return gouvernaurat;
    }

    public void setGouvernaurat(String gouvernaurat) {
        this.gouvernaurat = gouvernaurat;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Publication{" +
                "_id='" + _id + '\'' +
                ", comments=" + comments +
                ", likes=" + likes +
                ", owner=" + owner +
                ", name_file='" + name_file + '\'' +
                ", title='" + title + '\'' +
                ", textPub='" + textPub + '\'' +
                ", nom='" + nom + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", rue='" + rue + '\'' +
                ", ville='" + ville + '\'' +
                ", gouvernaurat='" + gouvernaurat + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }
}


