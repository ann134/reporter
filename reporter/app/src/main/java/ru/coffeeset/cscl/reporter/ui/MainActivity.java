package ru.coffeeset.cscl.reporter.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import java.util.ArrayList;
import java.util.List;

import ru.coffeeset.cscl.reporter.R;
import ru.coffeeset.cscl.reporter.controllers.utils.Helper;
import ru.coffeeset.cscl.reporter.controllers.utils.SupportAnimation;
import ru.coffeeset.cscl.reporter.ui.fragments.BaseFragment;
import ru.coffeeset.cscl.reporter.ui.fragments.dashboard.DashboardNavigatorFragment;
import ru.coffeeset.cscl.reporter.ui.fragments.filter.DateFragment;
import ru.coffeeset.cscl.reporter.ui.fragments.filter.FilterFragment;
import ru.coffeeset.cscl.reporter.ui.fragments.reportsList.ReportsNavigatorFragment;
import ru.coffeeset.cscl.reporter.ui.fragments.settings.SettingsNavigatorFragment;
import ru.coffeeset.cscl.reporter.ui.viewmodels.AccessListViewModel;


public class MainActivity extends BaseActivity {

    private Boolean isActionBarWasVisible = false;
    private AHBottomNavigation bottomNavBar;
    private final String STATE_NAVBAR = "STATE_NAVBAR";
    private Handler mBackPressedHandler = new Handler();
    private boolean doubleBackToExitPressedOnce;
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            showBars();
        }

        int position = 0;
        if (savedInstanceState != null) {
            position = savedInstanceState.getInt(STATE_NAVBAR);
        }
        initBottomBar(position);
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(STATE_NAVBAR, bottomNavBar.getCurrentItem());
        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBackPressedHandler != null) {
            mBackPressedHandler.removeCallbacks(mRunnable);
        }
    }


    void initBottomBar(int initPosition) {
        bottomNavBar = findViewById(R.id.navigation);

        AHBottomNavigationItem itemDashboard = new AHBottomNavigationItem(R.string.STR_NAVIGATION_DASHBOARD,
                R.drawable.ic_bar_dashboard,
                R.color.navBareActive);
        AHBottomNavigationItem itemReports = new AHBottomNavigationItem(R.string.STR_NAVIGATION_REPORTS,
                R.drawable.ic_bar_reports,
                R.color.navBareActive);
        AHBottomNavigationItem itemSettings = new AHBottomNavigationItem(R.string.STR_NAVIGATION_SETTINGS,
                R.drawable.ic_bar_settings,
                R.color.navBareActive);

        bottomNavBar.addItem(itemDashboard);
        bottomNavBar.addItem(itemReports);
        bottomNavBar.addItem(itemSettings);


        bottomNavBar.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);

        bottomNavBar.setDefaultBackgroundColor(Helper.Utils.getColor(R.color.navBare));
        bottomNavBar.setAccentColor(Helper.Utils.getColor(R.color.navBareActive));
        bottomNavBar.setInactiveColor(Helper.Utils.getColor(R.color.navBareInactive));

        bottomNavBar.setOnTabSelectedListener((position, wasSelected) -> {
            if (!wasSelected)
                setCurrentFragment(position);
            return true;
        });

        bottomNavBar.setCurrentItem(initPosition);
        setCurrentFragment(initPosition);
    }


    void setCurrentFragment(int position) {
        Fragment selectedFragment = getFragmentByPosition(position);
        FrameLayout container = findViewById(R.id.main_container);
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        setFragmentToContainer(fm, selectedFragment, container.getId(), false, true);
    }

    Fragment getFragmentByPosition(int position) {
        List<Fragment> bottomMenu = new ArrayList<>();
        bottomMenu.add(DashboardNavigatorFragment.newInstance());
        bottomMenu.add(ReportsNavigatorFragment.newInstance());
        bottomMenu.add(SettingsNavigatorFragment.newInstance());

        return bottomMenu.get(position);
    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager()
                .getBackStackEntryCount() > 0) {
            this.doubleBackToExitPressedOnce = true;
        }
        if (doubleBackToExitPressedOnce) {
            if (getSupportFragmentManager().getFragments().contains(getSupportFragmentManager().findFragmentByTag(FilterFragment.class.getSimpleName())) ||
                    getSupportFragmentManager().getFragments().contains(getSupportFragmentManager().findFragmentByTag(DateFragment.class.getSimpleName()))) {
                closeFilter();
                return;
            }
            super.onBackPressed();
            doubleBackToExitPressedOnce = false;
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.TOAST_PRESS_TWICE_TO_EXIT, Toast.LENGTH_SHORT).show();
        mBackPressedHandler.postDelayed(mRunnable, 2000);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.filter:
                showFilter(FilterFragment.newInstance());
                return true;

            case R.id.date:
                showFilter(DateFragment.newInstance());
                return true;

            case R.id.close:
                closeFilter();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void showFilter(Fragment fragment) {
        replaceCurrentFragmentWith(getSupportFragmentManager(), R.id.filter_container,
                fragment,
                SupportAnimation.FROM_UP_TO_DOWN, true);
    }


    public void closeFilter() {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.filter_container);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        setAnimationToTransaction(transaction, SupportAnimation.FROM_UP_TO_DOWN);
        transaction.remove(f)
                .commit();
        f.onDetach();
        ((BaseFragment) getCurrentFragment()).refreshActionBar();

        AccessListViewModel viewModel = ViewModelProviders.of(this).get(AccessListViewModel.class);
        viewModel.clearUnsavedChanges();

        //super.onBackPressed();
    }


    public Fragment getCurrentFragment() {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.main_container);

        if (f instanceof DashboardNavigatorFragment)
            f = getSupportFragmentManager().findFragmentById(R.id.dashboard_nav_container);
        else if (f instanceof ReportsNavigatorFragment)
            f = getSupportFragmentManager().findFragmentById(R.id.reports_nav_container);
        else if (f instanceof SettingsNavigatorFragment)
            f = getSupportFragmentManager().findFragmentById(R.id.settings_nav_container);

        return f;
    }


    public void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        this.startActivity(intent);
        this.finish();
    }


    public void selectBottonItem(int position) {
        bottomNavBar.setCurrentItem(position);
    }


    public void setActionBarIsVisible(Boolean visible) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) return;
        if (visible) {
            actionBar.show();
        } else {
            actionBar.hide();
        }
    }

    public void showBars() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) return;
        if (isActionBarWasVisible) {
            actionBar.show();
        }
        findViewById(R.id.navigation).setVisibility(View.VISIBLE);
    }

    public void hideBars() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) return;
        isActionBarWasVisible = actionBar.isShowing();
        actionBar.hide();
        findViewById(R.id.navigation).setVisibility(View.GONE);
    }
}


