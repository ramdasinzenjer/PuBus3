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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AdminAddBusStops extends Activity{
	
	 Button badstop; Spinner sp_busno;
	ArrayAdapter<String> sarray_bus;
	
	ListView mlist; ListAdapter adapter;	
	
	String sbsno,sbn_db,resp,sdate,resplist;
	
	//For Updating 
	EditText eadbstop,eadbtime;
	String s_bstop,s_btime,upresp,mbst,mbstt;
	
	//for mapping value from database
	ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> moslist = new ArrayList<HashMap<String, String>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addbusstop);
		mlist=(ListView)findViewById(R.id.mlist_bstopss);
		eadbstop=(EditText)findViewById(R.id.et_bstopps);
		eadbtime=(EditText)findViewById(R.id.et_bstimeee);
		badstop=(Button)findViewById(R.id.mbt_upbstop);
		sp_busno=(Spinner)findViewById(R.id.mspin_bn);
		
		badstop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				s_bstop=eadbstop.getText().toString();
				s_btime=eadbtime.getText().toString();
				
				new BusStopUpdateApi().execute();
				
			}
		});
		
		new Busno_fetch().execute();
			
	}

	public class Busno_fetch extends AsyncTask<String, String, String>
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
				Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_SHORT).show();
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
						new BusStopFetchApi().execute();
						
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
	
	//////////////////------------------------------------------------
	public class BusStopFetchApi extends AsyncTask<String, String, String>
	{

	
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String urlParameters=null;
			try {
				urlParameters= "busno="+URLEncoder.encode(sbsno,"UTF-8");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			resplist = Connectivity.excutePost(Constants.BUSSTOPFETCH_URL,
                    urlParameters);
			return resplist;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(resplist.contains("success"))
			{
				Toast.makeText(getApplicationContext(), resplist, Toast.LENGTH_SHORT).show();
				keyparsingmethod();
			}
			else {
				Toast.makeText(getApplicationContext(), resplist, Toast.LENGTH_SHORT).show();
				moslist.add(null);				
				moslist.clear();
				mlist.setAdapter(null);
			}
		}
		
	}
	public void keyparsingmethod()
	{
		
		moslist.add(null);
		moslist.clear();
		mlist.setAdapter(null);
		try
		{
			JSONObject jobject=new JSONObject(resplist);
			JSONObject jobject1=jobject.getJSONObject("Event");
			JSONArray ja=jobject1.getJSONArray("Details");
			int length=ja.length();

			for(int i=0;i<length;i++)
			{
				JSONObject data1=ja.getJSONObject(i);
				
				String stop=data1.getString("stop");
				String bustime=data1.getString("bustime");
				
	            HashMap<String, String> map = new HashMap<String, String>();
	            
	            map.put("stop", stop);
	            map.put("bustime", bustime);
	            	           
	            map.put("notification", "Bus stop : "+stop+"\n Time : "+bustime);
	    	        	            
	    	            moslist.add(map);
	    	            
	    	            adapter = new SimpleAdapter(getApplicationContext(), moslist,
	    	                R.layout.layout_single,
	    	                new String[] {"notification"}, new int[] {R.id.mtext_single});
	    	           
	    	            mlist.setAdapter(adapter);
	    	            
	    	            mlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	    	               
	    				@Override
	    	               public void onItemClick(AdapterView<?> parent, View view,
	    	                                            int position, long id) {               
	    	               Toast.makeText(getApplicationContext(), 
	    	             	   " "+moslist.get(+position).get("notification"), Toast.LENGTH_SHORT).show();	               
	    	             
	    	               openDialog(moslist.get(+position).get("stop"),
	    	            		   moslist.get(+position).get("bustime"));	    	               
	    				}
	    	                });           		    
			}
		}
		catch(Exception e)
		{
			System.out.println("error:"+e);
		}
	}
	
	
	public void openDialog(String bs_sp,String bs_time){
	      AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
	      
	      
	      mbst=bs_sp; mbstt=bs_time; 
	    	 alertDialogBuilder.setTitle("Choose Action");
		     alertDialogBuilder.setMessage("Would you like to update bus details?...");    	
		     alertDialogBuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
	         @Override
	         public void onClick(DialogInterface arg0, int arg1) {
	            
	            Toast.makeText(getApplicationContext(),"Update",Toast.LENGTH_SHORT).show();   
	            eadbstop.setText(mbst); eadbtime.setText(mbstt);
	                      
	         }
	      });
	     
		     alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
	         @Override
	         public void onClick(DialogInterface dialog, int which) {
	        	       	 
	        	 Toast.makeText(getApplicationContext(),"No",Toast.LENGTH_SHORT).show();
	        	 //finish();
	         }
	      });
	      
		     AlertDialog alertDialog = alertDialogBuilder.create();
		     alertDialog.show();
	   }
	
	//------------------------------------------------------------------------------------------
	
	public class BusStopUpdateApi extends AsyncTask<String, String, String>
	{

	
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String urlParameters=null;
			try {
				urlParameters= "busno="+URLEncoder.encode(sbsno,"UTF-8")+"&&"
						+"stop="+URLEncoder.encode(s_bstop,"UTF-8")+"&&"
						+"bustime="+URLEncoder.encode(s_btime,"UTF-8");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			upresp = Connectivity.excutePost(Constants.BUSSTOPUPDATE_URL,
                    urlParameters);
			return upresp;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(upresp.contains("success"))
			{
				Toast.makeText(getApplicationContext(), upresp, Toast.LENGTH_SHORT).show();
			}
			else {
				Toast.makeText(getApplicationContext(), upresp, Toast.LENGTH_SHORT).show();

			}
		}
		
	}
	
	
}
