package com.ricardo.controlasistenciaipd.fragments;


import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.ricardo.controlasistenciaipd.BuscadorAdapter;
import com.ricardo.controlasistenciaipd.DetalleAlumnoActivity;
import com.ricardo.controlasistenciaipd.R;
import com.ricardo.controlasistenciaipd.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlumnoFragment extends Fragment {
    View view;
    AppCompatActivity appCompatActivity;
    String recuperado = "", evento =  "", nomEvento = "";
    RecyclerView recyclerView;
    ArrayList<String> lstFound;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    MaterialSearchView materialSearchView;
    ArrayList<String> items = new ArrayList<String>();

    public AlumnoFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public AlumnoFragment(Bundle recupera, AppCompatActivity appCompatActivity) {
        // Required empty public constructor
        final Bundle r = recupera;
        if(r!=null){
            recuperado = recupera.getString("cod");
            evento = recupera.getString("eve");
            nomEvento = recupera.getString("nomEve");
        }
        this.appCompatActivity = appCompatActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        items.add("Denis Ricardo Morales Retamozo");
        items.add("Jesus Falen Garcia Suarez");
        items.add("Alan Arnold Pajuelo Lincon");
        items.add("Enrique Julio Flores Dibala");
        items.add("Laura Rosa Sosa Villanueva");
        items.add("Cesar Bernabe Quispe Rodriguez");
        items.add("Helton Javier Aiquipa Robles");
        items.add("Cindy Laura Maldonado Quispe");
        items.add("Nilton Gregorio Armas Domenech");
        items.add("Juan Pablo Messi Nazario");
        items.add("Juan Jose Ore Yepez");
        items.add("Guiliana Lisa Montes Fajardo");

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_alumno, container, false);
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar_busqueda);
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setTitle("Buscar Alumno");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        materialSearchView = (MaterialSearchView)view.findViewById(R.id.search_view);

        recyclerView = (RecyclerView)view.findViewById(R.id.lista);
        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        mAdapter = getBuscadorAdapter(items);
        recyclerView.setAdapter(mAdapter);

//        recyclerView.addOnItemTouchListener(
//                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
//                    @Override public void onItemClick(View view, int position) {
//                        final CardView cardView = view.findViewById(R.id.);
//                        int colorFrom = ContextCompat.getColor(getContext(), R.color.colorIcons);
//                        int colorTo = ContextCompat.getColor(getContext(), R.color.colorAccent);
//                        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
//                        colorAnimation.setDuration(800); // milliseconds
//                        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                            @Override
//                            public void onAnimationUpdate(ValueAnimator animator) {
//                                cardView.setBackgroundColor((int) animator.getAnimatedValue());
//                            }
//                        });
//                        colorAnimation.start();
//                        Intent intent = new Intent(getContext(), DetalleAlumnoActivity.class);
//                        startActivity(intent);
//                    }
//                })
//        );

        materialSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                recyclerView.setHasFixedSize(true);
                // use a linear layout manager
                mLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(mLayoutManager);

                // specify an adapter (see also next example)
                mAdapter = getBuscadorAdapter(items);
                recyclerView.setAdapter(mAdapter);
            }
        });
        materialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText != null && !newText.isEmpty()){
                    lstFound = new ArrayList<String>();
                    for(String item:items){
                        if(item.toUpperCase().contains(newText.toUpperCase()))lstFound.add(item);
                    }
                    recyclerView.setHasFixedSize(true);
                    // use a linear layout manager
                    mLayoutManager = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(mLayoutManager);

                    // specify an adapter (see also next example)
                    mAdapter = getBuscadorAdapter(lstFound);
                    recyclerView.setAdapter(mAdapter);
                }else{
                    recyclerView.setHasFixedSize(true);
                    // use a linear layout manager
                    mLayoutManager = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(mLayoutManager);

                    // specify an adapter (see also next example)
                    mAdapter = getBuscadorAdapter(items);
                    recyclerView.setAdapter(mAdapter);
                }
                return true;
            }
        });
        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
        appCompatActivity.getMenuInflater().inflate(R.menu.menu_item,menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        materialSearchView.setMenuItem(menuItem);
    }

    public BuscadorAdapter getBuscadorAdapter(final ArrayList<String> elementos){
        return new BuscadorAdapter(elementos, new BuscadorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                final CardView rootView = (CardView) v;
                int colorFrom = ContextCompat.getColor(getContext(), R.color.colorDivider);
                int colorTo = ContextCompat.getColor(getContext(), R.color.colorIcons);
                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                colorAnimation.setDuration(800); // milliseconds
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        rootView.setCardBackgroundColor((int) animator.getAnimatedValue());
                    }
                });
                colorAnimation.start();
                Intent intent = new Intent(getContext(), DetalleAlumnoActivity.class);
                intent.putExtra("detalle",elementos.get(position));
                startActivity(intent);
            }
        });
    }
}

