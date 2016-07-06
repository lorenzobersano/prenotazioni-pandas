package lorenzobersano.com.prenotazionipandas;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

/**
 * Created by Lorenzo Bersano on 22/03/2016.
 */
public class List_Dialog extends android.support.v4.app.DialogFragment {

    ArrayList<Integer> selectedItems;


    public List_Dialog() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String[] ingredienti, ingredientiSelezionati;
        String testoTitolo;
        boolean[] isSelectedArray;
        selectedItems = new ArrayList<>();
        Bundle args = new Bundle();

        if(getArguments() != null)  args = getArguments();

        if(getTargetRequestCode() == Fragment_Main.LISTINGREDIENTI_REQUEST_CODE) {
            ingredienti = getResources().getStringArray(R.array.ingredients_array);
            testoTitolo = "Seleziona ingredienti";
            ingredientiSelezionati = args.getStringArray("ingredienti");
        }
        else {
            ingredienti = getResources().getStringArray(R.array.ingredients_array);
            testoTitolo = "Seleziona salse";
            ingredientiSelezionati = args.getStringArray("salse");
        }

        isSelectedArray = new boolean[ingredienti.length];

        if(ingredientiSelezionati.length > 1)
            for(int i = 0; i < ingredientiSelezionati.length; i++)
                isSelectedArray[Integer.parseInt(ingredientiSelezionati[i]) - 1] = true;

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setTitle(testoTitolo);
        alertBuilder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.putExtra("selectedItems", selectedItems);
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                        dismiss();
                    }
                });

        alertBuilder.setMultiChoiceItems(ingredienti, isSelectedArray, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int pos, boolean isChecked) {
                if (isChecked) {
                    selectedItems.add(pos);
                } else if (selectedItems.contains(pos)) {
                    selectedItems.remove(Integer.valueOf(pos));
                }
            }
        });

        return alertBuilder.create();
    }
}
