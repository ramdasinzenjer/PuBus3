package srt.inzenjer.pbts;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import srt.inzenjer.connnectors.Connectivity;
import srt.inzenjer.connnectors.Constants;
import srt.inzenjer.pbts.CBusStatus.getBusloc;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class Bbstations extends Activity {
	
	Spinner sp_bussrc,sp_busdest; 
	ArrayAdapter<String> sarray_bussrc,sarray_busdest; String sbssrc,sbdest,resp;
	
	ListView mlist; ListAdapter adapter;
	
	//for mapping value from database
			ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bbstations);
		
		sp_bussrc=(Spinner)findViewById(R.id.mspinnsrc);
		sp_busdest=(Spinner)findViewById(R.id.mspinndest);
		mlist=(ListView)findViewById(R.id.mlist_buses);
		
		
		String[] dis = getResources().getStringArray(R.array.districts);
		sarray_bussrc=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,dis);
        sarray_bussrc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_bussrc.setAdapter(sarray_bussrc);
		sp_bussrc.setOnItemSelectedListener(new OnItemSelectedListener()
        {

	    	
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				sbssrc=arg0.getItemAtPosition(arg2).toString();
				
				((TextView) arg0.getChildAt(0)).setTextColor(Color.BLUE);
				
				Toast.makeText(getApplicationContext(), ""+sbssrc, Toast.LENGTH_SHORT).show();
			
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub	
			}
        	
        });
        
        
        sarray_busdest=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,dis);
        sarray_busdest.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_busdest.setAdapter(sarray_busdest);
        
        sp_busdest.setOnItemSelectedListener(new OnItemSelectedListener()
        {

	    	
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				sbdest=arg0.getItemAtPosition(arg2).toString();
				
				((TextView) arg0.getChildAt(0)).setTextColor(Color.BLUE);
				
				Toast.makeText(getApplicationContext(), ""+sbdest, Toast.LENGTH_SHORT).show();
				
				new getbusstops().execute();
			
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub	
			}
        	
        });
		
		
				
	}
	
	
	public class getbusstops extends AsyncTask<String, String, String>
	{

	
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String urlParameters=null;
			try {
				urlParameters= "src="+ URLEncoder.encode(sbssrc, "UTF-8") +"&&"
						+"dest="+ URLEncoder.encode(sbdest, "UTF-8");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			resp = Connectivity.excutePost(Constants.BUSBWSTOPS,
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
				oslist.clear();
				mlist.setAdapter(null);
			}
		}
		
	}
	public void parsingmethod()
	{
		
		oslist.add(null);
		oslist.clear();
		mlist.setAdapter(null);
		try
		{
			JSONObject jobject=new JSONObject(resp);
			JSONObject jobject1=jobject.getJSONObject("Event");
			JSONArray ja=jobject1.getJSONArray("Details");
			int length=ja.length();

			for(int i=0;i<length;i++)
			{
				JSONObject data1=ja.getJSONObject(i);
				
				String busno=data1.getString("busno");
				String src=data1.getString("src");
				String dest=data1.getString("dest");
				String start_time=data1.getString("start_time");
				String end_time=data1.getString("end_time");
				String bus_fare=data1.getString("bus_fare");
			
	            HashMap<String, String> map = new HashMap<String, String>();
	           
	            map.put("src", src);
	            map.put("dest", dest);
	            map.put("start_time", start_time);
	            map.put("end_time", end_time);
	            map.put("bus_fare", bus_fare);
	            map.put("busno", busno);
	           
	            map.put("notification", "Bus Number : "+busno+"\n Start time : "+start_time+"\n End time : "+end_time);
	    	        	            
	    	            oslist.add(map);
	    	            
	    	            adapter = new SimpleAdapter(getApplicationContext(), oslist,
	    	                R.layout.layout_single,
	    	                new String[] {"notification"}, new int[] {R.id.mtext_single});
	    	           
	    	            mlist.setAdapter(adapter);
	    	            
	    	            mlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	    	               
	    				@Override
	    	               public void onItemClick(AdapterView<?> parent, View view,
	    	                                            int position, long id) {               
	    	              // Toast.makeText(getApplicationContext(), 
	    	             //	   " "+oslist.get(+position).get("noti_data"), Toast.LENGTH_SHORT).show();	               
	    	               }
	    	                });
            		    
			}
			
		}
		catch(Exception e)
		{
			System.out.println("error:"+e);
		}
	}
	
	
}
