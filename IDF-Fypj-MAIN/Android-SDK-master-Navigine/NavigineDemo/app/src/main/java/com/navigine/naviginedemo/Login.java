package com.navigine.naviginedemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
EditText mEmail,mPassword;
Button mBtnLogin,mGuestlogin;
TextView mCreateBtn;
ProgressBar ProgressBar;
FirebaseAuth firebaseAuth;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail=findViewById(R.id.txtEmail);
        mPassword=findViewById(R.id.txtPassword);
        ProgressBar=findViewById(R.id.progressBar);
        firebaseAuth=firebaseAuth.getInstance();
        mBtnLogin=findViewById(R.id.btnLogin);
mGuestlogin=findViewById(R.id.btnGuestlogin);
        mCreateBtn=findViewById(R.id.btnGoToRegister);

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email=mEmail.getText().toString().trim();
                String password=mPassword.getText().toString().trim();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (!email.matches(emailPattern) && email.length() > 0)
                {

                    mEmail.setError("Email format error please use example@mail.com");
                    return;
                    // or
                }
                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required");
                    return;

                }
                if(password.length()<6){
                    mPassword.setError("Password must be at least 6 Character");
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();

                            sp=getSharedPreferences("MyUserProfile",MODE_PRIVATE);
                            SharedPreferences.Editor editor=sp.edit();
                            editor.putString("email",email);
                            editor.putString("QrData","NotSet");
                            editor.commit();
                            startActivity(new Intent(getApplicationContext(), SplashActivity.class));
                        }
                        else{
                            Toast.makeText(Login.this, "Error !"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            ProgressBar.setVisibility(View.VISIBLE);
                        }
                    }

                });

            }
        });
        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });
mGuestlogin.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        sp=getSharedPreferences("MyUserProfile",MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("Guest","True");
        editor.commit();
        startActivity(new Intent(getApplicationContext(), SplashActivity.class));

    }
});


    }
}