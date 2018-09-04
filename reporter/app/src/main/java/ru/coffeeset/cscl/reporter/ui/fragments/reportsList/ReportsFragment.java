package ru.coffeeset.cscl.reporter.ui.fragments.reportsList;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.coffeeset.cscl.reporter.R;
import ru.coffeeset.cscl.reporter.controllers.adapters.ReportsAdapter;
import ru.coffeeset.cscl.reporter.controllers.utils.Logger;
import ru.coffeeset.cscl.reporter.databinding.FragmentReportsBinding;
import ru.coffeeset.cscl.reporter.models.reports.Report;
import ru.coffeeset.cscl.reporter.ui.fragments.BaseFragment;
import ru.coffeeset.cscl.reporter.ui.fragments.reports.CardReportFragment;
import ru.coffeeset.cscl.reporter.ui.fragments.reports.DivisionsReportFragment;
import ru.coffeeset.cscl.reporter.ui.fragments.reports.GoldenReportFragment;
import ru.coffeeset.cscl.reporter.ui.fragments.reports.SalesMixReportFragment;


public class ReportsFragment extends BaseFragment {

    private FragmentReportsBinding viewDataBinding;
    private Context mContext;
    private ISupportActionBarListener mListener;

    private List<Report> listOfReports;
    private ReportsTabFragment parent;


    public static ReportsFragment newInstance(List<Report> listOfReports) {
        ReportsFragment fragment = new ReportsFragment();
        fragment.setListOfReports(listOfReports);
        return fragment;
    }

    public void setListOfReports(List<Report> listOfReports) {
        this.listOfReports = listOfReports;
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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.viewDataBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_reports,
                container,
                false);

        parent = (ReportsTabFragment) getParentFragment();

        return viewDataBinding.getRoot();
    }


    /*@Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refreshActionBar();
    }

    @Override
    public void refreshActionBar() {
        if (isVisible()) {
            mListener.onTitleSet(R.string.STR_ACTION_REPORTS);
            mListener.onHomeButtonSet(false);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.date).setVisible(false);
        menu.findItem(R.id.filter).setVisible(false);
        menu.findItem(R.id.refresh).setVisible(false);
        menu.findItem(R.id.search).setVisible(false);
        menu.findItem(R.id.close).setVisible(false);
    }
*/

    @Override
    public void onStart() {
        super.onStart();
        Logger.d("reportlist start");

        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        viewDataBinding.recyclerView.setLayoutManager(llm);

        ReportsAdapter.OnReportClickListener listener = this::onReportClicked;

        ReportsAdapter ra = new ReportsAdapter(listOfReports, listener, mContext);
        viewDataBinding.recyclerView.setAdapter(ra);
    }


    public void onReportClicked(Report report) {
        switch (report.getPosition()) {
            case Report.Position.divisions:
                parent.showReport(DivisionsReportFragment.newInstance());
                break;
            case Report.Position.salesMix:
                parent.showReport(SalesMixReportFragment.newInstance());
                break;
            case Report.Position.golden:
                parent.showReport(GoldenReportFragment.newInstance());
            case Report.Position.card:
                parent.showReport(CardReportFragment.newInstance());
            default:
                return;
        }
    }


}



