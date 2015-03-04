package com.tehutiscribe.tehutikit.Utilities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import java.util.List;

public class Social {

    public static void postToFacebook(Context context, String message) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, message);

        // See if official Facebook app is found
        boolean facebookAppFound = false;
        List<ResolveInfo> matches = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.katana")) {
                intent.setPackage(info.activityInfo.packageName);
                facebookAppFound = true;
                break;
            }
        }

        // As fallback, launch sharer.php in a browser
        if (!facebookAppFound) {
            String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + message;
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
        }

        context.startActivity(intent);
    }

    public static void postToTwitter(Context context, String message) {
        Intent twitterIntent = findTwitterClient(context.getPackageManager());

        //twitterIntent.setType("application/twitter");
        if (twitterIntent == null) {
            String twitterUrl = String.format("https://www.twitter.com/share?text=%s", message);
            twitterIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterUrl));
            context.startActivity(twitterIntent);
        } else {
            Intent chooser = Intent.createChooser(twitterIntent, "Share to Twitter");
            chooser.putExtra(Intent.EXTRA_TEXT, message);
            context.startActivity(chooser);
        }
    }

    public static Intent findTwitterClient(PackageManager manager) {
        final String[] twitterApps = {
                // package // name - nb installs (thousands)
                "com.twitter.android", // official - 10 000
                "com.twidroid", // twidroyd - 5 000
                "com.handmark.tweetcaster", // Tweecaster - 5 000
                "com.thedeck.android", // TweetDeck - 5 000
                "com.klinker.android.twitter" // Talon, because it's awesome
        };
        Intent tweetIntent = new Intent();
        tweetIntent.setType("text/plain");
        final PackageManager packageManager = manager;
        List<ResolveInfo> list = packageManager.queryIntentActivities(
                tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);

        for (int i = 0; i < twitterApps.length; i++) {
            for (ResolveInfo resolveInfo : list) {
                String p = resolveInfo.activityInfo.packageName;
                if (p != null && p.startsWith(twitterApps[i])) {
                    tweetIntent.setPackage(p);
                    return tweetIntent;
                }
            }
        }
        return null;
    }
}
