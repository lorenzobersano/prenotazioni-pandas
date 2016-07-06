package lorenzobersano.com.prenotazionipandas;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.paolorotolo.appintro.AppIntro;

/**
 * Created by lorenzobersano on 04/07/16.
 */

public class AppIntroPandas extends AppIntro {
    final String NUMERO_PANDAS_SVIZZERA = "3925273838";
    final String NUMERO_PANDAS_INGHILTERRA = "3441568091";

    @Override
    public void init(Bundle savedInstanceState) {
        addSlide(new FirstSlide());
        addSlide(new SecondSlide());
        addSlide(new ThirdSlide());
        askForPermissions(new String[]{Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS}, 1);
        showSkipButton(false);
    }

    private void loadMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSlideChanged(Fragment oldFragment, Fragment newFragment) {
        if(oldFragment == getSlides().get(0))   {
            if(numeroTrovato(NUMERO_PANDAS_INGHILTERRA) || numeroTrovato(NUMERO_PANDAS_SVIZZERA)) {
                TextView secondSlideTitle = (TextView)findViewById(R.id.second_slide_title);
                TextView secondSlideText = (TextView)findViewById(R.id.second_slide_text);
                ImageView secondSlideImage = (ImageView)findViewById(R.id.second_slide_image);
                Button btnInghilterra = (Button)findViewById(R.id.btnInghilterra);
                Button btnSvizzera = (Button)findViewById(R.id.btnSvizzera);

                secondSlideTitle.setText("Ebbravo!");
                secondSlideText.setText("A quanto pare hai già nella rubrica il numero del tuo paninaro preferito, molto bene!");
                secondSlideImage.setImageResource(R.drawable.logo_panda_ok);
                btnInghilterra.setVisibility(View.INVISIBLE);
                btnSvizzera.setVisibility(View.INVISIBLE);
            }
        }
    }

    public boolean numeroTrovato(String numero) {
        boolean found = false;

        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(numero));
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        Cursor contactLookup = contentResolver.query(uri, new String[] {BaseColumns._ID,
                ContactsContract.PhoneLookup.DISPLAY_NAME }, null, null, null);

        try {
            if (contactLookup != null && contactLookup.getCount() > 0) {
                found = true;
            }
        } finally {
            if (contactLookup != null) {
                contactLookup.close();
            }
        }
        return found;
    }

    @Override
    public void onDonePressed() {
        loadMainActivity();
    }

//    public void getStarted(View v){
//        loadMainActivity();
//    }
}


/*    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch ( requestCode ) {
            case PERMISSIONS_REQUEST_RW_CONTACTS: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (!numeroTrovato()) {
                        Intent addNumber = new Intent(ContactsContract.Intents.Insert.ACTION);
                        addNumber.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                        addNumber.putExtra(ContactsContract.Intents.Insert.NAME, "Pandas Panini");
                        addNumber.putExtra(ContactsContract.Intents.Insert.PHONE, NUMERO_PANDAS_SVIZZERA);
                        startActivity(addNumber);
                    }
                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Log.d("Permissions", "Permission Denied: " + permissions[0]);
                }
            }
            break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }
*/