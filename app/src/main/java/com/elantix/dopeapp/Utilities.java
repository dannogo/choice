package com.elantix.dopeapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.desarrollodroide.libraryfragmenttransactionextended.FragmentTransactionExtended;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by oleh on 3/25/16.
 */
public class Utilities {

    public static final int CAPTURE_IMAGE_WITH_CAMERA = 1888;
    public static final int PICK_IMAGE_FROM_GALLERY = 1887;
    public static final int PICK_IMAGE_FROM_WEB = 1886;
    public static final int EDIT_PROFILE = 1885;

    //temp
    public static Uri avatarUri = null;
    public static Boolean isLogedIn = true;
    public static String profileUsername = "";
    public static String profileFirstLastNames = "";

    public static int sAnimationNumber = 0;
    public static int sDopeNumber = 0;

    public static Boolean sRateAnimationDirection = true;
    public static RateStateBackup sRateStateBackups[] = new RateStateBackup[10];

    final public static int[] ANIMATION_TYPES = {
            FragmentTransactionExtended.SCALEX,
            FragmentTransactionExtended.SCALEY,
            FragmentTransactionExtended.SCALEXY,
            FragmentTransactionExtended.FADE,
            FragmentTransactionExtended.FLIP_HORIZONTAL,
            FragmentTransactionExtended.FLIP_VERTICAL,
            FragmentTransactionExtended.SLIDE_HORIZONTAL,
            FragmentTransactionExtended.SLIDE_HORIZONTAL_PUSH_TOP,
            FragmentTransactionExtended.SLIDE_VERTICAL,
            FragmentTransactionExtended.SLIDE_HORIZONTAL_PUSH_TOP,
            FragmentTransactionExtended.SLIDE_VERTICAL_PUSH_LEFT,
            FragmentTransactionExtended.GLIDE,
            FragmentTransactionExtended.STACK,
            FragmentTransactionExtended.CUBE,
            FragmentTransactionExtended.ROTATE_DOWN,
            FragmentTransactionExtended.ROTATE_UP,
            FragmentTransactionExtended.ACCORDION,
            FragmentTransactionExtended.TABLE_HORIZONTAL,
            FragmentTransactionExtended.TABLE_VERTICAL,
            FragmentTransactionExtended.ZOOM_FROM_LEFT_CORNER,
            FragmentTransactionExtended.ZOOM_FROM_RIGHT_CORNER,
            FragmentTransactionExtended.ZOOM_SLIDE_HORIZONTAL,
            FragmentTransactionExtended.ZOOM_SLIDE_VERTICAL
    };

    public static void logOutWithConfirmation(Context context){
        FragmentLogOutConfirmation dialog = new FragmentLogOutConfirmation();
        dialog.show(((Activity)context).getFragmentManager(), "ConfirmationLogout");
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
     */
    protected static void showExtremelyShortToast(Context context, String message){
        final Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 200);
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
}
