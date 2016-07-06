package lorenzobersano.com.prenotazionipandas;


import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Favs extends android.support.v4.app.Fragment {

    int padding = 0;
    final int N_SALSE = 9;
    String[] ingredientsArray, salseArray;
    View v;

    public Fragment_Favs() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_favs_, container, false);
        updateLista();
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity)getActivity()).setActionBarTitle("Preferiti");
        FloatingActionButton fab = (FloatingActionButton)getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
    }

    public void updateLista() {
        String text = "";
        ingredientsArray = getResources().getStringArray(R.array.ingredients_array);
        salseArray = getResources().getStringArray(R.array.salse_array);

        try {
            FileInputStream inputStream = getContext().openFileInput("panini.csv");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                text = stringBuilder.toString();
            }

            if (text != "") {
                String[] panini, valori;
                List<Panino> listPanini = new ArrayList<>();
                RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
                RecyclerAdapterPanini rap;
                ItemTouchHelper itemTouchHelper;

                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setHasFixedSize(true);
                panini = text.split("separator");

                for (int i = 0; i < panini.length; i++) {
                    valori = panini[i].split(";");
                    if(valori.length == 6)
                        listPanini.add(new Panino(valori[0], valori[1], valori[2], valori[3], valori[4], valori[5]));
                    else
                        listPanini.add(new Panino(valori[0], valori[1], valori[2], valori[3], valori[4]));
                }

                rap = new RecyclerAdapterPanini(listPanini, getActivity(), ingredientsArray, salseArray);
                itemTouchHelper = new ItemTouchHelper(rap.getSimpleItemTouchCallback());
                recyclerView.setAdapter(rap);
                setTextViewNoPaniniVisibility(View.INVISIBLE);
                itemTouchHelper.attachToRecyclerView(recyclerView);
            }
            else {
                setTextViewNoPaniniVisibility(View.VISIBLE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setTextViewNoPaniniVisibility(int visibility)    {
        TextView txtNoPanini = (TextView) v.findViewById(R.id.lblNoPanino);
        txtNoPanini.setVisibility(visibility);
    }
}
