package com.elantix.dopeapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.elantix.dopeapp.entities.ChatMessage;
import com.elantix.dopeapp.entities.ConversationInfo;
import com.elantix.dopeapp.entities.ConversationMemberInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by oleh on 5/28/16.
 */
public class HttpChat {

    private Context mContext;

    public HttpChat(Context context) {
        mContext = context;
    }


    public void getCrossFollowingFriends(String token, String query, Integer page, Integer count, String exlude_ids){

        String[] params = {token, query, String.valueOf(page), String.valueOf(count), exlude_ids};
        GetCrossFollowingFriends task = new GetCrossFollowingFriends();
        task.execute(params);
    }

    public void getConversationList(String token, String uid, String p, String count){
        String[] params = {token, uid, p, count};
        GetConversationsList task = new GetConversationsList();
        task.execute(params);
    }

    /**
     *
     * @param token
     * @param uids - coma-separated list of uids to be included in to conversation
     */
    public void createConversation(String token, String uids){
        String[] params = {token, uids};
        CreateConversation task = new CreateConversation();
        task.execute(params);
    }

    public void addMember(String token, String dialogId, String uids){
        String[] params = {token, dialogId, uids};
        AddMemberTask task = new AddMemberTask();
        task.execute(params);
    }

    public void deleteConversation(String token, String dialog_id, String dialog_position){
        String[] params = {token, dialog_id, dialog_position};
        DeleteConversationTask task = new DeleteConversationTask();
        task.execute(params);
    }

    public void leaveConversation(String token, String dialog_id, String convPosition){
        String[] params = {token, dialog_id, convPosition};
        LeaveConversationTask task = new LeaveConversationTask();
        task.execute(params);
    }

    public void getRecent(String token){
        GetRecentTask task = new GetRecentTask();
        task.execute(token);
    }

    public void removeMember(String token, String dialogId, String userId){
        String[] params = {token, dialogId, userId};
        RemoveMemberTask task = new RemoveMemberTask();
        task.execute(params);
    }

    public void sendDopeToChat(String token, String dialog_id, String message, String photo1, String photo2){
        String[] params = {token, dialog_id, message, photo1, photo2};
        SendDopeToChatTask task = new SendDopeToChatTask();
        task.execute(params);
    }

    /**
     *
     * @param token
     * @param message_id
     * @param vote  - 1 for left, and 2 for right
     */
    public void chatVote(String token, String message_id, String vote){
        String[] params = {token, message_id, vote};
        ChatVoteTask task = new ChatVoteTask();
        task.execute(params);
    }

    public void getDialogHistory(String token, String dialogId, String p, String count){
        String[] params = {token, dialogId, p, count};
        GetDialogHistory task = new GetDialogHistory();
        task.execute(params);
    }

    public class GetDialogHistory extends AsyncTask<String, Void, Object[]>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mContext instanceof ChatActivity) {
                ((ChatActivity) mContext).mProgressDialog = ProgressDialog.show(mContext, null, "Please wait...", true);
            }else if (mContext instanceof MessageActivity){
                ((MessageActivity) mContext).mProgressDialog = ProgressDialog.show(mContext, null, "Please wait...", true);
            }
        }

        @Override
        protected Object[] doInBackground(String... params) {

            Log.e("HttpChat GetDialog", "Dialog id: "+params[1]);
            String urlStr = "http://dopeapi.elantix.net/im.dialog";
            String paramStr = "token="+params[0]+"&dialog="+params[1];
            if (params[2] != null) paramStr += "&p="+params[2];
            if (params[3] != null) paramStr += "&count="+params[3];

            String response = Utilities.requestToServerPOST(urlStr, paramStr);

            try {
                JSONObject json = new JSONObject(response);
                boolean success = json.getBoolean("success");

                if (success){
                    JSONObject responseObj = json.getJSONObject("response");
                    int all_pages = responseObj.getInt("all_pages");
                    int count = responseObj.getInt("count");
                    JSONArray list = responseObj.getJSONArray("list");

                    ArrayList<ChatMessage> messages = new ArrayList<>();

                    for (int i=0; i< list.length(); i++){
                        JSONObject item = list.getJSONObject(i);
                        ChatMessage messageInfo = new ChatMessage();
                        messageInfo.id = item.getString("id");
                        messageInfo.dialog_id = item.getString("dialog_id");
                        messageInfo.sender = item.getString("sender");
                        messageInfo.message = item.getString("message");
                        messageInfo.date_send = item.getString("date_send");
                        messageInfo.photo1 = item.getString("photo1");
                        messageInfo.photo2 = item.getString("photo2");
                        messageInfo.photoSoc = item.getString("photoSoc");
                        messageInfo.username = item.getString("username");
                        messageInfo.fullname = item.getString("fullname");
                        messageInfo.avatar = item.getString("avatar");

                        messages.add(messageInfo);
                    }
                    Object[] result = {success, count, messages};
                    return  result;
                }else{
                    String response_text = json.getString("response_text");
                    Object[] result = {success, response_text};
                    return  result;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object[] result) {
            super.onPostExecute(result);
            if (mContext instanceof ChatActivity) {
                ((ChatActivity) mContext).mProgressDialog.dismiss();
            }else if (mContext instanceof MessageActivity){
                ((MessageActivity) mContext).mProgressDialog.dismiss();
            }

            if (result != null){
                if ((boolean)result[0]){
                    // Do stuff with result[2] which contains ArrayList of ChatMessage-s
                    ((ChatActivity)mContext).setDataToAdapter((ArrayList<ChatMessage>)result[2]);
                }else {
                    Toast.makeText(mContext, ""+result[1], Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    // NOT COMPLETED
    public class ChatVoteTask extends AsyncTask<String, Void, Object[]>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ((MessageActivity) mContext).mProgressDialog = ProgressDialog.show(mContext, null, "Please wait...", true);
        }

        @Override
        protected Object[] doInBackground(String... params) {

            String urlStr = "http://dopeapi.elantix.net/im.vote";
            String paramStr = "token="+params[0]+"&message_id="+params[1]+"&vote="+params[2];

            String response = Utilities.requestToServerPOST(urlStr, paramStr);
            try{
                JSONObject json = new JSONObject(response);
//                String
                boolean success = json.getBoolean("success");



            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object[] objects) {
            super.onPostExecute(objects);
            ((MessageActivity) mContext).mProgressDialog.dismiss();
        }
    }

    public class SendDopeToChatTask extends AsyncTask<String, Void, Object[]>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            ((TabPlusActivity) mContext).mProgressDialog = ProgressDialog.show(mContext, null, "Please wait...", true);
            if (mContext instanceof TabPlusActivity) {
                ((TabPlusActivity) mContext).mProgressDialog.setMessage("Sending dope to server...");
            }else{
                ((ChatActivity) mContext).mProgressDialog = ProgressDialog.show(mContext, null, "Please wait...", true);
            }
        }

        @Override
        protected Object[] doInBackground(String... params) {
            String charset = "UTF-8";
            String requestURL = "http://dopeapi.elantix.net/im.send";
            Log.e("HttpChat", "sendDopeToChat Dialog id: "+params[1]);
            try {
                MultipartUtility multipart = new MultipartUtility(requestURL, charset);
                multipart.addFormField("token", params[0]);
                multipart.addFormField("dialog", params[1]);
                multipart.addFormField("message", params[2]);
                if (params[3] != null) multipart.addFilePart("photo1", new File(params[3]));
                if (params[4] != null) multipart.addFilePart("photo2", new File(params[4]));

                List<String> response = multipart.finish();
                Log.w("HttpChat SendDopeToChat", "" + response.get(0));

                try {
                    JSONObject json = new JSONObject(response.get(0));
                    Boolean success = json.getBoolean("success");
                    String response_text = json.getString("response_text");
                    ChatMessage messageInfo = null;

                    if (success){
                        JSONObject responseObj = json.getJSONObject("response");
                        String outer_dialog_id = responseObj.getString("dialog_id");
                        JSONObject message = responseObj.getJSONObject("message");

                        messageInfo = new ChatMessage();
                        messageInfo.id = message.getString("id");
                        messageInfo.dialog_id = message.getString("dialog_id");
                        messageInfo.sender = message.getString("sender");
                        messageInfo.message = message.getString("message");
                        messageInfo.date_send = message.getString("date_send");
                        messageInfo.is_read = message.getString("is_read");
                        messageInfo.deletes = message.getString("deletes");
                        messageInfo.photo1 = message.getString("photo1");
                        messageInfo.photo2 = message.getString("photo2");
                        messageInfo.photoSoc = message.getString("photoSoc");
                        messageInfo.upload = message.getString("upload");
                        messageInfo.avatar = message.getString("avatar");
                        messageInfo.username = message.getString("username");
                        messageInfo.fullname = message.getString("fullname");

                    }

                    Object[] result = {success, response_text, messageInfo, params[1]};
                    return result;

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object[] result) {
            super.onPostExecute(result);
            if (mContext instanceof TabPlusActivity){
                ((TabPlusActivity) mContext).mProgressDialog.dismiss();
            }else{
                ((ChatActivity) mContext).mProgressDialog.dismiss();
            }

            if (result != null){
                if ((boolean)result[0]){
                    // Do stuff with result[2] that contains ChatMessage object

                    if (mContext instanceof TabPlusActivity) {
                        TabPlusActivity tabPlusActivity = (TabPlusActivity) mContext;
                        Intent intent = new Intent(tabPlusActivity, ChatActivity.class);
                        intent.putExtra("dialog_id", "" + result[3]);
//                    intent.putExtra("messageObj", serializedObject);
                        tabPlusActivity.startActivity(intent);
                        tabPlusActivity.finish();
                    }else {
                        ChatMessage messageInfo = (ChatMessage)result[2];
                        ((ChatActivity) mContext).showNewMessage(messageInfo);
                    }

                }else{
                    Toast.makeText(mContext, ""+result[1], Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public class RemoveMemberTask extends AsyncTask<String, Void, Object[]>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ((MessageActivity) mContext).mProgressDialog = ProgressDialog.show(mContext, null, "Please wait...", true);
        }

        @Override
        protected Object[] doInBackground(String... params) {

            String urlStr = "http://dopeapi.elantix.net/im.removemember";
            String paramStr = "token="+params[0]+"&dialog="+params[1]+"&uid="+params[2];

            String response = Utilities.requestToServerPOST(urlStr, paramStr);

            try {
                JSONObject json = new JSONObject(response);
                boolean success = json.getBoolean("success");
                String response_text = json.getString("response_text");

                Object[] result = {success, response_text};
                return result;

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object[] result) {
            super.onPostExecute(result);
            ((MessageActivity) mContext).mProgressDialog.dismiss();

            if (result != null){
                if ((boolean)result[0]){
                    Utilities.showExtremelyShortToast(mContext, (String)result[1], 500);
                    // Do stuff
                }else{
                    Toast.makeText(mContext, ""+result[1], Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public class GetRecentTask extends AsyncTask<String, Void, Object[]>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ((MessageActivity) mContext).mProgressDialog = ProgressDialog.show(mContext, null, "Please wait...", true);
        }

        @Override
        protected Object[] doInBackground(String... params) {
            String urlStr = "http://dopeapi.elantix.net/im.recent";
            String paramStr = "token="+params[0];

            String response = Utilities.requestToServerPOST(urlStr, paramStr);
            try {
                JSONObject json = new JSONObject(response);
                boolean success = json.getBoolean("success");
                Object secondParam;
                if (success){
                    JSONObject responseObj = json.getJSONObject("response");
                    Integer count = responseObj.getInt("count");
                    secondParam = count;
                }else{
                    String response_text = json.getString("response_text");
                    secondParam = response_text;
                }
                Object[] result = {success, secondParam};
                return result;

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object[] result) {
            super.onPostExecute(result);
            ((MessageActivity) mContext).mProgressDialog.dismiss();
            if (result != null){
                if ((boolean)result[0]){
                    // Do stuff with params[1] which contains count value;
                }else{
                    Toast.makeText(mContext, ""+result[1], Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public class LeaveConversationTask extends AsyncTask<String, Void, Object[]>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ((MessageActivity) mContext).mProgressDialog = ProgressDialog.show(mContext, null, "Please wait...", true);
        }

        @Override
        protected Object[] doInBackground(String... params) {

            String urlStr = "http://dopeapi.elantix.net/im.leave";
            String paramStr = "token="+params[0]+"&dialog="+params[1];

            String response = Utilities.requestToServerPOST(urlStr, paramStr);

            try{
                JSONObject json = new JSONObject(response);
                boolean success = json.getBoolean("success");
                String response_text = json.getString("response_text");

                Object[] result = {success, response_text, params[2]};
                return result;

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object[] result) {
            super.onPostExecute(result);
            ((MessageActivity) mContext).mProgressDialog.dismiss();

            if (result != null){
                if ((boolean)result[0]){
                    Utilities.showExtremelyShortToast(mContext, (String)result[1], 500);
                    // Do stuff
                    if (mContext instanceof MessageActivity){
                        FragmentNewMessage fragment = ((FragmentNewMessage) ((MessageActivity) mContext).mCurrentFragment);
                        int position = Integer.parseInt((String)result[2]);
                        fragment.removeConversation(position);
                    }
                }else{
                    Toast.makeText(mContext, ""+result[1], Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    public class DeleteConversationTask extends AsyncTask<String, Void, Object[]>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ((MessageActivity) mContext).mProgressDialog = ProgressDialog.show(mContext, null, "Please wait...", true);
        }

        @Override
        protected Object[] doInBackground(String... params) {
            String urlStr = "http://dopeapi.elantix.net/im.delete";
            String paramStr = "token="+params[0]+"&dialog="+params[1];

            String response = Utilities.requestToServerPOST(urlStr, paramStr);

            try{
                JSONObject json = new JSONObject(response);
                boolean success = json.getBoolean("success");
                String response_text = json.getString("response_text");

                Object[] result = {success, response_text, params[2]};
                return result;

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object[] result) {
            super.onPostExecute(result);
            MessageActivity activity = ((MessageActivity) mContext);
            activity.mProgressDialog.dismiss();
            if (result != null){
                if ((boolean)result[0]){
                    Utilities.showExtremelyShortToast(mContext, (String)result[1], 500);
                    // Do stuff
                    if (activity.mCurrentFragment instanceof FragmentNewMessage){
                        FragmentNewMessage myFrag = ((FragmentNewMessage) activity.mCurrentFragment);
                        int position = Integer.parseInt((String) result[2]);
                        myFrag.adapter.mConvs.remove(position);
                        myFrag.adapter.notifyItemRemoved(position);
                    }
                }else {
                    Toast.makeText(mContext, ""+result[1], Toast.LENGTH_SHORT).show();
                }
            }

        }
    }


    public class AddMemberTask extends AsyncTask<String, Void, Object[]>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ((MessageActivity) mContext).mProgressDialog = ProgressDialog.show(mContext, null, "Please wait...", true);
        }

        @Override
        protected Object[] doInBackground(String... params) {

            String urlStr = "http://dopeapi.elantix.net/im.addmember";
            String paramStr = "token="+params[0]+"&dialogId="+params[1]+"&uids="+params[2];

            String response = Utilities.requestToServerPOST(urlStr, paramStr);

            try{
                JSONObject json = new JSONObject(response);
                boolean success = json.getBoolean("success");
                String response_text = json.getString("response_text");
                Object[] result = {success, response_text};
                return result;

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object[] result) {
            super.onPostExecute(result);
            ((MessageActivity) mContext).mProgressDialog.dismiss();
            if (result != null) {
                if ((boolean)result[0]){
                    Utilities.showExtremelyShortToast(mContext, (String)result[1], 500);
                    // Do stuff
                }else{
                    Toast.makeText(mContext, ""+result[1], Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    public class CreateConversation extends AsyncTask<String, Void, Object[]>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ((MessageActivity) mContext).mProgressDialog = ProgressDialog.show(mContext, null, "Please wait...", true);
        }

        @Override
        protected Object[] doInBackground(String... params) {

            String urlStr = "http://dopeapi.elantix.net/im.create";
            String paramStr = "token="+params[0]+"&uids="+params[1];
            String[] uids = params[1].split(",");

            String response = Utilities.requestToServerPOST(urlStr, paramStr);

            try {
                JSONObject json = new JSONObject(response);
                boolean success = json.getBoolean("success");
                if (success){
                    JSONObject responseObj = json.getJSONObject("response");
                    String message = responseObj.getString("message");
                    int dialog_id = responseObj.getInt("dialog_id");
                    JSONObject data = responseObj.getJSONObject("data");

                    ConversationInfo convInfo = new ConversationInfo();
                    convInfo.id = data.getString("id");
                    convInfo.dialogs_id = data.getString("dialogs_id");
                    Log.e("HttpChat", "CreateConversation Dialog_id: "+convInfo.dialogs_id);
                    convInfo.users_id = data.getString("users_id");
                    convInfo.fullname = data.getString("fullname");
                    convInfo.username = data.getString("username");
                    convInfo.avatar = data.getString("avatar");
                    convInfo.unreads = Integer.parseInt(data.getString("unreads"));

                    JSONObject members = data.getJSONObject("members");

                    for (int i=0; i<uids.length; i++){
                        JSONObject item = members.getJSONObject(uids[i]);

                        ConversationMemberInfo member = new ConversationMemberInfo();
                        member.id = item.getString("id");
                        member.username = item.getString("username");
                        member.fullname = item.getString("fullname");
                        member.avatar = item.getString("avatar");
                        member.email = item.getString("email");

                        convInfo.members.add(member);
                    }

                    Object[] result = {success, dialog_id, convInfo};
                    return result;
                }else{
                    String response_text = json.getString("response_text");
                    Object[] result = {success, response_text};
                    return result;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object[] result) {
            super.onPostExecute(result);
            MessageActivity activity = ((MessageActivity)mContext);
            activity.mProgressDialog.dismiss();
            if (result != null){
                if ((Boolean)result[0]){
                    // Save this convInfo somewhere
                    Log.e("HttpKit", "new conversation created. Id: " + result[1]);
                    Intent intent = new Intent(activity, TabPlusActivity.class);
                    intent.putExtra("dialog_id", (int)result[1]);
                    mContext.startActivity(intent);
                    ((MessageActivity) mContext).finish();
                }else {
                    Toast.makeText(mContext, "" + result[1], Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public class GetConversationsList extends AsyncTask<String, Void, Object[]>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ((MessageActivity) mContext).mProgressDialog = ProgressDialog.show(mContext, null, "Please wait...", true);
        }

        @Override
        protected Object[] doInBackground(String... params) {
            String urlStr = "http://dopeapi.elantix.net/im.list?token="+params[0]+"&uid="+params[1];
            if (params[2] != null) urlStr += "&p="+params[2];
            if (params[3] != null) urlStr += "&count="+params[3];

            String response = Utilities.RequestToServerGET(urlStr);
            try {
                JSONObject json = new JSONObject(response);
                Boolean success = json.getBoolean("success");
                if (success){
                    JSONObject responseObj = json.getJSONObject("response");
                    int count = responseObj.getInt("count");
                    Log.e("HttpKit Conversations", "Conversation count: "+count);
                    JSONArray array = responseObj.getJSONArray("list");

                    ConversationInfo[] conversations = new ConversationInfo[array.length()];
                    Arrays.fill(conversations, null);

                    for (int i=0; i<array.length(); i++){
                        JSONObject item = array.getJSONObject(i);
                        ConversationInfo conversation = new ConversationInfo();
                        conversation.id = item.getString("id");
                        conversation.dialogs_id = item.getString("dialogs_id");
                        conversation.users_id = item.getString("users_id");
                        conversation.creator_id = item.getString("creator_id");
                        conversation.is_read = item.getString("is_read");
                        conversation.is_group = item.getString("is_group");
                        conversation.fullname = item.getString("fullname");
                        conversation.username = item.getString("username");
                        conversation.avatar = item.getString("avatar");
                        conversation.date_updated = item.getString("date_updated");
                        conversation.last_message = item.getString("last_message");
                        conversation.unreads = item.getInt("unreads");

                        if (item.get("members") instanceof JSONObject) {

                            JSONObject members = item.getJSONObject("members");

                            Iterator<?> keys = members.keys();
                            while (keys.hasNext()) {
                                String key = (String) keys.next();
                                if (members.get(key) instanceof JSONObject) {
                                    JSONObject memberItem = members.getJSONObject(key);
                                    ConversationMemberInfo member = new ConversationMemberInfo();

                                    member.id = memberItem.getString("id");
                                    member.username = memberItem.getString("username");
                                    member.fullname = memberItem.getString("fullname");
                                    member.email = memberItem.getString("email");
                                    member.avatar = memberItem.getString("avatar");
                                    conversation.members.add(member);
                                }
                            }
                        }else if (item.get("members") instanceof JSONArray) {

                            JSONArray members = item.getJSONArray("members");
                            ConversationMemberInfo[] memberInfos = new ConversationMemberInfo[members.length()];
                            Arrays.fill(memberInfos, null);
                            for (int j = 0; j < members.length(); j++) {
                                JSONObject memberItem = members.getJSONObject(j);
                                ConversationMemberInfo member = new ConversationMemberInfo();

                                member.id = memberItem.getString("id");
                                member.username = memberItem.getString("username");
                                member.fullname = memberItem.getString("fullname");
                                member.email = memberItem.getString("email");
                                member.avatar = memberItem.getString("avatar");
//                            memberInfos[j] = member;
                                conversation.members.add(member);
                            }

                            for (int j = 0; j < memberInfos.length; j++) {
                                conversation.members.add(memberInfos[j]);
                            }
                        }

                        conversations[i] = conversation;
                    }

                    Object[] result = {success, count, conversations};
                    return result;
                }else{
                    String response_text = json.getString("response_text");
                    Object[] result = {success, response_text};
                    return result;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Object[] result) {
            super.onPostExecute(result);
            MessageActivity activity = ((MessageActivity) mContext);
            if (result != null){
                if ((Boolean)result[0]){
                    Log.e("HttpKit Conversation", "Count: " + result[1] + "\nnumber of items: " + ((ConversationInfo[]) result[2]).length);
                    if (((ConversationInfo[])result[2]).length > 0){
                        activity.switchPageHandler(MessageActivity.DirectMessages.NewMessage1, 1);
                        activity.convCount = (int) result[1];
                        activity.conversations = (ConversationInfo[])result[2];
//                        ((FragmentNewMessage)activity.mCurrentFragment).setDataToAdapter((int) result[1], (ConversationInfo[]) result[2]);
                    }else{
                        activity.switchPageHandler(MessageActivity.DirectMessages.NoMessage);
                    }
                }else{
                    Toast.makeText(mContext, ""+result[1], Toast.LENGTH_SHORT).show();
                }
            }
            activity.mProgressDialog.dismiss();
        }
    }

    public class GetCrossFollowingFriends extends AsyncTask<String, Void, Object[]>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ((MessageActivity) mContext).mProgressDialog = ProgressDialog.show(mContext, null, "Please wait...", true);
        }

        @Override
        protected Object[] doInBackground(String... params) {

            String urlStr = "http://dopeapi.elantix.net/users.friends";
            String paramStr = "token="+params[0];
            if (params[1] != null) paramStr += "&q="+params[1];
            if (params[2] != null) paramStr += "&p="+params[2];
            if (params[3] != null) paramStr += "&count="+params[3];
            if (params[4] != null) paramStr += "&exlude_ids="+params[4];

            String response = Utilities.requestToServerPOST(urlStr, paramStr);

            try{
                JSONObject json = new JSONObject(response);
                Boolean success = json.getBoolean("success");
                JSONObject responseObj = json.getJSONObject("response");
                int count = responseObj.getInt("count");
                JSONArray list = responseObj.getJSONArray("list");

                ProfileInfo[] infoList = new ProfileInfo[list.length()];
                Arrays.fill(infoList, null);

                for (int i=0; i<list.length(); i++){
                    JSONObject item = list.getJSONObject(i);

                    ProfileInfo info = new ProfileInfo();
                    info.id = item.getString("id");
                    info.username = item.getString("username");
                    info.fullname = item.getString("fullname");
                    info.avatar = item.getString("avatar");
                    info.bio = item.getString("bio");
                    info.date_follow = item.getString("date_follow");
                    info.is_chat = item.getString("is_chat");
                    info.dialog_id = item.getString("dialog_id");

                    infoList[i] = info;
                }

                Object[] result = {success, count, infoList};
                return result;

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object[] result) {
            super.onPostExecute(result);
            MessageActivity activity = ((MessageActivity) mContext);
            activity.mProgressDialog.dismiss();
            if (result != null){
                if ((boolean) result[0]){
                    ((FragmentNewMessage)activity.mCurrentFragment).setDataToAdapter((int)result[1], (ProfileInfo[])result[2]);
                }else{
                    Toast.makeText(mContext, "Can`t fetch list of friends", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
