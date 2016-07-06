package lorenzobersano.com.prenotazionipandas;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    FragmentManager fragmentManager = getSupportFragmentManager();
    String s, csvText;
    final int WHATSAPP_REQUEST = 1224;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED)
            setIntro();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if ((s = controllaDati()) == "") {
                    s = "Compila tutti i campi";
                    Snackbar.make(view, s, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    // Invia via Whatsapp
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, s);
                    sendIntent.setType("text/plain");
                    sendIntent.setPackage("com.whatsapp");
                    startActivityForResult(sendIntent, WHATSAPP_REQUEST);
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setSelected(true);
        navigationView.setCheckedItem(R.id.nav_home);

        setFragment(new Fragment_Main(), "FRAGMENT_MAIN");
    }

    public void setIntro()  {
        Intent intent = new Intent(this, AppIntroPandas.class);
        startActivity(intent);
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        String tag;
        int id = item.getItemId();
        Fragment fragment;

        if (id == R.id.nav_home) {
            fragment = new Fragment_Main();
            tag = "FRAGMENT_MAIN";
        }
        else if (id == R.id.nav_favs){
            tag = "FRAGMENT_FAVS";
            fragment = new Fragment_Favs();
        }
        else {
            tag = "FRAGMENT_ABOUT";
            fragment = new Fragment_About();
        }

        setFragment(fragment, tag);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setFragment(android.support.v4.app.Fragment fragment, String tag) {
        fragmentManager.beginTransaction().replace(R.id.mainLayout, fragment, tag).commit();
    }

    public String controllaDati(){
        String testo = "";
        android.support.v4.app.Fragment currentFragment;
        currentFragment = fragmentManager.findFragmentByTag("FRAGMENT_MAIN");
        if(currentFragment != null && currentFragment.isVisible()){
            TextView txt = (TextView)currentFragment.getView().findViewById(R.id.input_nome);
            RadioGroup rdbGroupTaglia = (RadioGroup)currentFragment.getView().findViewById(R.id.rdbGroupTaglia);
            RadioGroup rdbGroupTipoPane = (RadioGroup)currentFragment.getView().findViewById(R.id.rdbGroupTipoPane);
            RadioGroup rdbGroupCaldoFreddo = (RadioGroup)currentFragment.getView().findViewById(R.id.rdbGroupCaldoFreddo);
            Button btnOra = (Button)currentFragment.getView().findViewById(R.id.btnOraRitiro);
            TextView lblIngredienti = (TextView)currentFragment.getView().findViewById(R.id.lblIngredienti);
            TextView lblSalse = (TextView)currentFragment.getView().findViewById(R.id.lblSalse);

            RadioButton rdb;
            csvText = "";
            String s = txt.getText().toString();

            if(!(s.equals(""))) {
                testo += "Nome: " + s;
                csvText += s + ";";
            }
            else
                return "";

            if(!(rdbGroupTaglia.getCheckedRadioButtonId() == -1)) {
                rdb = (RadioButton) rdbGroupTaglia.findViewById(rdbGroupTaglia.getCheckedRadioButtonId());
                String rdbText = rdb.getText().toString();
                testo += "\nTaglia: " + rdbText.substring(0, rdbText.length() - 6).toLowerCase();
                csvText += rdbText.substring(0, rdbText.length() - 6).toLowerCase() + ";";
            }
            else
                return "";

            if(!(rdbGroupTipoPane.getCheckedRadioButtonId() == -1)) {
                rdb = (RadioButton)rdbGroupTipoPane.findViewById(rdbGroupTipoPane.getCheckedRadioButtonId());
                testo += " " + rdb.getText().toString().toLowerCase();
                csvText += rdb.getText().toString().toLowerCase() + ";";
            }
            else
                return "";

            if(!(rdbGroupCaldoFreddo.getCheckedRadioButtonId() == -1)) {
                rdb = (RadioButton)rdbGroupCaldoFreddo.findViewById(rdbGroupCaldoFreddo.getCheckedRadioButtonId());
                testo += " " + rdb.getText().toString().toLowerCase();
                csvText += rdb.getText().toString().toLowerCase() + ";";
            }
            else
                return "";

            if(!(lblIngredienti.getText().toString().isEmpty())) {
                testo += "\nIngredienti: " + lblIngredienti.getText();
                csvText += lblIngredienti.getText() + ";";
                if(lblSalse.getText().toString().isEmpty())
                    csvText += "separator";
            }
            else
                return "";

            if(!(lblSalse.getText().toString().isEmpty())) {
                testo += "\nSalse: " + lblSalse.getText();
                csvText += lblSalse.getText() + ";separator";
            }

            if(btnOra.getText().toString().contains(":"))
                testo += "\nPasso a prenderlo alle" + btnOra.getText().toString().substring(11);
            else
                return "";
        }
        return testo;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch(requestCode) {
            case WHATSAPP_REQUEST:
                // Dialog salva panino
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setIcon(R.drawable.logo_panda_ok);
                builder.setTitle("Panino creato");
                builder.setMessage("Vuoi salvarlo tra i preferiti?");
                builder.setPositiveButton("Sì", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Salva panino
                        String csv = "panini.csv";

                        try {
                            FileOutputStream fou = openFileOutput(csv, Context.MODE_APPEND);
                            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fou);
                            outputStreamWriter.write(csvText);
                            outputStreamWriter.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                });
                builder.show();
                break;
        }
    }
}
