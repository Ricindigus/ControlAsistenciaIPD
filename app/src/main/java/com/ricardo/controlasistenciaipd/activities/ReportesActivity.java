package com.ricardo.controlasistenciaipd.activities;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.content.Context;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.content.res.Resources.Theme;

import android.widget.TextView;

import com.ricardo.controlasistenciaipd.R;
import com.ricardo.controlasistenciaipd.fragments.AlumnoFragment;
import com.ricardo.controlasistenciaipd.fragments.GeneralFragment;

public class ReportesActivity extends AppCompatActivity {

    private String codigoPonente = "", codigoEvento = "", nombreEvento = "";
    private Toolbar toolbar;
    private Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportes);

        final Bundle recupera = getIntent().getExtras();
        if(recupera!=null){
            codigoPonente = recupera.getString("codigoPonente");
            codigoEvento = recupera.getString("codigoEvento");
            nombreEvento = recupera.getString("nombreEvento");
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar_reportes);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(new MyAdapter(toolbar.getContext(), new String[]{"Reporte General", "Reporte por Alumno"}));
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment = null;
                if(position == 0) fragment = new GeneralFragment(recupera);
                else fragment = new AlumnoFragment(recupera, ReportesActivity.this);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


    }

    private static class MyAdapter extends ArrayAdapter<String> implements ThemedSpinnerAdapter {
        private final ThemedSpinnerAdapter.Helper mDropDownHelper;

        public MyAdapter(Context context, String[] objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
            mDropDownHelper = new ThemedSpinnerAdapter.Helper(context);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null) {
                LayoutInflater inflater = mDropDownHelper.getDropDownViewInflater();
                view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            } else {
                view = convertView;
            }
            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(getItem(position));
            return view;
        }

        @Override
        public Theme getDropDownViewTheme() {
            return mDropDownHelper.getDropDownViewTheme();
        }

        @Override
        public void setDropDownViewTheme(Theme theme) {mDropDownHelper.setDropDownViewTheme(theme);}
    }

    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
            View rootView = null;
            int pagina = getArguments().getInt(ARG_SECTION_NUMBER);
            switch (pagina) {
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_general, container, false);
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_alumno, container, false);
                    break;
                default: break;
            }
            return rootView;
        }
    }
}
