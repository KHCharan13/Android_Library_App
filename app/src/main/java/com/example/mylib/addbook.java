package com.example.mylib;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class addbook extends AppCompatActivity {
    EditText bookname,bookautr,booklocation,bookcopies,bookgenre,bookurl;
    Button add,back;
    private NotificationManagerCompat notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbook);
        bookname=(EditText)findViewById(R.id.Bookname);
        bookautr=(EditText)findViewById(R.id.BookAuth);
        booklocation=(EditText)findViewById(R.id.BookLocation);
        bookcopies=(EditText)findViewById(R.id.Booksleft);
        bookgenre=(EditText)findViewById(R.id.BookGenre);
        bookurl=(EditText)findViewById(R.id.BookUrl);

        add=(Button) findViewById(R.id.BookAdd);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               insertdata();
            }
        });
    }



    public void onBackPressed(){
        startActivity(new Intent(addbook.this, Userdetails.class));
    }

    private void insertdata() {
        Map<String,Object> map =new HashMap<>();
        map.put("name",bookname.getText().toString());
        map.put("autr",bookautr.getText().toString());
        map.put("location",booklocation.getText().toString());
        map.put("copies",bookcopies.getText().toString());
        map.put("genre",bookgenre.getText().toString());
        map.put("url",bookurl.getText().toString());

        FirebaseDatabase.getInstance().getReference().child("books").push()
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(addbook.this,"data added succesfully ",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(addbook.this,"Failed to add data",Toast.LENGTH_SHORT).show();

                    }
                });
    }

}