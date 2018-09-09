package com.example.aliothman.dicref.models;

import java.util.ArrayList;

public class Parent {
    private  int id ;
    private  String text;
    private  String textAR;
    private  String textEn;
   // String img ;
    private ArrayList<Child> children;

    public String getTextAR() {
        return textAR;
    }

    public String getTextEn() {
        return textEn;
    }

    public void setTextAR(String textAR) {
        this.textAR = textAR;
    }

    public void setTextEn(String textEn) {
        this.textEn = textEn;
    }

//    public String getImg() {
//        return img;
//    }
//
//    public void setImg(String img) {
//        this.img = img;
//    }

    // ArrayList to store Child objects
    public ArrayList<Child> getChildren()
    {
        return children;
    }

    public void setChildren(ArrayList<Child> children)
    {
        this.children = children;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
