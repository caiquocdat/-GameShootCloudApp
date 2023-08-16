package com.caiquocdat.shootclouds.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.caiquocdat.shootclouds.R;
import com.caiquocdat.shootclouds.databinding.ItemRankBinding;
import com.caiquocdat.shootclouds.model.ScoreModel;

import java.util.List;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ViewHolder> {

    private List<ScoreModel> scoreList;
    private Context context;

    public ScoreAdapter(Context context, List<ScoreModel> scoreList) {
        this.context = context;
        this.scoreList = scoreList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRankBinding itemRankBinding = ItemRankBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(itemRankBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ScoreModel score = scoreList.get(position);
        if (position==0||position==2||position==4){
            holder.itemRankBinding.brgLinear.setBackgroundResource(R.drawable.img_brg_rank_1);
            holder.itemRankBinding.rankTv.setTextColor(ContextCompat.getColor(context, android.R.color.white));
            holder.itemRankBinding.pointTv.setTextColor(ContextCompat.getColor(context, android.R.color.white));
        }else{
            holder.itemRankBinding.brgLinear.setBackgroundResource(R.drawable.img_brg_rank_2);
        }
        String rankText = getRankText(position + 1);  // position + 1 vì position bắt đầu từ 0
        holder.itemRankBinding.rankTv.setText(rankText);
        holder.itemRankBinding.pointTv.setText(String.valueOf(score.getPoint()));
    }

    @Override
    public int getItemCount() {
        return scoreList.size();
    }

    private String getRankText(int rank) {
        switch(rank) {
            case 1:
                return "1st";
            case 2:
                return "2nd";
            case 3:
                return "3rd";
            default:
                return rank + "th";
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemRankBinding itemRankBinding;

        public ViewHolder(@NonNull ItemRankBinding itemRankBinding) {
            super(itemRankBinding.getRoot());
            this.itemRankBinding = itemRankBinding;

        }
    }
}

