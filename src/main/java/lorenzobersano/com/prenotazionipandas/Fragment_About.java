package lorenzobersano.com.prenotazionipandas;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by lorenzobersano on 04/07/16.
 */
public class Fragment_About extends Fragment {
    public Fragment_About() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.about, container, false);
        TextView sendEmail = (TextView)v.findViewById(R.id.mail_item);
        TextView goToGithub = (TextView)v.findViewById(R.id.github_item);
        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto: lorenzo.bersano@gmail.com"));
                startActivity(intent);
            }
        });

        goToGithub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/lorenzobersano"));
                startActivity(browserIntent);
            }
        });
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity)getActivity()).setActionBarTitle("Informazioni");
        FloatingActionButton fab = (FloatingActionButton)getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
    }
}
