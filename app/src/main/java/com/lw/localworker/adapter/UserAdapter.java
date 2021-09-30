package com.lw.localworker.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lw.localworker.DetailsActivity;
import com.lw.localworker.R;
import com.lw.localworker.UserActivity;
import com.lw.localworker.model.WModel;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter <UserAdapter.holder> implements Filterable {

    private List<WModel> listData;
    private List<WModel> exampleListFull;
    Context context;

    UserActivity activity;

    FirebaseDatabase firebaseDatabase;


    long totalReview;
    DatabaseReference databaseReference;

    public UserAdapter(List<WModel> listData, Context context) {
        this.listData = listData;
        this.activity = activity;
        this.context = context;
        exampleListFull = new ArrayList<>(listData);

    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_layout,parent,false);
        return new holder(view);    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        final WModel model=listData.get(position);
        checkFeedback(model.getWPNumber(),holder);


        holder.name.setText(model.getWName());
        holder.address.setText(model.getWAddress());
        holder.work.setText(model.getWWorkName());

        String pUrl=model.getWProfileUrl().replaceAll("^\"|\"$", "");

        Glide.with(context).load(pUrl).into(holder.profileImage);




        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name=model.getWName();
                String work=model.getWWorkName();
                String desc=model.getWDesc();
                String mNumber=model.getWPNumber();
                String email=model.getWEmailId();
                String wsap=model.getWWNumber();
                String address=model.getWAddress();

                String pUrl=model.getWProfileUrl();
                String g1=model.getWGalleryUrl1();
                String g2=model.getWGalleryUrl2();
                String g3=model.getWGalleryUrl3();
                String g4=model.getWGalleryUr4();




                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                prefs.edit().putString("pNumber", mNumber).commit(); // islogin is a boolean value of your login status

                Intent intent=new Intent(context, DetailsActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("work",work);
                intent.putExtra("mnumber",mNumber);
                intent.putExtra("email",email);
                intent.putExtra("wsap",wsap);
                intent.putExtra("address",address);
                intent.putExtra("purl",pUrl);
                intent.putExtra("g1",g1);
                intent.putExtra("g2",g2);
                intent.putExtra("g3",g3);
                intent.putExtra("g4",g4);
                intent.putExtra("desc",desc);

                intent.putExtra("Model",model);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }



    public Filter getFilter2() {
        return filterByCity;
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<WModel> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() ==0) {
                filteredList.addAll(exampleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                String filterPattern2 = prefs.getString("Ecity", "").toLowerCase().trim(); // get value of city  status
                Log.d("TAG", "performFiltering: "+filterPattern);
                Log.d("TAG", "performFiltering: item"+exampleListFull);
                for (WModel item : exampleListFull) {
                    Log.d("TAG", "performFiltering: for "+item);
                    assert filterPattern2 != null;
                    if (item.getWDesc().toLowerCase().contains(filterPattern) && item.getWCity().toLowerCase().contains(filterPattern2)) {
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
            listData.clear();
            listData.addAll((List) results.values);


            notifyDataSetChanged();
        }
    };



    private Filter filterByCity = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<WModel> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() ==0) {
                filteredList.addAll(exampleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                String filterPattern2 = prefs.getString("Ework", "").toLowerCase().trim(); // get value of city  status
                Log.d("TAG", "performFiltering: "+filterPattern);
                Log.d("TAG", "performFiltering: item"+exampleListFull);
                for (WModel item : exampleListFull) {
                    Log.d("TAG", "performFiltering: for "+item);
                    assert filterPattern2 != null;
                    if (item.getWDesc().toLowerCase().contains(filterPattern2) && item.getWCity().toLowerCase().contains(filterPattern)) {
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
            listData.clear();
            listData.addAll((List) results.values);
            if(listData.size()==0){

            }
            notifyDataSetChanged();
        }
    };




    private void checkFeedback(String wpNumber, final holder holder) {

        final String[] y = new String[1];
        firebaseDatabase = FirebaseDatabase.getInstance();

        final int[] maxid = {0};

        final int[] review = {0};
        databaseReference = firebaseDatabase.getReference("Feedback/"+wpNumber);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    maxid[0] = (int) snapshot.getChildrenCount();
                    //  Toast.makeText(Dashboard.this, "children is"+maxid, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String desk = ds.child("rate").getValue(String.class);
                    totalReview=ds.getChildrenCount();
                    Log.d("TAG", "onDataChange: "+totalReview);

                    assert desk != null;
                    review[0] = review[0] + Integer.parseInt(desk);

                    float y=desk.length();

                }
                Log.e("TAG", String.valueOf(review[0]));

                float finalRating=(float) review[0] / maxid[0];
                Log.d("TAG", "fn: "+finalRating);


                holder.rating.setText(String.valueOf(finalRating)+" Ratings  ");



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        };
        databaseReference.addListenerForSingleValueEvent(valueEventListener);



    }

    public class holder extends RecyclerView.ViewHolder{



        TextView name,rating,address,work;
        CircleImageView profileImage;

        public holder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.name);
            rating=itemView.findViewById(R.id.rating);
            address=itemView.findViewById(R.id.address);
            work=itemView.findViewById(R.id.work);

            profileImage=itemView.findViewById(R.id.profileImage);
        }
    }
}
