package ru.coffeeset.cscl.reporter.controllers.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.coffeeset.cscl.reporter.databinding.ItemSalesMixEmployeeBinding;
import ru.coffeeset.cscl.reporter.models.reports.SalesMix;


public class SalesMixAdapter extends RecyclerView.Adapter<SalesMixAdapter.ViewHolder> {

    private List<SalesMix.ReportItem> reportItems;
    private Context mContext;

    public SalesMixAdapter(List<SalesMix.ReportItem> list, Context context) {
        reportItems = list;
        mContext = context;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        final ItemSalesMixEmployeeBinding binding;

        private ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemSalesMixEmployeeBinding binding = ItemSalesMixEmployeeBinding.inflate(inflater, parent, false);

        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull SalesMixAdapter.ViewHolder holder, int position) {
        SalesMix.ReportItem reportItem = reportItems.get(position);
        holder.binding.setReportItem(reportItem);

        GridLayoutManager glm = new GridLayoutManager(mContext, 4);
        holder.binding.recyclerView.setLayoutManager(glm);
        SalesMixItemAdapter smia = new SalesMixItemAdapter(reportItem.getPurchaseCategoryInfo(), mContext);
        holder.binding.recyclerView.setAdapter(smia);
    }

    @Override
    public int getItemCount() {
        return reportItems.size();
    }
}
