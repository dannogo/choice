package com.elantix.dopeapp.entities;

import java.util.ArrayList;

/**
 * Created by oleh on 5/23/16.
 */
public class ConversationInfo {
    public String id;
    public String dialogs_id;
    public String users_id;
    public String creator_id;
    public String is_read;
    public String is_group;
    public String fullname;
    public String username;
    public String avatar;
    public String date_updated;
    public String last_message;
    public int unreads;
    public ArrayList<ConversationMemberInfo> members;

    public ConversationInfo(){
        members = new ArrayList<>();
    }

    public ConversationInfo(ConversationInfo another){
        this.id = another.id;
        this.dialogs_id = another.dialogs_id;
        this.users_id = another.users_id;
        this.creator_id = another.creator_id;
        this.is_read = another.is_read;
        this.is_group = another.is_group;
        this.fullname = another.fullname;
        this.username = another.username;
        this.avatar = another.avatar;
        this.date_updated = another.date_updated;
        this.last_message = another.last_message;
        this.unreads = another.unreads;
        this.members = new ArrayList<>(another.members);
    }


}
