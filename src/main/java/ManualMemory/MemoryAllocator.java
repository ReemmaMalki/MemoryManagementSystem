package ManualMemory;

import java.util.ArrayList;
import java.util.Scanner;


public class MemoryAllocator {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        final int MemorySize = 1000;

        ArrayList<Partition> partitions = new ArrayList<>();

        partitions.add(new Partition(0, MemorySize, "Unused"));

        System.out.println("-----------------------------Welcome to Main Memory Management Simulator-----------------------------");
        System.out.println("Command RQ:   Request for a contiguous block of memory ");
        System.out.println("              Example: RQ (process name) (requested memory size) (allocation policy e.g. W:worst fit)");
        System.out.println("Command RL:   Release of a contiguous block of memory");
        System.out.println("              Example: RL (process name) ");
        System.out.println("Command C:    Compact unused holes of memory into one single block");
        System.out.println("Command STAT: Report the regions of free and allocated memory");
        System.out.println("Command X:    Exit the program");
        System.out.println("-----------------------------------------------------------------------------------------------------\n");

        while (true) {
            System.out.print("allocator>");
            String command = scanner.next();

            if (command.equalsIgnoreCase("RQ")) {
                String proc = scanner.next();
                int partitionSize = scanner.nextInt();
                String policy = scanner.next();
                AllocateMemory(partitions, proc, partitionSize, policy);
            } else if (command.equalsIgnoreCase("RL")) {
                String procNum = scanner.next();
                procNum = ("Process " + procNum);  // P0
                ReleaseMemory(procNum, partitions);
            } else if (command.equalsIgnoreCase("C")) {
                Compaction(partitions);
                Stat(partitions);
            } else if (command.equalsIgnoreCase("STAT")) {
                Stat(partitions);
            } else if (command.equalsIgnoreCase("X")) {
                break;
            }
        }
    }

    // MEMORY ALLOCATION RELATED METHODS //
    private static void AllocateMemory(ArrayList<Partition> partitions, String proc, int partitionSize, String policy) {
        Boolean found = ProcNameMatch(partitions, proc);
        if (found == true) {
            System.out.println("Try diffrent procces name; " + proc + " is used. \n");
            return;
        }

        Partition newPartition = new Partition(partitionSize, "Process " + proc);

        Boolean added = false;
        if (policy.equalsIgnoreCase("B")) { // Best Fit policy
            added = BestFit(newPartition, partitions);
        } else if (policy.equalsIgnoreCase("W")) { // Worst Fit policy
            added = WorstFit(newPartition, partitions);
        } else if (policy.equalsIgnoreCase("F")) { // First Fit policy
            added = FirstFit(newPartition, partitions);
        } else {
            System.out.println("You have entered wrong ploicy letter. Try B:best fit, W:worst fit, and F:first fit");
            return;
        }

        if (added == false) {
            System.out.println("Partition Ocuupation Failed; no unused (Hole) partition fit the Process " + proc + "\n");
            return;
        }

        System.out.println("Process " + proc + " was allocated successfully in the address ["
                + newPartition.getStartAddress() + ":"
                + (newPartition.getStartAddress() + newPartition.getSize() - 1)
                + "] \n");

    }

    public static void insertNewParition(ArrayList<Partition> partitions, Partition newPartition, int index) {
        Partition selectedPartition = partitions.get(index); // get the chosen hole partition returned from the policy
        int startAddress = selectedPartition.getStartAddress();
        newPartition.setStartAddress(startAddress);

        partitions.remove(index); //remove the partition(node) which have the same start address (index) value
        partitions.add(index, newPartition); // replace it with the new partition(node)

        int internalFragmentationSize = selectedPartition.getSize() - newPartition.getSize(); // check the internal Fragmentation size of the hole 
        if (internalFragmentationSize != 0) {
            int startUnused = startAddress + newPartition.getSize(); // the start address of the new hole
            Partition newHole = new Partition(startUnused, internalFragmentationSize, "Unused");
            partitions.add(index + 1, newHole); // add the new hole after the new partition
        }
    }

    public static Boolean BestFit(Partition newPartition, ArrayList<Partition> partitions) {
        //Best Fit Policy//
        int index = -1, minSize = 2100000;
        for (int i = 0; i < partitions.size(); ++i) {
            if (partitions.get(i).getAllocatedBy().equals("Unused")) {
                if (partitions.get(i).getSize() >= newPartition.getSize()) { // true when hole is greater than or equal new node 
                    if (partitions.get(i).getSize() < minSize) { // true when hole is less than maxsize so far
                        minSize = partitions.get(i).getSize(); // change minSize to the cuurent hole size
                        index = i; // change index to the cuurent hole index
                    }
                }
            }
        }
        if (index == -1) {
            return false;
        }
        insertNewParition(partitions, newPartition, index);
        return true;
    }

    public static Boolean WorstFit(Partition newPartition, ArrayList<Partition> partitions) {
        //Worst Fit Policy//
        int index = -1, maxSize = 0;
        for (int i = 0; i < partitions.size(); ++i) {
            if (partitions.get(i).getAllocatedBy().equals("Unused")) {
                if (partitions.get(i).getSize() >= newPartition.getSize()) { // true when hole is greater than or equal new node 
                    if (partitions.get(i).getSize() > maxSize) { // true when hole is greater than maxsize so far
                        maxSize = partitions.get(i).getSize(); // change maxSize to the cuurent hole size
                        index = i; // change index to the cuurent hole index
                    }
                }
            }
        }
        if (index == -1) {
            return false;
        }
        insertNewParition(partitions, newPartition, index);
        return true;
    }

    public static Boolean FirstFit(Partition newPartition, ArrayList<Partition> partitions) {
        //First Fit Policy//
        int index = -1;
        for (int i = 0; i < partitions.size(); ++i) {
            if (partitions.get(i).getAllocatedBy().equals("Unused")) {
                if (partitions.get(i).getSize() >= newPartition.getSize()) { // true when hole is bigger then or equal new node 
                    index = i;
                    break;
                }
            }
        }
        if (index == -1) {
            return false;
        }
        insertNewParition(partitions, newPartition, index);
        return true;
    }

    private static Boolean ProcNameMatch(ArrayList<Partition> partitions, String proc) {
        // this methods for searching for a process that match the new process name
        proc = "Process " + proc;
        for (int i = 0; i < partitions.size(); ++i) {
            if (partitions.get(i).getAllocatedBy().equalsIgnoreCase(proc)) {
                return true;
            }
        }
        return false;
    }

    // RELEASE MEMORY METHOD //
    private static void ReleaseMemory(String procNum, ArrayList<Partition> partitions) {
        Boolean found = false;

        for (int i = 0; i < partitions.size(); ++i) {
            if (partitions.get(i).getAllocatedBy().equalsIgnoreCase(procNum)) {
                found = true;
                System.out.println(partitions.get(i).getAllocatedBy() + " was deallocated successfully from the memory address ["
                        + partitions.get(i).getStartAddress() + ":"
                        + (partitions.get(i).getStartAddress() + partitions.get(i).getSize() - 1)
                        + "] \n");
                partitions.get(i).setAllocatedBy("Unused");

                System.out.println(partitions.get(i).getAllocatedBy() + " was deallocated successfully from the memory address ["
                        + partitions.get(i).getStartAddress() + ":"
                        + (partitions.get(i).getStartAddress() + partitions.get(i).getSize() - 1)
                        + "] \n"); }

           //check if parition is a hole if yes remove it and combine it with current hole
                if ((i - 1) != -1 && "Unused".equals(partitions.get(i - 1).getAllocatedBy())) {
                    partitions.get(i).setStartAddress(partitions.get(i - 1).getStartAddress());
                    partitions.get(i).setSize(partitions.get(i).getSize() + partitions.get(i - 1).getSize());
                    partitions.remove(i - 1);
                }
            }
        
        if (found == false) {
            System.out.println("Partition deallocation faild; " + procNum + " is not found.\n");
        }
    }

    // REPOET STUTS OF MEMORY METHOD //
    public static void Stat(ArrayList<Partition> partitions) {
        //Report the regions of free and allocated memory.//
        for (int i = 0; i < partitions.size(); ++i) {
            int start = partitions.get(i).getStartAddress();
            int size = partitions.get(i).getSize();
            String status = partitions.get(i).getAllocatedBy();
            System.out.println(
                    "Addresses [" + start + ":"
                    + (start + size - 1) + "] "
                    + status
            );
        }
        System.out.println("");

    }

    // COMPACTION OF HOLES IN MEMORY //
    private static void Compaction(ArrayList<Partition> partitions) {
        //Compact unused holes of memory into one single block.//

        // get the size of all holes in the memory
        int holesSize = 0;
        for (int i = 0; i < partitions.size(); ++i) {
            if ("Unused".equals(partitions.get(i).getAllocatedBy())) {
                holesSize += partitions.get(i).getSize();
            }
        }
        // remove holes
        for (int i = 0; i < partitions.size(); ++i) {
            if ("Unused".equals(partitions.get(i).getAllocatedBy())) {
                partitions.remove(i);
                i--;
            }
        }
        // shift ocuupied partitions up 
        int startAdr = 0;
        for (int i = 0; i < partitions.size(); ++i) {
            if (partitions.get(i).getStartAddress() != startAdr) {
                partitions.get(i).setStartAddress(startAdr);
            }
            startAdr = partitions.get(i).getStartAddress() + partitions.get(i).getSize();
        }
        // add the new large hole
        partitions.add(new Partition(startAdr, holesSize, "Unused"));
    }

    // REPOET STUTS OF MEMORY METHOD //
}


