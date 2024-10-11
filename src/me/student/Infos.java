package me.student;

import me.student.Coordinates3D.Coordinates3DLinkedList;

public class Infos {
    private long                    time;
    private long                    memmoryConsumption;
    private Integer                 visitedCities;
    private Double                  foundPathCost;
    private Coordinates3DLinkedList path;

    public Infos(
        long time,
        long memmoryConsumption,
        Coordinates3DLinkedList path
        ) {
        this.time               = time;
        this.memmoryConsumption = memmoryConsumption;
        if(path == null) {
            this.foundPathCost      = null;
            this.visitedCities      = null;
            this.path               = null;
        }
        else {
            this.foundPathCost      = path.getPathDistance();
            this.visitedCities      = path.size();
            this.path               = path;
        }
    }

    public static String toString(Infos i) {
        return i.toString();
    }

    public String toString() {
        String s = "";
        if (path == null) {
            s +=  "fpc = " + String.valueOf(foundPathCost);
            s += "; time = " + String.valueOf(time) + "ms";
            s += "; Failed to find a path!";
            return s;
        }

        s +=  "fpc = " + String.valueOf(foundPathCost);
        s += "; time = " + String.valueOf(time) + "ms";
        s += "; mc = " + String.valueOf(memmoryConsumption) + "bits";
        s += "; ct = " + String.valueOf(visitedCities) + ";";

        return s;
    }

    public long                     getTime() { return this.time; }
    
    public long                     getMemmoryConsumption() { return this.memmoryConsumption; }

    public Integer                  getVisitedCities() { return this.visitedCities; }

    public Double                   getFoundPathCost() { return this.foundPathCost; }

    public Coordinates3DLinkedList  getPath() { return this.path; }

}
