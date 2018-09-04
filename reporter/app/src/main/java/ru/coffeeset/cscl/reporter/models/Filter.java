package ru.coffeeset.cscl.reporter.models;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import ru.coffeeset.cscl.reporter.controllers.repo.MainRepository;
import ru.coffeeset.cscl.reporter.controllers.utils.Logger;
import ru.coffeeset.cscl.reporter.models.accessList.IAccessItem;
import ru.coffeeset.cscl.reporter.models.accessList.City;
import ru.coffeeset.cscl.reporter.models.accessList.Country;
import ru.coffeeset.cscl.reporter.models.accessList.Division;
import ru.coffeeset.cscl.reporter.models.accessList.ItemType;
import ru.coffeeset.cscl.reporter.models.accessList.Organization;

public class Filter {

    private Country byCountry = startCountry();
    private City byCity;
    private Organization byOrganization;
    private Division byDivision;

    private boolean[] byFormat = {true, true};
    private boolean[] byProperty = {true, true, true, true};


    public Filter(){
    }

    public Filter(Filter filter){
        byCountry = filter.byCountry;
        byCity = filter.byCity;
        byOrganization = filter.byOrganization;
        byDivision = filter.byDivision;
        System.arraycopy(filter.byFormat, 0, byFormat, 0, 2);
        System.arraycopy(filter.byProperty, 0, byProperty, 0, 4);
    }


    private Country startCountry() {
        Logger.d("find start country");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            return startCountryForNew();
        else
            return startCountryForOld();
    }


    public <T extends IAccessItem> T getFieldByType(int i) {
        if (i == ItemType.TYPE_COUNTRY)
            return (T) byCountry;
        if (i == ItemType.TYPE_CITY)
            return (T) byCity;
        if (i == ItemType.TYPE_ORGANIZATION)
            return (T) byOrganization;
        if (i == ItemType.TYPE_DIVISION)
            return (T) byDivision;
        return null;
    }

    public boolean[] getByFormat(){
        return byFormat;
    }

    public boolean[] getByProperty(){
        return byProperty;
    }


    public void setByFormat(int format, boolean checked){
        Logger.d(byFormat[0]+" "+byFormat[1]);
        byFormat[format] = checked;
        Logger.d(byFormat[0]+" "+byFormat[1]);
    }

    public void setByProperty(int property, boolean checked){
        byProperty[property] = checked;
    }

    public <T extends IAccessItem> void setFieldByType(int type, T element) {

        switch (type) {
            case (ItemType.TYPE_COUNTRY):
                byCountry = (Country) element;
                return;
            case (ItemType.TYPE_CITY):
                byCity = (City) element;
                return;
            case (ItemType.TYPE_ORGANIZATION):
                byOrganization = (Organization) element;
                return;
            case (ItemType.TYPE_DIVISION):
                byDivision = (Division) element;
                return;
            default:
                return;
        }
    }



    public Country getByCountry() {
        return byCountry;
    }

    public City getByCity() {
        return byCity;
    }

    public Organization getByOrganization() {
        return byOrganization;
    }

    public Division getByDivision() {
        return byDivision;
    }




//find start country

    private Country startCountryForOld() {

        HashMap<Country, List<IAccessItem>> lists = new HashMap<>();
        for (IAccessItem c : MainRepository.users().getUser().getAccess().getElementsListByType(ItemType.TYPE_COUNTRY)) {
            for (IAccessItem o : MainRepository.users().getUser().getAccess().getElementsListByType(ItemType.TYPE_ORGANIZATION)) {
                if (((Organization) o).isChildFor((Country) c)) {
                    if (!lists.containsKey(c))
                        lists.put((Country) c, new ArrayList<>());
                    lists.get(c).add(o);
                }
            }
        }

        return getCountryWithMaxChild(lists);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Country startCountryForNew() {

        List<IAccessItem> organizations = MainRepository.users().getUser().getAccess().getElementsListByType(ItemType.TYPE_ORGANIZATION);
        HashMap<Country, List<IAccessItem>> lists = new HashMap<>();

        for (IAccessItem c : MainRepository.users().getUser().getAccess().getElementsListByType(ItemType.TYPE_COUNTRY)) {
            lists.put((Country) c, new ArrayList(organizations.stream().filter((o) -> ((Organization) o).isChildFor((Country) c)).collect(Collectors.toList())));
        }

        return getCountryWithMaxChild(lists);
    }

    private Country getCountryWithMaxChild(HashMap<Country, List<IAccessItem>> lists) {
        int max = 0;
        Country maxC = (Country) MainRepository.users().getUser().getAccess().getElementsListByType(ItemType.TYPE_COUNTRY).get(0);
        for (Country c : lists.keySet()) {
            if (lists.get(c).size() > max) {
                max = lists.get(c).size();
                maxC = c;
            }
        }
        return maxC;
    }

}
