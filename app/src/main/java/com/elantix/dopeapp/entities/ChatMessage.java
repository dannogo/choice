package com.elantix.dopeapp.entities;

import java.io.Serializable;

/**
 * Created by oleh on 5/28/16.
 */
public class ChatMessage implements Serializable{

    private final int DATELINE = 0, OTHERS_MSG = 1, MY_MSG = 2, OTHERS_PROPOSAL = 3, MY_PROPOSAL = 4;

    public String id;
    public String dialog_id;
    public String sender;
    public String message;
    public String date_send;
    public String is_read;
    public String deletes;
    public String photo1;
    public String photo2;
    public String photoSoc;
    public String upload;
    public String avatar;
    public String username;
    public String fullname;

    public int votes;
    public int leftVote;
    public int rightVote;
    public int leftPercent;
    public int rightPercent;
    public int myVote;

    public int proposalNum; // for choosing direction of animation

    public int viewType;

    public ChatMessage(){

    }

    public ChatMessage(ChatMessage another){
        this.id = another.id;
        this.dialog_id = another.dialog_id;
        this.sender = another.sender;
        this.message = another.message;
        this.date_send = another.date_send;
        this.is_read = another.is_read;
        this.deletes = another.deletes;
        this.photo1 = another.photo1;
        this.photo2 = another.photo2;
        this.photoSoc = another.photoSoc;
        this.upload = another.upload;
        this.avatar = another.avatar;
        this.username = another.username;
        this.fullname = another.fullname;

        this.votes = another.votes;
        this.leftVote = another.leftVote;
        this.rightVote = another.rightVote;
        this.myVote = another.myVote;

        this.proposalNum = another.proposalNum;

        this.viewType = another.viewType;
    }
}
