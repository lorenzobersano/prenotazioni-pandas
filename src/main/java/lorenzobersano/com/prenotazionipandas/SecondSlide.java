package lorenzobersano.com.prenotazionipandas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by lorenzobersano on 04/07/16.
 */
public class SecondSlide extends Fragment {
    final int CONTACT_REQUEST = 22;
    View v;
    Button btnSvizzera, btnInghilterra;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.second_slide, container, false);
        addNumbers(v);
        return v;
    }

    public void addNumbers(View v) {
        final String NUMERO_PANDAS_SVIZZERA = "3925273838";
        final String NUMERO_PANDAS_INGHILTERRA = "3441568091";
        btnSvizzera = (Button)v.findViewById(R.id.btnSvizzera);
        btnInghilterra = (Button)v.findViewById(R.id.btnInghilterra);

        btnSvizzera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContact("Panda\'s Panini - Svizzera", NUMERO_PANDAS_SVIZZERA);
            }
        });

        btnInghilterra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContact("Panda\'s Panini - Inghilterra", NUMERO_PANDAS_INGHILTERRA);
            }
        });
    }

    public void addContact(String name, String number) {
        Intent addNumber = new Intent(ContactsContract.Intents.Insert.ACTION);
        addNumber.setType(ContactsContract.RawContacts.CONTENT_TYPE);
        addNumber.putExtra(ContactsContract.Intents.Insert.NAME, name);
        addNumber.putExtra(ContactsContract.Intents.Insert.PHONE, number);
        startActivityForResult(addNumber, CONTACT_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case CONTACT_REQUEST:
                changeUI();
                break;
        }
    }

    public void changeUI() {
        TextView secondSlideTitle = (TextView)v.findViewById(R.id.second_slide_title);
        TextView secondSlideText = (TextView)v.findViewById(R.id.second_slide_text);
        ImageView secondSlideImage = (ImageView)v.findViewById(R.id.second_slide_image);

        secondSlideTitle.setText("Olé!");
        secondSlideText.setText("Numero aggiunto, sei quasi pronto!");
        secondSlideImage.setImageResource(R.drawable.logo_panda_ok);
        btnInghilterra.setVisibility(View.INVISIBLE);
        btnSvizzera.setVisibility(View.INVISIBLE);
    }
}