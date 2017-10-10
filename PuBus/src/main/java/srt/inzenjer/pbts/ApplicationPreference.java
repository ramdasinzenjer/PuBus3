package srt.inzenjer.pbts;

import srt.inzenjer.connnectors.Constants;
import android.app.Application;
import android.content.SharedPreferences;


 public class ApplicationPreference extends Application {
    private static SharedPreferences appSharedPrefs;
    private SharedPreferences.Editor prefsEditor;
    String Username,Password;
    Boolean LoginStatus;

    public Boolean getLoginStatus() {
        LoginStatus= appSharedPrefs.getBoolean(Constants.LOGINSTATUS, false);     
        return LoginStatus;
    }

    public void setLoginStatus(Boolean loginStatus) {
        prefsEditor.putBoolean(Constants.LOGINSTATUS,loginStatus);
        prefsEditor.commit();
    }

    @SuppressWarnings("static-access")
	@Override
    public void onCreate() {
        super.onCreate();
        this.appSharedPrefs = getApplicationContext().getSharedPreferences(
                Constants.PREFERENCE_PARENT, MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
        
    }

}
