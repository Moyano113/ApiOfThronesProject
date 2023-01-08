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

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private EditText txtUsuario;
    private EditText txtContrasena;
    private Button btnLogin;
    private Button   btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtUsuario = (EditText) findViewById(R.id.txtUsuario);
        txtContrasena = (EditText) findViewById(R.id.txtContrasena);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        //Botón "ENTRAR"
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuario = txtUsuario.getText().toString();
                String contrasena = txtContrasena.getText().toString();

                //Se comprueba que los campos no estén vacíos
                if(usuario.isEmpty() || contrasena.isEmpty()){
                    makeToast("Error: Campos vacíos");
                }else{
                    //Se crea una lista de User que coincida con el parametro 'usuario'
                    List<User> lu = User.find(User.class, "usuario = ?", usuario);
                    //Si la lista está vacia es que dicho usuario no existe en la BD
                    if(lu.isEmpty()){
                        makeToast("Usuario o contraseña incorrectos");
                    }else{
                        //Se comprueba si la contraseña del elemento de la lista es igual a la
                        //introducida por el usuario y de ser así se inicia la 'MainActivity'
                        if(contrasena.equals(lu.get(0).getContrasena())){
                            Intent aMain = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(aMain);
                        }else{
                            makeToast("Usuario o contraseña incorrectos");
                        }
                    }
                }
            }
        });

        //Botón "REGISTRARSE"
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuario    = txtUsuario.getText().toString();
                String contrasena = txtContrasena.getText().toString();

                //Se comprueba que los campos no esten vacíos
                if(usuario.isEmpty() || contrasena.isEmpty()){
                    makeToast("Error: Campos vacíos");
                }else{
                    //Se crea una lista de User que coincida con el parametro 'usuario'
                    List<User> lu = User.find(User.class, "usuario = ?", usuario);
                    //Si la lista está vacia es que dicho usuario no existe en la BD y por lo tanto
                    //puede registrarse
                    if(lu.isEmpty()){
                        User u = new User(usuario, contrasena);
                        u.save();
                        makeToast("Usuario registrado correctamente");
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                    }else{
                        makeToast("Usuario ya existente");
                    }
                }
            }
        });
    }

    //Método auxiliar usado para crear toast cada vez que sea necesario
    public void makeToast(String text) { Toast.makeText(this, text, Toast.LENGTH_LONG).show(); }
}