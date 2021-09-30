package com.lw.localworker.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lw.localworker.R;
import com.lw.localworker.TestAcivity;
import com.lw.localworker.WProfileActivity;
import com.lw.localworker.model.CityModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CityAdapterP extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE = 1;
    private final Context context;
    private final List<CityModel> listRecyclerItem;
    private List<CityModel> exampleListFull;
    WProfileActivity wProfileActivity;


    public CityAdapterP(WProfileActivity activity, Context context, List<CityModel> listRecyclerItem) {
        this.context = context;
        wProfileActivity=activity;
        this.listRecyclerItem = listRecyclerItem;
        exampleListFull = new ArrayList<>(listRecyclerItem);

    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView state;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.city);
            state = (TextView) itemView.findViewById(R.id.state);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (i) {
            case TYPE:

            default:

                View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.city_search_layout, viewGroup, false);

                return new CityAdapterP.ItemViewHolder((layoutView));
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        int viewType = getItemViewType(i);

        switch (viewType) {
            case TYPE:
            default:

                CityAdapterP.ItemViewHolder itemViewHolder = (CityAdapterP.ItemViewHolder) viewHolder;
                CityModel holidays = (CityModel) listRecyclerItem.get(i);

                itemViewHolder.name.setText(holidays.getCity()+",");
                itemViewHolder.state.setText(holidays.getState());

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText location=(EditText) wProfileActivity.findViewById(R.id.wCity);
                        location.setText(holidays.getCity()+","+"  "+holidays.getState());
                    }
                });
        }



    }


    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Object> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() ==0) {
                filteredList.addAll(exampleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                Log.d("TAG", "performFiltering: "+filterPattern);
                Log.d("TAG", "performFiltering: item"+exampleListFull);
                for (CityModel item : exampleListFull) {
                    Log.d("TAG", "performFiltering: for "+item);
                    if (item.getCity().toLowerCase().contains(filterPattern)) {
                        Log.d("TAG", "performFiltering: "+item);
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listRecyclerItem.clear();
            listRecyclerItem.addAll((Collection<? extends CityModel>) results.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public int getItemCount() {
        return listRecyclerItem.size();
    }


}
