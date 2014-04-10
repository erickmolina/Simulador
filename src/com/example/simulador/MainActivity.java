package com.example.simulador;

import java.util.Random;

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

import com.example.simulador.library.JSONParser;


public class MainActivity extends Activity {
	
	private ProgressDialog pDialog;
	private TextView tvLocation = null;
	private Button Boton = null;
	
	private JSONParser jsonParser = new JSONParser();
	private String Identificador = "LSxC-01yu";  
	private String Latitud;
	private String Longitud;
	private String Estado;
	
   
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        pDialog = ProgressDialog.show(this, "Location", "Esperando");
        
        tvLocation = (TextView) this.findViewById(R.id.Texto);
        configGPS();
        Boton = (Button) this.findViewById(R.id.Salir);
    }
    
    
    
    /*
	* Descripcion:
	* Entradas:
	* Salidas:
	* Reestricciones:
	*/
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
    	tvLocation.setText("latitud: "+Latitud+"\n"+"Logitud: "+Longitud+"\n"+Estado+"\n"+Identificador);
    	pDialog.dismiss();
    	
    	
    	Boton = (Button)findViewById(R.id.Salir);
    	Boton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				configGPS();				
			}
		});
    }
    
    
    /*
	* Descripcion:
	* Entradas:
	* Salidas:
	* Reestricciones:
	*/
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
			upd(location);
			//llamar al web service
	        int success =jsonParser.actualizarUbicacion(Identificador, Latitud, Longitud, Estado);
	        if (success!=1){
	        	Log.d("Fallo", "No se actualizo localizacion");
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
