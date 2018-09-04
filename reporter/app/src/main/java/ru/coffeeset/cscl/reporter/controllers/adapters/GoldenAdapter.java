package ru.coffeeset.cscl.reporter.controllers.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.coffeeset.cscl.reporter.controllers.utils.Logger;
import ru.coffeeset.cscl.reporter.databinding.ItemGoldenBinding;
import ru.coffeeset.cscl.reporter.models.reports.Golden;


public class GoldenAdapter extends RecyclerView.Adapter<GoldenAdapter.ViewHolder> {

    private List<Golden.ReportItem> reportItems;
    private Context mContext;


    public GoldenAdapter(List<Golden.ReportItem> list, Context context) {
        reportItems = list;
        mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ItemGoldenBinding binding;

        private ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemGoldenBinding binding = ItemGoldenBinding.inflate(inflater, parent, false);

        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Golden.ReportItem reportItem = reportItems.get(position);
        holder.binding.setReportItem(reportItem);

        for (Golden.Product p : reportItem.getProductInfo()) {
            if (p.getCategory().equals("coffee"))
                holder.binding.coffeeQty.setText(p.getQty());
            if (p.getCategory().equals("drink"))
                holder.binding.drinkQty.setText(p.getQty());
            if (p.getCategory().equals("food"))
                holder.binding.foodQty.setText(p.getQty());
        }
    }

    @Override
    public int getItemCount() {
        return reportItems.size();

    }





}
