package com.example.a0106chatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText etEmail ,etPassword;
    Button btnLogin;

    FirebaseAuth auth;
    ProgressBar nDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.et_lusername);
        etPassword =  findViewById(R.id.et_lpassword);
        btnLogin =  findViewById(R.id.btn_llogin);
        nDialog =  findViewById(R.id.progress_loader);

        auth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nDialog.setVisibility(View.VISIBLE);
                String emailText = etEmail.getText().toString();
                String passwordText = etPassword.getText().toString();


                if( TextUtils.isEmpty(emailText) || TextUtils.isEmpty(passwordText)){
                    nDialog.setVisibility(View.INVISIBLE);
                    Toast.makeText(Login.this, "All fields are required  ",Toast.LENGTH_SHORT).show();
                } else{
                    auth.signInWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            nDialog.setVisibility(View.INVISIBLE);


                            if(task.isSuccessful()){
                                Intent intent =  new Intent(getBaseContext(), Users.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                            }else{
                                Toast.makeText(Login.this, "Authentification field   ",Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });
    }
}
