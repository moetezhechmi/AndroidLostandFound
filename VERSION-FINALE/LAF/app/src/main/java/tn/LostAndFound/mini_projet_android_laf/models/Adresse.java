package tn.LostAndFound.mini_projet_android_laf.models;

public class Adresse {
   String  _id,location;
    Owner utilisateur;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Owner getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Owner utilisateur) {
        this.utilisateur = utilisateur;
    }

    @Override
    public String toString() {
        return "Adresse{" +
                "_id='" + _id + '\'' +
                ", location='" + location + '\'' +
                ", utilisateur=" + utilisateur +
                '}';
    }
}
