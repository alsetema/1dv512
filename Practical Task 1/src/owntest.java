import java.util.ArrayList;

public class owntest {
    public static void main(String[] args) {
        ArrayList<Process> listOfProcesses = new ArrayList<Process>();

        listOfProcesses.add(new Process(1, 0, 4));
        listOfProcesses.add(new Process(2, 2, 3));
        listOfProcesses.add(new Process(3, 1, 1));
        listOfProcesses.add(new Process(4, 3, 2));
        listOfProcesses.add(new Process(5, 4, 5));

        int[] processIds = new int[]{1, 2, 3, 4, 5};
        int[] processCT  = new int[]{4, 7, 8, 10, 15};
        int[] processTAT = new int[]{4, 6, 6, 7, 11};
        int[] processWT  = new int[]{0, 3, 5, 5, 6};


        FCFS myFcfs = new FCFS(listOfProcesses);
        myFcfs.run();
        myFcfs.printTable();
        System.out.println("\n");
        myFcfs.printGanttChart();
    }
}
