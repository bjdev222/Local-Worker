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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.lw.localworker.FeedbackShowActivity;
import com.lw.localworker.R;
import com.lw.localworker.model.ReviewModel;

public class FeedbackAdapter extends FirebaseRecyclerAdapter<ReviewModel,FeedbackAdapter.FeedbackHolder> {


    Context context;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FeedbackAdapter(@NonNull FirebaseRecyclerOptions<ReviewModel> options,Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull FeedbackHolder holder, int position, @NonNull ReviewModel model) {

        String title=model.getTitle();
        String uname=model.getUname();
        String desc=model.getDesc();
        String date=model.getDate();
        String rate=model.getRate();

        holder.uname.setText(uname.substring(0,5)+"******"+uname.substring(11,13));
        holder.title.setText(model.getTitle());
        holder.desc.setText(model.getDesc());
        holder.date.setText(model.getDate());

        String v=model.getRate();
        if(v.equals("1")){
            holder.s1.setImageResource(R.drawable.fs);
            holder.s2.setImageResource(R.drawable.bs);
            holder.s3.setImageResource(R.drawable.bs);
            holder.s4.setImageResource(R.drawable.bs);
            holder.s5.setImageResource(R.drawable.bs);
        }
        if(v.equals("2")){
            holder.s1.setImageResource(R.drawable.fs);
            holder.s2.setImageResource(R.drawable.fs);
            holder.s3.setImageResource(R.drawable.bs);
            holder.s4.setImageResource(R.drawable.bs);
            holder.s5.setImageResource(R.drawable.bs);
        }
        if(v.equals("3")){

            holder.s1.setImageResource(R.drawable.fs);
            holder.s2.setImageResource(R.drawable.fs);
            holder.s3.setImageResource(R.drawable.fs);
            holder.s4.setImageResource(R.drawable.bs);
            holder.s5.setImageResource(R.drawable.bs);
        }
        if(v.equals("4")){

            holder.s1.setImageResource(R.drawable.fs);
            holder.s2.setImageResource(R.drawable.fs);
            holder.s3.setImageResource(R.drawable.fs);
            holder.s4.setImageResource(R.drawable.fs);
            holder.s5.setImageResource(R.drawable.bs);
        }
        if(v.equals("5")){

            holder.s1.setImageResource(R.drawable.fs);
            holder.s2.setImageResource(R.drawable.fs);
            holder.s3.setImageResource(R.drawable.fs);
            holder.s4.setImageResource(R.drawable.fs);
            holder.s5.setImageResource(R.drawable.fs);
        }

        holder.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, FeedbackShowActivity.class);
                intent.putExtra("title",title);
                intent.putExtra("uname",uname);
                intent.putExtra("desc",desc);
                intent.putExtra("date",date);
                intent.putExtra("rate",rate);

                context.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public FeedbackHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_layout, parent, false);
        return new FeedbackHolder(view);
    }

    class FeedbackHolder extends RecyclerView.ViewHolder{


        TextView title,date,uname,desc;
        ImageView s1,s2,s3,s4,s5,next;
        public FeedbackHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            date=itemView.findViewById(R.id.date);
            uname=itemView.findViewById(R.id.uname);
            desc=itemView.findViewById(R.id.desc);

            s1=itemView.findViewById(R.id.s1);
            s2=itemView.findViewById(R.id.s2);
            s3=itemView.findViewById(R.id.s3);
            s4=itemView.findViewById(R.id.s4);
            s5=itemView.findViewById(R.id.s5);

            next=itemView.findViewById(R.id.next);

        }
    }
}
