package com.lw.localworker.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lw.localworker.R;
import com.lw.localworker.UserActivity;
import com.lw.localworker.model.CategoryModel;
import com.lw.localworker.model.WModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.categoryHolder> {

    private List<CategoryModel> listData;
    Context context;
    int flag;
    RequestOptions options;



    public CategoryAdapter(List<CategoryModel> listData, Context context,int flag) {
        this.listData = listData;
        this.context = context;
        this.flag = flag;
    }

    @NonNull
    @Override
    public categoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.category_layout,parent,false);
        return new categoryHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull categoryHolder holder, int position) {
        CategoryModel categoryModel=listData.get(position);

        holder.catName.setText(categoryModel.getName());

        String sUrl=(categoryModel.getUrl()).replaceAll("^\"|\"$", "");
        options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.animation)
                .error(R.drawable.loaderr)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
                .dontAnimate()
                .dontTransform();


        Glide.with(context).load(sUrl)
                .into(holder.catImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context, UserActivity.class);
                intent.putExtra("keyword",categoryModel.getName());
                intent.putExtra("flag",1);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });


    }


    @Override
    public int getItemCount() {

        if(flag==1) {
            return 2;
        }
        else {
            return listData.size();
        }
    }

    class categoryHolder extends RecyclerView.ViewHolder{

        CircleImageView catImage;
        TextView catName;

        public categoryHolder(@NonNull View itemView) {
            super(itemView);
            catImage=itemView.findViewById(R.id.catImage);
            catName=itemView.findViewById(R.id.catname);
        }
    }
}
