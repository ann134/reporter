package ru.coffeeset.cscl.reporter.models.accessList;

public interface IAccessItem {

    String ref = null;
    String name = null;

    int getType();

    String getRef();

    String getName();

    String getRefParent();

}
