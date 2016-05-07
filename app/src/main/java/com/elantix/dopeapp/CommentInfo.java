package com.elantix.dopeapp;

/**
 * Created by oleh on 5/4/16.
 */
public class CommentInfo {

    public String username;
    public String fullname;
    public String email;
    public String avatar;
    public String id;
    public String parent_id;
    public String user_id;
    public String dope_id;
    public String comment;
    public String date_create;

    @Override
    public String toString() {
        String result = "---------------\nusername: " +username+"\nfullname: "+ fullname+"\nemail: "+email+"\navatar: "+avatar+"\nid: "+id+
                "\nparent_id: "+parent_id+"\nuser_id: "+user_id+"\ndope_id: "+dope_id+"\ncomment: "+comment+"\ndate_create: "+date_create;
        return result;
    }
}
