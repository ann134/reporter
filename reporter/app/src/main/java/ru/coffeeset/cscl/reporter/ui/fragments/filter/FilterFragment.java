package ru.coffeeset.cscl.reporter.ui.fragments.filter;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.coffeeset.cscl.reporter.R;
import ru.coffeeset.cscl.reporter.controllers.adapters.FilterAdapter;
import ru.coffeeset.cscl.reporter.controllers.utils.Logger;
import ru.coffeeset.cscl.reporter.controllers.utils.SupportAnimation;
import ru.coffeeset.cscl.reporter.databinding.FragmentFilterBinding;
import ru.coffeeset.cscl.reporter.models.accessList.Format;
import ru.coffeeset.cscl.reporter.models.accessList.Property;
import ru.coffeeset.cscl.reporter.ui.MainActivity;
import ru.coffeeset.cscl.reporter.ui.viewmodels.AccessListViewModel;


public class FilterFragment extends BaseFilterFragment implements View.OnClickListener {


    private FragmentFilterBinding viewDataBinding;
    private Context mContext;
    private ISupportActionBarListener mListener;

    private FilterAdapter fa;
    private AccessListViewModel viewModel;


    public static FilterFragment newInstance() {
        return new FilterFragment();
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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.viewDataBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_filter,
                container,
                false);
        viewDataBinding.setFilter(viewModel.getFilter());
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
            mListener.onTitleSet(R.string.STR_ACTION_FILTER);
            mListener.onHomeButtonSet(false);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        Logger.d("onStart Filter");

        FilterAdapter.OnAccessClickListener listener = this::onAccessClick;

        viewDataBinding.toggleButtonCoffeeshop.setOnClickListener(this::onFormatClick);
        viewDataBinding.toggleButtonEspressit.setOnClickListener(this::onFormatClick);

        viewDataBinding.toggleButtonMaster.setOnClickListener(this::onPropertyClick);
        viewDataBinding.toggleButtonFranchise.setOnClickListener(this::onPropertyClick);
        viewDataBinding.toggleButtonSpv.setOnClickListener(this::onPropertyClick);
        viewDataBinding.toggleButtonGovernet.setOnClickListener(this::onPropertyClick);

        viewDataBinding.setDefault.setOnClickListener(this);
        viewDataBinding.setSaved.setOnClickListener(this);
        viewDataBinding.apply.setOnClickListener(this);


        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        viewDataBinding.recyclerView.setLayoutManager(llm);

        fa = new FilterAdapter(listener);
        viewDataBinding.recyclerView.setAdapter(fa);


        viewModel.getLiveDataSelectedElements().observe(this, list ->
                fa.setSelectedElements(list)
        );
    }


    public void onAccessClick(int i) {
        replaceChildFragmentWith(mContext,
                getFragmentManager(),
                getParentContainer(this.getView()),
                SelectFragment.newInstance(i),
                SupportAnimation.FROM_LEFT_TO_RIGHT,
                true);
    }

    public void onFormatClick(View v) {
        if (v.equals(viewDataBinding.toggleButtonCoffeeshop)) {
            viewModel.setFilterByFormat(Format.COFFEESHOP, viewDataBinding.toggleButtonCoffeeshop.isChecked());
            return;
        }
        if (v.equals(viewDataBinding.toggleButtonEspressit)) {
            viewModel.setFilterByFormat(Format.ESPRESSIT, viewDataBinding.toggleButtonEspressit.isChecked());
        }
    }

    public void onPropertyClick(View v) {
        if (v.equals(viewDataBinding.toggleButtonMaster)) {
            viewModel.setFilterByProperty(Property.MASTER, viewDataBinding.toggleButtonMaster.isChecked());
            return;
        }
        if (v.equals(viewDataBinding.toggleButtonFranchise)) {
            viewModel.setFilterByProperty(Property.FRANCHISE, viewDataBinding.toggleButtonFranchise.isChecked());
            return;
        }
        if (v.equals(viewDataBinding.toggleButtonSpv)) {
            viewModel.setFilterByProperty(Property.SPV, viewDataBinding.toggleButtonSpv.isChecked());
            return;
        }
        if (v.equals(viewDataBinding.toggleButtonGovernet)) {
            viewModel.setFilterByProperty(Property.GOVERNET, viewDataBinding.toggleButtonGovernet.isChecked());
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(viewDataBinding.apply)) {
            viewModel.setFilter();
            ((MainActivity) mContext).onBackPressed();
            return;
        }

        if (v.equals(viewDataBinding.setDefault)) {
            viewModel.setDefaultFilter();
            viewDataBinding.setFilter(viewModel.getFilter());
            return;
        }

        if (v.equals(viewDataBinding.setSaved)) {

        }
    }

}
