package ar.gob.afip.mobile.android.tutorial.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import ar.gob.afip.mobile.android.tutorial.login.validator.UserValidator;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText mUserEmail;
    TextInputEditText mUserPassword;
    Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUserEmail = findViewById(R.id.user_email_edit_text);
        mUserPassword = findViewById(R.id.user_password_edit_text);

        mLoginButton = findViewById(R.id.login_button);

        mLoginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.login_button) {
            String email = mUserEmail.getText().toString();
            String password = mUserPassword.getText().toString();
            Boolean emailError = false;
            String toastMessage = null;

            if(!UserValidator.isMailOk(email)){
                toastMessage = getString(R.string.error_email_invalid);
                mUserEmail.setError(toastMessage);
                emailError = true;
            }

            if(!UserValidator.isPassOk(password)){
                toastMessage = getString(R.string.error_pass_invalid);
                mUserPassword.setError(toastMessage);
                if(emailError){
                    toastMessage = getString(R.string.error_login_ambos);
                }
            }
            Toast.makeText(LoginActivity.this, (toastMessage != null ? toastMessage : "Sin Errores"), Toast.LENGTH_LONG).show();
        }
    }
}
