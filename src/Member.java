public class Member {
    private int id;
    private String name;
    private int borrowedCount;
    private int numViewBorrowed;
    private int numBorrows;
    private int numReturns;
    private double sessionFees;

    static public double TotalRevenue = 0.0;
    static public int TotalViewBorrowed = 0;
    static public int TotalBorrows = 0;
    static public int TotalReturns = 0;


    // Constants
    static final double FEE_PER_BORROW = 0.50;
    static final int MAX_BORROW_PER_USER = 5;


    public Member(int id, String name, int borrowedCount) {
        this.id = id;
        this.name = name;
        this.borrowedCount = borrowedCount;

        this.numViewBorrowed = 0;
        this.numBorrows = 0;
        this.numReturns = 0;
        this.sessionFees = 0.0;
    }

    private boolean canBorrow() {
        return borrowedCount < 5;
    }

    private boolean canReturn() {
        return borrowedCount > 0;
    }

    public void viewBorrowedCount() {
        numViewBorrowed++;
        Member.TotalViewBorrowed++;

        LibrarySimulator.displayInfo(String.format("You currently have %d book(s) borrowed.", borrowedCount));
    }

    public boolean borrowOne() {
        if (canBorrow()) {
            borrowedCount++;
            sessionFees += FEE_PER_BORROW;
            numBorrows++;
            Member.TotalRevenue += FEE_PER_BORROW;
            Member.TotalBorrows++;
            return true;
        }
        return false;
    }

    public boolean returnOne() {
        if (canReturn()) {
            borrowedCount--;
            numReturns++;
            Member.TotalReturns++;
            return true;
        }
        return false;
    }

    public void displayStatistics() {
        LibrarySimulator.displaySessionSummary(numBorrows, numReturns, sessionFees, numViewBorrowed);
    }

    public void reset() {
        this.numViewBorrowed = 0;
        this.numBorrows = 0;
        this.numReturns = 0;
        this.sessionFees = 0.0;
    }

    // Getters & Setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getBorrowedCount() {
        return borrowedCount;
    }

}
