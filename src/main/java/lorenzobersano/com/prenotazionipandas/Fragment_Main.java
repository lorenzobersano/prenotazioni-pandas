package lorenzobersano.com.prenotazionipandas;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Main extends android.support.v4.app.Fragment {

    TimePickerDialog timeDialog;
    public static final int LISTINGREDIENTI_REQUEST_CODE = 360;
    public static final int LISTSALSE_REQUEST_CODE = 1337;
    final int YES_NO_CALL = 420;
    final android.support.v4.app.Fragment thisFragment = this;
    boolean[] ingredienti;
    TextView lblIngredienti, lblSalse;
    Button btnOra, btnApriListaIngredienti, btnApriListaSalse;

    public Fragment_Main() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_, container, false);

        btnOra = (Button)v.findViewById(R.id.btnOraRitiro);
        btnApriListaIngredienti = (Button)v.findViewById(R.id.btnApriListaIngredienti);
        TextView txtNome = (TextView)v.findViewById(R.id.input_nome);
        lblIngredienti = (TextView)v.findViewById(R.id.lblIngredienti);
        lblSalse = (TextView)v.findViewById(R.id.lblSalse);

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save the fragment's state here
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((MainActivity)getActivity()).setActionBarTitle("Prenotazione");

        FloatingActionButton fab = (FloatingActionButton)getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);

        btnOra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar today = Calendar.getInstance();
                int hour = today.get(Calendar.HOUR_OF_DAY);
                int minute = today.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if(selectedHour >= 10 && selectedHour <= 16) {
                            if(selectedHour == 16 && selectedMinute != 0)   btnOra.setText("Inserire orario tra le 10 e le 16!");
                            else {
                                StringBuilder sb = new StringBuilder()
                                        .append("Ora ritiro: ")
                                        .append(pad(selectedHour)).append(":")
                                        .append(pad(selectedMinute));

                                String time = sb.toString();

                                btnOra.setText(time);
                            }
                        }
                        else
                            btnOra.setText("Inserire orario tra le 10 e le 16!");
                    }
                }, hour, minute, true);//Yes 24 hour time
                        mTimePicker.show();
            }
        });

        btnApriListaIngredienti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExpList_BottomSheet listDialog = new ExpList_BottomSheet();
                listDialog.setTargetFragment(thisFragment, LISTINGREDIENTI_REQUEST_CODE);
                if(lblIngredienti.getText().length() != 0 || lblSalse.getText().length() != 0)
                {
                    Bundle args = setSelectedItemsArray();
                    listDialog.setArguments(args);
                }
                listDialog.show(getActivity().getSupportFragmentManager().beginTransaction(), "ingredienti");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case LISTINGREDIENTI_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    ingredienti = intent.getBooleanArrayExtra("selectedItems");
                    if (ingredienti != null) {
                        String tmp = getSelectedItemsIds();
                        if (!(tmp.isEmpty())) {
                            String s;
                            s = tmp.substring(0, tmp.length() - 1);
                            lblIngredienti.setText(s);
                        } else {
                            lblIngredienti.setText("");
                            lblSalse.setText("");
                        }
                    }
                }
                break;
        }
    }

    public static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    public String getSelectedItemsIds() {
        String ret = "";
        for(int i = 0; i < ingredienti.length; i++)
            if(ingredienti[i])  {
                if(i >= 65 && i < 75)   setLblSalse(i, true);
                else                    ret += i + 1 + "-";
            }
        return ret;
    }

    public void setLblSalse(int i, boolean insertScore) {
        String salsa = Integer.toString(i - 64);
        if(lblSalse.getText().toString().isEmpty()) {
            if (insertScore) lblSalse.setText(salsa + "-");
            else             lblSalse.setText(salsa);
        } else  {
            if (insertScore) lblSalse.append(salsa + "-");
            else             lblSalse.append(salsa);
        }
    }

    public Bundle setSelectedItemsArray() {
        Bundle ret = new Bundle();
        boolean[] absoluteCheckedIngredients = new boolean[getResources().getStringArray(R.array.ingredients_array).length];
        String[] ingredientIds, salseIds;
        ingredientIds = lblIngredienti.getText().toString().split("-");
        salseIds = lblSalse.getText().toString().split("-");
        for(int i = 0; i < ingredientIds.length; i++)
            absoluteCheckedIngredients[Integer.parseInt(ingredientIds[i]) - 1] = true;
        for(int i = 0; i < salseIds.length; i++)
            if(salseIds[i] != "")   absoluteCheckedIngredients[Integer.parseInt(salseIds[i]) + 64] = true;
        ret.putBooleanArray("ingredienti", absoluteCheckedIngredients);
        return ret;
    }
}
