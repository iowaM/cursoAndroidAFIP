package ar.gob.afip.mobile.android.tutorial.login.validator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserValidator {

    public static boolean isMailOk(String mail){
        return mail.contains("@");
    }
    public static boolean isPassOk(String pass){
        Integer uppers = 0;
        Integer lowers = 0;
        Integer numbers = 0;
        for(Integer i = 0; i < pass.length(); i++){
            if(Character.isLowerCase(pass.charAt(i))) lowers++;
            if(Character.isUpperCase(pass.charAt(i))) uppers++;
            if(Character.isDigit(pass.charAt(i))) numbers++;
        }

        return (lowers >= 1 && uppers >= 1 && numbers >= 2 && pass.length() >= 10);
    }
    public static boolean isCUITOk(String cuit){
        boolean ok = true;

        if(cuit == null || cuit.length() != 11){
            ok = false;
        }

        return ok;
    }

    public static boolean isJSONOk(String jo){
        try{
            new JSONObject(jo);
        }catch (JSONException e){
            try{
                new JSONArray(jo);
            }catch (JSONException je){
                return false;
            }
        }
        return true;
    }
}
