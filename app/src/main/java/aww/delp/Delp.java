package aww.delp;

import android.content.Context;

import aww.delp.clients.Groupon;
import aww.delp.clients.Yelp;

public class Delp extends com.activeandroid.app.Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        Delp.context = this;
    }

    public static Groupon getGrouponClient() {
        return Groupon.getInstance(context);
    }

    public static Yelp getYelpClient() {
        return Yelp.getInstance(context);
    }

    public static Preferences getPreferences() {
        return Preferences.getInstance(context);
    }
}
