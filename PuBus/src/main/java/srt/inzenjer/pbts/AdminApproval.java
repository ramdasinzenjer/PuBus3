package srt.inzenjer.pbts;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import srt.inzenjer.connnectors.Connectivity;
import srt.inzenjer.connnectors.Constants;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class AdminApproval extends Activity{
	
	
	ListView mlist;
	ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
	String resdb,resdbup;	ApplicationPreference appPref;
	ListAdapter adapter;
	
	String status,name,email,phone,address,suname,sun;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ad_approval);
		appPref=(ApplicationPreference)getApplication();
		mlist=(ListView)findViewById(R.id.mlist_users);
		
		new UsersApiTask().execute();
		
	}

	
	public class UsersApiTask extends AsyncTask<String, String, String>
	{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			String urlParameters=null;
			try {
				urlParameters= "";
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			resdb = Connectivity.excutePost(Constants.USERDETAILS,
                    urlParameters);
			Log.e("MainHome", resdb);
			return resdb;
			
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			keyparser(resdb);
			Toast.makeText(getApplicationContext(), ""+resdb, Toast.LENGTH_SHORT).show();
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
				 status=data1.getString("status");
				name=data1.getString("name");
				 email=data1.getString("email");
				 phone=data1.getString("phone");
				 address=data1.getString("address");
				 suname=data1.getString("username");
				
				// Adding value HashMap key => value
	            HashMap<String, String> map = new HashMap<String, String>();
	            map.put("status", status);
	            map.put("name", name);
	            map.put("email", email);
	            map.put("phone", phone);
	            map.put("address", address);
	            map.put("username", suname);
	            
           	         
	            oslist.add(map);
	            
	            adapter = new SimpleAdapter(getApplicationContext(), oslist,
	                R.layout.layout_single,
	                new String[] {"name"}, new int[] {R.id.mtext_single});
	            mlist.setAdapter(adapter);
	            
	            mlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	               
				@Override
	               public void onItemClick(AdapterView<?> parent, View view,
	                                            int position, long id) {               
	               Toast.makeText(getApplicationContext(), 
	            		   " "+oslist.get(+position).get("name"), Toast.LENGTH_SHORT).show();	
	               openDialog(oslist.get(+position).get("status"), 
	            		   oslist.get(+position).get("name"),
	            		   oslist.get(+position).get("email"), 
	            		   oslist.get(+position).get("phone"),
	            		   oslist.get(+position).get("username"));
	               
	               }
	                });
			}
		}
			catch (Exception e)         
		{
				System.out.println("Error:"+e);
		}
	}
	
	public void openDialog(String stat, String snam, String sem, String sphn, String sunam){
	      AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
	      
	   final String mstat=stat; final String nam=snam; final String em=sem;
	   final String phn=sphn; sun=sunam;
	   	  
		   	alertDialogBuilder.setTitle("Please choose an action!");
		    alertDialogBuilder.setMessage("Name : "+nam+"\n Email : "+em+"\n Phone : "+phn);
	 if(mstat.equals("0"))    { 
		    alertDialogBuilder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
	         @Override
	         public void onClick(DialogInterface arg0, int arg1) {
	            
	            Toast.makeText(getApplicationContext(),"Accepted",Toast.LENGTH_SHORT).show();
	            new UpdateusersApiTask().execute();
       
	         }
	      });
	   }
	      
	      alertDialogBuilder.setNegativeButton("Ok",new DialogInterface.OnClickListener() {
	         @Override
	         public void onClick(DialogInterface dialog, int which) {
	        	
	        	 Toast.makeText(getApplicationContext(),"OK ",Toast.LENGTH_SHORT).show();
	        
	         }
	      });
	      
	    	
	      
	      AlertDialog alertDialog = alertDialogBuilder.create();
	      alertDialog.show();
	   }
	
	public class UpdateusersApiTask extends AsyncTask<String, String, String>
	{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			String urlParameters=null;
			try {
				urlParameters= "username=" +URLEncoder.encode(sun,"UTF-8");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			resdbup = Connectivity.excutePost(Constants.USERAPPROVE,
                    urlParameters);
			Log.e("AdminApproval", resdbup);
			return resdbup;
			
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Toast.makeText(getApplicationContext(), ""+resdbup, Toast.LENGTH_SHORT).show();
			new UsersApiTask().execute();
		}

}
	
}
