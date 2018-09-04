package ru.coffeeset.cscl.reporter.models.accessList;

import android.os.Build;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class City implements IAccessItem, Comparable {
    private String ref;
    private String refCountry;
    private String name;
    private List<Division> divisionList;


    @Override
    public int getType() {
        return ItemType.TYPE_CITY;
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

    public List<Division> getDivisionList() {
        return divisionList;
    }



//get list of childs

    public List<IAccessItem> getOrganizations(List<IAccessItem> list) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return list.stream().filter((i) -> ((Organization) i).isChildFor(this)).collect(Collectors.toList());
        } else {
            List<IAccessItem> newList = new ArrayList<>();
            for (IAccessItem i : list) {
                if (((Organization) i).isChildFor(this))
                    newList.add(i);
            }
            return newList;
        }
    }

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


// boolean

    public boolean isChildFor(Country country){
        return getRefParent().equals(country.getRef());
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return name.toLowerCase().compareTo(((City) o).getName().toLowerCase());
    }
}