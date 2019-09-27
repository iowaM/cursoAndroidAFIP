package ar.gob.afip.mobile.android.tutorial.login.interfaces;

import org.json.JSONObject;

public interface Observer {
    void onEvent(JSONObject o);
    void onEvent(Exception o);
}
