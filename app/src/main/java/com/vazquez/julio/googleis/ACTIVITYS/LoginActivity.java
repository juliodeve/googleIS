package com.vazquez.julio.googleis.ACTIVITYS;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.vazquez.julio.googleis.HTTPMANAGER.global;
import com.vazquez.julio.googleis.HTTPMANAGER.service;
import com.vazquez.julio.googleis.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient googleApiClient;
    SignInButton signInButton;
    public static final int SIGN_IN_CODE = 777;
    AutoCompleteTextView actMail;
    EditText etPass;
    Button btnAcces;
    Button btnInscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        actMail = (AutoCompleteTextView) findViewById(R.id.actMail);
        actMail.setText("");
        etPass = (EditText) findViewById(R.id.etPass);
        etPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        btnAcces = (Button) findViewById(R.id.btnAccess);
        btnInscription = (Button) findViewById(R.id.btnInscription);

        btnAcces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOnline()) {
                    taskBtnAcces();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.sin_internet, Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOnline()) {
                    if (esEmailCorrecto(actMail.getText().toString())) {
                        if (etPass.length() >= 8 & etPass.length() <= 16) {
                            taskBtnInscription();
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.contraseña_corta, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.correo_invalido, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), R.string.sin_internet, Toast.LENGTH_SHORT).show();
                }
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signInButton = (SignInButton) findViewById(R.id.btnGoogle);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setColorScheme(SignInButton.COLOR_DARK);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, SIGN_IN_CODE);
            }
        });

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            goMainScreen();
        } else {
            Toast.makeText(this, R.string.no_se_pudo_conectar, Toast.LENGTH_SHORT);
        }
    }

    private void goMainScreen() {
        Intent intent = new Intent(this, ActivityPrincipal.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void goMainScreen(String result, String mail) {
        Intent intent = new Intent(this, ActivityPrincipal.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("result", result);
        intent.putExtra("mail", mail);
        global.email = mail;
        global.ivar1 = 1;
        startActivity(intent);
    }

    public int objJSON(String respuesta) {
        int res = 0;
        try {
            JSONArray json = new JSONArray(respuesta);
            if (json.length() > 0) {
                res = 1;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }

    protected static boolean esEmailCorrecto(String email) {

        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

        Matcher mather = pattern.matcher(email);

        return mather.find();
    }

    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    private void taskBtnInscription() {
        final Thread tr = new Thread() {
            @Override
            public void run() {
                super.run();
                service s = new service();
                final String res = s.enviarPost(actMail.getText().toString(), etPass.getText().toString(), "http://juliovazquez.net/TrailWebServices/registro.php");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int r = objJSON(res);
                        if (res.equals("OK")) {
                            Toast.makeText(getApplication(), R.string.inscripcion_correcta, Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplication(), R.string.resivira_correo_confirmacion, Toast.LENGTH_SHORT).show();
                            actMail.setText("");
                            etPass.setText("");
                        } else {
                            Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        };
        tr.start();
    }

    private void taskBtnAcces() {
        final Thread tr = new Thread() {
            @Override
            public void run() {
                super.run();
                service s = new service();
                final String res = s.enviarPost(actMail.getText().toString(), etPass.getText().toString(), "http://juliovazquez.net/TrailWebServices/login.php");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (res.equals("OK")) {
                            goMainScreen("si", actMail.getText().toString());
                        } else {
                            if (res.equals("ACTIVAR")) {
                                Toast.makeText(getApplicationContext(), R.string.activar_cuenta, Toast.LENGTH_SHORT).show();
                            } else if (res.equals("USERPASS")) {
                                Toast.makeText(getApplicationContext(), R.string.correo_contraseña_incorrecta, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        };
        tr.start();
    }
}