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

public class AdminAddBus extends Activity{
	
	EditText  etbn,etbf,etstim,etetim; Spinner spsrc,spdest; Button mbadd;
	
	ArrayAdapter<String> sarray_bussrc,sarray_busdest; String sbssrc,sbdest,resplist,respdb;
	String sbn,sbf,sstime,setime;
	
	String bbn,bbst,bbet,bbf;
		
	ListView mlist; ListAdapter adapter;	
	//for mapping value from database
	ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addbus);
		
		spsrc=(Spinner)findViewById(R.id.mspinnaddsrc);
		spdest=(Spinner)findViewById(R.id.mspinnadddest);
		mlist=(ListView)findViewById(R.id.mlist_adbus);
		
		etbn=(EditText)findViewById(R.id.et_mbsno);
		etbf=(EditText)findViewById(R.id.et_mbsfare);
		etstim=(EditText)findViewById(R.id.et_mbsstime);
		etetim=(EditText)findViewById(R.id.et_mbsetime);
		
		mbadd=(Button)findViewById(R.id.mbt_addbuss);
		
		String[] dis = getResources().getStringArray(R.array.districts);
		sarray_bussrc=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,dis);
        sarray_bussrc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spsrc.setAdapter(sarray_bussrc);
		spsrc.setOnItemSelectedListener(new OnItemSelectedListener()
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
        spdest.setAdapter(sarray_busdest);
        
        spdest.setOnItemSelectedListener(new OnItemSelectedListener()
        {

	    	
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				sbdest=arg0.getItemAtPosition(arg2).toString();
				
				((TextView) arg0.getChildAt(0)).setTextColor(Color.BLUE);
				
				Toast.makeText(getApplicationContext(), ""+sbdest, Toast.LENGTH_SHORT).show();
							
			
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub	
			}
        	
        });
		
        
        mbadd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				sbn=etbn.getText().toString();
				sbf=etbf.getText().toString();
				sstime=etstim.getText().toString();
				setime=etetim.getText().toString();
				
				new putbusinfoup().execute();
				
			}
		});
        
        new getbusinfo().execute();
	}

	public class getbusinfo extends AsyncTask<String, String, String>
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
			resplist = Connectivity.excutePost(Constants.BUSNOFETCH_URL,
                    urlParameters);
			return resplist;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(resplist.contains("success"))
			{
				parsingmethod();
			}
			else {
				Toast.makeText(getApplicationContext(), resplist, Toast.LENGTH_SHORT).show();
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
			JSONObject jobject=new JSONObject(resplist);
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
            
	    	              openDialog(oslist.get(+position).get("busno"),
	    	            		  oslist.get(+position).get("start_time"),
	    	            		  oslist.get(+position).get("end_time"),
	    	            		  oslist.get(+position).get("bus_fare")
	    	            		  );
	    	              
	    				}
	    	                });
            		    
			}
			
		}
		catch(Exception e)
		{
			System.out.println("error:"+e);
		}
	}
	
	public void openDialog(String bn,String bst,String bet,String bf){
	      AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
	      
	      
	      bbn=bn; bbst=bst; bbet=bet; bbf=bf;
	    	 alertDialogBuilder.setTitle("Choose Action");
		     alertDialogBuilder.setMessage("Would you like to update bus details?...");    	
		     alertDialogBuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
	         @Override
	         public void onClick(DialogInterface arg0, int arg1) {
	            
	            Toast.makeText(getApplicationContext(),"Update",Toast.LENGTH_SHORT).show(); 
	            
	            etbn.setText(bbn); etstim.setText(bbst);
	            etetim.setText(bbet); etbf.setText(bbf);
	                   
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
	
	
	public class putbusinfoup extends AsyncTask<String, String, String>
	{

	
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String urlParameters=null;
			try {
				urlParameters= "busno="+ URLEncoder.encode(sbn, "UTF-8") +"&&"
						+"bus_fare="+ URLEncoder.encode(sbf, "UTF-8") +"&&"
						+"start_time="+ URLEncoder.encode(sstime, "UTF-8") +"&&"
						+"end_time="+ URLEncoder.encode(setime, "UTF-8") +"&&"
						+"src="+ URLEncoder.encode(sbssrc, "UTF-8") +"&&"
						+"dest="+ URLEncoder.encode(sbdest, "UTF-8");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			respdb = Connectivity.excutePost(Constants.BUSDATAIN_URL,
                    urlParameters);
			return respdb;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(respdb.contains("success"))
			{
				Toast.makeText(getApplicationContext(), respdb, Toast.LENGTH_SHORT).show();
			}
			else {
				Toast.makeText(getApplicationContext(), respdb, Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
}
