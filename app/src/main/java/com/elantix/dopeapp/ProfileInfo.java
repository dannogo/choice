package com.elantix.dopeapp;

/**
 * Created by oleh on 4/27/16.
 */
public class ProfileInfo {
    String id;
    String fb_id;
    String tw_id;
    String email;
    String username;
    String fullname;
    String bio;
    String avatar;
    String cover;
    String gender;
    String phone;
    String birthday;
    String date_reg;
    String ban;
    int dopes;
    int followers;
    int followings;
    int follow;

    @Override
    public String toString() {
        String stringRepresentation = "id: "+id+"\nfb_id: "+fb_id+"\ntw_id: "+tw_id+"\nemail: "+email+
                "\nusername: "+username+"\nfullname: "+fullname+"\nbio: "+bio +"\navatar: "+avatar+
                "\ncover: "+cover+"\ngender: "+gender+"\nphone: "+phone+"\nbirthday: "+birthday+
                "\ndate_reg: "+date_reg+"\nban: "+ban+"\ndopes: "+dopes + "\nfollowers: "+followers+
                "\nfollowings: "+followings+"\nfollow: "+follow;
        return stringRepresentation;
    }
}

