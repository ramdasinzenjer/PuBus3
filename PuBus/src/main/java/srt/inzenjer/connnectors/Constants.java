package srt.inzenjer.connnectors;


public interface Constants {

    //Progress Message
    String LOGIN_MESSAGE="Logging in...";
    String REGISTER_MESSAGE="Register in...";

    //Urls
    String BASE_URL="https://pubuss.000webhostapp.com/";
    String REGISTER_URL=BASE_URL+"mRegisterUser.php?";
    String LOGIN_URL=BASE_URL+"mLoginUser.php?";
    String NOTIFICATIONFETCH_URL=BASE_URL+"mNotifetch.php?";
    String BUSNOFETCH_URL=BASE_URL+"mBusinofetch.php?";
    String BUSLOCFETCH_URL=BASE_URL+"mBusloc.php?";
    String BUSBWSTOPS=BASE_URL+"mBusdatafetch.php?";
    String USERDETAILS=BASE_URL+"mUserdetails.php?";
    
    String USERAPPROVE=BASE_URL+"mUserapprove.php?";
    String BUSDATAIN_URL=BASE_URL+"mBusinfoin.php?";
    String NOTIFICATIONUP_URL=BASE_URL+"mNotiup.php?";
    String LOCATIONUP_URL =BASE_URL+"mLocup.php?";
    String BUSSTOPFETCH_URL =BASE_URL+"mBusstops.php?";
    String BUSSTOPUPDATE_URL=BASE_URL+"mBusstopup.php?";
    //Details
    String PASSWORD="Password";
    String USERNAME="Username";
    String LOGINSTATUS="LoginStatus";
    
    //SharedPreference
    String PREFERENCE_PARENT="Parent_Pref";
	
   
}
