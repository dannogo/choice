package com.elantix.dopeapp.entities;

/**
 * Created by oleh on 5/23/16.
 */
public class ConversationMemberInfo{
    public String id;
    public String username;
    public String fullname;
    public String email;
    public String bio;
    public String avatar;

    public ConversationMemberInfo(){}

    public ConversationMemberInfo(ConversationMemberInfo another){
        this.id = another.id;
        this.username = another.username;
        this.fullname = another.fullname;
        this.email = another.email;
        this.bio = another.bio;
        this.avatar = another.avatar;
    }
}
