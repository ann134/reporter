package ru.coffeeset.cscl.reporter.controllers.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.coffeeset.cscl.reporter.R;
import ru.coffeeset.cscl.reporter.databinding.ItemSalesMixCategoryBinding;
import ru.coffeeset.cscl.reporter.models.reports.SalesMix;


public class SalesMixItemAdapter extends RecyclerView.Adapter<SalesMixItemAdapter.ViewHolder> {

    private List<SalesMix.Category> categories;
    private Context mContext;

    public SalesMixItemAdapter(List<SalesMix.Category> list, Context context) {
        categories = list;
        mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final ItemSalesMixCategoryBinding binding;

        private ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    @NonNull
    @Override
    public SalesMixItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemSalesMixCategoryBinding binding = ItemSalesMixCategoryBinding.inflate(inflater, parent, false);

        return new SalesMixItemAdapter.ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull SalesMixItemAdapter.ViewHolder holder, int position) {
        SalesMix.Category category = categories.get(position);
        holder.binding.setCategory(category);
        holder.binding.cardView.setCardBackgroundColor(mContext.getResources().getColor(getColorByPosition(position)));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }


    private int getColorByPosition(int position) {
        switch (position) {
            case 0:
                return R.color.salesMixCoffee;
            case 1:
                return R.color.salesMixDrink;
            case 2:
                return R.color.salesMixFood;
            case 3:
                return R.color.salesMixOthers;
        }
        return 0;
    }

}
