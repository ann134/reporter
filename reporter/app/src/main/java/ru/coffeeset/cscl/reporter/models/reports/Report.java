package ru.coffeeset.cscl.reporter.models.reports;


public class Report {


    public static class Position {
        public static final int divisions = 0;
        public static final int salesMix = 1;
        public static final int golden = 2;
        public static final int card = 3;

    }

    public static class ID {
        public static final int salesMix = 160;
        public static final int golden = 150;
        public static final int revenue = 200;

    }




    private int position;
    private int name;
    private int description;


    public Report(int position){
        this.position = position;
    }

    public void setName(int name) {
        this.name = name;
    }

    public void setDescription(int description) {
        this.description = description;
    }



    public int getName() {
        return name;
    }

    public int getDescription() {
        return description;
    }

    public int getPosition() {
        return position;
    }

}
