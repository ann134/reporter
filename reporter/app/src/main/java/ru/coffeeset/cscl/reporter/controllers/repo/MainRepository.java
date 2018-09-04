package ru.coffeeset.cscl.reporter.controllers.repo;

public abstract class MainRepository {

    public static UserRepository users() {
        return UserRepository.getInstance();
    }

    public static ReportRepository reports() {
        return ReportRepository.getInstance();
    }

    /*public static AccessRepository access() {
        return AccessRepository.getInstance();
    }*/

}
