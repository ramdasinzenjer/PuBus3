package srt.inzenjer.pbts;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import srt.inzenjer.connnectors.Connectivity;
import srt.inzenjer.connnectors.Constants;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AdminAddLocations extends Activity implements LocationListener{
	
	Spinner sp_busno; EditText etloc; ImageView imv; Button blup;
	ArrayAdapter<String> sarray_bus; String sbsno,sbn_db,resp,sdate,respup,sl;
	
	ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
	
	String stplace; double latitude,longitude; Location location;
	
	String sh_id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ad_locations);
		sp_busno=(Spinner)findViewById(R.id.mspinner_busnum);
		etloc=(EditText)findViewById(R.id.edit_lc);
		imv=(ImageView)findViewById(R.id.img_loc);
		blup=(Button)findViewById(R.id.mbt_uploc);
		
		SharedPreferences share=getSharedPreferences("mKey", MODE_WORLD_READABLE);
		sh_id=share.getString("keyuid", "");
		getmyloc();
		imv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				etloc.setText(stplace);
			}
		});
		blup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				Calendar calobj = Calendar.getInstance();
				//System.out.println(df.format(calobj.getTime()));
				sdate=df.format(calobj.getTime());
				sl=etloc.getText().toString();
				
				new LocationUpApiTask().execute();
				
			}
		});
		
		
		new getbusno_date().execute();
	}
	
	public class getbusno_date extends AsyncTask<String, String, String>
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
			resp = Connectivity.excutePost(Constants.BUSNOFETCH_URL,
                    urlParameters);
			return resp;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(resp.contains("success"))
			{
				parsingmethod();
			}
			else {
				Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_SHORT).show();
				oslist.add(null);
				sp_busno.setAdapter(null);
			}
		}
		
	}
	public void parsingmethod()
	{
		try
		{
			JSONObject jobject=new JSONObject(resp);
			JSONObject jobject1=jobject.getJSONObject("Event");
			JSONArray ja=jobject1.getJSONArray("Details");
			int length=ja.length();
			/*oslist.add(null);
			sp_date.setAdapter(null);*/
			List<String> lables1 = new ArrayList<String>();
			for(int i=0;i<length;i++)
			{
				JSONObject data1=ja.getJSONObject(i);
				sbn_db=data1.getString("busno");
				String src=data1.getString("src");
				String dest=data1.getString("dest");
				String start_time=data1.getString("start_time");
				String end_time=data1.getString("end_time");
				String bus_fare=data1.getString("bus_fare");

				
	            HashMap<String, String> map = new HashMap<String, String>();
	            map.put("busno", sbn_db);
	            map.put("src", src);
	            map.put("dest", dest);
	            map.put("start_time", start_time);
	            map.put("end_time", end_time);
	            map.put("bus_fare", bus_fare);

	            
	            oslist.add(map);
	            
	            lables1.add(oslist.get(i).get("busno"));
	            
	            sarray_bus=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,lables1);
	            sarray_bus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	            sp_busno.setAdapter(sarray_bus);
	            
	            sp_busno.setOnItemSelectedListener(new OnItemSelectedListener()
		        {

			    	
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						sbsno=arg0.getItemAtPosition(arg2).toString();
						
						((TextView) arg0.getChildAt(0)).setTextColor(Color.BLUE);
						
						Toast.makeText(getApplicationContext(), ""+sbsno, Toast.LENGTH_SHORT).show();
						
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub	
					}
		        	
		        });
			    
			}
			
			
		}
		catch(Exception e)
		{
			System.out.println("error:"+e);
		}
		
	}
	//--------------------------------------------------------------------------------
	public class LocationUpApiTask extends AsyncTask<String, String, String>
	{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			String urlParameters=null;
			try {
				urlParameters="busno="+ URLEncoder.encode(sbsno, "UTF-8")+"&&"
						+"location="+ URLEncoder.encode(sl, "UTF-8")+"&&"
						+"timedate="+ URLEncoder.encode(sdate, "UTF-8")+"&&"
						+"updated_by="+ URLEncoder.encode(sh_id, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			respup = Connectivity.excutePost(Constants.LOCATIONUP_URL,
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
	
	
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		Geocoder gc= new Geocoder(this, Locale.ENGLISH);
        // Getting latitude of the current location
        latitude =  location.getLatitude();

        // Getting longitude of the current location
        longitude =  location.getLongitude();

try {
	List<Address> addresses = gc.getFromLocation(latitude,longitude, 1);
	
	if(addresses != null) {
		Address returnedAddress = addresses.get(0);
		StringBuilder strReturnedAddress = new StringBuilder("");
		for(int i=0; i<returnedAddress.getMaxAddressLineIndex(); i++) 
		{
			strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
			
		}
	
		stplace=strReturnedAddress.toString().trim();
		Toast.makeText( getBaseContext(),stplace,Toast.LENGTH_SHORT).show();
	}

	else{
		Toast.makeText(getBaseContext(),"GPS Disabled",Toast.LENGTH_SHORT).show();
	
	}
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
	}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	
	public void getmyloc()
	{
		try {
						
			 LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

			    // Creating a criteria object to retrieve provider
			    Criteria criteria = new Criteria();

			    // Getting the name of the best provider
			    String provider = locationManager.getBestProvider(criteria, true);

			    // Getting Current Location
			    location = locationManager.getLastKnownLocation(provider);
			    

			    if(location!=null){
			            onLocationChanged(location);
			            
			    }

			    locationManager.requestLocationUpdates(provider, 120000, 0, this);
		} catch (Exception e) {
			// TODO: handle exception
			
			e.printStackTrace();
		}
	}
	
}
