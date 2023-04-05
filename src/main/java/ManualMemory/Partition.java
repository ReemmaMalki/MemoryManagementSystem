package ManualMemory;

public class Partition {
    private String allocatedBy;
    private int startAddress;
    private int size;

    public Partition() {
        this.startAddress = -1;
        this.allocatedBy = "";
        this.size = 0;
    }

    public Partition(int StartAddress, int Size, String allocatedBy) {
        this.startAddress = StartAddress;
        this.allocatedBy = allocatedBy;
        this.size = Size;
    }

    public Partition(int Size, String allocatedBy) {
        this.startAddress = -1;
        this.allocatedBy = allocatedBy;
        this.size = Size;
    }

    public void setStartAddress(int StartAddress) {
        this.startAddress = StartAddress;
    }

    public void setAllocatedBy(String allocatedBy) {
        this.allocatedBy = allocatedBy;
    }

    public void setSize(int Size) {
        this.size = Size;
    }

    public int getStartAddress() {
        return this.startAddress;
    }

    public String getAllocatedBy() {
        return this.allocatedBy;
    }

    public int getSize() {
        return this.size;
    }

    public void increaseSize(int additionalSize) {
        this.size += additionalSize;
    }


}
