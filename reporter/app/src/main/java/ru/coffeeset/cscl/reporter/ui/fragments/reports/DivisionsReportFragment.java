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
import ru.coffeeset.cscl.reporter.controllers.adapters.RevenueAdapter;
import ru.coffeeset.cscl.reporter.controllers.network.HttpError;
import ru.coffeeset.cscl.reporter.controllers.network.RequestListener;
import ru.coffeeset.cscl.reporter.controllers.utils.Formatter;
import ru.coffeeset.cscl.reporter.controllers.utils.Logger;
import ru.coffeeset.cscl.reporter.databinding.FragmentReportDivisionsBinding;
import ru.coffeeset.cscl.reporter.models.DateEntry;
import ru.coffeeset.cscl.reporter.models.accessList.Division;
import ru.coffeeset.cscl.reporter.models.reports.Parameters;
import ru.coffeeset.cscl.reporter.models.reports.Report;
import ru.coffeeset.cscl.reporter.models.reports.ReportState;
import ru.coffeeset.cscl.reporter.models.reports.Revenue;
import ru.coffeeset.cscl.reporter.ui.MainActivity;
import ru.coffeeset.cscl.reporter.ui.viewmodels.AccessListViewModel;
import ru.coffeeset.cscl.reporter.ui.viewmodels.ReportsViewModel;


public class DivisionsReportFragment extends BaseReportFragment {

    private FragmentReportDivisionsBinding viewDataBinding;
    private Context mContext;
    private ISupportActionBarListener mListener;

    private AccessListViewModel viewModel;
    private ReportsViewModel viewModelRep;

    private DateEntry filterDate;
    private List<Division> listFilteredDivisions;

    public ObservableField<String> dateText = new ObservableField<>();
    public ObservableField<Revenue> report = new ObservableField<>();


    public static DivisionsReportFragment newInstance() {
        return new DivisionsReportFragment();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Logger.d("divisions onAttach");
        this.mContext = context;
        try {
            mListener = (ISupportActionBarListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement ISupportActionBarListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Logger.d("divisions onCreate");
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of((MainActivity) mContext).get(AccessListViewModel.class);
        viewModelRep = ViewModelProviders.of((MainActivity) mContext).get(ReportsViewModel.class);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Logger.d("divisions onCreateView");
        this.viewDataBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_report_divisions,
                container,
                false);

        report.set(new Revenue());
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
            mListener.onTitleSet(R.string.STR_REPORT_NAME_DIVISIONS);
            mListener.onHomeButtonSet(true);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        Logger.d("divisions onStart");

        viewDataBinding.swipeContainer.setOnRefreshListener(this::refresh);

        viewModel.getLiveDataFilteredDivisions().observe(this, list -> {
                    if (isVisible()) {
                        listFilteredDivisions = list;

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

    @Override
    public void onResume() {
        Logger.d("divisions onResume");
        super.onResume();
    }


    public void refresh() {
        viewDataBinding.swipeContainer.setRefreshing(false);
        report.set(new Revenue());

        super.getReportPost(new Revenue(), viewModelRep, getParams(), new RequestListener.ObjectCallback<Revenue>() {
            @Override
            public void onComplete(Revenue response) {
                if (response == null)
                    response = new Revenue();

                response.setState(ReportState.IDLE);
                report.set(response);
                show();
            }

            @Override
            public void onError(HttpError e) {
                Logger.e("Error HttpError: " + e.getDescription());
                Toast.makeText(mContext, e.getDescription(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private Parameters getParams() {

        return new Parameters(Report.ID.revenue,
                Formatter.getDateStringISO(filterDate.getDateFrom()),
                Formatter.getDateStringISO(filterDate.getDateToEnd()),
                Parameters.Granularity.day,
                listFilteredDivisions);
    }


    private void show() {

        if (report.get().getReportItems() != null) {

            LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
            viewDataBinding.recyclerView.setLayoutManager(llm);

            RevenueAdapter ra = new RevenueAdapter(report.get().getReportItems(), mContext);
            viewDataBinding.recyclerView.setAdapter(ra);

        } else
            Toast.makeText(mContext, R.string.TOAST_REPORT_NOTHING, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onPause() {
        super.onPause();
        Logger.d("divisions onPause");

        //cancelReQuests();
    }

    @Override
    public void onStop() {
        super.onStop();
        Logger.d("divisions onStop");

        cancelReQuests();
    }

    @Override
    public void onDestroyView() {
        Logger.d("divisions onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Logger.d("divisions onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Logger.d("divisions onDetach");
        super.onDetach();
    }

}
