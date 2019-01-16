/*
 * File:	Process.java
 * Course: 	Operating Systems
 * Code: 	1DV512
 * Author: 	Suejb Memeti
 * Date: 	November, 2018
 */

import java.util.*;


public class FCFS {


    Comparator<Process> sorter = (o1, o2) -> o1.getArrivalTime() - o2.getArrivalTime(); //the comparator to sort the list by arrival time

    // The list of processes to be scheduled
    public ArrayList<Process> processes;
    public Queue<Process> processQueue = new ArrayDeque<>();

    private Process currentProcess = null;
    private int currentProcessingTime = 0; //we use global to be able to verify later

    // Class constructor
    public FCFS(ArrayList<Process> processes) {
        this.processes = processes;
        initialiseTime();


    }


    public void run() {
        processes.sort(sorter); //we sor the big original array
        ArrayList<Process> ownProcessArray = new ArrayList<>(processes);
        processQueue.addAll(ownProcessArray);

        while (!processQueue.isEmpty()) {


            currentProcess = processQueue.poll();

            for (int counter = 0; counter < currentProcess.burstTime; counter++) {
                //increases the counter of the time for as long as the process is running, here it's "processing" it.
                currentProcessingTime++;
            }
            currentProcess.setCompletedTime(currentProcessingTime);
            currentProcess.setTurnaroundTime(calcTAT(currentProcess));
            currentProcess.setWaitingTime(calcWAT(currentProcess));
            if (!processQueue.isEmpty()) {
                while (currentProcessingTime < processQueue.peek().arrivalTime) {
                    currentProcessingTime++; //this is for the idle time to count and so on
                }
            }


        }



    }

    public void printTable() {
        //TODO Print the list of processes in form of a table here
        System.out.println("| Process ID | Arrival Time | Burst Time | Completed Time | Turnarround Time | Waiting Time |");
        for (int counter = 0; counter < processes.size(); counter++) {
            Process currentP = processes.get(counter);
            System.out.println("\t  " + currentP.getProcessId() + "\t\t\t\t" + currentP.getArrivalTime() + "\t\t\t  " + currentP.getBurstTime() + "\t\t\t\t  " + currentP.getCompletedTime() + "\t\t\t\t\t" + currentP.getTurnaroundTime() + "\t\t\t\t\t" + currentP.getWaitingTime());

        }

    }

    public void printGanttChart() {
        //TODO Print the demonstration of the scheduling algorithm using Gantt Chart

        //counter for the processes
        System.out.println("GANTT CHART");

        for (int processCounter = 0; processCounter < processes.size(); processCounter++) {
            System.out.print("[");
            System.out.print("P" + processes.get(processCounter).getProcessId());
            printBars(processes.get(processCounter).getTurnaroundTime() - processes.get(processCounter).getWaitingTime());
            printStars(processes.get(processCounter).getWaitingTime());
            System.out.print("]");


        }
        System.out.println("");
        for (Process proc : processes) {
            //printing the lower layer
            System.out.print(proc.getCompletedTime());
            printSpaces(proc.turnaroundTime * 2);

        }


    }

    private void printBars(int numberOfBars) {
        for (int counter = 0; counter <= numberOfBars; counter++) {
            System.out.print("|");
        }
    }

    private void printStars(int numberOfStars) {
        for (int counter = 0; counter <= numberOfStars; counter++) {
            System.out.print("*");
        }
    }

    private void printSpaces(int numberOfSpaces) {
        for (int counter = 0; counter <= numberOfSpaces; counter++) {
            System.out.print(" ");
        }
    }

    private void initialiseTime() {
        currentProcessingTime = 0;
    }


    public int calcTAT(Process p) {
        int output = 0;
        if (p.getCompletedTime() <= 0) {
            System.out.println("Cannot calculate the Turnaround time due to the emptiness of completed time");
        } else {
            output = p.getCompletedTime() - p.getArrivalTime();
        }
        return output;
    }

    public int calcWAT(Process p) {
        int output = 0;
        if (p.getTurnaroundTime() <= 0) {
            System.out.println("Cannot calculate the waiting time due to the emptiness of the turnaround time.");
        } else {
            output = p.getTurnaroundTime() - p.getBurstTime();
        }
        return output;
    }


    public int calcCT(Process p) {

        if (p.getArrivalTime() > currentProcessingTime) {
            currentProcessingTime = p.getArrivalTime() + p.getBurstTime();
            //We do this so if for some reason we calculate the process time twice then it wont mess up
            //the whole calculation for a repeated process
        } else {
            currentProcessingTime = currentProcessingTime + p.getBurstTime();
            //the normal condition that should ACTUALLY happen
        }
        return currentProcessingTime;
    }


}
