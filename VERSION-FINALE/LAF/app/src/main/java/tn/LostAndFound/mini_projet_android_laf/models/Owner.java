package tn.LostAndFound.mini_projet_android_laf.models;

public class Owner {
    public Owner(String firstName, String lastName, String photo, String _id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.photo = photo;
        this._id = _id;
    }

    String firstName, lastName ,photo, _id;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "Owner{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", photo='" + photo + '\'' +
                ", _id='" + _id + '\'' +
                '}';
    }
}
