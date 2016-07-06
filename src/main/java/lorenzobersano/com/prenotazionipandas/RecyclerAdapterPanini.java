package lorenzobersano.com.prenotazionipandas;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Lorenzo Bersano on 08/04/2016.
 */
public class RecyclerAdapterPanini extends RecyclerView.Adapter<RecyclerAdapterPanini.PaniniViewHolder>{
    List<Panino> panini;
    FragmentActivity activity;
    String[] ingredientsArray, salseArray;
    String whatsappMessage = "";
    ItemTouchHelper.SimpleCallback simpleItemTouchCallback;

    RecyclerAdapterPanini(final List<Panino> panini, final FragmentActivity activity, String[] ingredientsArray, String[] salseArray) {
        this.panini = panini;
        this.activity = activity;
        this.ingredientsArray = ingredientsArray;
        this.salseArray = salseArray;

        // Gestisco lo swipe della cardView
        simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //Remove swiped item from list and notify the RecyclerView
                // Salva panino
                int i = viewHolder.getAdapterPosition();
                String csv = "panini.csv";
                String tmp = "temp.csv";
                String text = "";
                String toFind = panini.get(i).nome + ";" + panini.get(i).dimensione + ";" + panini.get(i).tipo + ";" + panini.get(i).caldoFreddo;
                toFind += ";" + panini.get(i).ingredienti;
                if (panini.get(i).salse != null)
                    toFind += ";" + panini.get(i).salse;
                toFind += ";separator";

                try {
                    FileInputStream inputStream = activity.openFileInput("panini.csv");

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
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (text.contains(toFind))
                    text = text.replace(toFind, "");

                try {
                    FileOutputStream fou = activity.openFileOutput("panini.csv", Activity.MODE_PRIVATE);
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fou);
                    outputStreamWriter.write(text);
                    outputStreamWriter.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }


    @Override
    public int getItemCount() {
        return panini.size();
    }

    @Override
    public PaniniViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cards_layout, viewGroup, false);
        PaniniViewHolder pvh = new PaniniViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final PaniniViewHolder paniniViewHolder, final int i) {
        String[] codiciIngredienti, codiciSalse;
        String ingredienti = "", salse = "";
        String nome = panini.get(i).nome;
        String tipo = panini.get(i).dimensione + " " + panini.get(i).tipo + " " + panini.get(i).caldoFreddo;
        String ingredientiWhatsapp = panini.get(i).ingredienti;
        String salseWhatsapp = panini.get(i).salse;

        // Inizializzo messaggio whatsapp
        whatsappMessage = "Nome: " + nome + "\nPanino: " + tipo + "\nIngredienti: " + ingredientiWhatsapp;

        // Rende bold la parte che mi interessa della TextView
        SpannableStringBuilder str = new SpannableStringBuilder(paniniViewHolder.infoPanino.getText() + tipo);
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        paniniViewHolder.nome.setText(nome);
        paniniViewHolder.infoPanino.setText(str);
        codiciIngredienti = panini.get(i).ingredienti.split("-");

        for(int j = 0; j < codiciIngredienti.length; j++) {
            ingredienti += ingredientsArray[Integer.parseInt(codiciIngredienti[j]) - 1].toLowerCase();
            if(j != codiciIngredienti.length - 1)
                ingredienti += ", ";
        }

        if(!(panini.get(i).salse == null)) {
            codiciSalse = panini.get(i).salse.split("-");

            for (int j = 0; j < codiciSalse.length; j++) {
                salse += salseArray[Integer.parseInt(codiciSalse[j]) - 1];
                if (j != codiciSalse.length - 1)
                    salse += ", ";
            }

            whatsappMessage += "\nSalse: " + salseWhatsapp;

            // Metto a bold la parte che mi interessa della TextView salse
            str.clear();
            str = new SpannableStringBuilder(paniniViewHolder.salse.getText() + salse);
            str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            paniniViewHolder.salse.setText(str);
        }
        else
            paniniViewHolder.salse.setVisibility(View.INVISIBLE);

        // Metto a bold la parte che mi interessa della TextView ingredienti
        str.clear();
        str = new SpannableStringBuilder(paniniViewHolder.ingredienti.getText() + ingredienti);
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        paniniViewHolder.ingredienti.setText(str);

        paniniViewHolder.cardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar today = Calendar.getInstance();
                int hour = today.get(Calendar.HOUR_OF_DAY);
                int minute = today.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if(selectedHour >= 10 && selectedHour <= 16) {
                            if (selectedHour == 16 && selectedMinute != 0)
                                paniniViewHolder.cardButton.setText("Inserire orario tra le 10 e le 16!");
                            else {
                                StringBuilder sb = new StringBuilder()
                                        .append(Fragment_Main.pad(selectedHour)).append(":")
                                        .append(Fragment_Main.pad(selectedMinute));

                                String time = sb.toString();
                                paniniViewHolder.cardButton.setText("Ora selezionata: " + time);
                                whatsappMessage += "\nPasso a prenderlo alle " + time;

                                // Invia via Whatsapp
                                Intent sendIntent = new Intent();
                                sendIntent.setAction(Intent.ACTION_SEND);
                                sendIntent.putExtra(Intent.EXTRA_TEXT, whatsappMessage);
                                sendIntent.setType("text/plain");
                                sendIntent.setPackage("com.whatsapp");
                                activity.startActivity(sendIntent);
                            }
                        } else
                            paniniViewHolder.cardButton.setText("Inserire orario tra le 10 e le 16!");
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.show();
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public ItemTouchHelper.SimpleCallback getSimpleItemTouchCallback()  {
        return simpleItemTouchCallback;
    }

    public static class PaniniViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView nome;
        TextView infoPanino;
        TextView ingredienti;
        TextView salse;
        Button cardButton;

        PaniniViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            nome = (TextView)itemView.findViewById(R.id.nome);
            infoPanino = (TextView)itemView.findViewById(R.id.info_panino);
            ingredienti = (TextView)itemView.findViewById(R.id.ingredienti);
            salse = (TextView)itemView.findViewById(R.id.salse);
            cardButton = (Button)itemView.findViewById(R.id.card_button);
        }
    }
}
