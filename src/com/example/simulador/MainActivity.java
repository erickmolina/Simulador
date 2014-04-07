package com.example.simulador;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity {
	
	private ProgressDialog pDialog;
	private TextView tvLocation = null;
	private Button Boton = null;
	
	private String Identificador = "LCxS-02a";  
	private String Latitud;
	private String Longitud;
	private String Estado;
	
    private static final String url_actualizarUbicacion = "http://busstation-electivamoviles.rhcloud.com/actualizarUbicacion.php";
    
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        pDialog = ProgressDialog.show(this, "Location", "Esperando");
        
        tvLocation = (TextView) this.findViewById(R.id.Texto);
        configGPS();
        Boton = (Button) this.findViewById(R.id.Salir);
    }
    private void upd(Location location)
    {
    	String[] a = new String[4];
    	a[0]="Mecanico";
    	a[1]="Presas";
    	a[2]="Fuera de uso";
    	a[3]="Accidente";
    	Random r = new Random();

    	int limite=4;
    	int b = r.nextInt(limite);
    	
    	Latitud =String.valueOf(location.getLatitude());
    	Longitud =String.valueOf(location.getLongitude());
    	Estado = a[b];
    	tvLocation.setText("latitud: "+Latitud+"\n"+"Logitud: "+Longitud+"\n"+Estado+"\n"+/*Entrada.getText()*/"www");
    	pDialog.dismiss();
    	
    	
    	Boton = (Button)findViewById(R.id.Salir);
    	Boton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				configGPS();				
			}
		});

    
    }
    public void configGPS()
    {
    	LocationManager mLocationManager;
    	LocationListener mLocationListener;
    	
    	mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    	mLocationListener = new MyLocationListenert();
    	mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,100, 1, mLocationListener);
    	
    }

    private class MyLocationListenert implements LocationListener
    {

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			Log.d("simulador", "latitud: "+String.valueOf(location.getLatitude()));
			Log.d("simulador", "Logitud: "+String.valueOf(location.getLongitude()));			
			upd(location);
			// llamar al web servise con los datos location.getLatitude location.getLongitude y el estado
			
			// Creating HTTP client
	        HttpClient httpClient = new DefaultHttpClient();
	        // Creating HTTP Post
	        HttpPost httpPost = new HttpPost(url_actualizarUbicacion);
	 
	        // Building post parameters
	        // key and value pair
	        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(4);
	        nameValuePair.add(new BasicNameValuePair("identifier", Identificador));
	        nameValuePair.add(new BasicNameValuePair("latitud",Latitud));
	        nameValuePair.add(new BasicNameValuePair("longitud", Longitud));
	        nameValuePair.add(new BasicNameValuePair("state",Estado));
	 
	        // Url Encoding the POST parameters
	        try {
	            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
	        } catch (UnsupportedEncodingException e) {
	            // writing error to Log
	            e.printStackTrace();
	        }
	 
	        // Making HTTP Request
	        try {
	            HttpResponse response = httpClient.execute(httpPost);
	 
	            // writing response to log
	            Log.d("Http Response:", response.toString());
	        } catch (ClientProtocolException e) {
	            // writing exception to log
	            e.printStackTrace();
	        } catch (IOException e) {
	            // writing exception to log
	            e.printStackTrace();
	        }
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {	
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
    	
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
