package ru.coffeeset.cscl.reporter.ui.fragments.filter;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ru.coffeeset.cscl.reporter.R;
import ru.coffeeset.cscl.reporter.controllers.adapters.SelectionAdapter;
import ru.coffeeset.cscl.reporter.databinding.FragmentFilterSelectionBinding;
import ru.coffeeset.cscl.reporter.models.accessList.IAccessItem;
import ru.coffeeset.cscl.reporter.models.accessList.ItemType;
import ru.coffeeset.cscl.reporter.ui.MainActivity;
import ru.coffeeset.cscl.reporter.ui.viewmodels.AccessListViewModel;


public class SelectFragment extends BaseFilterFragment implements SearchView.OnQueryTextListener {

    private int type;
    private Context mContext;
    private ISupportActionBarListener mListener;
    private FragmentFilterSelectionBinding viewDataBinding;
    private AccessListViewModel viewModel;

    private List<IAccessItem> list;


    public static SelectFragment newInstance(int type) {
        SelectFragment f = new SelectFragment();
        Bundle args = new Bundle();
        args.putInt("bundle_content", type);
        f.setArguments(args);

        return f;
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
        if (getArguments() != null && getArguments().containsKey("bundle_content")) {
            type = getArguments().getInt("bundle_content");
        }

        viewModel = ViewModelProviders.of((MainActivity) mContext).get(AccessListViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.viewDataBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_filter_selection,
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
            //mListener.onTitleSet(R.string.STR_ACTION_DETAILS);
            mListener.onHomeButtonSet(true);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.search).setVisible(true);
        ((SearchView) menu.findItem(R.id.search).getActionView()).setOnQueryTextListener(this);
    }


    @Override
    public boolean onQueryTextChange(String userText) {
        search(userText);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }


//======

    @Override
    public void onStart() {
        super.onStart();

        loadList();
        refresh(list);
    }


    @NonNull
    private void loadList() {
        list = new ArrayList<>();
        if (type != ItemType.TYPE_COUNTRY)
            list.add(null);

        list.addAll(viewModel.selectList(type));
    }


    private void refresh(List<IAccessItem> list) {
        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        viewDataBinding.recyclerView.setLayoutManager(llm);

        SelectionAdapter.OnItemClickListener listener = this::onItemClick;

        SelectionAdapter fa = new SelectionAdapter(list, listener, mContext);
        viewDataBinding.recyclerView.setAdapter(fa);
    }


//=====  ТУТ ПЕРЕБОР СПИСКА!!!!

    private void search(String userText) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            searchForNew(userText);
        else
            searchForOld(userText);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void searchForNew(String userText) {
        List<IAccessItem> newList = list.stream().filter((i) -> i != null ? i.getName().toLowerCase().contains(userText.toLowerCase()) : false).collect(Collectors.toList());
        refresh(newList);
    }

    private void searchForOld(String userText) {
        List<IAccessItem> newList = new ArrayList<>();

        for (IAccessItem i : list) {
            if (i != null ? i.getName().toLowerCase().contains(userText.toLowerCase()) : false)
                newList.add(i);
        }
        refresh(newList);
    }



    public void onItemClick(IAccessItem element) {
        viewModel.putElement(type, element);
        ((MainActivity) mContext).onBackPressed();
    }

}
