package com.example.mylib;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class adapter extends FirebaseRecyclerAdapter<books,adapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private Context context;
    private String UserID;

    public adapter(@NonNull FirebaseRecyclerOptions<books> options) {
        super(options);


    }
    DatabaseReference databaseReference;
    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, final int position, @NonNull books model) {

        holder.Bname.setText(model.getName());
        holder.Author.setText(model.getAutr());
        holder.Location.setText(model.getLocation());
        holder.Genre.setText(model.getGenre());
        holder.Copies.setText(model.getCopies());
        String s= holder.Copies.getText().toString().trim();
        if(model.getUrl2().isEmpty())
        {
            holder.Blink.setText("Link not available");
        }
        else{
            holder.Blink.setText("Click here to access!!");

        }
        if(!s.equals("0"))
        {
            holder.Blink.setVisibility(View.GONE);
            holder.linkLable.setVisibility(View.GONE );
        }

        Glide.with(holder.imag.getContext())
                .load(model.getUrl())
                .placeholder(R.drawable.back)
                .error(R.drawable.back)
                .into(holder.imag);


        // this part is to put the action of the assign button in the recycler view
        holder.Assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus= DialogPlus.newDialog(holder.imag.getContext())
                        .setContentHolder(new ViewHolder(R.layout.assign_popup))
                        .setExpanded(true,800)
                        .create();

                String book = model.getName();
                String copies = model.getCopies();
                Integer ca = Integer.valueOf(copies)-1;
                Integer ca2 = Integer.valueOf(copies)+1;

                View v = dialogPlus.getHolderView();
                dialogPlus.show();
                Button ass,ret;
                ass = v.findViewById(R.id.assignButton);
                ret = v.findViewById(R.id.returnButton);
                EditText usn =(EditText)v.findViewById(R.id.usernumber);


                Date date = Calendar.getInstance().getTime();

                Calendar cal = Calendar.getInstance();
                cal.setTime(date);

                cal.add(Calendar.DATE, 12);
                Date date2 = cal.getTime();
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                String strDate = dateFormat.format(date2);



                ass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String,Object> map = new HashMap<>();
                        map.put("books",book.toString());
                        map.put("issued_date",strDate.toString());
                        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                        databaseReference.child(usn.getText().toString()).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(holder.Bname.getContext(), "Data assigned Succesfully",Toast.LENGTH_SHORT ).show();
                                    dialogPlus.dismiss();
                                }else{
                                    Toast.makeText(holder.Bname.getContext(), "Data not Succesfully assigned",Toast.LENGTH_SHORT ).show();
                                }
                            }
                        });

                        Map<String,Object> map1 = new HashMap<>();
                        map1.put("copies",ca.toString());
                        FirebaseDatabase.getInstance().getReference().child("books")
                                .child(getRef(position).getKey()).updateChildren(map1);

                    }


                });
                ass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String,Object> map = new HashMap<>();
                        map.put("books",null);
                        map.put("issued_date",null);
                        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                        databaseReference.child(usn.getText().toString()).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(holder.Bname.getContext(), "Book is returned Succesfully",Toast.LENGTH_SHORT ).show();
                                    dialogPlus.dismiss();
                                }else{
                                    Toast.makeText(holder.Bname.getContext(), "Data not Succesfully assigned",Toast.LENGTH_SHORT ).show();
                                }
                            }
                        });

                        Map<String,Object> map1 = new HashMap<>();
                        map1.put("copies",ca2.toString());
                        FirebaseDatabase.getInstance().getReference().child("books")
                                .child(getRef(position).getKey()).updateChildren(map1);

                    }


                });



            }

        });
        holder.Blink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a = model.getUrl2().toString().trim();
                Intent viewIntent =new Intent("android.intent.action.VIEW",Uri.parse(a));
                holder.Blink.getContext().startActivity(viewIntent);

            }
        });

        holder.Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus= DialogPlus.newDialog(holder.imag.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_popup))
                        .setExpanded(true,1700)
                        .create();

                //dialogPlus.show();

                View view1 = dialogPlus.getHolderView();
                EditText name = view1.findViewById(R.id.Bookname);
                EditText author = view1.findViewById(R.id.BookAuth);
                EditText genre = view1.findViewById(R.id.BookGenre);
                EditText copies = view1.findViewById(R.id.Booksleft);
                EditText location = view1.findViewById(R.id.BookLocation);
                EditText imgurl = view1.findViewById(R.id.BookUrl);
                EditText bookurl = view1.findViewById(R.id.BookUrl2);

                Button update = view1.findViewById(R.id.BookUpdate);

                name.setText(model.getName());
                author.setText(model.getAutr());
                genre.setText(model.getGenre());
                copies.setText(model.getCopies());
                location.setText(model.getLocation());
                imgurl.setText(model.getUrl());
                bookurl.setText(model.getUrl2());

                dialogPlus.show();

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String,Object> map = new HashMap<>();
                        map.put("name",name.getText().toString());
                        map.put("autr",author.getText().toString());
                        map.put("location",location.getText().toString());
                        map.put("copies",copies.getText().toString());
                        map.put("genre",genre.getText().toString());
                        map.put("url",imgurl.getText().toString());
                        map.put("url2",bookurl.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("books")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused){
                                        Toast.makeText(holder.Bname.getContext(), "Data Updated Succesfully",Toast.LENGTH_SHORT ).show();
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(holder.Bname.getContext(), "Error while Updating",Toast.LENGTH_SHORT ).show();
                                    }
                                });
                    }
                });

            }
        });
        holder.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.Bname.getContext());
                builder.setTitle("Are You Sure");
                builder.setMessage("Deleted Data Cannot Be retrieved");
                String book = holder.Bname.getText().toString().trim();
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("books")
                                .child(getRef(position).getKey()).removeValue();
                        Toast.makeText(holder.Bname.getContext(), "Deleted Successfully",Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(holder.Bname.getContext(), "Cancelled",Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });
    }

    private FirebaseAuth mAuth;
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mAuth = FirebaseAuth.getInstance();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_list,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{

        ImageView imag;
        TextView Bname,Author,Location,Copies,Genre;
        TextView Blink,linkLable;
        Button Edit,Delete,Assign;

         public myViewHolder(@NonNull View itemView) {
            super(itemView);
            String email =mAuth.getCurrentUser().getEmail();

            imag = (ImageView)itemView.findViewById(R.id.img);
            Bname=(TextView) itemView.findViewById(R.id.bname);
             Author=(TextView) itemView.findViewById(R.id.author);
             Location=(TextView) itemView.findViewById(R.id.location);
             Copies=(TextView) itemView.findViewById(R.id.copies);
             Genre =(TextView) itemView.findViewById(R.id.genre);
             Edit =(Button) itemView.findViewById(R.id.editButton);
             Delete =(Button) itemView.findViewById(R.id.deleteButton);
             Assign =(Button) itemView.findViewById(R.id.assignButton);
             Blink =(TextView) itemView.findViewById(R.id.link);
             linkLable =(TextView)itemView.findViewById(R.id.linklabel);
             if(!email.equals("baasu.kondeti@gmail.com")){
                 Edit.setVisibility(View.GONE);
                 Delete.setVisibility(View.GONE);
                 Assign.setVisibility(View.GONE);
             }

        }
    }

}
