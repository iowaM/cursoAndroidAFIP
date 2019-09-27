package ar.gob.afip.mobile.android.tutorial.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import ar.gob.afip.mobile.android.tutorial.login.interfaces.Observer;
import ar.gob.afip.mobile.android.tutorial.login.singleton.APIClient;
import ar.gob.afip.mobile.android.tutorial.login.singleton.MessageBroker;
import ar.gob.afip.mobile.android.tutorial.login.validator.UserValidator;

public class SplashActivity extends AppCompatActivity implements Observer {

    SharedPreferences prefs;
    private Exception onConfigError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    public void onResume(){
        super.onResume();

        prefs = ((Context) this).getSharedPreferences(APIClient.PREF_NAME, Context.MODE_PRIVATE);

        String jo = prefs.getString("APP_CONFIG", null);

        if (jo == null || !UserValidator.isJSONOk(jo)) {
            MessageBroker.getInstance().observe(MessageBroker.CONFIG, this);
            APIClient.getInstance(SplashActivity.this).obtenerConfig( );
        }else {
            toMain();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MessageBroker.getInstance().stopObserving(MessageBroker.CONFIG, this);
    }

    public void toMain(){
        Intent aMain = new Intent(this, MainActivity.class);
        MessageBroker.getInstance().stopObserving(MessageBroker.CONFIG,this);
        startActivity(aMain);
    }

    public void onEvent(JSONObject o) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("APP_CONFIG", o.toString());
        editor.apply();
        toMain();
    }

    public void onEvent(Exception e) {
        new AlertDialog.Builder(this).setTitle("Error de conexión")
                .setMessage("Ocurrio un error al obtener la configuracion. ¿Desea reintentar?")
                .setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        APIClient.getInstance(SplashActivity.this).obtenerConfig( );
                    }
                })
                .setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SplashActivity.this.finish();
                    }
                })
                .show();
    }
}
