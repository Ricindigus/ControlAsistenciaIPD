package com.ricardo.controlasistenciaipd.fragments;


import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.ricardo.controlasistenciaipd.pojos.Alumno;
import com.ricardo.controlasistenciaipd.adapters.BuscadorAdapter;
import com.ricardo.controlasistenciaipd.activities.DetalleAlumnoActivity;
import com.ricardo.controlasistenciaipd.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlumnoFragment extends Fragment {
    View view;
    AppCompatActivity appCompatActivity;
    String recuperado = "", evento =  "", nomEvento = "";
    RecyclerView recyclerView;
    ArrayList<Alumno> lstFound;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    MaterialSearchView materialSearchView;
    ArrayList<Alumno> items = new ArrayList<Alumno>();

    public AlumnoFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public AlumnoFragment(Bundle recupera, AppCompatActivity appCompatActivity) {
        // Required empty public constructor
        final Bundle r = recupera;
        if(r!=null){
            recuperado = recupera.getString("codigoPonente");
            evento = recupera.getString("codigoEvento");
            nomEvento = recupera.getString("nombreEvento");
        }
        this.appCompatActivity = appCompatActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_alumno, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        items.add(new Alumno("43372489","Denis Ricardo", "Morales Retamozo",true));
        items.add(new Alumno("44556677","Jesus Falen","Garcia Suarez",true));
        items.add(new Alumno("54342223","Alan Arnold","Pajuelo Lincon",true));
        items.add(new Alumno("57443322","Enrique Julio","Flores Dibala",true));
        items.add(new Alumno("66557744","Laura Rosa Sosa","Villanueva",false));
        items.add(new Alumno("44332211","Cesar Bernabe","Quispe Rodriguez",true));
        items.add(new Alumno("55443322","Helton Javier","Aiquipa Robles",true));
        items.add(new Alumno("25887765","Cindy Laura","Maldonado Quispe",false));
        items.add(new Alumno("55332211","Nilton Gregorio","Armas Domenech",true));
        items.add(new Alumno("33221109","Juan Pablo","Messi Nazario",true));
        items.add(new Alumno("32145332","Juan Jose","Ore Yepez",true));
        items.add(new Alumno("37221234","Guiliana Lisa","Montes Fajardo",false));
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
                    lstFound = new ArrayList<Alumno>();
                    for(Alumno item:items){
                        String nombreCompleto = (item.getNombres() + " " + item.getApellidos()).toUpperCase();
                        if(nombreCompleto.contains(newText.toUpperCase()))lstFound.add(item);
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

    public BuscadorAdapter getBuscadorAdapter(final ArrayList<Alumno> elementos){
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
                intent.putExtra("nombres",elementos.get(position).getNombres());
                intent.putExtra("apellidos", elementos.get(position).getApellidos());
                intent.putExtra("dni",elementos.get(position).getCodigo());
                intent.putExtra("sexo",elementos.get(position).getAsistencia());
                intent.putExtra("codigoPonente", recuperado);
                startActivity(intent);
            }
        });
    }
}

