package ru.coffeeset.cscl.reporter.ui.fragments.reports;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import ru.coffeeset.cscl.reporter.R;
import ru.coffeeset.cscl.reporter.controllers.adapters.SalesMixAdapter;
import ru.coffeeset.cscl.reporter.controllers.network.HttpError;
import ru.coffeeset.cscl.reporter.controllers.network.RequestListener;
import ru.coffeeset.cscl.reporter.controllers.utils.Formatter;
import ru.coffeeset.cscl.reporter.controllers.utils.Logger;
import ru.coffeeset.cscl.reporter.databinding.FragmentReportSalesMixBinding;
import ru.coffeeset.cscl.reporter.models.DateEntry;
import ru.coffeeset.cscl.reporter.models.accessList.Division;
import ru.coffeeset.cscl.reporter.models.reports.Parameters;
import ru.coffeeset.cscl.reporter.models.reports.Report;
import ru.coffeeset.cscl.reporter.models.reports.ReportState;
import ru.coffeeset.cscl.reporter.models.reports.SalesMix;
import ru.coffeeset.cscl.reporter.ui.MainActivity;
import ru.coffeeset.cscl.reporter.ui.viewmodels.AccessListViewModel;
import ru.coffeeset.cscl.reporter.ui.viewmodels.ReportsViewModel;


public class SalesMixReportFragment extends BaseReportFragment {

    private FragmentReportSalesMixBinding viewDataBinding;
    private Context mContext;
    private ISupportActionBarListener mListener;

    private AccessListViewModel viewModel;
    private ReportsViewModel viewModelRep;

    private DateEntry filterDate;
    private List<Division> listFilteredDivisions;

    public ObservableField<String> dateText = new ObservableField<>();
    public ObservableField<String> divisionName = new ObservableField<>();
    public ObservableField<SalesMix> report = new ObservableField<>();


    public static SalesMixReportFragment newInstance() {
        return new SalesMixReportFragment();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        try {
            mListener = (ISupportActionBarListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement ISupportActionBarListener");
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModelRep = ViewModelProviders.of((MainActivity) mContext).get(ReportsViewModel.class);
        viewModel = ViewModelProviders.of((MainActivity) mContext).get(AccessListViewModel.class);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.viewDataBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_report_sales_mix,
                container,
                false);

        report.set(new SalesMix());
        viewDataBinding.setFragment(this);

        return viewDataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        refreshActionBar();
    }

    @Override
    public void refreshActionBar() {
        if (isVisible()) {
            mListener.onTitleSet(R.string.STR_REPORT_NAME_SALES_MIX);
            mListener.onHomeButtonSet(true);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        viewDataBinding.swipeContainer.setOnRefreshListener(this::refresh);

        viewModel.getLiveDataFilteredDivisions().observe(this, list -> {
                    if (isVisible()) {
                        listFilteredDivisions = list;
                        divisionName.set(list.get(0).getName());

                        if (filterDate != null)
                            refresh();
                    }
                }
        );

        viewModel.getLiveDataDate().observe(this, date -> {
                    if (isVisible()) {
                        filterDate = date;
                        dateText.set(Formatter.getDateStringUsual(date.getDateToNow()));

                        if (listFilteredDivisions != null)
                            refresh();
                    }
                }
        );
    }

    public void refresh() {
        viewDataBinding.swipeContainer.setRefreshing(false);
        report.set(new SalesMix());

        super.getReportPost(new SalesMix(), viewModelRep, getParams(), new RequestListener.ObjectCallback<SalesMix>() {
            @Override
            public void onComplete(SalesMix response) {

                if (response == null)
                    response = new SalesMix();

                response.setState(ReportState.IDLE);
                report.set(response);
                show();
            }

            @Override
            public void onError(HttpError e) {
                Logger.e("Error HttpError: " + e.getDescription());
            }
        });

    }

    private Parameters getParams() {
        return new Parameters(Report.ID.salesMix,
                Formatter.getDateStringISO(filterDate.getDateFrom()),
                Formatter.getDateStringISO(filterDate.getDateToEnd()),
                Parameters.Granularity.day,
                listFilteredDivisions);
    }


    private void show() {

        if (report.get().getReportItems() != null) {

            LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
            viewDataBinding.recyclerView.setLayoutManager(llm);

            SalesMixAdapter sma = new SalesMixAdapter(report.get().getReportItems(), mContext);
            viewDataBinding.recyclerView.setAdapter(sma);

        } else
            Toast.makeText(mContext, R.string.TOAST_REPORT_NOTHING, Toast.LENGTH_SHORT).show();



        /*try {
            if (report.get() == null || report.get().getReportItems() == null || report.get().getReportItems().size() == 0)
                throw new IndexOutOfBoundsException();

            LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
            viewDataBinding.recyclerView.setLayoutManager(llm);

            SalesMixAdapter sma = new SalesMixAdapter(report.get().getReportItems(), mContext);
            viewDataBinding.recyclerView.setAdapter(sma);

        } catch (IndexOutOfBoundsException e) {
            Toast.makeText(mContext, R.string.TOAST_REPORT_NOTHING, Toast.LENGTH_SHORT).show();
        }*/
    }

}