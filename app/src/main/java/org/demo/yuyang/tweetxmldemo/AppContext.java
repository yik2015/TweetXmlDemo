package org.demo.yuyang.tweetxmldemo;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;

import org.demo.yuyang.tweetxmldemo.api.ApiHttpClient;
import org.demo.yuyang.tweetxmldemo.base.BaseApplication;
import org.demo.yuyang.tweetxmldemo.bean.Constants;
import org.demo.yuyang.tweetxmldemo.bean.User;
import org.demo.yuyang.tweetxmldemo.cache.DataCleanManager;
import org.demo.yuyang.tweetxmldemo.util.CyptoUtils;
import org.demo.yuyang.tweetxmldemo.util.StringUtils;
import org.demo.yuyang.tweetxmldemo.util.TLog;
import org.kymjs.kjframe.Core;
import org.kymjs.kjframe.http.HttpConfig;
import org.kymjs.kjframe.utils.KJLoger;

import java.util.Properties;
import java.util.UUID;

import static org.demo.yuyang.tweetxmldemo.AppConfig.*;

/**
 * Created on 3/14/16.
 */
public class AppContext extends BaseApplication {
    public static final int PAGE_SIZE = 20;

    private static AppContext instance;

    private int loginUid;

    private boolean login;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        init();
        initLogin();

//        TODO UIHelper.sendBroadcastForNotice(this);
    }

    private void init() {
        AsyncHttpClient client = new AsyncHttpClient();
        PersistentCookieStore cookieStore = new PersistentCookieStore(this);

        client.setCookieStore(cookieStore);
        ApiHttpClient.setHttpClient(client);

        ApiHttpClient.setCookie(ApiHttpClient.getCookie(this));

        KJLoger.openDebutLog(true);
        TLog.DEBUG = BuildConfig.DEBUG;

        HttpConfig.CACHEPATH = "OSChina/imagecache";
    }

    private void initLogin() {
        User user = getLoginUser();
        if (null != user && user.getId() > 0) {
            login = true;
            loginUid = user.getId();
        } else {
            cleanLoginInfo();
        }
    }

    public static AppContext getInstance() {
        return instance;
    }

    public void setProperties(Properties ps) {
        AppConfig.getAppConfig(this).set(ps);
    }

    public void setProperty(String key, String value) {
        AppConfig.getAppConfig(this).set(key, value);
    }

    public Properties getProperties() {
        return AppConfig.getAppConfig(this).get();
    }

    public String getProperty(String key) {
        String res = AppConfig.getAppConfig(this).get(key);
        return res;
    }

    public void removeProperty(String... key) {
        AppConfig.getAppConfig(this).remove(key);
    }

    public String getAppId() {
        String uniqueID = getProperty(AppConfig.CONF_APP_UNIQUEID);
        if (StringUtils.isEmpty(uniqueID)) {
            uniqueID = UUID.randomUUID().toString();
            setProperty(AppConfig.CONF_APP_UNIQUEID, uniqueID);
        }
        return uniqueID;
    }

    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (info == null) {
            info = new PackageInfo();
        }

        return info;
    }

    public void saveUserInfo(final User user) {
        loginUid = user.getId();
        login = true;
        setProperties(new Properties() {
            {
                setProperty("user.uid", String.valueOf(user.getId()));
                setProperty("user.name", user.getName());
                setProperty("user.face", user.getPortrait());// 用户头像-文件名
                setProperty("user.account", user.getAccount());
                setProperty("user.pwd",
                        CyptoUtils.encode("oschinaApp", user.getPwd()));
                setProperty("user.location", user.getLocation());
                setProperty("user.followers",
                        String.valueOf(user.getFollowers()));
                setProperty("user.fans", String.valueOf(user.getFans()));
                setProperty("user.score", String.valueOf(user.getScore()));
                setProperty("user.favoritecount",
                        String.valueOf(user.getFavoritecount()));
                setProperty("user.gender", String.valueOf(user.getGender()));
                setProperty("user.isRememberMe",
                        String.valueOf(user.isRememberMe()));// 是否记住我的信息
            }
        });
    }

    public void updateUserInfo(final User user) {
        setProperties(new Properties() {
            {
                setProperty("user.name", user.getName());
                setProperty("user.face", user.getPortrait());// 用户头像-文件名
                setProperty("user.followers",
                        String.valueOf(user.getFollowers()));
                setProperty("user.fans", String.valueOf(user.getFans()));
                setProperty("user.score", String.valueOf(user.getScore()));
                setProperty("user.favoritecount",
                        String.valueOf(user.getFavoritecount()));
                setProperty("user.gender", String.valueOf(user.getGender()));
            }
        });
    }

    public User getLoginUser() {
        User user = new User();
        user.setId(StringUtils.toInt(getProperty("user.uid"), 0));
        user.setName(getProperty("user.name"));
        user.setPortrait(getProperty("user.face"));
        user.setAccount(getProperty("user.account"));
        user.setLocation(getProperty("user.location"));
        user.setFollowers(StringUtils.toInt(getProperty("user.followers")));
        user.setFans(StringUtils.toInt(getProperty("user.fans"), 0));
        user.setScore(StringUtils.toInt(getProperty("user.score"), 0));
        user.setFavoritecount(StringUtils.toInt(
                getProperty("user.favoritecount"), 0));
        user.setIsRememberMe(StringUtils.toBool(getProperty("user.isRememberMe")));
        user.setGender(getProperty("user.gender"));

        return user;
    }

    public void cleanLoginInfo() {
        loginUid = 0;
        login = false;
        removeProperty("user.uid", "user.name", "user.face", "user.location",
                "user.followers", "user.fans", "user.score",
                "user.isRememberMe", "user.gender", "user.favoritecount");
    }

    public int getLoginUid() {
        return loginUid;
    }

    public boolean isLogin() {
        return login;
    }

    public void logout() {
        cleanLoginInfo();
        ApiHttpClient.cleanCookie();

        cleanCookie();
        login = false;
        loginUid = 0;

        Intent intent = new Intent(Constants.INTENT_ACTION_LOGOUT);
        sendBroadcast(intent);
    }

    public void cleanCookie() {
        removeProperty(AppConfig.CONF_COOKIE);
    }

    public void clearAppCache() {
        DataCleanManager.cleanDatabases(this);
        DataCleanManager.cleanInternalCache(this);

        if (isMethodsCompat(Build.VERSION_CODES.FROYO)) {
            // TODO
            DataCleanManager.cleanCustomCache(getExternalCacheDir());
        }

        Properties props=getProperties();
        for (Object key : props.keySet()) {
            String _key = key.toString();
            if (_key.startsWith("temp")) {
                removeProperty(_key);
            }
        }
        Core.getKJBitmap().cleanCache();
    }

    public static void setLoadImage(boolean flag) {
        set(KEY_LOAD_IMAGE, flag);
    }

    public static boolean isMethodsCompat(int versionCode) {
        int currentVersion = Build.VERSION.SDK_INT;
        return currentVersion >= versionCode;
    }

    public static String getTweetDraft() {
        return getPreferences().getString(
                KEY_TWEET_DRAFT + getInstance().getLoginUid(), "");
    }

    public static void setTweetDraft(String draft) {
        set(KEY_TWEET_DRAFT + getInstance().getLoginUid(), draft);
    }

    public static String getNoteDraft() {
        return getPreferences().getString(
                AppConfig.KEY_NOTE_DRAFT + getInstance().getLoginUid(), "");
    }

    public static void setNoteDraft(String draft) {
        set(AppConfig.KEY_NOTE_DRAFT + getInstance().getLoginUid(), draft);
    }

    public static boolean isFristStart() {
        return getPreferences().getBoolean(KEY_FRITST_START, true);
    }

    public static void setFristStart(boolean frist) {
        set(KEY_FRITST_START, frist);
    }

    //夜间模式
    public static boolean getNightModeSwitch() {
        return getPreferences().getBoolean(KEY_NIGHT_MODE_SWITCH, false);
    }

    // 设置夜间模式
    public static void setNightModeSwitch(boolean on) {
        set(KEY_NIGHT_MODE_SWITCH, on);
    }
}
