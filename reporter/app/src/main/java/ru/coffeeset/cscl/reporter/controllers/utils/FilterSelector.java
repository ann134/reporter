package ru.coffeeset.cscl.reporter.controllers.utils;


import ru.coffeeset.cscl.reporter.models.accessList.Country;
import ru.coffeeset.cscl.reporter.models.accessList.Format;
import java.util.ArrayList;
import java.util.List;

import ru.coffeeset.cscl.reporter.controllers.repo.MainRepository;
import ru.coffeeset.cscl.reporter.models.Filter;
import ru.coffeeset.cscl.reporter.models.accessList.IAccessItem;
import ru.coffeeset.cscl.reporter.models.accessList.City;
import ru.coffeeset.cscl.reporter.models.accessList.Division;
import ru.coffeeset.cscl.reporter.models.accessList.ItemType;
import ru.coffeeset.cscl.reporter.models.accessList.Organization;
import ru.coffeeset.cscl.reporter.models.accessList.Property;



public class FilterSelector {

    public static List<IAccessItem> selectList(int type, Filter f) {

        List<IAccessItem> list = new ArrayList<>(MainRepository.users().getUser().getAccess().getElementsListByType(type));
        Country country = f.getByCountry();
        City city = f.getByCity();
        Organization organization = f.getByOrganization();
        boolean[] format = f.getByFormat();
        boolean[] property = f.getByProperty();


        switch (type) {
            case (ItemType.TYPE_COUNTRY):
                return list;

            case (ItemType.TYPE_CITY):
                return country.getCities(list);

            case (ItemType.TYPE_ORGANIZATION):

                list = country.getOrganizations(list);
                list = city != null ? city.getOrganizations(list) : list;
                list = Organization.filterClosed(list);

                return list;

            case (ItemType.TYPE_DIVISION):

                list = country != null ? country.getDivisions(list) : list;
                list = city != null ? city.getDivisions(list) : list;
                list = organization != null ? organization.getDivisions(list) : list;

                list = format[Format.COFFEESHOP] ? list : Division.filterByFormat(Format.COFFEESHOP, list);
                list = format[Format.ESPRESSIT] ? list : Division.filterByFormat(Format.ESPRESSIT, list);

                list = property[Property.MASTER] ? list : Division.filterByProperty(Property.MASTER, list);
                list = property[Property.FRANCHISE] ? list : Division.filterByProperty(Property.FRANCHISE, list);
                list = property[Property.SPV] ? list : Division.filterByProperty(Property.SPV, list);
                list = property[Property.GOVERNET] ? list : Division.filterByProperty(Property.GOVERNET, list);

                list = Division.filterClosed(list);

                return list;
        }
        return list;
    }








    /*@RequiresApi(api = Build.VERSION_CODES.N)
    public static List<IAccessItem> selectListForNew(int type, Filter f) {

        List<IAccessItem> list = new ArrayList<>(MainRepository.users().getUser().getAccess().getElementsListByType(type));

        switch (type) {
            case (ItemType.TYPE_COUNTRY):
                return list;

            case (ItemType.TYPE_CITY):
                if (f.getByCountry() != null)
                    return list.stream().filter((i) -> ((City) i).isChildFor(f.getByCountry())).collect(Collectors.toList());

            case (ItemType.TYPE_ORGANIZATION):
                if (f.getByCity() != null)
                    list.retainAll(list.stream().filter((i) -> ((Organization) i).isChildFor(f.getByCity())).collect(Collectors.toList()));

                if (f.getByCountry() != null)
                    list.retainAll(list.stream().filter((i) -> ((Organization) i).isChildFor(f.getByCountry())).collect(Collectors.toList()));

                return list.stream().filter((i) -> !((Organization) i).isClosed()).collect(Collectors.toList());

            case (ItemType.TYPE_DIVISION):

                if (!f.getByFormat()[Format.COFFEESHOP]) {
                    list = list.stream().filter((i) -> !(((Division) i).isFormat(Format.COFFEESHOP))).collect(Collectors.toList());
                }

                if (!f.getByFormat()[Format.ESPRESSIT]) {
                    list = list.stream().filter((i) -> !(((Division) i).isFormat(Format.ESPRESSIT))).collect(Collectors.toList());
                }


                if (!f.getByProperty()[Property.MASTER]) {
                    list = list.stream().filter((i) -> !(((Division) i).isProperty(Property.MASTER))).collect(Collectors.toList());
                }

                if (!f.getByProperty()[Property.FRANCHISE]) {
                    list = list.stream().filter((i) -> !(((Division) i).isProperty(Property.FRANCHISE))).collect(Collectors.toList());
                }

                if (!f.getByProperty()[Property.SPV]) {
                    list = list.stream().filter((i) -> !(((Division) i).isProperty(Property.SPV))).collect(Collectors.toList());
                }

                if (!f.getByProperty()[Property.GOVERNET]) {
                    list = list.stream().filter((i) -> !(((Division) i).isProperty(Property.GOVERNET))).collect(Collectors.toList());
                }


                if (f.getByCity() != null && f.getByOrganization() != null)
                    return list.stream().filter((i) ->
                            (((Division) i).isChildFor(f.getByCity()) && ((Division) i).isChildFor(f.getByOrganization()))
                    ).collect(Collectors.toList());


                if (f.getByOrganization() != null)
                    return list.stream().filter((i) -> ((Division) i).isChildFor(f.getByOrganization())).collect(Collectors.toList());

                if (f.getByCity() != null)
                    return list.stream().filter((i) -> ((Division) i).isChildFor(f.getByCity())).collect(Collectors.toList());

                if (f.getByCountry() != null)
                    return list.stream().filter((i) -> ((Division) i).isChildFor(f.getByCountry())).collect(Collectors.toList());


                return list;
        }

        return list;
    }


    public static List<IAccessItem> selectListForOld(int type, Filter f) { // for api < 24

        Logger.d("For Old");

        List<IAccessItem> list = new ArrayList<>(MainRepository.users().getUser().getAccess().getElementsListByType(type));
        List<IAccessItem> newList = new ArrayList<>();

        switch (type) {
            case (ItemType.TYPE_COUNTRY):
                return list;

            case (ItemType.TYPE_CITY):
                if (f.getByCountry() != null) {
                    for (IAccessItem i : list) {
                        if (((City) i).isChildFor(f.getByCountry()))
                            newList.add(i);
                    }
                    return newList;
                }

            case (ItemType.TYPE_ORGANIZATION):
                if (f.getByCity() != null) {
                    newList.clear();
                    for (IAccessItem i : list) {
                        if (((Organization) i).isChildFor(f.getByCity()))
                            newList.add(i);
                    }
                    list.retainAll(newList);
                }

                if (f.getByCountry() != null) {
                    newList.clear();
                    for (IAccessItem i : list) {
                        if (((Organization) i).isChildFor(f.getByCountry()))
                            newList.add(i);
                    }
                    list.retainAll(newList);
                }

                newList.clear();
                for (IAccessItem i : list) {
                    if (!((Organization) i).isClosed())
                        newList.add(i);
                }
                list.retainAll(newList);


                return list;
            case (ItemType.TYPE_DIVISION):
                if (f.getByCity() != null && f.getByOrganization() != null) {
                    for (IAccessItem i : list) {
                        if (((Division) i).isChildFor(f.getByCity()) && ((Division) i).isChildFor(f.getByOrganization()))
                            newList.add(i);
                    }
                    //return newList;
                    list.retainAll(newList);
                }

                if (f.getByOrganization() != null) {
                    for (IAccessItem i : list) {
                        if (((Division) i).isChildFor(f.getByOrganization()))
                            newList.add(i);
                    }
                    //return newList;
                    list.retainAll(newList);
                }

                if (f.getByCity() != null) {
                    for (IAccessItem i : list) {
                        if (((Division) i).isChildFor(f.getByCity()))
                            newList.add(i);
                    }
                    //return newList;
                    list.retainAll(newList);
                }

                if (f.getByCountry() != null) {
                    for (IAccessItem i : list) {
                        if (((Division) i).isChildFor(f.getByCountry()))
                            newList.add(i);
                    }
                    //return newList;
                    list.retainAll(newList);
                }


                if (!f.getByFormat()[Format.COFFEESHOP]) {
                    newList.clear();
                    for (IAccessItem i : list) {
                        if (!((Division) i).isFormat(Format.COFFEESHOP))
                            newList.add(i);
                    }
                    list.retainAll(newList);
                }
                if (!f.getByFormat()[Format.ESPRESSIT]) {
                    newList.clear();
                    for (IAccessItem i : list) {
                        if (!((Division) i).isFormat(Format.ESPRESSIT))
                            newList.add(i);
                    }
                    list.retainAll(newList);
                }

                if (!f.getByProperty()[Property.MASTER]) {
                    newList.clear();
                    for (IAccessItem i : list) {
                        if (!((Division) i).isProperty(Property.MASTER))
                            newList.add(i);
                    }
                    list.retainAll(newList);
                }

                if (!f.getByProperty()[Property.FRANCHISE]) {
                    newList.clear();
                    for (IAccessItem i : list) {
                        if (!((Division) i).isProperty(Property.FRANCHISE))
                            newList.add(i);
                    }
                    list.retainAll(newList);
                }

                if (!f.getByProperty()[Property.SPV]) {
                    newList.clear();
                    for (IAccessItem i : list) {
                        if (!((Division) i).isProperty(Property.SPV))
                            newList.add(i);
                    }
                    list.retainAll(newList);
                }

                if (!f.getByProperty()[Property.GOVERNET]) {
                    newList.clear();
                    for (IAccessItem i : list) {
                        if (!((Division) i).isProperty(Property.GOVERNET))
                            newList.add(i);
                    }
                    list.retainAll(newList);
                }


                newList.clear();
                for (IAccessItem i : list) {
                    if (!((Division) i).isClosed())
                        newList.add(i);
                }
                //return newList;
                list.retainAll(newList);


                return list;
        }

        return newList;
    }*/
}