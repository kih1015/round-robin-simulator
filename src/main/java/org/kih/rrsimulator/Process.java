package org.kih.rrsimulator;

public class Process {
    private final Integer pid, arrivalTime, serviceTime;

    public Process(Integer pid, Integer arrivalTime, Integer serviceTime) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }

    public int getPid() {
        return pid;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }


    public int getServiceTime() {
        return serviceTime;
    }
}
