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
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!CommentInfo.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final CommentInfo other = (CommentInfo) obj;
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        String result = "---------------\nusername: " +username+"\nfullname: "+ fullname+"\nemail: "+email+"\navatar: "+avatar+"\nid: "+id+
                "\nparent_id: "+parent_id+"\nuser_id: "+user_id+"\ndope_id: "+dope_id+"\ncomment: "+comment+"\ndate_create: "+date_create;
        return result;
    }
}
