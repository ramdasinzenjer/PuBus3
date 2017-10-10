package srt.inzenjer.pbts;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import srt.inzenjer.connnectors.Connectivity;
import srt.inzenjer.connnectors.Constants;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class AdminNotificationss extends Activity{
	
	EditText  etnot; String snoti,sdate,resp,respup; Button btup;
	ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
	ListAdapter adapter; 	ListView mlist;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ad_notifications);
		
		etnot=(EditText)findViewById(R.id.edit_adnoti);
		btup=(Button)findViewById(R.id.mbt_upnoti);
		mlist=(ListView)findViewById(R.id.mlist_adnoti);
		
		btup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				Calendar calobj = Calendar.getInstance();
				//System.out.println(df.format(calobj.getTime()));
				sdate=df.format(calobj.getTime());
				snoti=etnot.getText().toString();
				new NotificationUpApiTask().execute();
				
			}
		});
		new NotificationFetchApiTask().execute();
	}

	public class NotificationFetchApiTask extends AsyncTask<String, String, String>
	{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			String urlParameters=null;
			try {
				urlParameters="rqcode="+ URLEncoder.encode("0", "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			resp = Connectivity.excutePost(Constants.NOTIFICATIONFETCH_URL,
                    urlParameters);
			Log.e("AdminNotification", resp);
			return resp;
			
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			keyparser(resp);
			Toast.makeText(getApplicationContext(), ""+resp, Toast.LENGTH_SHORT).show();
		}

}
	

	public void keyparser(String result)
	{
		try
		{
			JSONObject  jObject = new JSONObject(result);
			JSONObject  jObject1 = jObject.getJSONObject("Event");
			JSONArray ja = jObject1.getJSONArray("Details"); 
			int length=ja.length();
			for(int i=0;i<length;i++)
			{
				JSONObject data1= ja.getJSONObject(i);
				String noti_data=data1.getString("noti_data");
				String noti_date=data1.getString("noti_date");
				String noti_uid=data1.getString("noti_uid");
				
				
				// Adding value HashMap key => value
	            HashMap<String, String> map = new HashMap<String, String>();
	            map.put("noti_data", noti_data);
	            map.put("noti_date", noti_date);
	            map.put("noti_uid", noti_uid);
	           	            
	            map.put("notification", "Requested on date : "+noti_date+"."+
	            "\n Status : 1.");
	        	            
	            oslist.add(map);
	            
	            adapter = new SimpleAdapter(getApplicationContext(), oslist,
	                R.layout.layout_single,
	                new String[] {"notification"}, new int[] {R.id.mtext_single});
	            mlist.setAdapter(adapter);
	            
	            mlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	               
				@Override
	               public void onItemClick(AdapterView<?> parent, View view,
	                                            int position, long id) {               
	               Toast.makeText(getApplicationContext(), 
	            		   " "+oslist.get(+position).get("noti_data"), Toast.LENGTH_SHORT).show();	               
	               }
	                });
			}
		}
			catch (Exception e)         
		{
				System.out.println("Error:"+e);
		}
	}

	public class NotificationUpApiTask extends AsyncTask<String, String, String>
	{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			String urlParameters=null;
			try {
				urlParameters="noti_data="+ URLEncoder.encode(snoti, "UTF-8")+"&&"
						+"noti_date="+ URLEncoder.encode(sdate, "UTF-8")+"&&"
						+"status="+ URLEncoder.encode("1", "UTF-8")+"&&"
						+"noti_uid="+ URLEncoder.encode("admin", "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			respup = Connectivity.excutePost(Constants.NOTIFICATIONUP_URL,
                    urlParameters);
			Log.e("AdminNotification", respup);
			return respup;
			
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Toast.makeText(getApplicationContext(), ""+respup, Toast.LENGTH_SHORT).show();
		}

}
	
}
