package ru.coffeeset.cscl.reporter.controllers.adapters;


import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.coffeeset.cscl.reporter.R;
import ru.coffeeset.cscl.reporter.databinding.ItemFilterBinding;
import ru.coffeeset.cscl.reporter.models.accessList.IAccessItem;
import ru.coffeeset.cscl.reporter.models.accessList.ItemType;


public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {

    private SparseArray<IAccessItem> selectedElementsArray;
    private OnAccessClickListener listener;


    public interface OnAccessClickListener {
        void onAccessClick(int i);
    }


    public void setSelectedElements(SparseArray<IAccessItem> selectedElementsArray) {
        this.selectedElementsArray = selectedElementsArray;
        notifyDataSetChanged();
    }


    public FilterAdapter(OnAccessClickListener listener) {
        this.listener = listener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        final ItemFilterBinding binding;

        private ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }


    @NonNull
    @Override
    public FilterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemFilterBinding binding = ItemFilterBinding.inflate(inflater, parent, false);

        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull FilterAdapter.ViewHolder holder, int i) {
        IAccessItem element = selectedElementsArray.get(i);
        holder.binding.setElement(element);

        showType(holder, i);

        holder.binding.setClicker(v -> listener.onAccessClick(i));
    }

    @Override
    public int getItemCount() {
        return selectedElementsArray.size();
    }


    private void showType(@NonNull FilterAdapter.ViewHolder holder, int i) {
        if (i == ItemType.TYPE_COUNTRY)
            holder.binding.elementType.setText(R.string.STR_FILTER_COUNTRY);
        if (i == ItemType.TYPE_ORGANIZATION)
            holder.binding.elementType.setText(R.string.STR_FILTER_ORGANIZATION);
        if (i == ItemType.TYPE_CITY)
            holder.binding.elementType.setText(R.string.STR_FILTER_CITY);
        if (i == ItemType.TYPE_DIVISION)
            holder.binding.elementType.setText(R.string.STR_FILTER_DIVISION);

    }
}
