package ar.gob.afip.mobile.android.tutorial.login.singleton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ar.gob.afip.mobile.android.tutorial.login.interfaces.Observer;

public class MessageBroker {

    public static String CONFIG = "CONFIG";
    private static MessageBroker instance;
    private HashMap<String, List<Observer>> observerHashMap;

    private MessageBroker(){
        observerHashMap = new HashMap<>();
    }

    public static MessageBroker getInstance(){
        if(instance == null){
            instance = new MessageBroker();
        }
        return instance;
    }

    public void observe(String tag, Observer o){

        List<Observer> os = observerHashMap.get(tag);
        if(os == null){
            os = new ArrayList<>();
        }
        os.add(o);
        observerHashMap.put(tag, os);
    }

    public void stopObserving(String tag, Observer o){

        List<Observer> os = observerHashMap.get(tag);
        if(os != null) {
            os.remove(o);
        }
    }

    public void publish(String tag, JSONObject jo){
        List<Observer> os = observerHashMap.get(tag);
        if(os != null) {
            for (Observer o : os) {
                o.onEvent(jo);
            }
        }
    }

    public void publish(String tag, Exception e){
        List<Observer> os = observerHashMap.get(tag);
        if(os != null) {
            for (Observer o : os) {
                o.onEvent(e);
            }
        }
    }
}
