package ar.gob.afip.mobile.android.tutorial.login.singleton;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class APIClient {

    public static String PREF_NAME = "AppPrefs";

    private static APIClient instance;
    private RequestQueue queue;

    private APIClient(Context context){
        queue = Volley.newRequestQueue(context);
    }

    public static APIClient getInstance(Context context){
        if(instance == null){
            instance = new APIClient(context);
        }
        return instance;
    }

    public void obtenerConfig(){
        String url = "https://www.afip.gob.ar/appMovil/monotributo/config.json";
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        MessageBroker.getInstance().publish(MessageBroker.CONFIG, response);
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MessageBroker.getInstance().publish(MessageBroker.CONFIG, error);
            }
        });
        queue.add(jsonRequest);
    }
}
