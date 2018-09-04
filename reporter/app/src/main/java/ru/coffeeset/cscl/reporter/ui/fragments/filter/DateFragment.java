package ru.coffeeset.cscl.reporter.ui.fragments.filter;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.joda.time.DateTime;

import ru.coffeeset.cscl.reporter.R;
import ru.coffeeset.cscl.reporter.databinding.FragmentDateBinding;
import ru.coffeeset.cscl.reporter.models.DateEntry;
import ru.coffeeset.cscl.reporter.ui.MainActivity;
import ru.coffeeset.cscl.reporter.ui.viewmodels.AccessListViewModel;


public class DateFragment extends BaseFilterFragment implements View.OnClickListener {

    private FragmentDateBinding viewDataBinding;
    private Context mContext;
    private ISupportActionBarListener mListener;

    private AccessListViewModel viewModel;
    private DateEntry date;


    public static DateFragment newInstance() {
        return new DateFragment();
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
        viewModel = ViewModelProviders.of((MainActivity) mContext).get(AccessListViewModel.class);
        date = viewModel.getDateFilter();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.viewDataBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_date,
                container,
                false);
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
            mListener.onTitleSet(R.string.STR_ACTION_DATE);
            mListener.onHomeButtonSet(false);
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        viewDataBinding.apply.setOnClickListener(this);
        setDate();
    }


    private void setDate() {
        viewDataBinding.datePicker.init(
                date.getDateToNow().getYear(),
                date.getDateToNow().getMonthOfYear() - 1,
                date.getDateToNow().getDayOfMonth(),
                null);
    }


    @Override
    public void onClick(View v) {

        DateTime newDate = new DateTime(
                viewDataBinding.datePicker.getYear(),
                viewDataBinding.datePicker.getMonth() + 1,
                viewDataBinding.datePicker.getDayOfMonth(),
                0, 0, 0);

        viewModel.setDateFilter(new DateEntry(newDate));

        ((MainActivity) mContext).onBackPressed();
    }

}
