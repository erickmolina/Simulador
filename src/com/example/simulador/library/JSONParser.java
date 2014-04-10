package com.example.simulador.library;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




import android.util.Log;

public class JSONParser {
	
	
	private ServiceHandler _Serviceh = new ServiceHandler();
	private static String url_unidades = "http://busstation-electivamoviles.rhcloud.com/controllers/unidades.php";
	    
	
	public JSONParser(){
		
	}
	
	
	/*
	* Descripcion:
	* Entradas:
	* Salidas:
	* Reestricciones:
	*/
	public JSONObject obtenerJSON(String URL, List<NameValuePair> params ){
		
	    String jsonStr = _Serviceh.makeServiceCall(URL, ServiceHandler.POST,params);
	 
        Log.d("Response: ", "> " + jsonStr);
 
        if (jsonStr != null) {
        	try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                 return jsonObj;
                
        
        	} 
        	catch (JSONException e) {
        		e.printStackTrace();
        	}
        } 
        else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
            return null;
        }
		return null;
		
	}
	
	
	
	/*
	* Descripcion:
	* Entradas:
	* Salidas:
	* Reestricciones:
	*/
	public  int  actualizarUbicacion(String pIdentificador, String pLatitud, String pLongitud, String pEstado){
	
		int success = 0;
	    List<NameValuePair> params1 = new ArrayList<NameValuePair>();
	    params1.add(new BasicNameValuePair("identifier", pIdentificador));
	    params1.add(new BasicNameValuePair("latitud",pLatitud));
	    params1.add(new BasicNameValuePair("longitud", pLongitud));
	    params1.add(new BasicNameValuePair("state",pEstado));
	    
        JSONObject jsonObj = obtenerJSON(url_unidades,params1);
                 
        try{
        	
	        success = jsonObj.getInt("success");           
        }
        catch (JSONException e) {
    		e.printStackTrace();
    	}
                
        
        return success;
	}
	
}

