package ru.coffeeset.cscl.reporter.ui.fragments.settings;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;

import ru.coffeeset.cscl.reporter.R;
import ru.coffeeset.cscl.reporter.controllers.adapters.FilterAdapter;
import ru.coffeeset.cscl.reporter.controllers.repo.MainRepository;
import ru.coffeeset.cscl.reporter.controllers.utils.Formatter;
import ru.coffeeset.cscl.reporter.controllers.utils.Logger;
import ru.coffeeset.cscl.reporter.controllers.utils.SupportAnimation;
import ru.coffeeset.cscl.reporter.databinding.FragmentSettingsBinding;
import ru.coffeeset.cscl.reporter.models.DateEntry;
import ru.coffeeset.cscl.reporter.ui.MainActivity;
import ru.coffeeset.cscl.reporter.ui.fragments.BaseFragment;
import ru.coffeeset.cscl.reporter.ui.fragments.filter.DateFragment;
import ru.coffeeset.cscl.reporter.ui.fragments.filter.FilterFragment;
import ru.coffeeset.cscl.reporter.ui.viewmodels.AccessListViewModel;


public class SettingsFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private FragmentSettingsBinding viewDataBinding;
    private Context mContext;
    private ISupportActionBarListener mListener;

    private FilterAdapter fa;
    private AccessListViewModel viewModel;

    private DateEntry dateFilter;


    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Logger.d("settings onCreate");
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
        Logger.d("settings onCreate");
        viewModel = ViewModelProviders.of((MainActivity) mContext).get(AccessListViewModel.class);
        dateFilter = viewModel.getDateFilter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Logger.d("settings onCreateView");
        this.viewDataBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_settings,
                container,
                false);
        return viewDataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Logger.d("settings onActivityCreated");

        refreshActionBar();
    }

    @Override
    public void refreshActionBar() {
        if (isVisible()) {
            mListener.onTitleSet(R.string.STR_ACTION_SETTINGS);
            mListener.onHomeButtonSet(false);
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.date).setVisible(false);
        menu.findItem(R.id.filter).setVisible(false);
        menu.findItem(R.id.refresh).setVisible(false);
        menu.findItem(R.id.search).setVisible(false);
        menu.findItem(R.id.close).setVisible(false);
    }

    @Override
    public void onStart() {
        super.onStart();

        viewDataBinding.version.setText(getString(R.string.STR_SETTINGS_VERSION, "???"));
        viewDataBinding.username.setText(getString(R.string.STR_SETTINGS_USER, MainRepository.users().getUser().getLogin()));
        viewDataBinding.logout.setOnClickListener((v) -> createDialog());

        viewDataBinding.date.setText(getString(R.string.STR_SETTINGS_DATE,
                Formatter.getDateStringWithMonth(dateFilter.getDateToNow())));
        viewDataBinding.date.setOnClickListener(this);
        viewDataBinding.filter.setText(getString(R.string.STR_ACTION_FILTER));
        viewDataBinding.filter.setOnClickListener(this);

        viewDataBinding.animation.setText(R.string.STR_SETTINGS_ANIMATION);
        viewDataBinding.switcher.setChecked(viewModel.getAnimation());
        viewDataBinding.switcher.setOnCheckedChangeListener(this);

    }

    @Override
    public void onResume() {
        Logger.d("settings onResume");
        super.onResume();
    }


    @Override
    public void onClick(View view) {
        if (view.equals(viewDataBinding.date)) {
            showFragment(DateFragment.newInstance());
            return;
        }

        if (view.equals(viewDataBinding.filter)) {
            showFragment(FilterFragment.newInstance());
            return;
        }
    }

    public void showFragment(Fragment fragment) {
        replaceChildFragmentWith(mContext,
                getFragmentManager(),
                getParentContainer(this.getView()),
                fragment,
                SupportAnimation.FROM_LEFT_TO_RIGHT,
                true);
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        viewModel.setAnimation(b);
    }


    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.STR_DIALOG_TITTLE)
                .setPositiveButton(R.string.STR_DIALOG_POSITIVE,
                        (dialog, which) -> doLogout())
                .setNegativeButton(R.string.STR_DIALOG_NEGATIVE,
                        (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
        Button buttonPositive = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        buttonPositive.setTextColor(ContextCompat.getColor(mContext, R.color.textAccent));
        Button buttonNegative = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        buttonNegative.setTextColor(ContextCompat.getColor(mContext, R.color.textAccent));
    }

    public void doLogout() {
        MainRepository.users().deleteUser();
        ((MainActivity) mContext).startLoginActivity();
    }




    @Override
    public void onPause() {
        super.onPause();
        Logger.d("settings onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Logger.d("settings onStop");
    }

    @Override
    public void onDestroyView() {
        Logger.d("settings onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Logger.d("settings onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Logger.d("settings onDetach");
        super.onDetach();
    }



}
