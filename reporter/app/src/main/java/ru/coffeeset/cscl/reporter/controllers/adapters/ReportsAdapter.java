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
import ru.coffeeset.cscl.reporter.databinding.ItemReportBinding;
import ru.coffeeset.cscl.reporter.models.reports.Report;


public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.ViewHolder> {

    private List<Report> reports;
    private OnReportClickListener itemListener;
    private Context mContext;

    public interface OnReportClickListener {
        void onReportClick(Report reportForList);
    }

    public ReportsAdapter(List<Report> reports, OnReportClickListener itemListener, Context mContext) {
        this.reports = reports;
        this.itemListener = itemListener;
        this.mContext = mContext;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        final ItemReportBinding binding;

        private ViewHolder(View v) {
            super(v);
            binding = DataBindingUtil.bind(v);
        }
    }


    @NonNull
    @Override
    public ReportsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemReportBinding binding = ItemReportBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding.getRoot());
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        Report report = reports.get(i);

        holder.binding.setReport(report);
        holder.binding.icon.setImageResource(getImageFor(report));

        report.setName(getNameFor(report.getPosition()));
        report.setDescription(getDescriptionFor(report.getPosition()));

        holder.binding.setClicker(v -> itemListener.onReportClick(report));
    }

    private int getImageFor(Report reportForList) {
        String image = "ico_rep_" + reportForList.getPosition() % 5;
        return mContext.getResources().getIdentifier(image, "drawable", mContext.getPackageName());
    }

    private int getNameFor(int id) {
        switch (id) {
            case Report.Position.divisions:
                return R.string.STR_REPORT_NAME_DIVISIONS;
            case Report.Position.salesMix:
                return R.string.STR_REPORT_NAME_SALES_MIX;
            case Report.Position.golden:
                return R.string.STR_REPORT_NAME_GOLDEN;
            case Report.Position.card:
                return R.string.STR_REPORT_NAME_CARD;
            default:
                return 0;
        }
    }

    private int getDescriptionFor(int id) {
        switch (id) {
            case Report.Position.divisions:
                return R.string.STR_REPORT_DESCRIPTION_DIVISIONS;
            case Report.Position.salesMix:
                return R.string.STR_REPORT_DESCRIPTION_SALES_MIX;
            case Report.Position.golden:
                return R.string.STR_REPORT_DESCRIPTION_GOLDEN;
            case Report.Position.card:
                return R.string.STR_REPORT_DESCRIPTION_CARD;
            default:
                return 0;
        }
    }


    @Override
    public int getItemCount() {
        return reports.size();
    }
}


