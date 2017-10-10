package srt.inzenjer.pbts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AdminHome extends Activity{
	
	Button btaddbus,btaddloc,btaddnotif,btaddbusstop,btapproval;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adminhome);
		btaddbus=(Button)findViewById(R.id.mbt_addbusdetils);
		btaddbusstop=(Button)findViewById(R.id.mbt_addbusstops);
		btaddloc=(Button)findViewById(R.id.mbt_addbusloc);
		btaddnotif=(Button)findViewById(R.id.mbt_addnotif);
		btapproval=(Button)findViewById(R.id.mbt_approveuser);
		
		btaddbus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		//		Toast.makeText(getApplicationContext(), "Add bus details", Toast.LENGTH_SHORT).show();
				Intent i=new Intent(getApplicationContext(),AdminAddBus.class);
				startActivity(i);
			}
		});
		
		btaddbusstop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			//	Toast.makeText(getApplicationContext(), "Add bus stop details", Toast.LENGTH_SHORT).show();
				Intent i=new Intent(getApplicationContext(),AdminAddBusStops.class);
				startActivity(i);
			
			}
		});
		
		btaddloc.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				Intent i=new Intent(getApplicationContext(),AdminAddLocations.class);
				startActivity(i);
			
			}
		});
		
		btaddnotif.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		
				Intent i=new Intent(getApplicationContext(),AdminNotificationss.class);
				startActivity(i);
			}
		});
		
		btapproval.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent i=new Intent(getApplicationContext(),AdminApproval.class);
				startActivity(i);
			}
		});
		
		
	}

}
