package ru.coffeeset.cscl.reporter.models.accessList;


import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import ru.coffeeset.cscl.reporter.controllers.utils.Logger;
import ru.coffeeset.cscl.reporter.models.DateFilter;
import ru.coffeeset.cscl.reporter.models.Filter;


public class AccessList {


    private static List<Currency> currencies;
    private static List<Country> countries;
    private static List<Organization> organizations;
    private static List<City> cities;
    private static List<Division> divisions;

    private static List<IAccessItem> allAccessItemsList;
    private static List<Division> filteredDivisions;


    private static Filter filter;
    private static DateFilter dateFilter;


//create
    public void createElementsLists(List<Currency> currencyList) {

        Logger.d("createElementsLists");

        allAccessItemsList = new ArrayList<>();
        currencies = new ArrayList<>();
        countries = new ArrayList<>();
        organizations = new ArrayList<>();
        cities = new ArrayList<>();
        divisions = new ArrayList<>();

        for (Currency currency : currencyList) {
            currencies.add(currency);
            allAccessItemsList.add(currency);
            for (Country country : currency.getCountryList()) {
                countries.add(country);
                allAccessItemsList.add(country);
                for (Organization organization : country.getOrganizationList()) {
                    organizations.add(organization);
                    allAccessItemsList.add(organization);
                    for (City city : organization.getCityList()) {
                        if (!isCityExist(city)) {
                            cities.add(city);
                            allAccessItemsList.add(city);
                        }
                        for (Division division : city.getDivisionList()) {
                            divisions.add(division);
                            allAccessItemsList.add(division);


                        }
                    }
                }
            }
        }

        sortLists();

    }

    private boolean isCityExist(City city) {
        for (City c : cities) {
            if (city.getRef().equals(c.getRef()))
                return true;
        }
        return false;
    }


//sort
    private void sortLists() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sortListsForNew();
        } else {
            sortListsForOld();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sortListsForNew() {
        countries.sort(Comparator.comparing(Country::getName));
        organizations.sort(Comparator.comparing(Organization::getName));
        cities.sort(Comparator.comparing(City::getName));
        divisions.sort(Comparator.comparing(Division::getName));
    }

    private void sortListsForOld() {  // for api < 24
        Collections.sort(countries);
        Collections.sort(organizations);
        Collections.sort(cities);
        Collections.sort(divisions);
    }


//delete
    public void deleteAccessLists() {
        //тут однажды был нулл пойнтер эксепшен
        currencies.clear();
        countries.clear();
        organizations.clear();
        cities.clear();
        divisions.clear();
        allAccessItemsList.clear();
        filteredDivisions.clear();
    }


//getters

    public List<Country> getCountries() {
        return countries;
    }

    public List<Organization> getOrganizations() {
        return organizations;
    }

    public List<City> getCities() {
        return cities;
    }

    public List<Division> getDivisions() {
        return divisions;
    }

    public <T extends IAccessItem> List<T> getElementsListByType(int type) {
        if (type == ItemType.TYPE_COUNTRY)
            return (List<T>) countries;
        if (type == ItemType.TYPE_CITY)
            return (List<T>) cities;
        if (type == ItemType.TYPE_ORGANIZATION)
            return (List<T>) organizations;
        if (type == ItemType.TYPE_DIVISION)
            return (List<T>) divisions;
        return new ArrayList<>();
    }


//filter
    public void setFilter(Filter filter) {
        AccessList.filter = filter;
    }

    public Filter getFilter() {
        if (filter==null)
            setFilter(new Filter());

        return filter;
    }

//dateFilter
    public void setDateFilter(DateFilter dateFilter) {
        AccessList.dateFilter = dateFilter;
    }

    public DateFilter getDateFilter() {
        if (dateFilter==null)
            setDateFilter(new DateFilter());

        return dateFilter;
    }

//FilteredDivisions
    public void setFilteredDivisions() {
        createFilteredDivisions();
    }

    public List<Division> getFilteredDivisions() {
        if (filteredDivisions == null)
            createFilteredDivisions();

        return filteredDivisions;
    }




    public IAccessItem findByRef(String ref) {
        for (IAccessItem item : allAccessItemsList) {
            if (ref.toLowerCase().equals(item.getRef().toLowerCase()))
                return item;
        }
        return null;
    }



//FILTERED ++++++++++++++++++++++++++++++++++


    private void createFilteredDivisions() {

        Logger.d("createFilteredDivisions");

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            filteredDivisions = getFilteredAccessList();
        } else {
            filteredDivisions = getFilteredAccessListForOld();
        }*/

        filteredDivisions = getFilteredAccessList();
    }

    private List<Division> getFilteredAccessList() {

        List<IAccessItem> list = new ArrayList<>(divisions);
        Filter f = getFilter();

        Country country = f.getByCountry();
        City city = f.getByCity();
        Organization organization = f.getByOrganization();
        Division division = f.getByDivision();
        boolean[] format = f.getByFormat();
        boolean[] property = f.getByProperty();

        list = country != null ? country.getDivisions(list) : list;
        list = city != null ? city.getDivisions(list) : list;
        list = organization != null ? organization.getDivisions(list) : list;
        list = division != null ? division.getDivision(list) : list;

        list = format[Format.COFFEESHOP] ? list : Division.filterByFormat(Format.COFFEESHOP, list);
        list = format[Format.ESPRESSIT] ? list : Division.filterByFormat(Format.ESPRESSIT, list);

        list = property[Property.MASTER] ? list : Division.filterByProperty(Property.MASTER, list);
        list = property[Property.FRANCHISE] ? list : Division.filterByProperty(Property.FRANCHISE, list);
        list = property[Property.SPV] ? list : Division.filterByProperty(Property.SPV, list);
        list = property[Property.GOVERNET] ? list : Division.filterByProperty(Property.GOVERNET, list);

        return Division.getListDivisions(list);
    }


    /*@RequiresApi(api = Build.VERSION_CODES.N)
    private List<Division> getFilteredAccessList() {

        List<Division> list = new ArrayList<>(divisions);
        Filter f = getFilter();


        if (f.getByCountry() != null)
            list = list.stream().filter((d) -> d.isChildFor(f.getByCountry())).collect(Collectors.toList());


        if (f.getByCity() != null)
            list = list.stream().filter((d) -> d.isChildFor(f.getByCity())).collect(Collectors.toList());


        if (f.getByOrganization() != null)
            list = list.stream().filter((d) -> d.isChildFor(f.getByOrganization())).collect(Collectors.toList());


        if (f.getByDivision() != null)
            list = list.stream().filter((d) -> d.getRef().equals(f.getByDivision().getRef())).collect(Collectors.toList());





        if (!f.getByFormat()[Format.COFFEESHOP])
            list = list.stream().filter((d) -> !d.isFormat(Format.COFFEESHOP)).collect(Collectors.toList());

        if (!f.getByFormat()[Format.ESPRESSIT])
            list = list.stream().filter((d) -> !d.isFormat(Format.ESPRESSIT)).collect(Collectors.toList());

        if (!f.getByProperty()[Property.MASTER])
            list = list.stream().filter((d) -> !d.isProperty(Property.MASTER)).collect(Collectors.toList());

        if (!f.getByProperty()[Property.FRANCHISE])
            list = list.stream().filter((d) -> !d.isProperty(Property.FRANCHISE)).collect(Collectors.toList());

        if (!f.getByProperty()[Property.SPV])
            list = list.stream().filter((d) -> !d.isProperty(Property.SPV)).collect(Collectors.toList());

        if (!f.getByProperty()[Property.GOVERNET])
            list = list.stream().filter((d) -> !d.isProperty(Property.GOVERNET)).collect(Collectors.toList());


        return list;
    }


    private List<Division> getFilteredAccessListForOld() { // for api < 24

        //Logger.d("for old");

        List<Division> list = new ArrayList<>(divisions);
        List<Division> newList = new ArrayList<>();
        Filter f = getFilter();


        if (f.getByCountry() != null) {
            newList.clear();
            for (Division d : list) {
                if (d.isChildFor(f.getByCountry()))
                    newList.add(d);
            }
            list.retainAll(newList);
        }

        if (f.getByCity() != null) {
            newList.clear();
            for (Division d : list) {
                if (d.isChildFor(f.getByCity()))
                    newList.add(d);
            }
            list.retainAll(newList);
        }

        if (f.getByOrganization() != null) {
            newList.clear();
            for (Division d : list) {
                if (d.isChildFor(f.getByOrganization()))
                    newList.add(d);
            }
            list.retainAll(newList);
        }

        if (f.getByDivision() != null) {
            newList.clear();
            for (Division d : list) {
                if (d.getRef().equals(f.getByDivision().getRef()))
                    newList.add(d);
            }
            list.retainAll(newList);
        }







        if (!f.getByFormat()[Format.COFFEESHOP]) {
            newList.clear();
            for (Division d : list) {
                if (!d.isFormat(Format.COFFEESHOP))
                    newList.add(d);
            }
            list.retainAll(newList);
        }

        if (!f.getByFormat()[Format.ESPRESSIT]) {
            newList.clear();
            for (Division d : list) {
                if (!d.isFormat(Format.ESPRESSIT))
                    newList.add(d);
            }
            list.retainAll(newList);
        }

        if (!f.getByProperty()[Property.MASTER]) {
            newList.clear();
            for (Division d : list) {
                if (!d.isProperty(Property.MASTER))
                    newList.add(d);
            }
            list.retainAll(newList);
        }

        if (!f.getByProperty()[Property.FRANCHISE]) {
            newList.clear();
            for (Division d : list) {
                if (!d.isProperty(Property.FRANCHISE))
                    newList.add(d);
            }
            list.retainAll(newList);
        }

        if (!f.getByProperty()[Property.SPV]) {
            newList.clear();
            for (Division d : list) {
                if (!d.isProperty(Property.SPV))
                    newList.add(d);
            }
            list.retainAll(newList);
        }

        if (!f.getByProperty()[Property.GOVERNET]) {
            newList.clear();
            for (Division d : list) {
                if (!d.isProperty(Property.GOVERNET))
                    newList.add(d);
            }
            list.retainAll(newList);
        }

        return list;
    }*/




}
