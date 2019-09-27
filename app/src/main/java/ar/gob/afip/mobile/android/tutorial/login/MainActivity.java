package ar.gob.afip.mobile.android.tutorial.login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.json.JSONObject;

import ar.gob.afip.mobile.android.library.dnidigital.activities.DNIDigitalReaderActivity;
import ar.gob.afip.mobile.android.library.dnidigital.model.DNIInfo;
import ar.gob.afip.mobile.android.library.liblogin.activities.AFIPWebAuthActivity;
import ar.gob.afip.mobile.android.rootdetection.RootDetection;
import ar.gob.afip.mobile.android.rootdetection.RootDetectionImpl;
import ar.gob.afip.mobile.android.rootdetection.RootDetectionListener;
import ar.gob.afip.mobile.android.rootdetection.RootMethod;
import ar.gob.afip.mobile.android.tutorial.login.helper.DownloadImageTask;
import ar.gob.afip.mobile.android.tutorial.login.singleton.APIClient;
import ar.gob.afip.mobile.android.tutorial.login.singleton.MessageBroker;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    SharedPreferences prefs;
    JSONObject news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = ((Context) this).getSharedPreferences(APIClient.PREF_NAME, Context.MODE_PRIVATE);
        try {
            news = new JSONObject(prefs.getString("APP_CONFIG", null)).getJSONObject("news");

            String backURL = news.getString("backImageUrl");
            String topURL = news.getString("frontImageUrl");

            ImageView backImg = findViewById(R.id.back_image);
            new DownloadImageTask(backImg).execute(backURL);

            ImageView topImg = findViewById(R.id.top_image);
            new DownloadImageTask(topImg).execute(topURL);
            topImg.setOnClickListener(this);

        }catch (Exception e){

        }

        findViewById(R.id.iniciar_sesion_button).setOnClickListener(this);
        findViewById(R.id.dni_digital_button).setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.top_image) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(news.getJSONObject("onTap").getString("url")));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }catch (Exception e){
            }
        }
        if(viewId == R.id.iniciar_sesion_button){
            Intent intent  = new Intent(MainActivity.this , AFIPWebAuthActivity.class);

            Bundle b = new Bundle();
            b.putString(AFIPWebAuthActivity.AUTH_URL, "https://auth.afip.gob.ar/contribuyente");
            b.putString(AFIPWebAuthActivity.SYSTEM_HOST, "monotributo.afip.gob.ar");
            b.putString(AFIPWebAuthActivity.SYSTEM_ID, "admin_mono");
            intent.putExtras(b);
            this.openIntentWithoutRoot(this, intent, 2);
        }
        if(viewId == R.id.dni_digital_button){
            Intent intent = new Intent(MainActivity.this, DNIDigitalReaderActivity.class);
            this.openIntentWithoutRoot(this, intent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            DNIInfo dniInfo = (DNIInfo) data.getExtras().get(DNIDigitalReaderActivity.DNI_INFO);
            String dniPhoto = (String) data.getExtras().get(DNIDigitalReaderActivity.DNI_PHOTO);
            new AlertDialog.Builder(this).setTitle("Data Obtenida")
                    .setMessage("Nombre: " + dniInfo.getFirstName())
                    .setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.this.finish();
                        }
                    })
                    .show();
        }
    }

    private void openIntentWithoutRoot(Context ctx, final Intent intent, final Integer requestCode){
        RootDetection d = RootDetectionImpl.getInstance();
        d.detectRootedDevice(ctx, new RootDetectionListener() {
            @Override
            public void deviceRooted(Class aClass, RootMethod rootMethod, boolean b) {
                rootedAction();
            }

            @Override
            public void rootNotDetected(boolean b) {
                startActivityForResult(intent,requestCode);
            }

            @Override
            public void cannotVerifyIfRooted(boolean b) {
                // no se puede verificar si esta rooteado, quizás por algún error en la biblioteca.
            }
        });
    }

    private void rootedAction(){
        new AlertDialog.Builder(this).setTitle("Dispositivo Rootead")
                .setMessage("El dispositivo con el que intenta acceder se encuentra con permisos de Root. No puede continuar en la aplicacion.")
                .setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.finish();
                    }
                })
                .show();
    }
}
