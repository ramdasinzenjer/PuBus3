package srt.inzenjer.pbts;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import srt.inzenjer.connnectors.Connectivity;
import srt.inzenjer.connnectors.Constants;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CBusStatus extends Activity{
	
	Spinner sp_busno; TextView tbusno,tlastloc;
	ArrayAdapter<String> sarray_bus; String sbsno,sbn_db,sloc,resp,lresp;
	
	//for mapping value from database
			ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cbusstatus);
		sp_busno=(Spinner)findViewById(R.id.mspinner_bus);
		tbusno=(TextView)findViewById(R.id.mtexttitlebus);
		tlastloc=(TextView)findViewById(R.id.mtextlastloc);
		
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
						
						tbusno.setText("Bus Nuber : "+oslist.get(arg2).get("busno")+"\n Source : "
						+oslist.get(arg2).get("src") +"\n Destination :  "+oslist.get(arg2).get("dest")
						+"\n Starting Time :  "+oslist.get(arg2).get("start_time")+"\n End Time : "+
						oslist.get(arg2).get("end_time")+"\n Bus Fare : "+oslist.get(arg2).get("bus_fare"));
					
						new getBusloc().execute();
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
	
	
	//-----------------------------------------------------------------------------------
	
	
	public class getBusloc extends AsyncTask<String, String, String>
	{

	
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String urlParameters=null;
			try {
				urlParameters= "busno="+ URLEncoder.encode(sbsno, "UTF-8");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			lresp = Connectivity.excutePost(Constants.BUSLOCFETCH_URL,
                    urlParameters);
			return lresp;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(lresp.contains("success"))
			{
				locparser();
			}
			else {
				
				tlastloc.setText("Locatin Unavailable.");
				Toast.makeText(getApplicationContext(), lresp, Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	public void locparser()
	{
		try
		{
			JSONObject jobject=new JSONObject(lresp);
			JSONObject jobject1=jobject.getJSONObject("Event");
			JSONArray ja=jobject1.getJSONArray("Details");
			int length=ja.length();

			for(int i=0;i<length;i++)
			{
				JSONObject data1=ja.getJSONObject(i);
			
				String location=data1.getString("location");
				String timedate=data1.getString("timedate");
				
				tlastloc.setText("Last location : "+location +"\n Updated on : "+timedate);
				
				SharedPreferences share=getSharedPreferences("LOCATION", MODE_WORLD_READABLE);
				SharedPreferences.Editor ed=share.edit();
				ed.putString("mloc", location);
				ed.putString("mltime", timedate);
				ed.commit();
				
				    
			}
					
		}
		catch(Exception e)
		{
			System.out.println("error:"+e);
		}
	}

	public void mymapview(View v)
	{
		
		Intent i=new Intent(getApplicationContext(),Maphome.class);
		startActivity(i);
	}

}
