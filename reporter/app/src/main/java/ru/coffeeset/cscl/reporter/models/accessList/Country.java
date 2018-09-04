package ru.coffeeset.cscl.reporter.models.accessList;

import android.os.Build;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Country implements IAccessItem, Comparable {
    private String ref;
    private String refCurrency;
    private String name;
    private List<Organization> organizationList;


    @Override
    public int getType() {
        return ItemType.TYPE_COUNTRY;
    }

    @Override
    public String getRef() {
        return ref;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getRefParent() {
        return refCurrency;
    }

    public List<Organization> getOrganizationList() {
        return organizationList;
    }



//get list of childs

    public List<IAccessItem> getCities(List<IAccessItem> list) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return list.stream().filter((i) -> ((City) i).isChildFor(this)).collect(Collectors.toList());
        } else {
            List<IAccessItem> newList = new ArrayList<>();
            for (IAccessItem i : list) {
                if (((City) i).isChildFor(this))
                    newList.add(i);
            }
            return newList;
        }
    }

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

    @Override
    public int compareTo(@NonNull Object o) {
        return name.toLowerCase().compareTo(((Country) o).getName().toLowerCase());
    }
}
