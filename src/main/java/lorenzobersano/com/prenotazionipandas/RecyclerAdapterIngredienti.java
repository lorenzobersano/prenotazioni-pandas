package lorenzobersano.com.prenotazionipandas;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.innodroid.expandablerecycler.ExpandableRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapterIngredienti extends ExpandableRecyclerAdapter<RecyclerAdapterIngredienti.IngredientsListItem> {
    public static final int TYPE_INGREDIENT = 1001;
    String[] categories, ingredients;
    static int childrenCount = 0;
    private boolean[] absoluteCheckedArray, carniUovaCheckedArray, salumiCheckedArray, formaggiCheckedArray, inVasettoCheckedArray,
                      verduraCrudaCheckedArray, verduraCottaCheckedArray, pesceCheckedArray, salseCheckedArray;

    public RecyclerAdapterIngredienti(Context context, String[] ingredients, String[] categories) {
        super(context);
        this.categories = categories;
        this.ingredients = ingredients;
        absoluteCheckedArray = new boolean[ingredients.length];
        childrenCount = 0;
        setItems(getItems());
    }

    public boolean[] getAbsoluteCheckedArray() {
        return absoluteCheckedArray;
    }

    public void setAbsoluteCheckedArray(boolean[] absoluteCheckedArray) {
        this.absoluteCheckedArray = absoluteCheckedArray;
        setCheckedItems();
    }

    public static class IngredientsListItem extends ExpandableRecyclerAdapter.ListItem  {
        public String text;
        public int id;
        public boolean isChecked;

        public IngredientsListItem(String group)    {
            super(TYPE_HEADER);
            text = group;
        }

        public IngredientsListItem(String first, String last) {
            super(TYPE_INGREDIENT);
            text = first + last;
            id = childrenCount;
            isChecked = false;
            childrenCount++;
        }
    }

    public class HeaderViewHolder extends ExpandableRecyclerAdapter.HeaderViewHolder {
        TextView name;

        public HeaderViewHolder(View view) {
            super(view, (ImageView) view.findViewById(R.id.item_arrow));
            name = (TextView) view.findViewById(R.id.item_header_name);
        }

        public void bind(int position) {
            super.bind(position);
            name.setText(visibleItems.get(position).text);
        }
    }

    public class IngredientViewHolder extends ExpandableRecyclerAdapter.ViewHolder {
        CheckedTextView name;

        public IngredientViewHolder(View view) {
            super(view);
            name = (CheckedTextView) view.findViewById(R.id.checked_list_item);
        }

        public void bind(final int position) {
            name.setText(visibleItems.get(position).text);
            name.setId(visibleItems.get(position).id);
            int id = name.getId();
            if(id < 6)              name.setChecked(carniUovaCheckedArray[position - 1]);
            if(id >= 6 && id < 18)  name.setChecked(salumiCheckedArray[position - 2]);
            if(id >= 18 && id < 30) name.setChecked(formaggiCheckedArray[position - 3]);
            if(id >= 30 && id < 39) name.setChecked(inVasettoCheckedArray[position - 4]);
            if(id >= 39 && id < 47) name.setChecked(verduraCrudaCheckedArray[position - 5]);
            if(id >= 47 && id < 62) name.setChecked(verduraCottaCheckedArray[position - 6]);
            if(id >= 62 && id < 65) name.setChecked(pesceCheckedArray[position - 7]);
            if(id >= 65)            name.setChecked(salseCheckedArray[position - 8]);
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    name.setChecked(!name.isChecked());
                    int viewId = v.getId();
                    if(viewId < 6)                  carniUovaCheckedArray[viewId] = !carniUovaCheckedArray[viewId];
                    if(viewId >= 6 && viewId < 18)  salumiCheckedArray[viewId - 6] = !salumiCheckedArray[viewId - 6];
                    if(viewId >= 18 && viewId < 30) formaggiCheckedArray[viewId - 18] = !formaggiCheckedArray[viewId - 18];
                    if(viewId >= 30 && viewId < 39) inVasettoCheckedArray[viewId - 30] = !inVasettoCheckedArray[viewId - 30];
                    if(viewId >= 39 && viewId < 47) verduraCrudaCheckedArray[viewId - 39] = !verduraCrudaCheckedArray[viewId - 39];
                    if(viewId >= 47 && viewId < 62) verduraCottaCheckedArray[viewId - 47] = !verduraCottaCheckedArray[viewId - 47];
                    if(viewId >= 62 && viewId < 65) pesceCheckedArray[viewId - 62] = !pesceCheckedArray[viewId - 62];
                    if(viewId >= 65)                salseCheckedArray[viewId - 65] = !salseCheckedArray[viewId - 65];
                    absoluteCheckedArray[viewId] = !absoluteCheckedArray[viewId];
                }
            });
        }
    }

    @Override

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                return new HeaderViewHolder(inflate(R.layout.list_ingredienti_group_item, parent));
            case TYPE_INGREDIENT:
            default:
                return new IngredientViewHolder(inflate(R.layout.list_item, parent));
        }
    }

    @Override
    public void onBindViewHolder(ExpandableRecyclerAdapter.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                ((HeaderViewHolder) holder).bind(position);
                break;
            case TYPE_INGREDIENT:
            default:
                ((IngredientViewHolder) holder).bind(position);
                break;
        }
    }

    private List<IngredientsListItem> getItems() {
        List<IngredientsListItem> items = new ArrayList<>();
        List<String> carniUova = new ArrayList<>();
        List<String> salumi = new ArrayList<>();
        List<String> formaggi = new ArrayList<>();
        List<String> inVasetto = new ArrayList<>();
        List<String> verduraCruda = new ArrayList<>();
        List<String> verduraCotta = new ArrayList<>();
        List<String> pesce = new ArrayList<>();
        List<String> salse = new ArrayList<>();
        List<String> arrayToScan = new ArrayList<>();
        int i;

        for(i = 0; i < ingredients.length; i++) {
            if(i < 6)               carniUova.add(ingredients[i]);
            if(i >= 6 && i < 18)    salumi.add(ingredients[i]);
            if(i >= 18 && i < 30)   formaggi.add(ingredients[i]);
            if(i >= 30 && i < 39)   inVasetto.add(ingredients[i]);
            if(i >= 39 && i < 47)   verduraCruda.add(ingredients[i]);
            if(i >= 47 && i < 62)   verduraCotta.add(ingredients[i]);
            if(i >= 62 && i < 65)   pesce.add(ingredients[i]);
            if(i >= 65)             salse.add(ingredients[i]);
        }

        carniUovaCheckedArray = new boolean[carniUova.size()];
        salumiCheckedArray = new boolean[salumi.size()];
        formaggiCheckedArray = new boolean[formaggi.size()];
        inVasettoCheckedArray = new boolean[inVasetto.size()];
        verduraCrudaCheckedArray = new boolean[verduraCruda.size()];
        verduraCottaCheckedArray = new boolean[verduraCotta.size()];
        pesceCheckedArray = new boolean[pesce.size()];
        salseCheckedArray = new boolean[salse.size()];

        for(i = 0; i < categories.length; i++)  {
            items.add(new IngredientsListItem(categories[i]));
            switch(i){
                case 0:
                    arrayToScan = carniUova;
                    break;
                case 1:
                    arrayToScan = salumi;
                    break;
                case 2:
                    arrayToScan = formaggi;
                    break;
                case 3:
                    arrayToScan = inVasetto;
                    break;
                case 4:
                    arrayToScan = verduraCruda;
                    break;
                case 5:
                    arrayToScan = verduraCotta;
                    break;
                case 6:
                    arrayToScan = pesce;
                    break;
                case 7:
                    arrayToScan = salse;
                    break;
            }
            for(int j = 0; j < arrayToScan.size(); j++) {
                items.add(new IngredientsListItem(arrayToScan.get(j), ""));
            }
        }

        return items;
    }

    public void setCheckedItems() {
        for(int i = 0; i < absoluteCheckedArray.length; i++) {
            if(absoluteCheckedArray[i]) {
                if (i < 6)              carniUovaCheckedArray[i] = true;
                if (i >= 6 && i < 18)   salumiCheckedArray[i - 6] = true;
                if (i >= 18 && i < 30)  formaggiCheckedArray[i - 18] = true;
                if (i >= 30 && i < 39)  inVasettoCheckedArray[i - 30] = true;
                if (i >= 39 && i < 47)  verduraCrudaCheckedArray[i - 39] = true;
                if (i >= 47 && i < 62)  verduraCottaCheckedArray[i - 47] = true;
                if (i >= 62 && i < 65)  pesceCheckedArray[i - 62] = true;
                if (i >= 65)            salseCheckedArray[i - 65] = true;
            }
        }
    }
}