package com.elantix.dopeapp;

import android.net.Uri;

import java.util.HashMap;

/**
 * Created by oleh on 4/22/16.
 */
public class DopeInfo {

    String fullname;
    String username;
    String email;
    Uri avatar;
    String id;
    String userId;
    String question;
    Uri photo1;
    Uri photo2;
    Uri photoSoc;
    String dateCreate;
    String top10;
    String top100;
    String ban;
    int votesAll;
    int votes1;
    int votes2;
    int percent1;
    int percent2;
    int comments;
    int myVote;

    @Override
    public String toString() {

        String info = "========================================================" +
                "\nfullname: "+fullname+"\nusername: "+username+"\nemail: "+email+"avatar: "+avatar+
                "\nid: "+id+"\nuserId: "+userId+"\nquestion: "+question+"\nphoto1: "+photo1+"\nphoto2: "+photo2+
                "\ndateCreate: "+dateCreate+"\ntop10: "+top10+"\ntop100: "+top100+"\nban: "+ban+
                "\nvotesAll: "+votesAll+"\nvotes1: "+votes1+"\nvotes2: "+votes2+"\npercent1: "+percent1+
                "\npercent2: "+percent2+"\ncomments: "+comments+"\nmyVote: "+myVote;
        return info;
    }

}
