package com.example.mylib;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Profile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseUser user;
    private DatabaseReference reference,reference1;
    private String UserID;
    private Button signout;
    private Button Homepage;
    private FirebaseAuth mAuth;

    public Fragment_Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Profile newInstance(String param1, String param2) {
        Fragment_Profile fragment = new Fragment_Profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__profile, container, false);


        signout = view.findViewById(R.id.signout);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(),MainActivity.class));
            }
        });


        user = FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("User");
        UserID= user.getUid();

        final TextView uid = (TextView)view.findViewById(R.id.uidtext);
        final TextView eid = (TextView)view.findViewById(R.id.eidtext);
        final TextView welcome =(TextView)view.findViewById(R.id.textView4);
        final TextView books =(TextView)view.findViewById(R.id.books1text);
        final TextView fine =(TextView)view.findViewById(R.id.fine);

        reference.child(UserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile =snapshot.getValue(User.class);
                if(userProfile!= null){
                    String user= userProfile.Userid;
                    String email = userProfile.Email;
                    welcome.setText("Welcome,"+ user +" !");
                    uid.setText(user);
                    eid.setText(email);
                    reference1 = FirebaseDatabase.getInstance().getReference("Users") ;
                    reference1.child(user).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if(task.isSuccessful()){
                                        DataSnapshot dataSnapshot= task.getResult();
                                        String book = String.valueOf(dataSnapshot.child("books").getValue());
                                        String fines = String.valueOf(dataSnapshot.child("issued_date").getValue());
                                        if(!book.equals("null")) {
                                            books.setText(book);
                                            try {
                                                addfine(fines);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                fine.setText(e.toString());
                                            }
                                        }
                                    }
                                }

                        private void addfine(String fines)throws Exception {
                            SimpleDateFormat obj = new SimpleDateFormat("MM-dd-yyyy HH:mm");
                                Date date1 = Calendar.getInstance().getTime();
                                DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm");
                                Date date2 =(Date)obj.parse(fines);
                                long time_difference = date1.getTime() - date2.getTime();
                                long days_difference = (time_difference / (1000*60*60*24)) % 365;
                                int finenum =0;
                                int i = (int)days_difference;
                                if(i>0){
                                    finenum = i*2;
                                    fine.setText("Due Date:"+fines+" Fine is"+finenum);
                                }
                            fine.setText("Due Date:"+fines+"\nFine is "+finenum);

                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),"Error Occured!",Toast.LENGTH_SHORT).show();

            }
        });

        return view;

    }
}