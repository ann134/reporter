package ru.coffeeset.cscl.reporter.models.accessList;

import java.util.List;

public class Currency implements IAccessItem {
    private String ref;
    private String name;
    private List<Country> countryList;


    @Override
    public int getType() {
        return -1;
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
        return "";
    }


    public List<Country> getCountryList() {
        return countryList;
    }
}
