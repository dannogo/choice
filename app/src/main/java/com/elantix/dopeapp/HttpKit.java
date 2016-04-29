package com.elantix.dopeapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * Created by oleh on 4/20/16.
 */
public class HttpKit {
    private Context mContext;
    public HttpKit(Context context) {
        mContext = context;
    }

    public void regAndLogin(String[] params){
        RegRequestTask task = new RegRequestTask();
        task.execute(params);
    }

    public void login(String[] params){
        LoginRequestTask task = new LoginRequestTask();
        task.execute(params);
    }

    public void passRecovery(String email){
        PassRecoveryTask task = new PassRecoveryTask();
        task.execute(email);
    }

    private enum POSTRequestPurpose{
        Registration, Login, ChangePassword
    }

    public void changePassword(String token, String oldPass, String newPass, String confirmPass){
        String[] params = {token, oldPass, newPass, confirmPass};
        ChangePasswordTask task = new ChangePasswordTask();
        task.execute(params);
    }

    /**
     *
     * @param token   authorization token
     * @param timeday     1 for dopes created before 15:00, 2 for dopes created after this time. If null - default 1
     */
    public void get10Dopes(String token, String timeday){
        Utilities.sDopeListType = Utilities.DopeListType.Ten;
        GetDopesTask task = new GetDopesTask();
        String[] params = {"10", token, timeday};
        task.execute(params);
    }

    /**
     *
     * @param token authorization token
     * @param page if null - default 1
     * @param count if null - default 10
     */
    public void get100Dopes(String token, String page, String count){
        Utilities.sDopeListType = Utilities.DopeListType.Hundred;
        GetDopesTask task = new GetDopesTask();
        String[] params = {"100", token, page, count};
        task.execute(params);
    }

    public void vote(String token, String dopeId, String numberOfPicture){
        String[] params = {token, dopeId, numberOfPicture};
        VoteTask task = new VoteTask();
        task.execute(params);
    }

    public void shareDope(String dopeId, String token){
        String[] params = {dopeId, token};
        ShareDopeTask task = new ShareDopeTask();
        task.execute(params);
    }

    // not in use
    public void upload(String token, String path, String position){
        String[] params = {token, path, position};
        UploadTask task = new UploadTask();
        task.execute(params);
    }

    public void logOut(String token){
        LogOutTask task = new LogOutTask();
        task.execute(token);
    }

    public void createDope(String token, String question, String photo1, String photo2){
        String[] params = {token, question, photo1, photo2};
        CreateDopeTask task = new CreateDopeTask();
        task.execute(params);
    }

    public void getProfileInformation(String uid, String token){
        String[] params = {uid, token};
        ProfileInformationTask task = new ProfileInformationTask();
        task.execute(params);
    }

    public static String RequestToServerGET(String urlStr){

        StringBuffer response = new StringBuffer();
        URL url;
        try {
            url = new URL(urlStr);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            Log.w("StartLogin", "Sending 'GET' request to URL : " + url);
            Log.w("StartLogin", "Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

        }catch (IOException e){
            e.printStackTrace();
        }

        Log.w("StartLogin", response.toString());
        return response.toString();
    }

    public static String requestToServerPOST(String urlStr, String[] params, POSTRequestPurpose purpose){
        String paramsStr = "";
        if (purpose == POSTRequestPurpose.Registration) {
            if (params[0].equals("facebook")) {
                paramsStr += "fb_id=" + params[1] + "&email=" + params[2];
            } else if (params[0].equals("twitter")) {
                paramsStr += "tw_id=" + params[1] + "&email=" + params[2];
            } else if (params[0].equals("connected_facebook")) {
                paramsStr += "fb_id=" + params[1] + "&email=" + params[2] + "&username=" + params[3] + "&password=" + params[4];
            } else if (params[0].equals("connected_twitter")) {
                paramsStr += "tw_id=" + params[1] + "&email=" + params[2] + "&username=" + params[3] + "&password=" + params[4];
            } else if (params[0].equals("connected_email")) {
                paramsStr += "email=" + params[1] + "&username=" + params[2] + "&password=" + params[3];
            }
        }else if (purpose == POSTRequestPurpose.Login){
            if (params[0].equals("facebook")){
                paramsStr += "fb_id="+params[1]+"&email="+params[2];
            }else if (params[0].equals("twitter")){
                paramsStr += "tw_id="+params[1]+"&email="+params[2];
            }else if (params[0].equals("username")){
                paramsStr += "username="+params[1]+"&password="+params[2];
            }else if (params[0].equals("connected_facebook") ||
                    params[0].equals("connected_twitter")){
                paramsStr += "username="+params[3]+"&password="+params[4];
            }else if (params[0].equals("connected_email")){
                paramsStr += "username="+params[2]+"&password="+params[3];
            }
        }else{
            paramsStr += "token="+params[0]+"&oldpass="+params[1]+"&password="+params[2]+"&confirm="+params[3];
        }

        URL url;
        HttpURLConnection connection = null;
        try{
            url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(paramsStr);
            wr.flush();
            wr.close();

            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null){
                response.append(line);
                response.append("\n");
            }
            rd.close();
            String responseStr = response.toString();
            Log.w("HttpKit POST", responseStr);
            return responseStr;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null){
                connection.disconnect();
            }
        }
        return null;
    }

    public class CreateDopeTask extends AsyncTask<String, Void, String[]>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String[] doInBackground(String... params) {
            String charset = "UTF-8";
            String requestURL = "http://api.svcontact.ru/dopes.create";
            try {
                MultipartUtility multipart = new MultipartUtility(requestURL, charset);
                multipart.addFormField("token", params[0]);
                multipart.addFormField("question", params[1]);
                multipart.addFilePart("photo1", new File(params[2]));
                multipart.addFilePart("photo2", new File(params[3]));
                List<String> response = multipart.finish();
                Log.e("HttpKit CreateDopeTask", ""+response.get(0));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return new String[0];
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            ((TabPlusActivity)mContext).mProgressDialog.dismiss();
            ((TabPlusActivity)mContext).switchPageHandler(TabPlusActivity.PlusPage.Done);
        }
    }

//    public class CreateDopeTask extends AsyncTask<String, Void, String>{
//
//        @Override
//        protected String doInBackground(String... params) {
//            String photo1 = getIsolatedFileName(params[2]);
//            String photo2 = getIsolatedFileName(params[3]);
//
////            String urlStr = "http://api.svcontact.ru/dopes.create?token="+params[0]+"&question="+params[1]+"&photo1="+params[2]+"&photo2="+params[3];
//            String urlStr = "http://api.svcontact.ru/dopes.create?token="+params[0]+"&question="+params[1]+"&photo1="+photo1+"&photo2="+photo2;
//            String response = RequestToServerGET(urlStr);
//
//            try {
//                JSONObject json = new JSONObject(response);
//                Boolean success = json.getBoolean("success");
//                if (success){
//                    JSONObject objectResponse = json.getJSONObject("response");
//                    String userId = objectResponse.getString("user_id");
//                    return userId;
//                }else{
//                    String responseText = json.getString("response_text");
//                    return responseText;
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            Toast.makeText(mContext, ""+s, Toast.LENGTH_LONG).show();
//            ((TabPlusActivity)mContext).mProgressDialog.dismiss();
//            ((TabPlusActivity) mContext).switchPageHandler(TabPlusActivity.PlusPage.Done);
//        }
//    }



    // DEPRECATED
    public class UploadTask extends AsyncTask<String, Void, String[]>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String[] doInBackground(String... params) {
            String charset = "UTF-8";
            String requestURL = "http://api.svcontact.ru/files.dopes";



            try {
                MultipartUtility multipart = new MultipartUtility(requestURL, charset);
                multipart.addFormField("token", params[0]);
                multipart.addFilePart("file", new File(params[1]));
                List<String> response = multipart.finish();
                Log.e("HttpKit UploadTask", ""+response.get(0));
                try {
                    JSONObject json = new JSONObject(response.get(0));
                    Boolean success = json.getBoolean("success");
                    String responseState = (success) ? "success" : "error";

                    String responseText;
                    if (success){
                        responseText = json.getString("response");
                    }else {
                        responseText = json.getString("response_text");
                    }
                    String[] result = {params[2], responseState, responseText};
                    return result;

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            String[] result = {params[2]};
            return result;
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
            TabPlusActivity activity = ((TabPlusActivity) mContext);
            if (result.length == 1){
                Toast.makeText(mContext, "Something went wrong while uploading "+result[0]+" image to server", Toast.LENGTH_LONG).show();
            }else if(result[1].equals("error")){
                Toast.makeText(mContext, "Image "+result[0]+" error: " + result[2], Toast.LENGTH_SHORT).show();
            }else{
                if (result[0].equals("left")){
                    activity.uploadedPicture1 = result[2];
                    Log.d("HttpKit upload task one", "left: "+result[2]);
                }else {
                    Log.d("HttpKit upload task one", "right: "+result[2]);
                    activity.uploadedPicture2 = result[2];
                }
            }

            Log.w("HttpKit upload task", "uploadedPicture1: " + activity.uploadedPicture1 + "\nuploadedPicture2: " + activity.uploadedPicture2);
            if (activity.uploadedPicture1 != null && activity.uploadedPicture2 != null){
                activity.createDope();
            }else if (activity.uploadedPicture1 == null && activity.uploadedPicture2 == null){
                activity.mProgressDialog.dismiss();
            }

//            activity.uploadedPicture1 =
        }
    }

    public class ChangePasswordTask extends AsyncTask<String, Void, String[]>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ((ChangePasswordActivity)mContext).mProgressDialog = ProgressDialog.show(mContext, null, "Please wait...", true);
        }

        @Override
        protected String[] doInBackground(String... params) {
            String urlStr = "http://api.svcontact.ru/users.changepass";
            String response = requestToServerPOST(urlStr, params, POSTRequestPurpose.ChangePassword);

            try{
                JSONObject json = new JSONObject(response);
                Boolean success = json.getBoolean("success");
                String responseState = (success) ? "success" : "error";

                String responseText = json.getString("response_text");
                String[] result = {responseState, responseText};
                return result;

            }catch (JSONException e){
                e.printStackTrace();
            }

            String[] result = {"error", "Something went wrong"};
            return result;
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
            Toast.makeText(mContext, result[1], Toast.LENGTH_SHORT).show();
            if (result[0].equals("success")){
                ((ChangePasswordActivity)mContext).finish();
            }
            ((ChangePasswordActivity)mContext).mProgressDialog.dismiss();
        }
    }


    public class RegRequestTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... params) {

            String urlStr = "http://api.svcontact.ru/users.reg";
            String response = requestToServerPOST(urlStr, params, POSTRequestPurpose.Registration);

            try{
                JSONObject json = new JSONObject(response);
                Boolean success = json.getBoolean("success");
                if (success){
                    return params;
                }else{
                    String responseText = json.getString("response_text");
                    if (responseText.equals("User exist")) {
                        return params;
                    }else{
                        String[] result = {responseText};
                        return result;
                    }
                }

            }catch (JSONException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);

            if (result.length == 1){
                Toast.makeText(mContext, "" + result[0], Toast.LENGTH_LONG).show();
            }else{
                LoginRequestTask task = new LoginRequestTask();
                task.execute(result);
            }

        }
    }

    private class LoginRequestTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... params) {

            String urlStr = "http://api.svcontact.ru/users.login";
            String response = requestToServerPOST(urlStr, params, POSTRequestPurpose.Login);

            try {

                JSONObject json = new JSONObject(response.toString());
                Boolean success = json.getBoolean("success");
                if (success){
                    JSONObject responseJson = json.getJSONObject("response");
                    String token = responseJson.getString("token");
                    String uid = responseJson.getString("uid");
                    String[] result = {uid, token};
                    return result;
                }else{
                    String errorText = json.getString("response_text");
                    String[] result = {errorText};
                    return result;
                }

            }catch (JSONException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);

            if (result.length>1) {

                Utilities.sUid = result[0];
                Utilities.sToken = result[1];
                SharedPreferences.Editor editor = mContext.getSharedPreferences(Utilities.MY_PREFS_NAME, mContext.MODE_PRIVATE).edit();
                editor.putString("uid", Utilities.sUid);
                editor.putString("token", Utilities.sToken);
                editor.commit();

                if (mContext instanceof WelcomeActivity){
                    ((WelcomeActivity)mContext).afterLoginAction();
                }else if (mContext instanceof AuthActivity){
                    Log.w("HttpKit", "postExecute");
                    ((AuthActivity)mContext).afterLoginAction();
                }else if(mContext instanceof SignInActivity){
                    ((SignInActivity)mContext).afterLoginAction();
                }else{
                    Log.w("HttpKit", "UNKNOWN ACTIVITY");
                }

            }else{
                Toast.makeText(mContext, result[0], Toast.LENGTH_LONG).show();
            }
        }
    }



    private static boolean isLastQuestionMark(String str){

        return String.valueOf(str.charAt(str.length()-1)).equals("?");
    }

    public class ShareDopeTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ((ShareDopeActivity)mContext).mProgressDialog = ProgressDialog.show(mContext, null, "Please wait...", true);
        }

        @Override
        protected String doInBackground(String... params) {

            String urlStr = "http://api.svcontact.ru/dopes.share?dope="+params[0];
            if (params[1] != null){
                urlStr += "&token="+params[1];
            }
            String response = RequestToServerGET(urlStr);

            try {
                JSONObject json = new JSONObject(response);
                Boolean success = json.getBoolean("success");
                if (success){
                    String successResponse = json.getString("response");
                    return successResponse;
                }else {
                    String errorResponse = json.getString("response_text");
                    return errorResponse;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ShareDopeActivity activity = ((ShareDopeActivity)mContext);
            activity.mProgressDialog.dismiss();
            if (s != null){
                if (s.startsWith("http")){
                    activity.mLink = s;
                    activity.mLinkField.setText(s);
                }else {
                    Toast.makeText(mContext, s, Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(mContext, "Request failed", Toast.LENGTH_LONG).show();
            }
        }
    }

    private static String getIsolatedFileName(String original){
        String[] arrBySlash = original.split("/");
        String lastPart = arrBySlash[arrBySlash.length-1];
        String[] resultArr = lastPart.split("\\.");
        return resultArr[0];
    }

    public class GetUserDopesTask extends AsyncTask<String, Void, String[]>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ((MainActivity)mContext).mProgressDialog = ProgressDialog.show(mContext, null, "Please wait...", true);
        }

        @Override
        protected String[] doInBackground(String... params) {

            String urlStr = "http://api.svcontact.ru/users.profile?uid="+params[0];

//            if (params[0] instanceof String[]){
//                String[] uidList = (String[]) params[0];
//                for (int i=0; i<uidList.length; i++){
//                    urlStr += uidList[i]+",";
//                }
//                if (urlStr != null && urlStr.length() > 0 && urlStr.charAt(urlStr.length()-1)==',') {
//                    urlStr = urlStr.substring(0, urlStr.length()-1);
//                }
//            }else{
//                String uid = (String) params[0];
//                urlStr += uid;
//            }


            if (params[1] != null) urlStr += "&token="+params[1];
            if (params[2] != null) urlStr += "&p="+params[2];
            if (params[3] != null) urlStr += "&count="+params[3];

            String response = RequestToServerGET(urlStr);
            try{
                JSONObject json = new JSONObject(response);
                Boolean success = json.getBoolean("success");
                if (!success){
                    // Do parsing stuff
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return new String[0];
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            ((MainActivity)mContext).mProgressDialog.dismiss();
        }
    }

    public class ProfileInformationTask extends AsyncTask<String, Void, Object>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ((MainActivity)mContext).mProgressDialog = ProgressDialog.show(mContext, null, "Please wait...", true);
        }

        @Override
        protected Object doInBackground(String... params) {


            String urlStr = "http://api.svcontact.ru/users.profile?uid="+params[0];
            if (params[1] != null){
                urlStr += "&token="+params[1];
            }
            String response = RequestToServerGET(urlStr);

            try {
                JSONObject json = new JSONObject(response);
                Boolean success = json.getBoolean("success");


                if (!success){
                    String responseText = json.getString("response_text");
                    return responseText;
                }else{
                    JSONObject objectResponse = json.getJSONObject("response");
                    ProfileInfo info = new ProfileInfo();

                    info.id = objectResponse.getString("id");
                    info.fb_id = objectResponse.getString("fb_id");
                    info.tw_id = objectResponse.getString("tw_id");
                    info.email = objectResponse.getString("email");
                    info.username = objectResponse.getString("username");
                    info.fullname = objectResponse.getString("fullname");
                    info.bio = objectResponse.getString("bio");
                    info.avatar = objectResponse.getString("avatar");
                    info.cover = objectResponse.getString("cover");
                    info.gender = objectResponse.getString("gender");
                    info.phone = objectResponse.getString("phone");
                    info.birthday = objectResponse.getString("birthday");
                    info.date_reg = objectResponse.getString("date_reg");
                    info.ban = objectResponse.getString("ban");
                    info.dopes = objectResponse.getInt("dopes");
                    info.followers = objectResponse.getInt("followers");
                    info.followings = objectResponse.getInt("followings");
                    info.follow = objectResponse.getInt("follow");

                    return info;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return new String[0];
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
//            for (int i=0; i<result.length; i++){
//                Log.w("GetProfileInfo", ""+i+": "+result[i]);
//            }
            ((MainActivity)mContext).mProgressDialog.dismiss();

            if (result instanceof String){
                Toast.makeText(mContext, ""+result, Toast.LENGTH_SHORT).show();
            }else {

                if (((MainActivity) mContext).mCurrentFragment instanceof FragmentProfileOverview) {
                    ((FragmentProfileOverview) ((MainActivity) mContext).mCurrentFragment).setInfo((ProfileInfo)result);
                }
            }

        }
    }

    public class LogOutTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            String urlStr = "http://api.svcontact.ru/users.logout?token="+params[0];
            String response = RequestToServerGET(urlStr);

            try {
                JSONObject json = new JSONObject(response);
                Boolean success = json.getBoolean("success");
                String responseText = json.getString("response_text");
                String result = (success) ? "success" : responseText;
                return result;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!s.equals("success")){
                Toast.makeText(mContext, ""+s, Toast.LENGTH_SHORT).show();
            }
        }
    }



    public class VoteTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            String urlStr = "http://api.svcontact.ru/dopes.vote?token="+params[0]+"&dope="+params[1]+"&vote="+params[2];
            String response = RequestToServerGET(urlStr);

            try {
                JSONObject json = new JSONObject(response);
                Boolean success = json.getBoolean("success");
                if (!success){
                    String responseText = json.getString("response_text");
                    return responseText;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null){
                Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class GetDopesTask extends AsyncTask<String, Void, Object[]>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ((MainActivity)mContext).mProgressDialog = ProgressDialog.show(mContext, null, "Please wait...", true);
        }

        @Override
        protected Object[] doInBackground(String... params) {

            String urlStr = "http://api.svcontact.ru/dopes.day10";
            if (params[0].equals("100")){
                urlStr += "0?";
                if (params[1] != null){
                    urlStr += "token="+params[1];
                }
                if (params[2] != null){
                    if (!isLastQuestionMark(urlStr)){
                        urlStr += "&";
                    }
                    urlStr += "p="+params[2];
                }
                if (params[3] != null) {
                    if (!isLastQuestionMark(urlStr)){
                        urlStr += "&";
                    }
                    urlStr += "count=" + params[3];
                }
            }else {
                urlStr += "?";
                if (params[1] != null){
                    urlStr += "token="+params[1];
                }
                if (params[2] != null) {
                    if (!isLastQuestionMark(urlStr)){
                        urlStr += "&";
                    }
                    urlStr += "timeday="+params[2];
                }
            }

            String response = RequestToServerGET(urlStr);

            try {
                JSONObject json = new JSONObject(response);
                Boolean success = json.getBoolean("success");
                if(success){
                    JSONArray array = json.getJSONArray("response");
                    DopeInfo[] dopes = new DopeInfo[array.length()];
                    Arrays.fill(dopes, null);
                    for (int i=0; i<array.length(); i++) {
                        JSONObject item = array.getJSONObject(i);
                        DopeInfo dopeInfo = new DopeInfo();
                        dopeInfo.fullname = item.getString("fullname");
                        dopeInfo.username = item.getString("username");
                        dopeInfo.email = item.getString("email");
                        dopeInfo.avatar = Uri.parse(item.getString("avatar"));
                        dopeInfo.id = item.getString("id");
                        dopeInfo.userId = item.getString("user_id");
                        dopeInfo.question = item.getString("question");
                        dopeInfo.photo1 = Uri.parse(item.getString("photo1"));
                        dopeInfo.photo2 = Uri.parse(item.getString("photo2"));
                        dopeInfo.dateCreate = item.getString("date_cteate");
                        dopeInfo.top10 = item.getString("top10");
                        dopeInfo.top100 = item.getString("top100");
                        dopeInfo.ban = item.getString("ban");
                        JSONObject votes = item.getJSONObject("votes");
                        dopeInfo.votesAll = votes.getInt("all");
                        JSONObject quantity = votes.getJSONObject("quantity");
                        dopeInfo.votes1 = quantity.getInt("1");
                        dopeInfo.votes2 = quantity.getInt("2");
                        JSONObject percent = votes.getJSONObject("percent");
                        dopeInfo.percent1 = percent.getInt("1");
                        dopeInfo.percent2 = percent.getInt("2");
                        dopeInfo.comments = item.getInt("comments");
                        dopeInfo.myVote = item.getInt("myvote");
                        dopes[i] = dopeInfo;
                    }

                    return dopes;
                }else{
                    String responseText = json.getString("response_text");
                    String[] result = {responseText};
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
            ((MainActivity)mContext).mProgressDialog.dismiss();

            if (result[0] instanceof String){
                Toast.makeText(mContext, "" + result[0], Toast.LENGTH_LONG).show();
            }else{
                ((MainActivity)mContext).saveDopes((DopeInfo[]) result);
                ((MainActivity)mContext).launchFragment(((MainActivity) mContext).page);
//                for (int i=0; i<result.length; i++){
//                    Log.w("HttpKit", "" + result[i]);
//                }
            }
        }
    }

    public class PassRecoveryTask extends AsyncTask<String, Void, String[]>{

        @Override
        protected String[] doInBackground(String... params) {
            String urlStr = "http://api.svcontact.ru/users.forgot?email="+params[0];

            String response = RequestToServerGET(urlStr);

            try {
                JSONObject json = new JSONObject(response);
                Boolean success = json.getBoolean("success");
                String responseText = json.getString("response_text");

                String[] result = {null, null};
                if (success){
                    result[0] = "success";
                }else{
                    result[0] = "error";
                }
                    result[1] = responseText;
                    return result;

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
            if (result[0].equals("success")){
                ((SignInActivity)mContext).switchPageHandler(SignInActivity.StartLogin.Login);
            }
            Toast.makeText(mContext, result[1], Toast.LENGTH_LONG).show();
        }
    }


}
