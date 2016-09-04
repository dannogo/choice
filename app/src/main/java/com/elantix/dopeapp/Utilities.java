package com.elantix.dopeapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.elantix.dopeapp.entities.ConversationInfo;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by oleh on 3/25/16.
 */
public class Utilities {

    public static final int CAPTURE_IMAGE_WITH_CAMERA = 1888;
    public static final int PICK_IMAGE_FROM_GALLERY = 1887;
    public static final int PICK_IMAGE_FROM_WEB = 1886;
    public static final int EDIT_PROFILE = 1885;
    public static final int SIGN_IN_UP = 1884;
    public static final int SIGN_IN = 1883;
    public static final int LEAVE_GROUP = 1882;

    public static final String MY_PREFS_NAME = "DopePrefs";
    public static String sToken = null;
    public static String sUid = null;

    public static DopeInfo[] sDopes10;
    public static DopeInfo[] sDopes100;
    public static DopeInfo[] sDopesFriendsFeed;

    public static ProfileInfo sCurProfile;
    public static ProfileInfo sMyProfile;
    public static String[] sMyFollowings;
    public static ArrayList<ChainLink> sFragmentHistory = new ArrayList<>();
//    public static ConversationInfo[] sConversations;
    public static ArrayList<ConversationInfo> sConversations;

    public static RateStateBackup sRateStateBackups[] = new RateStateBackup[10];
    public static DopeListType sDopeListType;

    public static final String ADCOLONY_APP_ID = "appfe8d7cd82e6d4e8f80";
    public static final String ADCOLONY_ZONE_ID = "vzadc01d03c0db434790";
//    public static String ANDROID_ID;
    public static String FirebaseCloudToken;
    public static DecorStyle decorStyle = DecorStyle.Second;

    public enum DecorStyle{
        First, Second
    }


    public enum DopeListType{
        Ten, Hundred, Friends
    }


    public static class UriToBitmapTask extends AsyncTask<Uri, Void, Bitmap> {

        private Byte tryCounter = 0;
        private Context context;
        private String message;
        private String link;
        private String packageName;

        public UriToBitmapTask(Context context, String message, String link, String packageName){
            this.context = context;
            this.message = message;
            this.link = link;
            this.packageName = packageName;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (context instanceof ShareDopeActivity){
                ((ShareDopeActivity)context).mProgressDialog = ProgressDialog.show(context, null, "Please wait...", true);
            }else if (context instanceof ShareProfileActivity){
                ((ShareProfileActivity)context).mProgressDialog = ProgressDialog.show(context, null, "Please wait...", true);
            }
            if (packageName != null) {
                if(!isPackageInstalled(packageName,context)) {
                    Log.e("UriToBitmapTask", "Cancel");
                    if (context instanceof ShareDopeActivity) {
                        ((ShareDopeActivity) context).mProgressDialog.dismiss();
                    } else if (context instanceof ShareProfileActivity) {
                        ((ShareProfileActivity) context).mProgressDialog.dismiss();
                    }

                    String appName = packageName.split("\\.")[1];
                    String appNameCapitalized = Character.toUpperCase(appName.charAt(0)) + appName.substring(1);
                    Toast.makeText(context, appNameCapitalized+" have not been installed.", Toast.LENGTH_SHORT).show();
                    try {
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+packageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+packageName)));
                    }
                    this.cancel(true);
                }
            }
        }

        @Override
        protected Bitmap doInBackground(Uri... params) {

            try {
                Bitmap bitmap = Glide.with(context).load(params[0]).asBitmap().into(-1, -1).get();
                return bitmap;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
                if (tryCounter <= 2) {
                    doInBackground(params);
                }
                tryCounter++;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (context instanceof ShareDopeActivity){
                ((ShareDopeActivity)context).mProgressDialog.dismiss();
            }else if(context instanceof ShareProfileActivity){
                ((ShareProfileActivity)context).mProgressDialog.dismiss();
            }
            Log.w("Utilities", "UtiToBitmapTask result: "+bitmap);
            if (bitmap != null) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");
                if (packageName != null) {
                    share.setPackage(packageName);
                }
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                File f = new File(Environment.getExternalStorageDirectory()
                        + File.separator + "temporary_file.jpg");
                try {
                    f.createNewFile();
                    FileOutputStream fo = new FileOutputStream(f);
                    fo.write(bytes.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                share.putExtra(Intent.EXTRA_TEXT, message + "\n" + link);
                share.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.app_name));
//                share.putExtra(Intent.EXTRA_TITLE, context.getResources().getString(R.string.app_name));
                share.putExtra(Intent.EXTRA_STREAM,
                        Uri.parse("file:///sdcard/temporary_file.jpg"));
                context.startActivity(Intent.createChooser(share, "Share to"));
            }
        }
    }


    public static void initShareIntent(Context context, Uri imageUri, String message, String link){
        initShareIntent(context, imageUri, message, link, null);
    }

    public static void initShareIntent(Context context, Uri imageUri, String message, String link, String packageName){
        UriToBitmapTask task = new UriToBitmapTask(context, message, link, packageName);
        task.execute(imageUri);
    }

//    public static void shareImageWhatsApp(Context context, Uri imageUri, String message, String link) {
//
//        Bitmap adv = BitmapFactory.decodeResource(context.getResources(), R.drawable.paper_plane);
//        Intent share = new Intent(Intent.ACTION_SEND);
//        share.setType("image/jpeg");
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        adv.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        File f = new File(Environment.getExternalStorageDirectory()
//                + File.separator + "temporary_file.jpg");
//        try {
//            f.createNewFile();
//            new FileOutputStream(f).write(bytes.toByteArray());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        share.putExtra(Intent.EXTRA_STREAM,
//                Uri.parse( Environment.getExternalStorageDirectory()+ File.separator+"temporary_file.jpg"));
//        share.putExtra(Intent.EXTRA_TEXT, message + "\n" + link);
//        share.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.app_name));
//        if(isPackageInstalled("com.whatsapp",context)){
//            share.setPackage("com.whatsapp");
//            context.startActivity(Intent.createChooser(share, "Share Image"));
//
//        }else{
//
//            Toast.makeText(context.getApplicationContext(), "Please Install Whatsapp", Toast.LENGTH_LONG).show();
//        }
//
//    }

    private static boolean isPackageInstalled(String packagename, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


//    public static void shareOnWhatsapp(Context context, Uri imageUri, String message, String link){
//        /**
//         * Show share dialog BOTH image and text
//         */
////        Uri imageUri = Uri.parse(pictureFile.getAbsolutePath());
//        Log.w("Utilities", "shareOnWhatsApp");
//        Intent shareIntent = new Intent();
//        shareIntent.setAction(Intent.ACTION_SEND);
//        //Target whatsapp:
//        shareIntent.setPackage("com.whatsapp");
//        //Add text and then Image URI
//        shareIntent.putExtra(Intent.EXTRA_TEXT, message);
////        shareIntent.putExtra(Intent.EXTRA_ORIGINATING_URI, message);
//        shareIntent.putExtra(Intent.EXTRA_TITLE, context.getResources().getString(R.string.app_name));
//        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
//        shareIntent.setType("image/jpeg");
//        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
////        Intent shareIntent = new Intent();
////        shareIntent.setAction(Intent.ACTION_SEND);
////        shareIntent.putExtra(Intent.EXTRA_TEXT,message + "\n\nLink : " + link );
//////        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(sharePath));
////        shareIntent.setType("image/*");
////        context.startActivity(Intent.createChooser(shareIntent, "Share image via:"));
//
//        try {
//            context.startActivity(shareIntent);
//        } catch (android.content.ActivityNotFoundException ex) {
//            Toast.makeText(context, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
//            try {
//                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.whatsapp")));
//            } catch (android.content.ActivityNotFoundException anfe) {
//                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.whatsapp")));
//            }
//        }
//    }

//    public static void shareOnFacebook(Activity context, String link, DopeInfo dopeInfo){
    public static void shareOnFacebook(Activity context, String link, Uri image, String description){
        FacebookSdk.sdkInitialize(context);
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(link))
                .setImageUrl(image)
                .setContentDescription(description)
                .setContentTitle(context.getResources().getString(R.string.app_name))
                .build();

        ShareDialog dialog = new ShareDialog(context);
        dialog.show(content, ShareDialog.Mode.AUTOMATIC);
    }

    public static Bitmap createTrimmedBitmap(Bitmap bmp) {

        int imgHeight = bmp.getHeight();
        int imgWidth  = bmp.getWidth();
        int smallX=0,largeX=imgWidth,smallY=0,largeY=imgHeight;
        int left=imgWidth,right=imgWidth,top=imgHeight,bottom=imgHeight;
        for(int i=0;i<imgWidth;i++)
        {
            for(int j=0;j<imgHeight;j++)
            {
                if(bmp.getPixel(i, j) != Color.TRANSPARENT){
                    if((i-smallX)<left){
                        left=(i-smallX);
                    }
                    if((largeX-i)<right)
                    {
                        right=(largeX-i);
                    }
                    if((j-smallY)<top)
                    {
                        top=(j-smallY);
                    }
                    if((largeY-j)<bottom)
                    {
                        bottom=(largeY-j);
                    }
                }
            }
        }
        Log.d("Utilities createTrimmed", "left:" + left + " right:" + right + " top:" + top + " bottom:" + bottom);
        bmp=Bitmap.createBitmap(bmp,left,top,imgWidth-left-right, imgHeight-top-bottom);

        return bmp;
    }

    public static ConversationInfo findConversationById(ArrayList<ConversationInfo> conversations, String dialogId){
        if (dialogId == null){
            Log.e("findConversationById", "dialogId is NULL");
            return null;
        }
        if (conversations == null || conversations.isEmpty()){
            Log.e("findConversationById", "Utilities.sConversations is NULL or isEmpty");
            return null;
        }else{
            for (int i=0; i<conversations.size(); i++){
                if (conversations.get(i).dialogs_id.equals(dialogId)){
                    return conversations.get(i);
                }
            }
            return null;
        }
    }

    public static String RequestToServerGET(String urlStr) {

        StringBuffer response = new StringBuffer();
        URL url;
        try {
            url = new URL(urlStr);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(7000);
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

        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.w("StartLogin", response.toString());
        return response.toString();
    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public static String convertDate(String in){
        return convertDate(in, false);
    }

    public static String convertDate(String in, boolean justAmPm){

        if (in.equals("null") || in.isEmpty() || in == null){
            return "";
        }else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = format.parse(in);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            SimpleDateFormat formatOut;
            if (justAmPm){
                formatOut = new SimpleDateFormat("h:mma");
                return formatOut.format(date).toLowerCase();
            }else {
                formatOut = new SimpleDateFormat("HH:mm MMM dd");
            }

            return formatOut.format(date);
        }
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String requestToServerPOST(String urlStr, String paramsStr){

        Log.w("HttpKit POST", "url: "+urlStr);
        Log.w("HttpKit POST", "params: "+paramsStr);
        URL url;
        HttpURLConnection connection = null;
        try {
            url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setConnectTimeout(7000);

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(paramsStr);
            wr.flush();
            wr.close();

            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
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
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

    public static boolean deleteDirectory(File path) {
        if( path.exists() ) {
            File[] files = path.listFiles();
            if (files == null) {
                return true;
            }
            for(int i=0; i<files.length; i++) {
                if(files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                }
                else {
                    files[i].delete();
                }
            }
        }
        return( path.delete() );
    }

    /**
     * method is used for checking valid email id format.
     *
     * @param email
     * @return boolean true for valid false for invalid
     */
    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static void logOutWithConfirmation(Context context){
        FragmentLogOutConfirmation dialog = new FragmentLogOutConfirmation();
        dialog.show(((Activity) context).getFragmentManager(), "ConfirmationLogout");
    }

    public enum ActionsWhichRequireLogin {
        Comment, Follow, DirectMessage
    }

    public static void loginProposalDialog(final MainActivity activity, ActionsWhichRequireLogin action){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Username Required");
        String messageEnd;
        switch (action){
            case Comment:
                messageEnd = "comment!";
                break;
            case Follow:
                messageEnd = "follow others or to be followed!";
                break;
            case DirectMessage:
                messageEnd = "send Direct Messages!";
                break;
            default:
                messageEnd = "comment!";
        }
        builder.setMessage("You need to have a username to "+messageEnd);
        builder.setPositiveButton("Log In", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.switchPageHandler(MainActivity.Page.Profile);
            }
        });
//        builder.setNeutralButton("Pick Username", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * lauches ACTION_GET_CONTENT (choosing picture from phone`s gallery)
     */
    public static void chooseFromLibrary(Activity activity){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), Utilities.PICK_IMAGE_FROM_GALLERY);
    }

    /**
     * Handles Android M permission access
     * @param context
     * @param permissionName  name of permission you want to get granted. For example MediaStore.ACTION_IMAGE_CAPTURE
     * @return
     */
    public static boolean hasPermissionInManifest(Context context, String permissionName) {
        final String packageName = context.getPackageName();
        try {
            final PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
            final String[] declaredPermisisons = packageInfo.requestedPermissions;
            if (declaredPermisisons != null && declaredPermisisons.length > 0) {
                for (String p : declaredPermisisons) {
                    if (p.equals(permissionName)) {
                        return true;
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Shows extremelly short Toast
     * @param context
     * @param message
     * @param duration in miliseconds
     */
    protected static void showExtremelyShortToast(Context context, String message, int duration){
        final Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, duration);
    }

    /**
     * Creates random String with A-Z and 0-9 characters
     * @return random String
     */
    public static String createRandomFileName(){
        final String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final int N = alphabet.length();

        Random r = new Random();
        StringBuilder stringBuilder = new StringBuilder("");

        for (int i = 0; i < 12; i++) {
            stringBuilder.append(alphabet.charAt(r.nextInt(N)));
        }

        return stringBuilder.toString();
    }


    /**
     * Returns height of status bar
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public enum FollowingListType{
        FriendsSearch, ProfileFollowing, ProfileFollowers
    }

    /**
     * Feed back via email
     * @param context
     */
    public static void contactUs(Context context){
        PackageManager manager = context.getPackageManager();
        String version = "unknown";
        try {
            PackageInfo info = manager.getPackageInfo(
                    context.getPackageName(), 0);
            version = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("Exception", e.getMessage());
        }


        String extraText = "[Your feedback here]";
        StringBuilder stringBuilder = new StringBuilder(extraText);

        stringBuilder.append('\n');
        stringBuilder.append('\n');
        stringBuilder.append(" -----------------");
        stringBuilder.append('\n');
        stringBuilder.append("Device name: " + getDeviceName());
        stringBuilder.append('\n');
        stringBuilder.append("OS version: " + android.os.Build.VERSION.RELEASE);
        stringBuilder.append('\n');
        stringBuilder.append("Application version: " + version);


        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", context.getResources().getString(R.string.contact_us_email), null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.contact_us_topic_text));
        emailIntent.putExtra(Intent.EXTRA_TEXT, stringBuilder.toString());
        context.startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    /**
     * Returns device name
     */

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        if (manufacturer.equalsIgnoreCase("HTC")) {
            // make sure "HTC" is fully capitalized.
            return "HTC " + model;
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        String phrase = "";
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase += Character.toUpperCase(c);
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase += c;
        }
        return phrase;
    }

    // DEPRECATED
    /**
     * Returns bitmap with rounded left or right corners depending on isLeftSide parameter
     * @param bm bitmap to transform
     * @param imageView imageView to which result bitmap should fit
     * @param isLeftSide put true if you want left corners to be rounded
     * @param corner value in px that determines corner size
     * @return
     */
    public static Bitmap getOneSideRoundedBitmap(Bitmap bm, ImageView imageView, Boolean isLeftSide, int corner) {

        float w = imageView.getWidth();
        float h = imageView.getHeight();
        Bitmap newBitmap = scaleCenterCrop(bm, (int) h, (int) w);

        Point pointTop;
        Point pointBottom;
        Path path = new Path();

        if (isLeftSide) {
            pointTop = new Point(0, 0);
            pointBottom = new Point(0, (int)h);

            path.moveTo(corner, 0);
            path.lineTo(w, 0);
            path.lineTo(w, h);
            path.lineTo(corner, h);
            path.quadTo(pointBottom.x, pointBottom.y, 0, h - corner);
            path.lineTo(0, corner);
            path.quadTo(pointTop.x, pointTop.y, corner, 0);

        }else{
            pointTop = new Point((int)w, 0);
            pointBottom = new Point((int)w, (int)h);

            path.lineTo(w - corner, 0);
            path.quadTo(pointTop.x, pointTop.y, w, corner);
            path.lineTo(w, h-corner);
            path.quadTo(pointBottom.x, pointBottom.y, w - corner, h);
            path.lineTo(0, h);
            path.lineTo(0,0);

        }
        Bitmap bmOut = Bitmap.createBitmap((int)w, (int)h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmOut);

        Paint paint = new Paint();
        paint.setAntiAlias(false);
        paint.setColor(0xff424242);

        Rect rect = new Rect(0, 0, (int)w, (int)h);
        RectF rectF = new RectF(rect);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawPath(path, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(newBitmap, rect, rect, paint);

        return bmOut;
    }


    public static Bitmap getOneSideRoundedBitmap(Context context, Bitmap bm, Boolean isLeftSide, int corner) {

        float w = context.getResources().getDimension(R.dimen.chat_pic_and_rate_container_width) / 2;
        float h = context.getResources().getDimension(R.dimen.chat_pic_and_rate_container_height);

        Bitmap newBitmap = scaleCenterCrop(bm, (int) h, (int) w);

        Point pointTop;
        Point pointBottom;
        Path path = new Path();

        if (isLeftSide) {
            pointTop = new Point(0, 0);
            pointBottom = new Point(0, (int)h);

            path.moveTo(corner, 0);
            path.lineTo(w, 0);
            path.lineTo(w, h);
            path.lineTo(corner, h);
            path.quadTo(pointBottom.x, pointBottom.y, 0, h - corner);
            path.lineTo(0, corner);
            path.quadTo(pointTop.x, pointTop.y, corner, 0);

        }else{
            pointTop = new Point((int)w, 0);
            pointBottom = new Point((int)w, (int)h);

            path.lineTo(w - corner, 0);
            path.quadTo(pointTop.x, pointTop.y, w, corner);
            path.lineTo(w, h-corner);
            path.quadTo(pointBottom.x, pointBottom.y, w - corner, h);
            path.lineTo(0, h);
            path.lineTo(0,0);

        }
        Bitmap bmOut = Bitmap.createBitmap((int)w, (int)h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmOut);

        Paint paint = new Paint();
        paint.setAntiAlias(false);
        paint.setColor(0xff424242);

        Rect rect = new Rect(0, 0, (int)w, (int)h);
        RectF rectF = new RectF(rect);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawPath(path, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(newBitmap, rect, rect, paint);

        return bmOut;
    }



    public static Bitmap scaleCenterCrop(Bitmap source, int newHeight, int newWidth) {
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();

        // Compute the scaling factors to fit the new height and width, respectively.
        // To cover the final image, the final scaling will be the bigger
        // of these two.
        float xScale = (float) newWidth / sourceWidth;
        float yScale = (float) newHeight / sourceHeight;
        float scale = Math.max(xScale, yScale);

        // Now get the size of the source bitmap when scaled
        float scaledWidth = scale * sourceWidth;
        float scaledHeight = scale * sourceHeight;

        // Let's find out the upper left coordinates if the scaled bitmap
        // should be centered in the new size give by the parameters
        float left = (newWidth - scaledWidth) / 2;
        float top = (newHeight - scaledHeight) / 2;

        // The target rectangle for the new, scaled version of the source bitmap will now
        // be
        RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

        // Finally, we create a new bitmap of the specified size and draw our new,
        // scaled bitmap onto it.
        Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
        Canvas canvas = new Canvas(dest);
        canvas.drawBitmap(source, null, targetRect, null);

        return dest;
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @author paulburke
     */
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

}
