package com.example.mylib;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mylib.MainActivity;
import com.example.mylib.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;

    private Button regis,login;
    private EditText userid,email,password;
    private ProgressBar circle;
    FirebaseDatabase db;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        mAuth = FirebaseAuth.getInstance();

        regis =(Button) findViewById(R.id.reg);
        login =(Button) findViewById(R.id.login);
        userid =(EditText) findViewById(R.id.uid);
        email =(EditText) findViewById(R.id.uid2);
        password =(EditText) findViewById(R.id.pass);
        circle =(ProgressBar) findViewById(R.id.circ);

        regis.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.reg:
                register();
                break;
            case R.id.login:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }


    }

    private void register() {

        String Email = email.getText().toString().trim();
        String Userid = userid.getText().toString().trim();
        String Password = password.getText().toString().trim();

        if(Email.isEmpty()){
            email.setError("Enter this field");
            email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            email.setError("Enter in the format '123@abc.com'");
            email.requestFocus();
            return;

        }

        circle.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(Email,Password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            User user = new User(Userid,Email,Password);


                            db=FirebaseDatabase.getInstance();
                            reference = db.getReference("Users");
                            reference.child(Userid).setValue(user);
                            FirebaseDatabase.getInstance().getReference("User")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterUser.this,"Successfully registered",Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(RegisterUser.this,"Task Failed",Toast.LENGTH_SHORT).show();
                                    }
                                    circle.setVisibility(View.GONE);
                                }
                            });

                        }else {
                            Toast.makeText(RegisterUser.this,"Task Failed",Toast.LENGTH_LONG).show();
                            circle.setVisibility(View.GONE);
                        }

                    }
                });
    }
}