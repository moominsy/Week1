package com.example.contact;

public class Contact {
    private Long Photoid;
    private String Phonenum;
    private String Name;


    public Long getPhotoid(){
        return Photoid;
    }
    public String getPhonenum(){
        return Phonenum;
    }
    public String getName(){
        return Name;
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
}
