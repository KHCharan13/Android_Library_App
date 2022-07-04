package com.example.mylib;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    private FirebaseAuth mAuth;
    Button log;
    TextView resetpass,reg;
    EditText password,email;
    ProgressBar circle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        log = (Button) findViewById(R.id.login);
        reg = (TextView)findViewById(R.id.register);
        resetpass=(TextView)findViewById(R.id.forgot);
        email=(EditText)findViewById(R.id.uid);
        password=(EditText)findViewById(R.id.pass);
        circle=(ProgressBar)findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();
        circle.setVisibility(View.GONE);
        reg.setOnClickListener(this);
        log.setOnClickListener(this);

        circle.setVisibility(View.GONE);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register:
                startActivity(new Intent(MainActivity.this,RegisterUser.class));
                break;
            case R.id.login:
                loginuser();
                break;
        }
    }

    private void loginuser() {

        String Eid = email.getText().toString().trim();
        String Pass = password.getText().toString().trim();

        if(Eid.isEmpty()){
            email.setError("Enter");
            email.requestFocus();
            return;
        }
        if(Pass.isEmpty()){
            password.setError("Enter");
            password.requestFocus();
            return;
        }
        circle.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(Eid,Pass).addOnCompleteListener(MainActivity.this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(MainActivity.this , Userdetails.class));
                }
                else{
                    Toast.makeText(MainActivity.this,"nope",Toast.LENGTH_SHORT).show();
                }
                circle.setVisibility(View.GONE);
            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();
        circle.setVisibility(View.VISIBLE);
        FirebaseUser user= mAuth.getCurrentUser();
        if(user != null){
            startActivity(new Intent(MainActivity.this,Userdetails.class));
        }
        else{
            circle.setVisibility(View.GONE);
        }
    }
}


