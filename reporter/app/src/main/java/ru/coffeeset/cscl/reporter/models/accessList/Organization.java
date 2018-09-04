package ru.coffeeset.cscl.reporter.models.accessList;

import android.os.Build;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ru.coffeeset.cscl.reporter.controllers.repo.MainRepository;

public class Organization implements IAccessItem, Comparable {
    private String ref;
    private String refCountry;
    private String name;
    private List<City> cityList;


    @Override
    public int getType() {
        return ItemType.TYPE_ORGANIZATION;
    }

    @Override
    public String getRef() {
        return ref.toLowerCase();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getRefParent() {
        return refCountry.toLowerCase();
    }

    public List<City> getCityList() {
        return cityList;
    }



// static

    public static List<IAccessItem> filterClosed(List<IAccessItem> list){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return list.stream().filter((i) -> !((Organization) i).isClosed()).collect(Collectors.toList());
        } else {
            List<IAccessItem> newList = new ArrayList<>();
            for (IAccessItem i : list) {
                if (!((Organization) i).isClosed())
                    newList.add(i);
            }
            return newList;
        }
    }


//get list of childs

    public List<IAccessItem> getDivisions(List<IAccessItem> list) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return list.stream().filter((i) -> ((Division) i).isChildFor(this)).collect(Collectors.toList());
        } else {
            List<IAccessItem> newList = new ArrayList<>();
            for (IAccessItem i : list) {
                if (((Division) i).isChildFor(this))
                    newList.add(i);
            }
            return newList;
        }
    }



//boolean

    public boolean isChildFor(Country country) {
        return getRefParent().equals(country.getRef());
    }

    public boolean isChildFor(City city) {
        for (Division d : MainRepository.users().getUser().getAccess().getDivisions()) {
            if (d.getRefOrganization().equals(getRef()) && d.getRefCity().equals(city.getRef()))
                return true;
        }
        return false;
    }

    public boolean isClosed() {
        for (City c : cityList) {
            for (Division d : c.getDivisionList())
                if (!d.isClosed())
                    return false;
        }
        return true;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return name.toLowerCase().compareTo(((Organization) o).getName().toLowerCase());
    }
}
