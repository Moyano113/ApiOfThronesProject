package com.example.apiofthronesproject.controller;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.apiofthronesproject.R;
import com.example.apiofthronesproject.fragment.SettingsFragment;
import com.example.apiofthronesproject.model.User;
import com.example.apiofthronesproject.util.SavedPreferences;

import es.dmoral.toasty.Toasty;


public class LoginActivity extends AppCompatActivity {
    private EditText txtUsuario;
    private EditText txtContrasena;
    private Button btnLogin;
    private Button btnRegister;
    private CtrlUser cu;
    private boolean notificaciones;
    private boolean sesion;
    MainActivity ma;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Se cargan las preferencias y se asignan sus respectivos valores a las variables 'sesion'
        //y 'notificaciones'
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        notificaciones = preferences.getBoolean("notificaciones", true);
        sesion = preferences.getBoolean("inicioSesion", true);

        txtUsuario = (EditText) findViewById(R.id.txtUsuario);
        txtContrasena = (EditText) findViewById(R.id.txtContrasena);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        cu = new CtrlUser(getApplicationContext());
        ma = new MainActivity();

        //Si "Mantener sesion iniciada" está activo directamente pasa a la MainActivity
        if (sesion == true) {
            Intent iMain = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(iMain);
            finish();
        }



        //Botón "ENTRAR"
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuario = txtUsuario.getText().toString();
                String contrasena = txtContrasena.getText().toString();

                //Comrpueba que no haya campos vacíos
                if ((usuario.equals("") || contrasena.equals(""))) {
                    //makeToast("Campos vacíos");
                    createToasty("warning", LoginActivity.this, "Campos vacíos");
                //Mediante la base de datos comprueba que la contraseña y el usuario sean correctos
                } else if (cu.validate(usuario, contrasena) == 0) {
                    //makeToast("Sesión iniciada");
                    createToasty("success", LoginActivity.this, "Sesión iniciada");
                    //Pasa a la MainActivity
                    Intent iMain = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(iMain);
                    finish();
                } else {
                    //makeToast("Usuario o contraseña incorrectos");
                    createToasty("error", LoginActivity.this, "Usuario o contraseña incorrectos");
                }
            }
        });

        //Botón "REGISTRARSE"
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User u = new User();
                u.setUsuario(txtUsuario.getText().toString());   //Se recogen los valores de los
                u.setContrasena(txtContrasena.getText().toString()); //cuadros de texto.

                if (cu.isNull(u) == 0) { //Se comprueba que ninguno de los campos sea nulo.
                    //makeToast("Campos vacíos");
                    createToasty("warning", LoginActivity.this, "Campos vacíos");
                } else if (cu.insertUser(u)) { //Se intenta insertar el usuario y cambia de actividad.
                    //makeToast("Registrado Correctamente");
                    createToasty("success", LoginActivity.this, "Registrado correctamente");
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                } else { //Si no se ha podido insertar es porque ya existe dicho usuario.
                    //makeToast("Usuario ya existente");
                    createToasty("warning", LoginActivity.this, "Usuario ya existente");
                }
            }
        });
    }

    //Método auxiliar usado para crear toast cada vez que sea necesario
    public void makeToast(String text) {
        if (notificaciones == true) {
            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        }
    }

    //Creacion de los Toasty
    public void createToasty(String key, Context c, String msg){
        if(notificaciones == true){
            switch (key){
                case "error":
                    Toasty.error(c, msg).show();
                    break;
                case "info":
                    Toasty.info(c, msg).show();
                    break;
                case "success":
                    Toasty.success(c, msg).show();
                    break;
                case "warning":
                    Toasty.warning(c, msg).show();
                    break;
                case "normal":
                    Toasty.normal(c, msg).show();
                    break;
                default:
                    Toasty.normal(c, msg).show();
                    break;
            }
        }
    }
}