package com.example.apiofthronesproject.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.apiofthronesproject.R;
import com.example.apiofthronesproject.model.User;


public class LoginActivity extends AppCompatActivity {
    private EditText txtUsuario;
    private EditText txtContrasena;
    private Button btnLogin;
    private Button btnRegister;
    private CtrlUser cu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtUsuario = (EditText) findViewById(R.id.txtUsuario);
        txtContrasena = (EditText) findViewById(R.id.txtContrasena);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        cu = new CtrlUser(getApplicationContext());


        //Botón "ENTRAR"
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuario = txtUsuario.getText().toString();
                String contrasena = txtContrasena.getText().toString();

                if((usuario.equals("") || contrasena.equals(""))){
                    makeToast("Campos vacíos");
                }else if(cu.validate(usuario, contrasena) == 0){
                    Intent iMain = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(iMain);
                    finish();
                }else{
                    makeToast("Usuario o contraseña incorrectos");
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

                if(cu.isNull(u) == 0){ //Se comprueba que ninguno de los campos sea nulo.
                    makeToast("Campos vacíos");
                }else if(cu.insertUser(u)){ //Se intenta insertar el usuario y cambia de actividad.
                    makeToast("Registrado Correctamente");
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }else{ //Si no se ha podido insertar es porque ya existe dicho usuario.
                    makeToast("Usuario ya existente");
                }
            }
        });
    }

    //Método auxiliar usado para crear toast cada vez que sea necesario
    public void makeToast(String text) { Toast.makeText(this, text, Toast.LENGTH_LONG).show(); }
}