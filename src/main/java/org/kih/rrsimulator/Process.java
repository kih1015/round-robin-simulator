package org.kih.rrsimulator;

public class Process {
    private final int pid, arrivalTime, serviceTime;
    private int remainTime, waitingTime, endTime;

    public Process(int pid, int arrivalTime, int serviceTime) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.remainTime = serviceTime;
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

    public int getRemainTime() {
        return remainTime;
    }

    public void decRemainTime() {
        --remainTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void incWaitingTime() {
        ++waitingTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }
}
