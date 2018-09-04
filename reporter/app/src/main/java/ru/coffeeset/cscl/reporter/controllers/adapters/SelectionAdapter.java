package ru.coffeeset.cscl.reporter.controllers.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.coffeeset.cscl.reporter.databinding.ItemSelectBinding;
import ru.coffeeset.cscl.reporter.models.accessList.IAccessItem;


public class SelectionAdapter extends RecyclerView.Adapter<SelectionAdapter.ViewHolder> {

    private List<IAccessItem> list;
    private Context mContext;
    private SelectionAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(IAccessItem element);
    }

    public SelectionAdapter(List<IAccessItem> list, SelectionAdapter.OnItemClickListener listener, Context context) {
        this.listener = listener;
        this.list = list;
        this.mContext = context;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        final ItemSelectBinding binding;

        private ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    @NonNull
    @Override
    public SelectionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemSelectBinding binding = ItemSelectBinding.inflate(inflater, parent, false);
        return new SelectionAdapter.ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull SelectionAdapter.ViewHolder holder, int i) {
        IAccessItem element = list.get(i);
        holder.binding.setElement(element);


        holder.binding.setClicker(v -> listener.onItemClick(element));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
