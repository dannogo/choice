package com.elantix.dopeapp.entities;

import java.io.Serializable;

/**
 * Created by oleh on 5/28/16.
 */
public class ChatMessage implements Serializable{

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
}
