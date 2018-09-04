package ru.coffeeset.cscl.reporter.controllers.adapters;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

import ru.coffeeset.cscl.reporter.R;
import ru.coffeeset.cscl.reporter.controllers.utils.Logger;
import ru.coffeeset.cscl.reporter.databinding.ItemRevenueBinding;
import ru.coffeeset.cscl.reporter.models.reports.Revenue;



public class RevenueAdapter extends RecyclerView.Adapter<RevenueAdapter.ViewHolder> {

    private List<Revenue.ReportItem> reportItems;
    private Context mContext;

    private int lastPosition;

    public RevenueAdapter(List<Revenue.ReportItem> list, Context context) {
        reportItems = list;
        mContext = context;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        final ItemRevenueBinding binding;

        private ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    @NonNull
    @Override
    public RevenueAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemRevenueBinding binding = ItemRevenueBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull RevenueAdapter.ViewHolder holder, int position) {
        Revenue.ReportItem division = reportItems.get(position);
        holder.binding.setDivision(division);
        holder.binding.setAdapter(this);



        /*if (true) {
            ObjectAnimator.ofInt(holder.binding.progressBar, "progress", 0, (int) division.getProgress())
                    .setDuration(1500)
                    .start();
        } else {
            holder.binding.progressBar.setProgress((int) division.getProgress());
        }*/

        lastPosition = 0;
       /* for (int i = 0; i<= division.getProgress(); i++){*/
/*
       Logger.d(division.getDivision().getName());
            startItemAnimation(holder.binding.progressBar, (int) division.getProgress(), 1);*/
        /*}*/


        Logger.d(division.getDivision().getName());
        ValueAnimator anim = ValueAnimator.ofInt(0, (int) division.getProgress());
        anim.addUpdateListener(valueAnimator -> {
            int val = (int) valueAnimator.getAnimatedValue();
            Logger.d(val);
            holder.binding.progressBar.setProgress(val);
            holder.binding.progressBar.setProgressTintList(mContext.getResources().getColorStateList(getProgressColor(val)));

                /*RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                layoutParams.width = val;
                view.setLayoutParams(layoutParams);*/
        });
        anim.setDuration(300);
        anim.start();




    }


    public void startItemAnimation(View view, int width, int position) {
        if (position > lastPosition) {
            ValueAnimator anim = ValueAnimator.ofInt(0, width);
            anim.addUpdateListener(valueAnimator -> {
                int val = (int) valueAnimator.getAnimatedValue();
                Logger.d(val);
                ((ProgressBar)view).setProgress(val);
                ((ProgressBar)view).setBackgroundColor(getProgressColor(val));

                /*RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                layoutParams.width = val;
                view.setLayoutParams(layoutParams);*/
            });
            anim.setDuration(300);
            anim.start();
            lastPosition = position;
        } else {
            ((ProgressBar)view).setProgress(width);
            //view.setLayoutParams(new RelativeLayout.LayoutParams(width, RelativeLayout.LayoutParams.MATCH_PARENT));
        }
    }

    public int getProgressColor(double progress) {
        if (progress < 50)
            return R.color.revenueProgressRed;
        if (progress < 70)
            return R.color.revenueProgressYel1;
        if (progress < 90)
            return R.color.revenueProgressYel2;
        if (progress < 100)
            return R.color.revenueProgressYel3;

        return R.color.revenueProgressGreen;
    }


    /*public int getProgressColor(double progress) {
        if (progress < 50)
            return ContextCompat.getColor(mContext, R.color.revenueProgressRed);
        if (progress < 70)
            return ContextCompat.getColor(mContext, R.color.revenueProgressYel1);
        if (progress < 90)
            return ContextCompat.getColor(mContext, R.color.revenueProgressYel2);
        if (progress < 100)
            return ContextCompat.getColor(mContext, R.color.revenueProgressYel3);

        return ContextCompat.getColor(mContext, R.color.revenueProgressGreen);
    }*/

    @Override
    public int getItemCount() {
        return reportItems.size();
    }


}
