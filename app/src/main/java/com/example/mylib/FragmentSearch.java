package com.example.mylib;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentSearch#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSearch extends Fragment{

    String[] type = { "India", "USA", "China", "Japan", "Other"};
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentSearch() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentSearch.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSearch newInstance(String param1, String param2) {
        FragmentSearch fragment = new FragmentSearch();
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
    }
    RecyclerView recyclerView;
    adapter mainadapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView =(RecyclerView)view.findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        FirebaseRecyclerOptions<books> options =
                new FirebaseRecyclerOptions.Builder<books>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("abc"), books.class)
                        .build();

        mainadapter = new adapter(options);
        recyclerView.setAdapter(mainadapter);
        SearchView searchView = (SearchView)view.findViewById(R.id.searchView2);

        SearchView searchView2 = (SearchView)view.findViewById(R.id.searchView3);

        SearchView searchView3 = (SearchView)view.findViewById(R.id.searchView4);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                txtSearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                txtSearch(s);
                return false;
            }
        });
        searchView2.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                txtSearch2(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                txtSearch2(s);
                return false;
            }
        });
        searchView3.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                txtSearch3(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                txtSearch3(s);
                return false;
            }
        });
        return view;
    }

    private void txtSearch(String s) {

        FirebaseRecyclerOptions<books> options =
                new FirebaseRecyclerOptions.Builder<books>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("books").orderByChild("name").startAt(s).endAt(s+"~"), books.class)
                        .build();

        mainadapter = new adapter(options);
        mainadapter.startListening();
        recyclerView.setAdapter(mainadapter);
    }
    private void txtSearch2(String s) {

        FirebaseRecyclerOptions<books> options =
                new FirebaseRecyclerOptions.Builder<books>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("books").orderByChild("autr").startAt(s).endAt(s+"~"), books.class)
                        .build();

        mainadapter = new adapter(options);
        mainadapter.startListening();
        recyclerView.setAdapter(mainadapter);
    }
    private void txtSearch3(String s) {

        FirebaseRecyclerOptions<books> options =
                new FirebaseRecyclerOptions.Builder<books>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("books").orderByChild("genre").startAt(s).endAt(s+"~"), books.class)
                        .build();

        mainadapter = new adapter(options);
        mainadapter.startListening();
        recyclerView.setAdapter(mainadapter);
    }
    @Override
    public void onStart() {
        super.onStart();
        mainadapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mainadapter.stopListening();
    }

}