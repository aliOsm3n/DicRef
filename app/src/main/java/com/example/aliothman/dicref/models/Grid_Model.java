package com.example.aliothman.dicref.models;

public class Grid_Model {
    int id ;
    String name  ;
    String photo ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Grid_Model(int id, String name, String photo) {
        this.id = id;
        this.name = name;
        this.photo = photo;
    }
}
