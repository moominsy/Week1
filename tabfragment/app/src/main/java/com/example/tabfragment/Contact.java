package com.example.tabfragment;

import android.net.Uri;

public class Contact {
    private Long Photoid;
    private String Phonenum;
    private String Name;
    private Uri URL;


    public Long getPhotoid(){
        return Photoid;
    }
    public String getPhonenum(){
        return Phonenum;
    }
    public String getName(){
        return Name;
    }
    public Uri getURL(){
        return URL;
    }

    public void setPhotoid(Long Photoid){
        this.Photoid = Photoid;
    }
    public void setPhonenum(String Phonenum){
        this.Phonenum = Phonenum;
    }
    public void setName(String Name){
        this.Name = Name;
    }
    public void setURL(Uri URL){ this.URL = URL; }
}
