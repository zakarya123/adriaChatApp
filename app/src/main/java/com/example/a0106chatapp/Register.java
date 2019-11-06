package com.example.a0106chatapp;

import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity {


    FirebaseAuth auth;
    DatabaseReference reference;
    EditText etEmail, etUsername, etPassword, etLogin;
    ProgressBar nDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nDialog =  findViewById(R.id.progress_loader);
        etEmail =  findViewById(R.id.et_email);
        etUsername =  findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        Button btn_register =  findViewById(R.id.btn_register);
        Button btn_login =  findViewById(R.id.btn_login);

        auth = FirebaseAuth.getInstance();


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nDialog.setVisibility(View.VISIBLE);
                String usernameText = etUsername.getText().toString();
                String emailText = etEmail.getText().toString();
                String passwordText = etPassword.getText().toString();

                if(TextUtils.isEmpty(usernameText) || TextUtils.isEmpty(emailText) || TextUtils.isEmpty(passwordText)){
                    nDialog.setVisibility(View.INVISIBLE);
                    Toast.makeText(Register.this, "All fields are required  ",Toast.LENGTH_SHORT).show();
                } else{
                    register(usernameText,emailText,passwordText);
                }
            }
        });


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),Login.class);
                startActivity(i);
            }
        });
    }

    private void register(final String username , String email, String password){
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                nDialog.setVisibility(View.INVISIBLE);
                if(task.isSuccessful()){
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    String userId = firebaseUser.getUid();

                    reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("id",userId);
                    hashMap.put("username",username);

                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(Register.this, "Register succesfully  ",Toast.LENGTH_SHORT).show();

                                    Intent intent =  new Intent(getBaseContext(), Authenetification.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                        }
                    });



                }else{
                    Toast.makeText(Register.this, "you can't register with this email or password ",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
