package lorenzobersano.com.prenotazionipandas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.innodroid.expandablerecycler.ExpandableRecyclerAdapter;

/**
 * Created by Lorenzo Bersano on 09/06/2016.
 */
public class ExpList_BottomSheet extends BottomSheetDialogFragment {

    String[] ingredients, categories;
    RecyclerView recycler;
    RecyclerAdapterIngredienti adapter;
    View v;
    BottomSheetBehavior bottomSheetBehavior;
    FloatingActionButton fab;
    boolean[] absoluteCheckedArray;

    public ExpList_BottomSheet() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {
        Bundle args = new Bundle();
        v = inflater.inflate(R.layout.list_ingredienti, container, false);

        ingredients = getResources().getStringArray(R.array.ingredients_array);
        categories = getResources().getStringArray(R.array.categorie_array);

        recycler = (RecyclerView)v.findViewById(R.id.recyclerViewIngredienti);
        adapter = new RecyclerAdapterIngredienti(getContext(), ingredients, categories);
        adapter.setMode(ExpandableRecyclerAdapter.MODE_ACCORDION);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(adapter);
        recycler.setNestedScrollingEnabled(false);
        bottomSheetBehavior.from(recycler).setState(BottomSheetBehavior.STATE_EXPANDED);

        if(getArguments() != null)   {
            args = getArguments();
            if(args.getBooleanArray("ingredienti") != null) {
                absoluteCheckedArray = args.getBooleanArray("ingredienti");
                adapter.setAbsoluteCheckedArray(absoluteCheckedArray);
            }
        }

        fab = (FloatingActionButton)v.findViewById(R.id.fabLista);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                absoluteCheckedArray = adapter.getAbsoluteCheckedArray();
                Intent intent = new Intent();
                intent.putExtra("selectedItems", absoluteCheckedArray);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                dismiss();
            }
        });

        return v;
    }
}
