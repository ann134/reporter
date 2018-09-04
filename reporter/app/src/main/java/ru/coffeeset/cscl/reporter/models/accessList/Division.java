package ru.coffeeset.cscl.reporter.models.accessList;

import android.os.Build;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ru.coffeeset.cscl.reporter.controllers.repo.MainRepository;

public class Division implements IAccessItem, Comparable {
    private String ref;
    private String name;
    private String refOrganization;
    private String refCity;

    private int format;
    private int property;

    private boolean closed;


    @Override
    public int getType() {
        return ItemType.TYPE_DIVISION;
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
        return refCity.toLowerCase();
    }

    public String getRefCity() {
        return refCity.toLowerCase();
    }

    public String getRefOrganization() {
        return refOrganization.toLowerCase();
    }

    public int getFormat() {
        return format;
    }

    public int getProperty() {
        return property;
    }

    public boolean getClosed() {
        return closed;
    }



// static

    public static List<Division> getListDivisions (List<IAccessItem> list){

        List<Division> newList = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            list.stream().forEach((i) -> newList.add((Division) i));
        } else {
            for (IAccessItem i : list) {
                    newList.add((Division)i);
            }
        }

        return newList;
    }

    public static List<IAccessItem> filterClosed (List<IAccessItem> list){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return list.stream().filter((i) -> !(((Division) i).isClosed()) ).collect(Collectors.toList());
        } else {
            List<IAccessItem> newList = new ArrayList<>();
            for (IAccessItem i : list) {
                if (!((Division) i).isClosed())
                    newList.add(i);
            }
            return newList;
        }
    }

    public static List<IAccessItem> filterByFormat (int format, List<IAccessItem> list){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return list.stream().filter((i) -> !(((Division) i).isFormat(format)) ).collect(Collectors.toList());
        } else {
            List<IAccessItem> newList = new ArrayList<>();
            for (IAccessItem i : list) {
                if (!((Division) i).isFormat(format))
                    newList.add(i);
            }
            return newList;
        }
    }

    public static List<IAccessItem> filterByProperty (int property, List<IAccessItem> list){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return list.stream().filter((i) -> !(((Division) i).isProperty(property)) ).collect(Collectors.toList());
        } else {
            List<IAccessItem> newList = new ArrayList<>();
            for (IAccessItem i : list) {
                if (!((Division) i).isProperty(property))
                    newList.add(i);
            }
            return newList;
        }
    }



//get list of one division?

    public List<IAccessItem> getDivision(List<IAccessItem> list) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return list.stream().filter((i) -> i.getRef().toLowerCase().equals(this.ref.toLowerCase())).collect(Collectors.toList());
        } else {
            List<IAccessItem> newList = new ArrayList<>();
            for (IAccessItem i : list) {
                if (i.getRef().toLowerCase().equals(this.ref.toLowerCase()))
                    newList.add(i);
            }
            return newList;
        }
    }



//boolean

    public boolean isChildFor(City city) {
        return getRefCity().equals(city.getRef());
    }

    public boolean isChildFor(Organization organization) {
        return getRefOrganization().equals(organization.getRef());
    }

    public boolean isChildFor(Country country) {
        for (City c : MainRepository.users().getUser().getAccess().getCities()) {
            if (getRefCity().equals(c.getRef()) && c.getRefParent().equals(country.getRef()))
                return true;
        }
        return false;
    }


    public boolean isFormat(int format){
        return this.format == format;
    }

    public boolean isProperty(int property){
        return this.property == property;
    }

    public boolean isClosed(){
       return closed;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return name.toLowerCase().compareTo(((Division) o).getName().toLowerCase());
    }

}