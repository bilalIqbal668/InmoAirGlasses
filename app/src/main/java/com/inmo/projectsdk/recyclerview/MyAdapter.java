package com.inmo.projectsdk.recyclerview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.inmo.projectsdk.R;
import com.inmo.projectsdk.utils.CardUtils;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {
    private List<String> datas;
    private Context context;
    public MyAdapter(Context context, List<String> list) {
        datas = list;
        this.context = context;
    }
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(View.inflate(parent.getContext(), R.layout.item_recyclerview, null));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.tv.setText(datas.get(position));
        holder.setCardShadow();
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        public TextView tv;
        public CardView cardView;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.title);
            cardView = itemView.findViewById(R.id.cardview);
        }

        public void setCardShadow() {
//            CardUtils.init();
//            CardUtils.setCardShadowColor(cardView, context.getResources().getColor(R.color.teal_700), context.getResources().getColor(R.color.teal_200));
        }
    }
}
