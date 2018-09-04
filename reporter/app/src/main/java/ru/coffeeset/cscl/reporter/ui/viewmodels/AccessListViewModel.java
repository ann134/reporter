package ru.coffeeset.cscl.reporter.ui.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Build;
import android.util.SparseArray;

import java.util.List;

import ru.coffeeset.cscl.reporter.controllers.repo.MainRepository;
import ru.coffeeset.cscl.reporter.controllers.utils.FilterSelector;
import ru.coffeeset.cscl.reporter.controllers.utils.Logger;
import ru.coffeeset.cscl.reporter.models.DateEntry;
import ru.coffeeset.cscl.reporter.models.DateFilter;
import ru.coffeeset.cscl.reporter.models.Filter;
import ru.coffeeset.cscl.reporter.models.accessList.City;
import ru.coffeeset.cscl.reporter.models.accessList.Division;
import ru.coffeeset.cscl.reporter.models.accessList.IAccessItem;
import ru.coffeeset.cscl.reporter.models.accessList.ItemType;
import ru.coffeeset.cscl.reporter.models.accessList.Organization;


public class AccessListViewModel extends ViewModel {

    private Filter filter;
    private SparseArray<IAccessItem> selectedElementsArray;

    private MutableLiveData<List<Division>> liveDataFilteredDivisions = new MutableLiveData<>();
    private MutableLiveData<DateEntry> liveDataDate = new MutableLiveData<>();
    private MutableLiveData<SparseArray<IAccessItem>> liveDataSelectedElements = new MutableLiveData<>();


//===================================================================================

    // куда это деть????
    private boolean animation = true;

    public boolean getAnimation() {
        return animation;
    }

    public void setAnimation(boolean b) {
        animation = b;
    }

//===================================================================================


    public DateEntry getDateFilter() {
        return MainRepository.users().getUser().getAccess().getDateFilter().getDate();
    }

    public Filter getFilter() {
        if (filter == null)
            loadFilter();
        return filter;
    }


    private SparseArray<IAccessItem> getSelectedElementsArray() {
        if (selectedElementsArray == null)
            createSelectedElementsArray();

        return selectedElementsArray;
    }

    private void createSelectedElementsArray() {
        if (filter == null)
            loadFilter();

        SparseArray<IAccessItem> map = new SparseArray<>();
        map.append(ItemType.TYPE_COUNTRY, filter.getByCountry());
        map.append(ItemType.TYPE_ORGANIZATION, filter.getByOrganization());
        map.append(ItemType.TYPE_CITY, filter.getByCity());
        map.append(ItemType.TYPE_DIVISION, filter.getByDivision());
        this.selectedElementsArray = map;
    }

    private void loadFilter() {
        filter = new Filter(MainRepository.users().getUser().getAccess().getFilter());
        Logger.d(filter.getByFormat()[0] + " "  +filter.getByFormat()[1]);
    }




    public void putElement(int type, IAccessItem element) {

        filter.setFieldByType(type, element);
        selectedElementsArray.append(type, element);

        for (int i = type + 1; i < 4; i++) {
            if (!selectList(i).contains(selectedElementsArray.get(i))) {
                filter.setFieldByType(i, null);
                selectedElementsArray.append(i, null);
            }
        }

        if (type == ItemType.TYPE_DIVISION && element!=null && selectedElementsArray.get(ItemType.TYPE_ORGANIZATION) == null) {
            Organization org = (Organization)MainRepository.users().getUser().getAccess().findByRef(((Division) element).getRefOrganization());
            filter.setFieldByType(ItemType.TYPE_ORGANIZATION, org);
            selectedElementsArray.append(ItemType.TYPE_ORGANIZATION, org);
        }

        if (type == ItemType.TYPE_DIVISION && element!=null  && selectedElementsArray.get(ItemType.TYPE_CITY) == null) {
            City city = (City) MainRepository.users().getUser().getAccess().findByRef(((Division) element).getRefCity());
            filter.setFieldByType(ItemType.TYPE_CITY, city);
            selectedElementsArray.append(ItemType.TYPE_CITY, city);
        }

        setLiveDataSelectedElements();
    }

    public void setFilterByFormat(int format, boolean checked){
        Logger.d(format+ "  "+ checked);
        filter.setByFormat(format, checked);
    }

    public void setFilterByProperty(int property, boolean checked){
        filter.setByProperty(property, checked);
    }


    public void setDefaultFilter() {
        filter = new Filter();
        createSelectedElementsArray();

        setLiveDataSelectedElements();
    }



    public void setFilter() {
        MainRepository.users().getUser().getAccess().setFilter(new Filter(filter));
        MainRepository.users().getUser().getAccess().setFilteredDivisions();

        setLiveDataFilteredDivisions();
    }

    public void setDateFilter(DateEntry date) {
        DateFilter newDateFilter = new DateFilter();
        newDateFilter.setDate(date);
        MainRepository.users().getUser().getAccess().setDateFilter(newDateFilter);

        setLiveDataDate();
    }




    public List<IAccessItem> selectList(int type) {
        Logger.d("select List for filter select");
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            return FilterSelector.selectListForNew(type, filter);
        else
            return FilterSelector.selectListForOld(type, filter);*/

        return FilterSelector.selectList(type, filter);

    }


    public void clearUnsavedChanges() {
        Logger.d("clearUnsavedChanges");
        loadFilter();
        createSelectedElementsArray();

        setLiveDataSelectedElements();
    }


//==================================================================================================================

    public LiveData<List<Division>> getLiveDataFilteredDivisions() {
        if (liveDataFilteredDivisions.getValue() == null)
            setLiveDataFilteredDivisions();
        return liveDataFilteredDivisions;
    }

    public LiveData<DateEntry> getLiveDataDate() {
        if (liveDataDate.getValue() == null)
            setLiveDataDate();
        return liveDataDate;
    }

    public LiveData<SparseArray<IAccessItem>> getLiveDataSelectedElements() {
        if (liveDataSelectedElements.getValue() == null)
            setLiveDataSelectedElements();
        return liveDataSelectedElements;
    }


    private void setLiveDataFilteredDivisions() {
        liveDataFilteredDivisions.setValue(MainRepository.users().getUser().getAccess().getFilteredDivisions());
    }

    private void setLiveDataDate() {
        liveDataDate.setValue(MainRepository.users().getUser().getAccess().getDateFilter().getDate());
    }

    private void setLiveDataSelectedElements() {
        liveDataSelectedElements.setValue(getSelectedElementsArray());
    }

}
