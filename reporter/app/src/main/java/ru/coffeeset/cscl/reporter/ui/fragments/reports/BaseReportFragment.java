package ru.coffeeset.cscl.reporter.ui.fragments.reports;

import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.json.JSONException;
import org.json.JSONObject;

import ru.coffeeset.cscl.reporter.R;
import ru.coffeeset.cscl.reporter.controllers.ReporterConfig;
import ru.coffeeset.cscl.reporter.controllers.network.HttpError;
import ru.coffeeset.cscl.reporter.controllers.network.NetworkController;
import ru.coffeeset.cscl.reporter.controllers.network.RequestListener;
import ru.coffeeset.cscl.reporter.controllers.utils.Logger;
import ru.coffeeset.cscl.reporter.models.reports.IReport;
import ru.coffeeset.cscl.reporter.models.reports.Parameters;
import ru.coffeeset.cscl.reporter.ui.fragments.BaseFragment;
import ru.coffeeset.cscl.reporter.ui.fragments.dashboard.DashboardNavigatorFragment;
import ru.coffeeset.cscl.reporter.ui.fragments.reportsList.ReportsNavigatorFragment;
import ru.coffeeset.cscl.reporter.ui.viewmodels.ReportsViewModel;


public abstract class BaseReportFragment extends BaseFragment {

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.date).setVisible(true);
        menu.findItem(R.id.filter).setVisible(true);
        menu.findItem(R.id.refresh).setVisible(true);
        menu.findItem(R.id.search).setVisible(false);
        menu.findItem(R.id.close).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                Fragment fragment = getCurrentFragment();
                ((BaseReportFragment) fragment).refresh();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public Fragment getCurrentFragment() {
        Fragment f = getFragmentManager().findFragmentById(R.id.main_container);

        if (f instanceof DashboardNavigatorFragment) {
            f = getFragmentManager().findFragmentById(R.id.dashboard_nav_container);
        } else if (f instanceof ReportsNavigatorFragment)
            f = getFragmentManager().findFragmentById(R.id.reports_nav_container);

        return f;
    }


    public abstract void refresh();


    public <T extends IReport> void getReportPost(T rep, ReportsViewModel viewModel, Parameters p, RequestListener.ObjectCallback<T> handler) {

        JSONObject json = null;
        try {
            json = new JSONObject();
            json.put("parameters", p.getSerialize());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Logger.d("json" + json);


        viewModel.getReportPost(rep, json, new RequestListener.ObjectCallback<T>() {
            @Override
            public void onComplete(T response) {
                handler.onComplete(response);
            }

            @Override
            public void onError(HttpError e) {
                Logger.e("Error HttpError: " + e.getDescription());
            }
        });
    }


    public void cancelReQuests() {
        NetworkController.getInstance().cancelPendingRequests(ReporterConfig.HTTP_TAG);
    }

}
